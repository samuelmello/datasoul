/*
 * ConfigObj.java
 *
 * Created on 22 de Marco de 2006, 21:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.render.ContentManager;
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
    private String mainDisplayEngine;
    private String monitorDisplayEngine;
    private String monitorTemplateFilter;
    private String alertTemplateFilter;
    private String generalTemplateFilter;
    private String templateText;
    
    
    public static final int CLOCKMODE_24_SEC = 0;
    public static final int CLOCKMODE_24_NOSEC = 1;
    public static final int CLOCKMODE_12_SEC = 2;
    public static final int CLOCKMODE_12_NOSEC = 3;
    public static final String[] CLOCKMODE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24_Hours_with_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("24_Hours_without_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM_with_Seconds"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("AM/PM_without_Seconds")};

    
    /** Creates a new instance of ConfigObj */
    private ConfigObj() {
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
        //properties.add("MainDisplayEngine");
        //properties.add("MonitorDisplayEngine");
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
        properties.add("MonitorTemplateFilter");
        properties.add("AlertTemplateFilter");
        properties.add("GeneralTemplateFilter");
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

    public void setMainOutputSizeWidth(String mainOutputSizeWidth) {
        this.mainOutputSizeWidth = mainOutputSizeWidth;
    }

    public String getMainOutputSizeHeight() {
        return mainOutputSizeHeight;
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

    public void setMonitorOutputSizeWidth(String monitorOutputSizeWidth) {
        this.monitorOutputSizeWidth = monitorOutputSizeWidth;
    }

    public String getMonitorOutputSizeHeight() {
        return monitorOutputSizeHeight;
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
    
    public String getMainDisplayEngine(){
        return this.mainDisplayEngine;
    }
    
    public void setMainDisplayEngine(String engine){
        this.mainDisplayEngine = engine;
        ContentManager.setMainDisplayEngine(engine);
    }
    
    public String getMonitorDisplayEngine(){
        return this.monitorDisplayEngine;
    }
    
    public void setMonitorDisplayEngine(String engine){
        this.monitorDisplayEngine = engine;
        ContentManager.setMonitorDisplayEngine(engine);
    }

    

    public void setMonitorTemplateFilter(String monitorTemplateFilter){
        this.monitorTemplateFilter = monitorTemplateFilter;
        TemplateManager.getInstance().tableModelChanged();
    }

    public String getMonitorTemplateFilter(){
        if (monitorTemplateFilter == null){
            return "";
        }else{
            return monitorTemplateFilter;
        }
    }
    
    public void setAlertTemplateFilter(String alertTemplateFilter){
        this.alertTemplateFilter = alertTemplateFilter;
        TemplateManager.getInstance().tableModelChanged();
    }
    
    public String getAlertTemplateFilter(){
        if (alertTemplateFilter == null){
            return "";
        }else{
            return alertTemplateFilter;
        }
    }

    public void setGeneralTemplateFilter(String generalTemplateFilter){
        this.generalTemplateFilter = generalTemplateFilter;
        TemplateManager.getInstance().tableModelChanged();
    }
    
    public String getGeneralTemplateFilter(){
        if (generalTemplateFilter == null){
            return "";
        }else{
            return generalTemplateFilter;
        }
    }

    public String getTemplateText(){
        return templateText;
    }

    public void setTemplateText(String templateText){
        this.templateText = templateText;
    }

}
