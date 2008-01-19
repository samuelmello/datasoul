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

package datasoul.song;

import datasoul.util.ShowDialog;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public class AllSongsListTable extends SongListTable{
    
    private static AllSongsListTable instance;
    
    /** Creates a new instance of AllSongsListTable */
    private AllSongsListTable() {
        String path = System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "songs";
        
        File file = new File(path);
        String[] files = file.list();
        
        // there is at least one file in the directroy?
        if (files!=null){
            int size = files.length;
            
            for(int i=0; i<size;i++){
                if(files[i].contains(".song")){
                    File songFile = new File(path + System.getProperty("file.separator") + files[i]);
                    
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
                        song.readObject(node);
                        song.setFilePath(songFile.getPath());
                    } catch (Exception e) {
                        ShowDialog.showReadFileError(songFile, e);
                    }
                    
                    this.addItem(song);
                }
            }
            
        }//if there is any file
        this.setView("FileName");
        this.sortByName();
    }
    
    public static AllSongsListTable getInstance(){
        if(instance==null){
            instance = new AllSongsListTable();
        }
        return instance;
    }
    
}
