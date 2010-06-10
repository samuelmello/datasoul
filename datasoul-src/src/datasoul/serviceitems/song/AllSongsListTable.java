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
 * AllSongsListTable.java
 *
 * Created on 5 de Marco de 2006, 00:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.serviceitems.song;

import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.util.ShowDialog;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class AllSongsListTable extends SongListTable{
    
    private static AllSongsListTable instance;
    
    /** Creates a new instance of AllSongsListTable */
    private AllSongsListTable() {
        String path = ConfigObj.getActiveInstance().getStoragePathSongs();
        
        File file = new File(path);
        String[] files = file.list();
        
        // there is at least one file in the directroy?
        if (files!=null){
            int size = files.length;
            
            for(int i=0; i<size;i++){
                if(files[i].contains(".song")){
                    refreshSong(files[i]);
                }
            }
            
        }//if there is any file
        this.setView("FileName");
        this.sortByName();
    }
    
    public void refreshSong(String name){
        String path = ConfigObj.getActiveInstance().getStoragePathSongs();
        File songFile = new File(path + File.separator + name);

        Document dom=null;
        Node node = null;
        Song song;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(songFile);

            //node = dom.getDocumentElement().getChildNodes().item(0);
            node = dom.getElementsByTagName("Song").item(0);

        }catch(Exception e) {
            ShowDialog.showReadFileError(songFile, e);
        }

        song = new Song();
        try {
            song.readObject(node, null);
            song.setFilePath(songFile.getPath());
        } catch (Exception e) {
            ShowDialog.showReadFileError(songFile, e);
        }

        // Clean up some fields that don't make sense 
        // in the the song list
        song.setDuration("0");
        song.setNotes("");
        song.setTemplate(DisplayControlConfig.getInstance().getDefaultTemplateSong());

        // If already exists, remove old entry
        ArrayList<Song> toRemove = new ArrayList<Song>();
        for (Object obj : objectList){
            if (obj instanceof Song){
                if ( ((Song)obj).getTitle().equals(song.getTitle()) ){
                    toRemove.add((Song)obj);
                }
            }
        }
        objectList.removeAll(toRemove);

        this.addItem(song);
    }

    public static AllSongsListTable getInstance(){
        if(instance==null){
            instance = new AllSongsListTable();
        }
        return instance;
    }

    public void updateDefaultTemplate() {
        for (Object obj : objectList){
            if (obj instanceof Song){
                ((Song)obj).setTemplate(DisplayControlConfig.getInstance().getDefaultTemplateSong());
            }
        }
    }

    public int getSongCount(){
        return objectList.size();
    }

    public int getSongsWithChords(){

        int i = 0;

        for (Object obj : objectList){
            if (obj instanceof Song){
                Song s = ((Song)obj);
                if (s.getChordsComplete().trim().length() > 0 ||
                        s.getChordsSimplified().trim().length() > 0){
                    i++;
                }
            }
        }

        return i;

    }
    
}

