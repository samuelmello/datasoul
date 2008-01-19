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

    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {

        if(isSelected){
            setBackground(Color.decode("0xb8cfe5"));
            setText(value.toString());
            return this;
        }

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

        return this;
    }
    
    
}
