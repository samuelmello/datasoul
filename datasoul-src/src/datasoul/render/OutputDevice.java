/* 
 * Copyright 2005-2010 Samuel Mello
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

package datasoul.render;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import com.sun.jna.Platform;
import java.awt.Rectangle;

/**
 *
 * @author samuel
 */
public class OutputDevice {

    private GraphicsDevice device;
    private GraphicsConfiguration gconfig;
    private int usage;
    private boolean isDefault;
    private boolean isNone; // Null output device, placeholder for remote only

    public static final int USAGE_MAIN = 0;
    public static final int USAGE_MONITOR = 1;

    /**
     * Creates a new OutputDevice
     * Try to find a device with IDstring equal to hintID
     * @param hintID
     */
    public OutputDevice(String hintID, int usage){

        this.usage = usage;

        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (GraphicsDevice gd : local.getScreenDevices()){
            if (gd.getIDstring().equals(hintID)){
                this.device= gd;
                break;
            }
        }

        String none = java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NONE");
        if (hintID.equals(none)){
            isNone = true;
        }else{
            isNone = false;
        }
        
        /*
         * If not found, determine default monitor
         */
        try{
            if (this.device == null){
                if (usage == USAGE_MAIN){
                    /* main, get first monitor that is not the default */
                    for (GraphicsDevice gd : local.getScreenDevices()){
                        if (!gd.getIDstring().equals( local.getDefaultScreenDevice().getIDstring() )){
                            this.device= gd;
                            break;
                        }
                    }
                }else{
                    /* monitor, use default device if system has up to 2 monitor, otherwise try the 3rd */
                    if ( local.getScreenDevices().length <= 2 ){
                        this.device = local.getDefaultScreenDevice();
                    }else{
                        this.device = local.getScreenDevices()[2];
                    }
                }
            }
            // just to double check...
            if (this.device == null){
                this.device = local.getDefaultScreenDevice();
            }
        }catch(Exception e){
            this.device = local.getDefaultScreenDevice();
        }

        this.gconfig = device.getDefaultConfiguration();

        if (this.device == local.getDefaultScreenDevice()){
            this.isDefault = true;
        }else{
            this.isDefault = false;
        }
    }


    public String getName(){
        if (isNone){
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NONE");
        }else{
            return device.getIDstring();
        }
    }

    /**
     * Monitor is allowed only in systems with at least 2 monitors
     * @return
     */
    public static boolean isMonitorAllowed(){
        //return (GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1);
        return true;
    }

    /* Methods used by usage stats */
    public static int getNumDisplays(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
    }

    public boolean isNone(){
        return isNone;
    }
    
    public String getDiplayGeometry(){
        if (usage == USAGE_MONITOR && ! isMonitorAllowed()){
            return "disabled";
        }else{
            return ((int) gconfig.getBounds().getWidth()) + "x"+ ((int) gconfig.getBounds().getHeight());
        }
    }

    public void setWindowFullScreen(JFrame frame){
        // For fake displays, just ignore it
        if (isNone){
            return;
        }

        /**
         * Use setFullScreenWindow only for the default display.
         * This is required to cover OS taskbar and menus,
         * but for Mac, when setting a window full screen it covers all screens
         *
         * Another workaround for windows...
         * By some weird reason, Windows Vista/7 minimize frames set as Fullscreen
         * when they lose focus. Using setBounds works properly in windows, but for
         * MacOS it does not cover the menu bar and dock, so we need platform
         * dependant code here.
         */
        frame.setResizable(true);
        if (isDefault && Platform.isWindows() == false){
            if (device.getFullScreenWindow() == null){
                device.setFullScreenWindow(frame);
            }
        }else{
            frame.setBounds(device.getDefaultConfiguration().getBounds());
        }
        frame.setVisible(true);
    }

    public void closeFullScreen(JFrame frame) {
        frame.setVisible(false);
        if (isDefault && Platform.isWindows() == false){
            device.setFullScreenWindow(null);
        }
    }

    public int getProportionalHeight(int width){

        float fwidth = (float) width;
        float sizewidth = (float) gconfig.getBounds().getWidth();
        float sizeheight = (float) gconfig.getBounds().getHeight();

        return (int) (fwidth * (sizeheight / sizewidth));

    }

    public int getWidth(){
        return (int) gconfig.getBounds().getWidth();
    }

    public int getHeight(){
        return (int) gconfig.getBounds().getHeight();
    }
    
    public Rectangle getBounds(){
        return device.getDefaultConfiguration().getBounds();
    }

}

