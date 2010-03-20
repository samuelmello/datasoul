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

import datasoul.render.OutputDevice;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class ConfigObj extends AbstractConfig {
    
    static ConfigObj activeInstance;
    static ConfigObj nextInstance;
    
    private boolean monitorOutput;
    private boolean detectMonitors;
    private int clockMode;    
    private String storageLoc;

    private OutputDevice mainOutputDevice;
    private OutputDevice monitorOutputDevice;

    private boolean isGstreamerActive;

    private int qualityMain;
    
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
        this.isGstreamerActive = true;
        // Default output devices
        setMainOutputDevice("");
        setMonitorOutputDevice("");
        // Now load
        load("datasoul.config");
    }
    
    public void save(){
        save("datasoul.config");
    }
    
    public static synchronized ConfigObj getActiveInstance(){
        if(activeInstance==null){
            activeInstance = new ConfigObj();
        }
        return activeInstance;
    }

    public static synchronized ConfigObj getNextInstance(){
        if(nextInstance==null){
            nextInstance = new ConfigObj();
        }
        return nextInstance;
    }

    protected void registerProperties() {
        super.registerProperties();
        properties.add("DetectMonitorsIdx"); // keep as first
        properties.add("MonitorOutputIdx");
        properties.add("ClockModeIdx");        
        properties.add("StorageLoc");
        properties.add("QualityMainIdx");
        properties.add("MainOutputDevice");
        properties.add("MonitorOutputDevice");
    }
    
    public ArrayList<String> getProperties(){
        return properties;
    }


    public void setMainOutputIdx(String mainOutput) {
        // keep for backward compatibility
    }

    public void setMainOutputPositionLeft(String mainOutputPositionLeft) {
        // Keep for backward compatibility
    }

    public void setMainOutputPositionTop(String mainOutputPositionTop) {
        // Keep for backward compatibility
    }

    public void setMainOutputSizeWidth(String mainOutputSizeWidth) {
        // Keep for backward compatibility
    }

    public void setMainOutputSizeHeight(String mainOutputSizeHeight) {
        // Keep for backward compatibility
    }

    public boolean getMonitorOutput() {
        return monitorOutput && OutputDevice.isMonitorAllowed();
    }

    public String getMonitorOutputIdx() {
        if (getMonitorOutput()==false){
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

    public boolean getDetectMonitors() {
        return detectMonitors;
    }

    public String getDetectMonitorsIdx() {
        if (detectMonitors==false){
            return "0";
        }else{
            return "1";
        }
    }

    public void setDetectMonitors(boolean detectMonitors) {
        this.detectMonitors = detectMonitors;
    }

    public void setDetectMonitorsIdx(String detectMonitors) {
        if (detectMonitors.equals("0")){
            this.detectMonitors = false;
        }else{
            this.detectMonitors = true;
        }
    }

    public void setMonitorOutputPositionLeft(String monitorOutputPositionLeft) {
        // Keep for backward compatibility
    }

    public void setMonitorOutputPositionTop(String monitorOutputPositionTop) {
        // Keep for backward compatibility
    }
    
    public void setMonitorOutputSizeWidth(String monitorOutputSizeWidth) {
        // Keep for backward compatibility
    }

    public void setMonitorOutputSizeHeight(String monitorOutputSizeHeight) {
        // Keep for backward compatibility
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
    
    public void setTemplateText(String templateText){
        // keep for backward compatibility
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

    public int getMainRenderWidth(){

        int ret = mainOutputDevice.getWidth();

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

        return mainOutputDevice.getProportionalHeight(getMainRenderWidth());
    }

    public int getMonitorRenderWidth(){

        int ret = monitorOutputDevice.getWidth();

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

    public int getMonitorRenderHeight(){
        return monitorOutputDevice.getProportionalHeight(getMonitorRenderWidth());
    }

    public String getMainOutputDevice(){
        if (mainOutputDevice != null && ! detectMonitors){
            return mainOutputDevice.getName();
        }else{
            return "";
        }
    }

    public void setMainOutputDevice(String s){
        this.mainOutputDevice = new OutputDevice(s, OutputDevice.USAGE_MAIN);
    }

    public String getMonitorOutputDevice(){
        if (monitorOutputDevice != null && ! detectMonitors && OutputDevice.isMonitorAllowed()){
            return monitorOutputDevice.getName();
        }else{
            return "";
        }
    }

    public void setMonitorOutputDevice(String s){
        if (OutputDevice.isMonitorAllowed()){
            this.monitorOutputDevice = new OutputDevice(s, OutputDevice.USAGE_MONITOR);
        }
    }

    public OutputDevice getMainOutputDeviceObj(){
        return mainOutputDevice;
    }

    public OutputDevice getMonitorOutputDeviceObj(){
        return monitorOutputDevice;
    }

    public boolean isGstreamerActive(){
        return isGstreamerActive;
    }

    public void setGstreamerActive(boolean b){
        this.isGstreamerActive = b;
    }

}
