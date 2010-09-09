/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.gstreamer.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import datasoul.render.ContentRender;
import datasoul.render.gstreamer.GstContentRender;
import datasoul.render.gstreamer.GstManagerClient;

/**
 *
 * @author samuel
 */
public class GstDisplayCmdUpdateContent extends GstDisplayCmd {

    /**
	 * 
	 */
	private static final long serialVersionUID = -711860229878479862L;

	public enum ArgType {
        ARG_TYPE_VOID,
        ARG_TYPE_INT,
        ARG_TYPE_FLOAT,
        ARG_TYPE_STRING,
        ARG_TYPE_BOOLEAN,
        ARG_TYPE_IMAGE
    };

    private int argint;
    private float argfloat;
    private String argString;
    private boolean argboolean;
    private byte[] argImage;

    private ArgType type;
    private String method;
    private GstContentRender.Target target;

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_VOID;
    }

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method, int arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_INT;
        this.argint = arg;
    }

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method, float arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_FLOAT;
        this.argfloat = arg;
    }

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method, String arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_STRING;
        this.argString = arg;
    }

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method, boolean arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_BOOLEAN;
        this.argboolean = arg;
    }

    public GstDisplayCmdUpdateContent (GstContentRender.Target target, String method, BufferedImage arg){
        this.target = target;
        this.method = method;
        this.type = ArgType.ARG_TYPE_IMAGE;
        this.argImage = null;
        if (arg != null){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(arg, "png", baos);
                this.argImage = baos.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run(){

        ContentRender tgt;
        if (target == GstContentRender.Target.TARGET_MAIN){
            tgt = GstManagerClient.getInstance().getMainRender();
        }else{
            tgt = GstManagerClient.getInstance().getMonitorRender();
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
                    tgt.getClass().getMethod(method, BufferedImage.class).invoke(tgt, img);
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
