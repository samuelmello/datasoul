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
 * TextServiceItem.java
 *
 * Created on February 10, 2006, 9:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.config.DisplayControlConfig;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author samuelm
 */
public class TextServiceItem extends ServiceItem {
    
    private String text;
    public final static String SLIDE_BREAK = "==";
    public final static String CHORUS_MARK = "===";
    
    /** Creates a new instance of TextServiceItem */
    public TextServiceItem() {
        super();
        this.text = "";
        this.template = DisplayControlConfig.getInstance().getDefaultTemplateText();
        cellEditor = new TextAreaCellEditor();
    }
    
    protected void registerProperties() {
        super.registerProperties();
        properties.add("Text");        
    }
             
    public void setText(String text){
        
        this.text = text.trim().replace("\r\n", "\n");
        
        String chorus[];
        chorus = this.text.trim().split("\n"+TextServiceItem.CHORUS_MARK+"\n");
        
        slides.clear();
        for (int k=0; k<chorus.length; k++){

            String slidesStr[] = chorus[k].split("\n"+TextServiceItem.SLIDE_BREAK+"\n");
            TextServiceItemRenderer j;
            for (int i=0; i<slidesStr.length; i++){
                j = new TextServiceItemRenderer();
                String str = slidesStr[i].replace("\n\n","\n \n");
                j.setText(str);
                if (i==0){
                    j.setMark(true);
                }
                slides.add(j);
            }
        }
    }
    
    public String getText(){
        return this.text;
    }

    public String getSlideText (int rowIndex){
        
        try{
            TextServiceItemRenderer r = (TextServiceItemRenderer) slides.get(rowIndex);
            return r.getText();
        }catch(Exception e){
            return "";
        }
        
    }
    
    @Override 
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1){
            return true;
        }else{
            return false;
        }
            
    }

    
    public class TextAreaCellEditor extends AbstractCellEditor implements TableCellEditor {
        
        public Object getCellEditorValue() {
            fireTableChanged();
            return null;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            TextServiceItemRenderer r = (TextServiceItemRenderer) slides.get(row);
            return r.getComponent(false, true);
        }
        
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= 2;
            }
            return false;
        }        
        
    }
    
    
}
