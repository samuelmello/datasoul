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
 * SongListTable.java
 *
 * Created on 8 de Janeiro de 2006, 22:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.serviceitems.song;

import datasoul.util.ListTable;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrador
 */
public class SongListTable extends ListTable{
    
    int songColumn;
    /**
     * Creates a new instance of SongListTable
     */
    public SongListTable() {
        songColumn = 1;
    }

    public int getSongColumn(){
        return songColumn;
    }

    public Song getSong(String name){
        for(int i=0; i<this.getRowCount();i++){
            if(((Song)this.getValueAt(i,songColumn)).getTitle().equalsIgnoreCase(name)){
                return ((Song)this.getValueAt(i,songColumn));
            }                        
        }
        return null;
    }
    
    public void setView(String view){
        for(int i=0;i < this.getRowCount();i++){
            Song song = (Song)this.getValueAt(i,1);
            song.setView(view);
        }
    }

    public String getColumnName(int columnIndex) {
        if(columnIndex==1)
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Song_Name");
        else
            return "";
    }
    
    public int getColumnCount() {
        return 2;
    }    

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            Song song = (Song)this.getValueAt(rowIndex,1);
            return song.getSongIcon();
        }else{
            Object object = objectList.get(rowIndex);
            return  object;
        }
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex==1){
            objectList.set(rowIndex,(String)aValue.toString());
            tableModelChanged();
        }
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        if(column==0){
            return ImageIcon.class;
        }else{
            return Object.class;
        }
    }
    
}
