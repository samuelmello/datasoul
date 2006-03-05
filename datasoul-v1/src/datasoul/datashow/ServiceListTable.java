/*
 * ServiceListTable.java
 *
 * Created on 9 de Janeiro de 2006, 22:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;

/**
 *
 * @author Administrador
 */
public class ServiceListTable extends ListTable{
    
    static ServiceListTable instance;
    /** Creates a new instance of ServiceListTable */
    private ServiceListTable() {
       //so para teste
       Song song = new Song();
       song.setSongName("Opa");
       this.addItem(song);        
    }
    
    static ServiceListTable getInstance(){
        if(instance==null){
            instance = new ServiceListTable();
        }
        return instance;
    }
    
}
