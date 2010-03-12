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
import datasoul.datashow.LivePanel;
import datasoul.datashow.PreviewPanel;
import datasoul.datashow.TimerControlPanel;
import datasoul.serviceitems.song.AddSongForm;
import datasoul.serviceitems.song.SongsSearchPanel;
import java.awt.Cursor;

/**
 *
 * @author Administrador
 */
public class ObjectManager {

    // make this match the tab index for simplicity
    public static final int VIEW_SERVICE = 0;
    public static final int VIEW_SONGS = 1;
    public static final int VIEW_PROJECTOR = 2;
    public static final int VIEW_TEMPLATES = 3;
    public static final int VIEW_CONFIG = 4;
    public static final int VIEW_HELP = 5;
    public static final int VIEW_TAB_COUNT = 6;


    // special not a tab
    public static final int VIEW_ADD_SONGS = 6;

    private static ObjectManager instance;
    
    private SongsSearchPanel songsSearchPanel;

    private AuxiliarPanel auxiliarPanel;
    private LivePanel livePanel;
    private PreviewPanel previewPanel;
    private TimerControlPanel timerControlPanel;
    
    private int viewActive;
    
    private AddSongForm addSongForm;
    
    private DatasoulMainForm datasoulMainForm;
    
    /** Creates a new instance of ObjectManager */
    private ObjectManager() {
        setViewActive(this.VIEW_PROJECTOR);
    }
    
    public static ObjectManager getInstance(){
        if(instance == null){
            instance = new ObjectManager();
        }
        return instance;
    }
    
    
    //SONGS PANEL OBJECTS
    public SongsSearchPanel getSongsSearchPanel(){
        return songsSearchPanel;
    }
    public void setSongsSearchPanel(SongsSearchPanel songsSearchPanel){
        this.songsSearchPanel = songsSearchPanel;
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

    public int getViewActive() {
        return viewActive;
    }
    public void setViewActive(int viewActive) {
        this.viewActive = viewActive;
    }

    public AddSongForm getAddSongForm() {
        return addSongForm;
    }
    public void setAddSongForm(AddSongForm addSongForm) {
        this.addSongForm = addSongForm;
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
    
}
