/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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

/*
 * BackgroundConfig.java
 *
 * Created on 6 November 2006, 22:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.render.ContentManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author samuelm
 */
public class BackgroundConfig extends AbstractConfig {
    
    private static BackgroundConfig instance = null;

    public static final int MODE_STATIC = 0;
    public static final int MODE_VIDEO = 1;

    private BufferedImage image;
    private int mode;
    private String videofile;
    
    public static synchronized BackgroundConfig getInstance(){
        if (instance == null){
            instance = new BackgroundConfig();
        }
        return instance;
    }
    
    /** Creates a new instance of ConfigObj */
    private BackgroundConfig() {
        File imgfile = new File(getConfigPath()+"background.png");
        if (imgfile.exists() && imgfile.canRead()){
            try {
                BufferedImage img = ImageIO.read(imgfile);
                setBackgroundImg(img);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        load("background.config");
    }
    

    public void save() {
        File imgfile = new File(getConfigPath()+"background.png");
        if (!imgfile.exists() || imgfile.canWrite()){
            try {
                if (image == null && imgfile.exists()){
                    imgfile.delete();
                }else{
                    ImageIO.write(image, "png", imgfile);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        save("background.config");
    }

    public void setMainBackgroundImgStr(String strImage){
        // Keep for backward compatibility
    }
    
    public void setMonitorBackgroundImgStr(String strImage){
        // Keep for backward compatibility
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("Mode");
        properties.add("VideoFile");
    }

    public BufferedImage getBackgroundImg(){
        return image;
    }
    
    public void setBackgroundImg(BufferedImage img){
        image = img;
        ContentManager.getInstance().paintBackground(img);
    }

    public String getMode(){
        return Integer.toString(mode);
    }

    public int getModeAsInt(){
        return mode;
    }

    public void setMode(String s){
        mode = Integer.parseInt(s);
    }

    public void setMode(int m){
        mode = m;
    }

    public String getVideoFile(){
        return videofile;
    }

    public void setVideoFile(String s){
        videofile = s;
    }

}
