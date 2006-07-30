/*
 * SongListTable.java
 *
 * Created on 8 de Janeiro de 2006, 22:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedCharacterIterator;
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
            
    public void setView(String view){
        for(int i=0;i < this.getRowCount();i++){
            Song song = (Song)this.getValueAt(i,1);
            song.setView(view);
        }
    }

    public String getColumnName(int columnIndex) {
        if(columnIndex==1)
            return "Song Name";
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
