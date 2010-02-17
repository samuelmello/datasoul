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
 * ConfigObj.java
 *
 * Created on 22 de Marco de 2006, 21:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.templates.DisplayTemplate;
import datasoul.templates.TemplateManager;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class ConfigObj extends AbstractConfig {
    
    static ConfigObj instance;
    
    private boolean mainOutput;
    private String mainOutputPositionLeft;
    private String mainOutputPositionTop;
    private String mainOutputSizeWidth;
    private String mainOutputSizeHeight;
    private boolean monitorOutput;
    private String monitorOutputPositionLeft;
    private String monitorOutputPositionTop;
    private String monitorOutputSizeWidth;
    private String monitorOutputSizeHeight;
    private int clockMode;    
    private String templateText;
    private String storageLoc;

    private int qualityMain;
    private int qualityMonitor;
    
    public static final int CLOCKMODE_24_SEC = 0;
    public static final int CLOCKMODE_24_NOSEC = 1;
    public static final int CLOCKMODE_12_SEC = 2;
    public static final int CLOCKMODE_12_NOSEC = 3;
    public static final String[] CLOCKMODE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24_Hours_with_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24_Hours_without_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM_with_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM_without_Seconds")};

    public static final int QUALITY_640 = 0;
    public static final int QUALITY_800 = 1;
    public static final int QUALITY_1024 = 2;
    public static final int QUALITY_ORIGINAL = 3;
    public static final String[] QUALITY_TABLE = { "Fast (640 px)", "Normal (800 px)", "High (1024 px)", "Maximum" };

    
    /** Creates a new instance of ConfigObj */
    private ConfigObj() {
        // Default quality:
        this.qualityMain = QUALITY_800;
        this.qualityMonitor = QUALITY_800;
        load("datasoul.config");
    }
    
    public void save(){
        save("datasoul.config");
    }
    
    public static synchronized ConfigObj getInstance(){
        if(instance==null){
            instance = new ConfigObj();
        }
        return instance;
    }

    protected void registerProperties() {
        super.registerProperties();
        properties.add("MainOutputIdx");
        properties.add("MainOutputPositionLeft");
        properties.add("MainOutputPositionTop");
        properties.add("MainOutputSizeWidth");
        properties.add("MainOutputSizeHeight");
        properties.add("MonitorOutputIdx");
        properties.add("MonitorOutputPositionLeft");
        properties.add("MonitorOutputPositionTop");
        properties.add("MonitorOutputSizeWidth");
        properties.add("MonitorOutputSizeHeight");
        properties.add("TemplateText");
        properties.add("ClockModeIdx");        
        properties.add("StorageLoc");
        properties.add("QualityMainIdx");
        properties.add("QualityMonitorIdx");
    }
    
    public ArrayList<String> getProperties(){
        return properties;
    }

    public boolean getMainOutput() {
        return mainOutput;
    }

    public String getMainOutputIdx() {
        if (mainOutput==false){
            return "0";
        }else{
            return "1";
        }
    }

    public void setMainOutput(boolean mainOutput) {
        this.mainOutput = mainOutput;
    }

    public void setMainOutputIdx(String mainOutput) {
        if (mainOutput.equals("0")){
            this.mainOutput = false;
        }else{
            this.mainOutput = true;
        }
    }
    
    public String getMainOutputPositionLeft() {
        return mainOutputPositionLeft;
    }

    public void setMainOutputPositionLeft(String mainOutputPositionLeft) {
        this.mainOutputPositionLeft = mainOutputPositionLeft;
    }

    public String getMainOutputPositionTop() {
        return mainOutputPositionTop;
    }

    public void setMainOutputPositionTop(String mainOutputPositionTop) {
        this.mainOutputPositionTop = mainOutputPositionTop;
    }

    public String getMainOutputSizeWidth() {
        return mainOutputSizeWidth;
    }

    public int getMainOutputSizeWidthAsInt() {
        int ret = DisplayTemplate.TEMPLATE_WIDTH;
        try {
            ret = Integer.parseInt(mainOutputSizeWidth);
        }catch(Exception e){
            // ignore
        }
        return ret;
    }

    public void setMainOutputSizeWidth(String mainOutputSizeWidth) {
        this.mainOutputSizeWidth = mainOutputSizeWidth;
    }

    public String getMainOutputSizeHeight() {
        return mainOutputSizeHeight;
    }

    public int getMainOutputSizeHeightAsInt() {
        int ret = DisplayTemplate.TEMPLATE_HEIGHT;
        try {
            ret = Integer.parseInt(mainOutputSizeHeight);
        }catch(Exception e){
            // ignore
        }
        return ret;
    }

    public void setMainOutputSizeHeight(String mainOutputSizeHeight) {
        this.mainOutputSizeHeight = mainOutputSizeHeight;
    }

    public boolean getMonitorOutput() {
        return monitorOutput;
    }

    public String getMonitorOutputIdx() {
        if (monitorOutput==false){
            return "0";
        }else{
            return "1";
        }
    }

    public void setMonitorOutput(boolean monitorOutput) {
        this.monitorOutput = monitorOutput;
    }

    public void setMonitorOutputIdx(String monitorOutput) {
        if (monitorOutput.equals("0")){
            this.monitorOutput = false;
        }else{
            this.monitorOutput = true;
        }
    }

    public String getMonitorOutputPositionLeft() {
        return monitorOutputPositionLeft;
    }

    public void setMonitorOutputPositionLeft(String monitorOutputPositionLeft) {
        this.monitorOutputPositionLeft = monitorOutputPositionLeft;
    }

    public String getMonitorOutputPositionTop() {
        return monitorOutputPositionTop;
    }

    public void setMonitorOutputPositionTop(String monitorOutputPositionTop) {
        this.monitorOutputPositionTop = monitorOutputPositionTop;
    }
    
    public String getMonitorOutputSizeWidth() {
        return monitorOutputSizeWidth;
    }

    public int getMonitorOutputSizeWidthAsInt() {
        int ret = DisplayTemplate.TEMPLATE_WIDTH;
        try {
            ret = Integer.parseInt(monitorOutputSizeWidth);
        }catch(Exception e){
            // ignore
        }
        return ret;
    }

    public void setMonitorOutputSizeWidth(String monitorOutputSizeWidth) {
        this.monitorOutputSizeWidth = monitorOutputSizeWidth;
    }

    public String getMonitorOutputSizeHeight() {
        return monitorOutputSizeHeight;
    }

    public int getMonitorOutputSizeHeightAsInt() {
        int ret = DisplayTemplate.TEMPLATE_HEIGHT;
        try {
            ret = Integer.parseInt(monitorOutputSizeHeight);
        }catch(Exception e){
            // ignore
        }
        return ret;
    }

    public void setMonitorOutputSizeHeight(String monitorOutputSizeHeight) {
        this.monitorOutputSizeHeight = monitorOutputSizeHeight;
    }

    public int getClockModeIdx() {
        return clockMode;
    }

    public String getClockMode() {
        return CLOCKMODE_TABLE[clockMode];
    }
    
    public void setClockModeIdx(String clockMode) {
        setClockModeIdx(Integer.parseInt(clockMode));
    }
    
    public void setClockModeIdx(int clockMode) {
        this.clockMode = clockMode;
    }

    public void setClockMode(String cm){
        for (int i=0; i<CLOCKMODE_TABLE.length; i++){
            if (cm.equalsIgnoreCase(CLOCKMODE_TABLE[i]))
                setClockModeIdx(i);
        }
    }
    
    public void setMainDisplayEngine(String engine){
        // keep for backward compatibility
    }
    
    public void setMonitorDisplayEngine(String engine){
        // keep for backward compatibility
    }

    public void setMonitorTemplateFilter(String monitorTemplateFilter){
        // keep for backward compatibility
    }

    public void setAlertTemplateFilter(String alertTemplateFilter){
        // keep for backward compatibility
    }
    
    public void setGeneralTemplateFilter(String generalTemplateFilter){
        // keep for backward compatibility
    }
    
    public String getTemplateText(){
        return templateText;
    }

    public void setTemplateText(String templateText){
        this.templateText = templateText;
    }

    public String getStorageLoc(){
        return storageLoc;
    }
    
    public void setStorageLoc(String storageLoc){
        this.storageLoc = storageLoc;
    }

    public void setQualityMainIdx(int i){
        this.qualityMain = i;
    }

    public void setQualityMainIdx(String i){
        setQualityMainIdx(Integer.parseInt(i));
    }

    public void setQualityMain(String str){
        for (int i=0; i<QUALITY_TABLE.length; i++){
            if (str.equalsIgnoreCase(QUALITY_TABLE[i])){
                setQualityMainIdx(i);
            }
        }
    }

    public int getQualityMainIdx(){
        return this.qualityMain;
    }

    public String getQualityMain(){
        return QUALITY_TABLE[this.qualityMain];
    }

    public void setQualityMonitorIdx(int i){
        this.qualityMonitor = i;
    }

    public void setQualityMonitorIdx(String i){
        setQualityMonitorIdx(Integer.parseInt(i));
    }

    public void setQualityMonitor(String str){
        for (int i=0; i<QUALITY_TABLE.length; i++){
            if (str.equalsIgnoreCase(QUALITY_TABLE[i])){
                setQualityMonitorIdx(i);
            }
        }
    }

    public int getQualityMonitorIdx(){
        return this.qualityMonitor;
    }

    public String getQualityMonitor(){
        return QUALITY_TABLE[this.qualityMonitor];
    }

    public int getMainRenderWidth(){

        int ret = getMainOutputSizeWidthAsInt();

        switch(qualityMain){
            case QUALITY_640:
                if (ret > 640)
                    ret = 640;
                break;
            case QUALITY_800:
                if (ret > 800)
                    ret = 800;
                break;
            case QUALITY_1024:
                if (ret > 1024)
                    ret = 1024;
                break;
            case QUALITY_ORIGINAL:
                break;
        }

        return ret;
    }

    public int getMainRenderHeight(){

        float renderwidth = getMainRenderWidth();
        float sizewidth = getMainOutputSizeWidthAsInt();
        float sizeheight = getMainOutputSizeHeightAsInt();

        return (int) (renderwidth * (sizeheight / sizewidth));
    }

    public int getMonitorRenderWidth(){

        int ret = getMonitorOutputSizeWidthAsInt();

        switch(qualityMonitor){
            case QUALITY_640:
                if (ret > 640)
                    ret = 640;
                break;
            case QUALITY_800:
                if (ret > 800)
                    ret = 800;
                break;
            case QUALITY_1024:
                if (ret > 1024)
                    ret = 1024;
                break;
            case QUALITY_ORIGINAL:
                break;
        }

        return ret;
    }

    public int getMonitorRenderHeight(){

        float renderwidth = getMonitorRenderWidth();
        float sizewidth = getMonitorOutputSizeWidthAsInt();
        float sizeheight = getMonitorOutputSizeHeightAsInt();

        return (int) (renderwidth * (sizeheight / sizewidth));
    }


}
