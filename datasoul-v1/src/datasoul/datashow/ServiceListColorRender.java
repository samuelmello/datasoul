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

import datasoul.serviceitems.ContentlessServiceItem;
import datasoul.serviceitems.song.Song;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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

        
        ServiceItem serviceItem;
        if (table.getModel() instanceof ServiceListTable){
            serviceItem = ((ServiceListTable) table.getModel()).getServiceItem(row);
        }else{
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
        
        String text = "";
        if (value != null){
            text = value.toString();
        }
        
        
        if(isSelected){
            setBackground(table.getSelectionBackground());
            setText(text);
            return this;
        }

        Color c = serviceItem.getBackgroundColor();

        if (c != null) {
            setBackground(c);
            setText(text);
        } else {
            super.setValue(value);
        }

        return this;
    }
    
    
}
