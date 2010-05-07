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
 * Alert.java
 *
 * Created on March 29, 2006, 10:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.config.DisplayControlConfig;
import datasoul.render.ContentManager;

/**
 *
 * @author samuelm
 */
public class Alert extends Thread {
    
    private int time;
    private String text;
    private boolean showOnMain;
    private boolean showOnMonitor;
    private String mainTemplate;
    private String monitorTemplate;

    private AlertControlPanel panel;
    
    public Alert(AlertControlPanel panel){
        this.panel = panel;
    }
    
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isShowOnMain() {
        return showOnMain;
    }

    public void setShowOnMain(boolean showOnMain) {
        this.showOnMain = showOnMain;
    }

    public boolean isShowOnMonitor() {
        return showOnMonitor;
    }

    public void setShowOnMonitor(boolean showOnMonitor) {
        this.showOnMonitor = showOnMonitor;
    }
    
    public void run(){
        
        if ( showOnMain == false && showOnMonitor == false) {
            return;
        }
        
        
        // show
        ContentManager cm = ContentManager.getInstance();
        if (showOnMain){
            ContentManager.getInstance().setAlertTemplateMain(getMainTemplate());
            ContentManager.getInstance().setAlertActiveMain(true);
        }
        if (showOnMonitor){
            ContentManager.getInstance().setAlertTemplateMonitor(getMonitorTemplate());
            ContentManager.getInstance().setAlertActiveMonitor(true);
        }
        cm.setAlertText(text);
        cm.alertShow( DisplayControlConfig.getInstance().getSlideShowHideTime() );
        
        
        // wait
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            //ex.printStackTrace();
        }

        // hide
        if (showOnMain){
            ContentManager.getInstance().setAlertActiveMain(false);
        }
        if (showOnMonitor){
            ContentManager.getInstance().setAlertActiveMonitor(false);
        }
        cm.alertHide(DisplayControlConfig.getInstance().getSlideShowHideTime());
        
        panel.notifyAlertEnd();
    }

    public String getMainTemplate() {
        return mainTemplate;
    }

    public void setMainTemplate(String mainTemplate) {
        this.mainTemplate = mainTemplate;
    }

    public String getMonitorTemplate() {
        return monitorTemplate;
    }

    public void setMonitorTemplate(String monitorTemplate) {
        this.monitorTemplate = monitorTemplate;
    }
           
    
    
}

