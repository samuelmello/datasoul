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
 */

package datasoul.config;

import java.io.File;
import java.util.ArrayList;

import datasoul.render.OutputDevice;
import datasoul.util.ObjectManager;

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
    private boolean storageLocChecked;
    private boolean trackDuration;
    private boolean autoStartTimer;
    private String sofficePath;
    private boolean acceptRemoteDisplays;
    private boolean acceptRemoteAlerts;

    private OutputDevice mainOutputDevice;
    private OutputDevice monitorOutputDevice;

    private boolean onlineCheckUpdate;
    private boolean onlineUsageStats;

    public static final int CLOCKMODE_24_SEC = 0;
    public static final int CLOCKMODE_24_NOSEC = 1;
    public static final int CLOCKMODE_12_SEC = 2;
    public static final int CLOCKMODE_12_NOSEC = 3;
    public static final String[] CLOCKMODE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24 HOURS WITH SECONDS"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24 HOURS WITHOUT SECONDS"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM WITH SECONDS"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM WITHOUT SECONDS")};

    /** Creates a new instance of ConfigObj */
    private ConfigObj() {
        this.onlineCheckUpdate = true;
        this.autoStartTimer = true;
        this.sofficePath = "soffice";
        // Default output devices
        setMainOutputDevice("");
        setMonitorOutputDevice("");
        detectMonitors = true;
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
        properties.add("MainOutputDevice");
        properties.add("MonitorOutputDevice");
        properties.add("OnlineCheckUpdate");
        properties.add("OnlineUsageStats");
        properties.add("TrackDuration");
        properties.add("AutoStartTimer");
        properties.add("SofficePath");
        properties.add("AcceptRemoteDisplays");
        properties.add("AcceptRemoteAlerts");
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
        if (storageLocChecked == false){
            File f = new File(storageLoc);
            if (! f.exists() ){
                storageLoc = System.getProperty("user.home")+File.separator+".datasoul"+File.separator+"data";

                File datadir = new File(storageLoc);
                if (!datadir.exists()){
                    datadir.mkdirs();
                }

                ConfigObj.getActiveInstance().save();
            }
            storageLocChecked = true;
        }
        return storageLoc;
    }
    
    public void setStorageLoc(String storageLoc){
        this.storageLoc = storageLoc;
    }

    public int getMainRenderWidth(){
        return  mainOutputDevice.getWidth();
    }

    public int getMainRenderHeight(){
        return mainOutputDevice.getProportionalHeight(getMainRenderWidth());
    }

    public int getMonitorRenderWidth(){
        return monitorOutputDevice.getWidth();
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

    public String getOnlineCheckUpdate(){
        if (onlineCheckUpdate){
            return "1";
        }else{
            return "0";
        }
    }

    public void setOnlineCheckUpdate(String s){
        if (s.equals("0")){
            onlineCheckUpdate = false;
        }else{
            onlineCheckUpdate = true;
        }
    }

    public boolean getOnlineCheckUpdateBool(){
        return onlineCheckUpdate;
    }

    public String getOnlineUsageStats(){
        if (onlineUsageStats){
            return "1";
        }else{
            return "0";
        }
    }

    public void setOnlineUsageStats(String s){
        if (s.equals("0")){
            onlineUsageStats = false;
        }else{
            onlineUsageStats = true;
        }
    }

    public boolean getOnlineUsageStatsBool(){
        return onlineUsageStats;
    }

    public String getStoragePathLegacyTemplates(){
        return getStorageLoc() + File.separator + "templates";
    }

    public String getStoragePathTemplates(){
        return getStorageLoc() + File.separator + "templatez";
    }

    public String getStoragePathSongs(){
        return getStorageLoc() + File.separator + "songs";
    }
    
    public String getStoragePathServiceLists(){
        return getStorageLoc() + File.separator + "servicelists";
    }

    public String getTrackDuration(){
        if (trackDuration){
            return "1";
        }else{
            return "0";
        }
    }

    public boolean getTrackDurationBool(){
        return trackDuration;
    }

    public void setTrackDuration(String s){
        trackDuration = s.equals("1");
    }

    public String getAutoStartTimer(){
        if (autoStartTimer){
            return "1";
        }else{
            return "0";
        }
    }

    public boolean getAutoStartTimerBool(){
        return autoStartTimer;
    }

    public void setAutoStartTimer(String s){
        autoStartTimer = s.equals("1");
    }

    public String getSofficePath(){
        return sofficePath;
    }

    public void setSofficePath(String s){
        this.sofficePath = s;
    }

    public String getAcceptRemoteDisplays(){
        if (acceptRemoteDisplays){
            return "1";
        }else{
            return "0";
        }
    }

    public boolean getAcceptRemoteDisplaysBool(){
        return acceptRemoteDisplays;
    }

    public void setAcceptRemoteDisplays(String s){
        acceptRemoteDisplays = s.equals("1");
    }

    public String getAcceptRemoteAlerts(){
        if (acceptRemoteAlerts){
            return "1";
        }else{
            return "0";
        }
    }

    public boolean getAcceptRemoteAlertsBool(){
        return acceptRemoteAlerts;
    }

    public void setAcceptRemoteAlerts(String s){
        acceptRemoteAlerts = s.equals("1");
    }


}
