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
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.DatasoulMainForm;
import datasoul.datashow.AuxiliarPanel;
import datasoul.datashow.BackgroundConfigFrame;
import datasoul.datashow.LivePanel;
import datasoul.datashow.PreviewPanel;
import datasoul.datashow.TimerControlPanel;
import datasoul.templates.TemplateManagerForm;
import datasoul.config.ConfigFrame;
import java.awt.Cursor;

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
    
    private DatasoulMainForm datasoulMainForm;

    private BackgroundConfigFrame backgroundConfigFrame;
    private TemplateManagerForm templateManagerForm;
    private ConfigFrame configFrame;
    
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
    
}

