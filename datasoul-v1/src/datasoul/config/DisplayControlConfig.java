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
 * DisplayControlConfig.java
 *
 * Created on 6 November 2006, 23:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.serviceitems.song.AllSongsListTable;

/**
 *
 * @author samuelm
 */
public class DisplayControlConfig extends AbstractConfig {
    
    
    private static DisplayControlConfig instance = null; 
    
    public static synchronized DisplayControlConfig getInstance(){
        if (instance == null){
            instance = new DisplayControlConfig();
        }
        return instance;
    }
    
    /** Creates a new instance of ConfigObj */
    private DisplayControlConfig() {
        // Default value
        automaticGoLive = true;

        load("displayControl.config");
    }
    
    public void save(){
        save("displayControl.config");
    }
    
    private int slideTransitionTime;
    private int slideShowHideTime;
    private boolean monitorFollowMainControls;
    private String templateMonitor;

    private boolean automaticGoLive;

    private String defaultTemplateText;
    private String defaultTemplateSong;
    private String defaultTemplateImage;
    private String monitorTemplateText;
    private String monitorTemplateSong;
    private String monitorTemplateImage;
    private String monitorTemplateContentless;


    public int getSlideTransitionTime() {
        return slideTransitionTime;
    }

    public void setSlideTransitionTime(String slideTransitionTime) {
        int x = -1;
        try{
            x = Integer.parseInt(slideTransitionTime);
        }catch(Exception e){
            // do nothing
        }
        setSlideTransitionTime(x);
    }
    
    public void setSlideTransitionTime(int slideTransitionTime) {
        if (slideTransitionTime >= 0){
            this.slideTransitionTime = slideTransitionTime;
        }else{
            this.slideTransitionTime = 0;
        }
        save();
    }

    public int getSlideShowHideTime() {
        return slideShowHideTime;
    }

    public void setSlideShowHideTime(String slideShowHideTime) {
        int x = -1;
        try{
            x = Integer.parseInt(slideShowHideTime);
        }catch(Exception e){
            // do nothing
        }
        setSlideShowHideTime(x);
    }
    
    public void setSlideShowHideTime(int slideShowHideTime) {
        if (slideShowHideTime >= 0){
            this.slideShowHideTime = slideShowHideTime;
        }else{
            this.slideShowHideTime = 0;
        }
        save();
    }
    
    public boolean getMonitorFollowMainControls(){
        return monitorFollowMainControls;
    }

    public String getMonitorFollowMainControlsIdx(){
        if (monitorFollowMainControls==false){
            return "0";
        }else{
            return "1";
        }
    }
    
    public void setMonitorFollowMainControls(boolean b){
        monitorFollowMainControls = b;
        save();
    }
    
    public void setMonitorFollowMainControlsIdx(String str){
        setMonitorFollowMainControls( str.equals("1") );
    }

    public String getTemplateMonitor() {
        return templateMonitor;
    }

    public void setTemplateMonitor(String templateMonitor) {
        this.templateMonitor = templateMonitor;
        save();
    }

    public String getAutomaticGoLive(){
        if (automaticGoLive)
            return "1";
        else
            return "0";
    }

    public boolean getAutomaticGoLiveBool(){
        return automaticGoLive;
    }

    public void setAutomaticGoLive(boolean b){
        this.automaticGoLive = b;
        save();
    }

    public void setAutomaticGoLive(String str){
        setAutomaticGoLive( str.equals("1") );
    }

    public String getDefaultTemplateText() {
        if (defaultTemplateText != null)
            return defaultTemplateText;
        else
            return "";
    }

    public void setDefaultTemplateText(String defaultTemplateText) {
        this.defaultTemplateText = defaultTemplateText;
        save();
    }

    public String getDefaultTemplateSong() {
        if (defaultTemplateSong != null)
            return defaultTemplateSong;
        else
            return "";
    }

    public void setDefaultTemplateSong(String defaultTemplateSong) {
        boolean needUpdate = false;
        if (this.defaultTemplateSong != null && !this.defaultTemplateSong.equals(defaultTemplateSong)){
            needUpdate = true;
        }
        this.defaultTemplateSong = defaultTemplateSong;
        save();
        if (needUpdate){
            AllSongsListTable.getInstance().updateDefaultTemplate();
        }
    }

    public String getDefaultTemplateImage() {
        if (defaultTemplateImage != null)
            return defaultTemplateImage;
        else
            return "";
    }

    public void setDefaultTemplateImage(String defaultTemplateImage) {
        this.defaultTemplateImage = defaultTemplateImage;
        save();
    }

    public String getMonitorTemplateText() {
        if (monitorTemplateText != null){
            return monitorTemplateText;
        }else{
            return "";
        }
    }

    public void setMonitorTemplateText(String monitorTemplateText) {
        this.monitorTemplateText = monitorTemplateText;
        save();
    }

    public String getMonitorTemplateSong() {
        if (monitorTemplateSong != null){
            return monitorTemplateSong;
        }else{
            return "";
        }
    }

    public void setMonitorTemplateSong(String monitorTemplateSong) {
        this.monitorTemplateSong = monitorTemplateSong;
        save();
    }

    public String getMonitorTemplateImage() {
        if (monitorTemplateImage != null){
            return monitorTemplateImage;
        }else{
            return "";
        }
    }

    public void setMonitorTemplateImage(String monitorTemplateImage) {
        this.monitorTemplateImage = monitorTemplateImage;
        save();
    }

    public String getMonitorTemplateContentless() {
        if (monitorTemplateContentless != null){
            return monitorTemplateContentless;
        }else{
            return "";
        }
    }

    public void setMonitorTemplateContentless(String monitorTemplateContentless) {
        this.monitorTemplateContentless = monitorTemplateContentless;
        save();
    }


    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("SlideTransitionTime");
        properties.add("SlideShowHideTime");
        properties.add("MonitorFollowMainControlsIdx");
        properties.add("TemplateMonitor");
        properties.add("AutomaticGoLive");
        properties.add("DefaultTemplateText");
        properties.add("DefaultTemplateSong");
        properties.add("DefaultTemplateImage");
        properties.add("MonitorTemplateText");
        properties.add("MonitorTemplateSong");
        properties.add("MonitorTemplateImage");
        properties.add("MonitorTemplateContentless");
    }
    
}

