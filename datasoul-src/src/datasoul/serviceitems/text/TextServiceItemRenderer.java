/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
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
 * TextServiceItemRenderer.java
 *
 * Created on February 10, 2006, 10:16 PM
 *
 */

package datasoul.serviceitems.text;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import datasoul.serviceitems.ServiceItemRenderer;

/**
 *
 * @author samuelm
 */
public class TextServiceItemRenderer implements ServiceItemRenderer {
    
    private TextServiceItemTextArea area;
    
    private boolean showMark;

    private static Color selectedBackground;
    private static Color notSelectedBackground;
    private static Color selectedForeground;
    private static Color notSelectedForeground;

    static {
        JTable aux = new JTable();
        selectedBackground = new Color( aux.getSelectionBackground().getRGB()) ;
        selectedForeground = new Color( aux.getSelectionForeground().getRGB());
        notSelectedBackground = new Color( aux.getBackground().getRGB());
        notSelectedForeground = new Color( aux.getForeground().getRGB());
    }
    
    /** Creates a new instance of TextServiceItemRenderer */
    public TextServiceItemRenderer() {
        area = new TextServiceItemTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(true);
        area.setDoubleBuffered(true);
    }
    
    public void setWidth(int width) {
        area.setSize(width, 10000);
    }
    
    public int getHeight() {
        return area.getPreferredSize().height;
    }
    
    public Component getComponent(boolean selected, boolean hasFocus) {

        if (selected){
            area.setBackground(selectedBackground);
            area.setForeground(selectedForeground);
        }else{
            area.setBackground(notSelectedBackground);
            area.setForeground(notSelectedForeground);
        }
        
        return area;
    }
    
    public void setText(String t){
        area.setText(t);
    }

    public String getText(){
        return area.getText();
    }
    
    public boolean getShowMark() {
        return this.showMark;
    }
    
    public void setMark(boolean showMark){
        this.showMark = showMark;
        area.setShowMark(showMark);
    }

    public boolean getMark() {
        return showMark;
    }
    
}

