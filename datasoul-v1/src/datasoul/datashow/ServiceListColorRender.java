/*
 * ServiceListColorRender.java
 *
 * Created on 7 de Abril de 2006, 23:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.song.Song;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrador
 */
public class ServiceListColorRender extends DefaultTableCellRenderer{
    
    /** Creates a new instance of ServiceListColorRender */
    public ServiceListColorRender() {
    }

    public void setValue(Object value) { 
        if (value instanceof Song) { 
            setBackground(Color.decode("0xfffff5")); 
//                setForeground(c.getTextColor()); 
            setText(value.toString()); 
        } else if (value instanceof TextServiceItem) { 
            setBackground(Color.decode("0xf5f5ff")); 
//                setForeground(c.getTextColor()); 
            setText(value.toString()); 
        } else { 
            super.setValue(value); 
        } 
    } 
    
}
