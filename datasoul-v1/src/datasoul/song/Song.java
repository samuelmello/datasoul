/*
 * Song.java
 *
 * Created on 3 de Janeiro de 2006, 20:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public class Song extends ServiceItem{
  
    private String songName="";
    private String songAuthor="";
    private String lyrics="";
    private String chordsComplete="";
    private String chordsSimplified="";
    private String obs="";

    private String filePath="";        
    private String view="SongName";
    
    /** Creates a new instance of Song */
    public Song() {
        super();
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("SongName");
        properties.add("SongAuthor");
        properties.add("Lyrics");
        properties.add("ChordsComplete");
        properties.add("ChordsSimplified");
        properties.add("Obs");

    }

    public void setSongName(String songName){
        this.songName = songName;
        this.setTitle(songName);
    }
    public void setSongAuthor(String songAuthor){
        this.songAuthor = songAuthor;
    }
    public void setLyrics(String lyrics){
        lyrics = lyrics.trim();
        
        this.lyrics = lyrics;

        String slidesStr[] ;
        if(lyrics.contains("\r\n")){
            slidesStr = lyrics.split("\r\n\r\n");
        }else{
            slidesStr = lyrics.split("\n\n");
        }
        slides.clear();
        TextServiceItemRenderer j;
        for (int i=0; i<slidesStr.length; i++){
            j = new TextServiceItemRenderer();
            j.setText(slidesStr[i]);
            slides.add(j);
        }        
    }
    
    public void setChordsComplete(String chordsComplete){
        this.chordsComplete = chordsComplete;
    }
    public void setChordsSimplified(String chordsSimplified){
        this.chordsSimplified = chordsSimplified;
    }
    public void setObs(String obs){
        this.obs = obs;
    }

    public String getSongName(){
        return this.songName;
    }
    
    public String getSongAuthor(){
        return this.songAuthor;
    }
    
    public String getLyrics(){
        return this.lyrics;
    }
    
    public String getChordsComplete(){
        return this.chordsComplete;
    }
    
    public String getChordsSimplified(){
        return this.chordsSimplified;
    }
    
    public String getObs(){
        return this.obs;
    }

    public String getFileName(){
        
        return this.getSongName();
    }
    public void setFilePath(String file){
        this.filePath = file;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public boolean containsStringInField(String field, String strSearch) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        
        return (boolean)(((String)this.getClass().getMethod("get"+field).invoke(this)).toLowerCase().contains(strSearch.toLowerCase()));     
    }
    
    public void setView(String view){
        this.view = view;
    }

    public String getView(){
        return this.view;
    }
    
    public String toString(){
        String ret=this.songName;
        try {
            ret = (String)this.getClass().getMethod("get"+this.view).invoke(this);  
        } catch (Exception ex) {
            System.out.println("Error in getValueAt!\nError:"+ex.getMessage());
        }
        
        return ret;
    }
    

}
