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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author samuelm
 */
public class BackgroundConfig extends AbstractConfig {
    
    private static BackgroundConfig instance = null; 
    
    public static synchronized BackgroundConfig getInstance(){
        if (instance == null){
            instance = new BackgroundConfig();
        }
        return instance;
    }
    
    /** Creates a new instance of ConfigObj */
    private BackgroundConfig() {
        load("background.config");
    }
    

    public void save() {
        save("background.config");
    }

    private BufferedImage mainBackgroundImg;
    private BufferedImage monitorBackgroundImg;
    private String mainBackgroundImgStrCache = null;
    private String monitorBackgroundImgStrCache = null;

    public String getMainBackgroundImgStr(){
        if (mainBackgroundImgStrCache == null)
            mainBackgroundImgStrCache = getImgStr(this.mainBackgroundImg);
        return mainBackgroundImgStrCache;
    }    
    
    public String getMonitorBackgroundImgStr(){
        if (monitorBackgroundImgStrCache == null)
            monitorBackgroundImgStrCache = getImgStr(this.monitorBackgroundImg);
        return monitorBackgroundImgStrCache;
    }    

    private String getImgStr(BufferedImage img){
        
        if (img == null)
            return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( img, "png", baos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        byte[] ba = baos.toByteArray();

        int len=ba.length,j;
        StringBuffer sb=new StringBuffer(len*2);
        for (j=0;j<len;j++) {
            String sByte=Integer.toHexString((int)(ba[j] & 0xFF));
            sb.append(stringAlign2chars(sByte));
        }
        return sb.toString();
    }

    private String stringAlign2chars(String str){
        if (str.length()!=2)
            return '0'+str;
        else
            return str;
    }
    
    public void setMainBackgroundImgStr(String strImage){
        setImgStr( 0, strImage );
        mainBackgroundImgStrCache = strImage;
    }
    
    public void setMonitorBackgroundImgStr(String strImage){
        setImgStr( 1, strImage );
        monitorBackgroundImgStrCache = strImage;
    }

    /**
     * @param idx possible values: 0 for Main, 1 for monitor
     */
    private void setImgStr(int idx, String strImage){

        if (strImage.equals("")){
            if (idx == 0){
                this.mainBackgroundImg = null;
            }else if (idx == 1){
                this.monitorBackgroundImg = null;
            }
            return;
        }
        
        String str="";
        int intAux=0;
        byte[] bytes = new byte[strImage.length()/2];
        for(int i=0; i< strImage.length()-1;i=i+2){
            str = strImage.substring(i,i+2);
            intAux = Integer.parseInt(str,16);
            bytes[i/2]=(byte)intAux;
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            if (idx == 0){
                setMainBackgroundImg( ImageIO.read(bais), strImage );
            }else if (idx == 1){
                setMonitorBackgroundImg( ImageIO.read(bais), strImage );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public BufferedImage getMainBackgroundImg(){
        return mainBackgroundImg;
    }
    
    public void setMainBackgroundImg(BufferedImage img){
        setMainBackgroundImg(img, null);
    }

    public void setMainBackgroundImg(BufferedImage img, String strImage){
        this.mainBackgroundImg = img;
        this.mainBackgroundImgStrCache = strImage;
        ContentManager.getInstance().paintBackgroundMain(img);
        save();
    }

    public BufferedImage getMonitorBackgroundImg(){
        return monitorBackgroundImg;
    }
    
    public void setMonitorBackgroundImg(BufferedImage img){
        setMonitorBackgroundImg(img, null);
    }

    public void setMonitorBackgroundImg(BufferedImage img, String strImage){
        this.monitorBackgroundImg = img;
        this.monitorBackgroundImgStrCache = strImage;
        ContentManager.getInstance().paintBackgroundMonitor(img);
        save();
    }
    
    protected void registerProperties() {
        super.registerProperties();
        properties.add("MainBackgroundImgStr");
        properties.add("MonitorBackgroundImgStr");
    }
    
}
