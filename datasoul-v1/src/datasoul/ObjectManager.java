/*
 * ObjectManager.java
 *
 * Created on 26 de Junho de 2006, 22:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul;

import datasoul.datashow.AuxiliarPanel;
import datasoul.datashow.LivePanel;
import datasoul.datashow.PreviewPanel;
import datasoul.datashow.ServiceListPanel;
import datasoul.song.AddSongForm;
import datasoul.song.ChordsManagerPanel;
import datasoul.song.SongViewerPanel;
import datasoul.song.SongsSearchPanel;

/**
 *
 * @author Administrador
 */
public class ObjectManager {
    
    public static int VIEW_PROJECTOR = 0;
    public static int VIEW_SONGS = 1;
    public static int VIEW_TEMPLATES = 2;
    public static int VIEW_CONFIG = 3;
    public static int VIEW_ADD_SONGS = 4;    

    private static ObjectManager instance;
    
    private ChordsManagerPanel chordsManagerPanel1;
    private ServiceListPanel serviceListPanel;
    private SongViewerPanel songViewerPanel;
    private SongsSearchPanel songsSearchPanel;

    private AuxiliarPanel auxiliarPanel;
    private LivePanel livePanel;
    private PreviewPanel previewPanel;
    
    private int viewActive;
    
    private AddSongForm addSongForm;
    
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
    public ChordsManagerPanel getChordsManagerPanel(){
        return chordsManagerPanel1;
    }
    public ServiceListPanel getPraiseListPanel(){
        return serviceListPanel;
    }
    public SongViewerPanel getSongViewerPanel(){
        return songViewerPanel;
    }
    public SongsSearchPanel getSongsSearchPanel(){
        return songsSearchPanel;
    }
    public void setChordsManagerPanel(ChordsManagerPanel chordsManagerPanel){
        this.chordsManagerPanel1 = chordsManagerPanel;
    }
    public void setPraiseListPanel(ServiceListPanel serviceListPanel){
        this.serviceListPanel = serviceListPanel;
    }
    public void setSongViewerPanel(SongViewerPanel songViewerPanel){
        this.songViewerPanel = songViewerPanel;
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
    public ServiceListPanel getServiceListPanel(){
        return serviceListPanel;
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
    public void setServiceListPanel(ServiceListPanel serviceListPanel){
        this.serviceListPanel = serviceListPanel;
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
}
