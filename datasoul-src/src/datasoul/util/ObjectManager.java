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
 * ObjectManager.java
 *
 * Created on 26 de Junho de 2006, 22:58
 *
 */

package datasoul.util;

import java.awt.Cursor;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigFrame;
import datasoul.config.ConfigObj;
import datasoul.datashow.AlertControlPanel;
import datasoul.datashow.AuxiliarPanel;
import datasoul.datashow.BackgroundConfigFrame;
import datasoul.datashow.LivePanel;
import datasoul.datashow.PreviewPanel;
import datasoul.datashow.TimerControlPanel;
import datasoul.render.SwingDisplayWindow;
import datasoul.render.vlcj.VlcjBackgroundFrame;
import datasoul.templates.TemplateManagerForm;

/**
 *
 * @author Administrador
 */
public class ObjectManager {

    private static ObjectManager instance;
    
    private AuxiliarPanel auxiliarPanel;
    private LivePanel livePanel;
    private PreviewPanel previewPanel;
    private TimerControlPanel timerControlPanel;
    private AlertControlPanel alertControlPanel;
    
    private DatasoulMainForm datasoulMainForm;

    private BackgroundConfigFrame backgroundConfigFrame;
    private TemplateManagerForm templateManagerForm;
    private ConfigFrame configFrame;
    
    private SwingDisplayWindow mainDisplay;
    private SwingDisplayWindow monitorDisplay;
    private VlcjBackgroundFrame mainVideo;
    private boolean isOutputVisible;


    /** Creates a new instance of ObjectManager */
    private ObjectManager() {
    }
    
    public static ObjectManager getInstance(){
        if(instance == null){
            instance = new ObjectManager();
        }
        return instance;
    }
    
    //DATASHOW PANEL OBJECTS
    public AuxiliarPanel getAuxiliarPanel(){
        return auxiliarPanel;
    }
    public LivePanel getLivePanel(){
        return livePanel;
    }
    public PreviewPanel getPreviewPanel(){
        return previewPanel;
    }
    public void setAuxiliarPanel(AuxiliarPanel auxiliarPanel){
        this.auxiliarPanel = auxiliarPanel;
    }
    public void setLivePanel(LivePanel livePanel){
        this.livePanel = livePanel;
    }
    public void setPreviewPanel(PreviewPanel previewPanel){
        this.previewPanel = previewPanel;
    }

    public DatasoulMainForm getDatasoulMainForm() {
        return datasoulMainForm;
    }

    public void setBusyCursor(){
        datasoulMainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
    }
    
    public void setDefaultCursor(){
        datasoulMainForm.setCursor(Cursor.getDefaultCursor());
    }
    
    public void setDatasoulMainForm(DatasoulMainForm datasoulMainForm) {
        this.datasoulMainForm = datasoulMainForm;
    }

    public void setTimerControlPanel(TimerControlPanel pnl){
        this.timerControlPanel = pnl;
    }

    public TimerControlPanel getTimerControlPanel(){
        return timerControlPanel;
    }

    public void setAlertControlPanel(AlertControlPanel acp){
        this.alertControlPanel = acp;
    }
    
    public AlertControlPanel getAlertControlPanel(){
        return alertControlPanel;
    }
    
    /**
     * @return the backgroundConfigFrame
     */
    public BackgroundConfigFrame getBackgroundConfigFrame() {
        if (backgroundConfigFrame == null){
            backgroundConfigFrame = new BackgroundConfigFrame();
            backgroundConfigFrame.setLocationRelativeTo(datasoulMainForm);
        }
        return backgroundConfigFrame;
    }

    /**
     * @param backgroundConfigFrame the backgroundConfigFrame to set
     */
    public void setBackgroundConfigFrame(BackgroundConfigFrame backgroundConfigFrame) {
        this.backgroundConfigFrame = backgroundConfigFrame;
    }

    /**
     * @return the templateManagerForm
     */
    public TemplateManagerForm getTemplateManagerForm() {
        if (templateManagerForm == null){
            templateManagerForm = new TemplateManagerForm();
            templateManagerForm.setLocationRelativeTo(datasoulMainForm);
        }
        return templateManagerForm;
    }

    /**
     * @param templateManagerForm the templateManagerForm to set
     */
    public void setTemplateManagerForm(TemplateManagerForm templateManagerForm) {
        this.templateManagerForm = templateManagerForm;
    }

    /**
     * @return the configFrame
     */
    public ConfigFrame getConfigFrame() {
        if (configFrame == null){
            configFrame = new ConfigFrame();
            configFrame.setLocationRelativeTo(datasoulMainForm);
        }
        return configFrame;
    }

    /**
     * @param configFrame the configFrame to set
     */
    public void setConfigFrame(ConfigFrame configFrame) {
        this.configFrame = configFrame;
    }

    public void initMainDisplay(){

        mainDisplay = new SwingDisplayWindow();
        mainDisplay.registerAsMain();
        mainVideo= new VlcjBackgroundFrame();
        mainVideo.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL - MAIN DISPLAY"));
        mainVideo.registerAsMain();
        
        mainVideo.setOverlay(mainDisplay);
    }

    public void initMonitorDisplay(){

        if (ConfigObj.getActiveInstance().getMonitorOutput() ){
            monitorDisplay = new SwingDisplayWindow();
            //monitorDisplay.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL - MONITOR DISPLAY"));
            monitorDisplay.registerAsMonitor();
        }
    }

    public boolean isOutputVisible(){
        return isOutputVisible;
    }

    public void setOutputVisible(boolean b){

        isOutputVisible = b;

        mainVideo.setVisible(b);
        mainDisplay.setVisible(b);

        if (ConfigObj.getActiveInstance().getMonitorOutput())
            monitorDisplay.setVisible(b);
    }

    public VlcjBackgroundFrame getMainVideoFrame(){
        return mainVideo;
    }

}

