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

import datasoul.servicelist.ContentlessServiceItem;
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

        Object serviceItem = ServiceListTable.getActiveInstance().getServiceItem(row);
        String text = "";
        if (value != null){
            text = value.toString();
        }
        
        
        if(isSelected){
            setBackground(Color.decode("0xb8cfe5"));
            setText(text);
            return this;
        }

        if (serviceItem instanceof Song) {
            setBackground(Color.decode("0xddddff"));
            setText(text);
        } else if (serviceItem instanceof TextServiceItem) {
            setBackground(Color.decode("0xffffdd"));
            setText(text);
        } else if (serviceItem instanceof ContentlessServiceItem) {
            setBackground(Color.decode("0xddffff"));
            setText(text);
        } else {
            super.setValue(value);
        }

        return this;
    }
    
    
}
