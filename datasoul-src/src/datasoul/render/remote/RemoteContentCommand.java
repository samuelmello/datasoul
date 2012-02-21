/* 
 * Copyright 2012 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.remote;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import datasoul.config.ConfigObj;
import datasoul.render.ContentRender;
import datasoul.serviceitems.imagelist.ImageListServiceRenderer;
import datasoul.util.ObjectManager;

/**
 *
 * @author samuel
 */
public class RemoteContentCommand implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 758446189373705635L;

    public enum ArgType {
        ARG_TYPE_VOID,
        ARG_TYPE_INT,
        ARG_TYPE_FLOAT,
        ARG_TYPE_STRING,
        ARG_TYPE_BOOLEAN,
        ARG_TYPE_IMAGE,
        ARG_TYPE_SHOW_OUTPUT,
    };
    
    public enum Target{
        TARGET_MAIN,
        TARGET_MONITOR
    }

    private int argint;
    private float argfloat;
    private String argString;
    private boolean argboolean;
    private byte[] argImage;

    private ArgType type;
    private String method;
    private Target target;

    @Override
    public String toString(){
        String param = "(null)";
        switch(type){
            case ARG_TYPE_VOID:
                param = "(null)";
                break;
            case ARG_TYPE_INT:
                param = Integer.toString(argint);
                break;
            case ARG_TYPE_FLOAT:
                param = Float.toString(argfloat);
                break;
            case ARG_TYPE_STRING:
                param = argString;
                break;
            case ARG_TYPE_BOOLEAN:
                param = Boolean.toString(argboolean);
                break;
            case ARG_TYPE_IMAGE:
                param = (argImage == null) ? "(null)" : "(image)";
                break;
        }
        return "RemoteContentCommand["+method+"="+param+"]@"+this.hashCode();
    }

    public RemoteContentCommand (Target target, String method){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_VOID;
    }

    public RemoteContentCommand (Target target, String method, int arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_INT;
        this.argint = arg;
    }

    public RemoteContentCommand (Target target, String method, float arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_FLOAT;
        this.argfloat = arg;
    }

    public RemoteContentCommand (Target target, String method, String arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_STRING;
        this.argString = arg;
    }

    public RemoteContentCommand (Target target, String method, boolean arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_BOOLEAN;
        this.argboolean = arg;
    }
    
    public RemoteContentCommand(Target target, String method, ImageListServiceRenderer arg) {
       this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_IMAGE;
        this.argImage = null;
        if (arg != null){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(arg.getTmpFile());
                byte[] buffer = new byte[1024];
                int i;
                while ((i = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, i);
                }
                this.argImage = baos.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public RemoteContentCommand (Target target, boolean arg){
        this.target = target;
        this.type = ArgType.ARG_TYPE_SHOW_OUTPUT;
        this.argboolean = arg;
    }

    public void run(){

        ContentRender tgt;
        if (target == Target.TARGET_MAIN){
            tgt = ObjectManager.getInstance().getMainContentRender();
        }else if (ConfigObj.getActiveInstance().getMonitorOutput()){
            tgt = ObjectManager.getInstance().getMonitorContentRender();
        }else{
            return;
        }

        try{
           switch(type){
                case ARG_TYPE_VOID:
                    tgt.getClass().getMethod(method).invoke(tgt);
                    break;
                case ARG_TYPE_INT:
                    tgt.getClass().getMethod(method, int.class).invoke(tgt, argint);
                    break;
                case ARG_TYPE_FLOAT:
                    tgt.getClass().getMethod(method, float.class).invoke(tgt, argfloat);
                    break;
                case ARG_TYPE_STRING:
                    tgt.getClass().getMethod(method, String.class).invoke(tgt, argString);
                    break;
                case ARG_TYPE_BOOLEAN:
                    tgt.getClass().getMethod(method, boolean.class).invoke(tgt, argboolean);
                    break;
                case ARG_TYPE_IMAGE:
                    BufferedImage img = null;
                    if (argImage != null){
                        try {
                            ByteArrayInputStream bais = new ByteArrayInputStream(argImage);
                            img = ImageIO.read(bais);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    ImageListServiceRenderer render = new ImageListServiceRenderer();
                    render.setImageWithoutTempFile(img);
                    tgt.getClass().getMethod(method, ImageListServiceRenderer.class).invoke(tgt, render);
                    break;
                case ARG_TYPE_SHOW_OUTPUT:
                    ObjectManager.getInstance().setOutputVisible(argboolean);
                    break;
            }
        }catch(NoSuchMethodException ex){
            ex.printStackTrace();
        }catch(IllegalAccessException ex){
            System.out.println("--> "+method+" type="+type);
            ex.printStackTrace();
        }catch(InvocationTargetException ex){
            System.out.println("--> "+method+" type="+type);
            ex.printStackTrace();
        }
    }
}
