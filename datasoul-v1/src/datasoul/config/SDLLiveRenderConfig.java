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
 * SDLLiveRenderConfig.java
 *
 * Created on 22 November 2006, 20:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.render.ContentManager;
import datasoul.render.SDLLiveContentRender;

/**
 *
 * @author samuelm
 */
public class SDLLiveRenderConfig extends AbstractConfig {
    
    private static SDLLiveRenderConfig monitor;
    private static SDLLiveRenderConfig main;

    public static final int VIDEODEINTRELACE_NONE = 0;
    public static final int VIDEODEINTRELACE_BLEND = 1;
    public static final int VIDEODEINTRELACE_SMARTBLEND = 2;
    public static final int VIDEODEINTRELACE_SMOOTHBLEND = 3;
    public static final String[] VIDEODEINTRELACE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("None"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Blend"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Smart_blend"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Smooth_blend")};

    public static final int VIDEOMODE_PAL = 0;
    public static final int VIDEOMODE_NTSC = 1;
    public static final int VIDEOMODE_SECAM = 2;
    public static final String[] VIDEOMODE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("PAL"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NTSC"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SECAM")};

    public static final int VIDEOINPUT_TUNER = 0;
    public static final int VIDEOINPUT_COMPOSITE = 1;
    public static final int VIDEOINPUT_COMPSVIDEO = 2;
    public static final int VIDEOINPUT_SVIDEO = 3;
    public static final String[] VIDEOINPUT_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Tuner"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Composite"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Composite2/S-Video"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("S-Video")};
    
    
    private boolean isMonitor;
    
    private String x11Display;
    private boolean setX11Display;
    private int videoInput;
    private int videoMode;
    private int videoDeintrelace;
    private String deviceName;
    
    @Override
    protected void registerProperties() {
        properties.add("SetX11DislpayIdx");
        properties.add("X11Display");
        properties.add("VideoInputIdx");
        properties.add("VideoModeIdx");
        properties.add("VideoDeintrelaceIdx");
        properties.add("DeviceName");
    }

    public static SDLLiveRenderConfig getInstance(boolean isMonitor) {
        if (isMonitor){
            if (monitor == null){
                monitor = new SDLLiveRenderConfig(isMonitor);
            }
            return monitor;
        }else{
            if (main == null){
                main = new SDLLiveRenderConfig(isMonitor);
            }
            return main;
        }
    }


    private SDLLiveRenderConfig(boolean isMonitor){
        this.isMonitor = isMonitor;
        if (isMonitor){
            load("sdlliverender.monitor.config");
        }else{
            load("sdlliverender.main.config");
        }
    }
    
    public void save(){
        if (isMonitor){
            save("sdlliverender.monitor.config");
        }else{
            save("sdlliverender.main.config");
        }
    }
    
    public String getX11Display() {
        return x11Display;
    }

    public void setX11Display(String x11Display) {
        this.x11Display = x11Display;
    }
    
    public String getSetX11DislpayIdx(){
        if (setX11Display){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setSetX11DislpayIdx(String idx){
        if (idx.equals("true")){
            setSetX11Display(true);
        }else{
            setSetX11Display(false);
        }
    }

    public boolean getSetX11Display(){
        return setX11Display;

    }
    
    public void setSetX11Display(boolean value){
        this.setX11Display = value;
    }    

    public String getVideoInput() {
        return VIDEOINPUT_TABLE[videoInput];
    }
    
    public int getVideoInputIdx() {
        return videoInput;
    }

    public void setVideoInputIdx(String videoInput) {
        setVideoInputIdx(Integer.parseInt(videoInput));
    }
    
    public void setVideoInputIdx(int videoInput) {

        this.videoInput = videoInput;
        if (getRender() != null){
            getRender().setInputSrc(videoInput);
        }
        
    }
    
    public void setVideoInput(String str){
        for (int i=0; i<VIDEOINPUT_TABLE.length; i++){
            if (str.equalsIgnoreCase(VIDEOINPUT_TABLE[i]))
                setVideoInputIdx(i);
        }
    }
    
    

    public String getVideoMode() {
        return VIDEOMODE_TABLE[videoMode];
    }

    public int getVideoModeIdx() {
        return videoMode;
    }
    
    public void setVideoModeIdx(String videoMode) {
        setVideoModeIdx(Integer.parseInt(videoMode));
    }
    
    public void setVideoModeIdx(int videoMode) {
        this.videoMode = videoMode;
        if (getRender() != null){
            getRender().setInputMode(videoMode);
        }
    }

    public void setVideoMode(String str){
        for (int i=0; i<VIDEOMODE_TABLE.length; i++){
            if (str.equalsIgnoreCase(VIDEOMODE_TABLE[i]))
                setVideoModeIdx(i);
        }
    }
    
    
    public String getVideoDeintrelace() {
        return VIDEODEINTRELACE_TABLE[videoDeintrelace];
    }

    public int getVideoDeintrelaceIdx() {
        return videoDeintrelace;
    }

    public void setVideoDeintrelaceIdx(String videoDeintrelace) {
        setVideoDeintrelaceIdx(Integer.parseInt(videoDeintrelace));
    }
    
    public void setVideoDeintrelaceIdx(int videoDeintrelace) {
        this.videoDeintrelace = videoDeintrelace;
        if (getRender() != null){
            getRender().setDeintrelaceMode(videoDeintrelace);
        }
        
    }

    public void setVideoDeintrelace(String str){
        for (int i=0; i<VIDEODEINTRELACE_TABLE.length; i++){
            if (str.equalsIgnoreCase(VIDEODEINTRELACE_TABLE[i]))
                setVideoDeintrelaceIdx(i);
        }
    }

    public String getDeviceName(){
        return deviceName;
    }
    
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName.trim();
    }
    
    public SDLLiveContentRender getRender(){
        SDLLiveContentRender render = null;

        if (isMonitor){
            if (ConfigObj.getInstance().getMonitorOutput() && ContentManager.getMonitorDisplay() instanceof SDLLiveContentRender ){
                render = (SDLLiveContentRender) ContentManager.getMonitorDisplay();
            }
        }else{
            if (ConfigObj.getInstance().getMainOutput() && ContentManager.getMainDisplay() instanceof SDLLiveContentRender ){
                render = (SDLLiveContentRender) ContentManager.getMainDisplay();
            }
        }

        return render;
    }

}
