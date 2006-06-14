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
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Administrador
 */
public class SongListTable extends ListTable{
    
    /**
     * Creates a new instance of SongListTable
     */
    public SongListTable() {
    }

    public void setView(String view){
        for(int i=0;i < this.getRowCount();i++){
            Song song = (Song)this.getValueAt(i,0);
            song.setView(view);
        }
    }

    public String getColumnName(int columnIndex) {
        return "Song Name";
    }
    
}
