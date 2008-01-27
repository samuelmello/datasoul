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
 * ServiceItem.java
 *
 * Created on February 10, 2006, 9:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.config.ConfigObj;
import datasoul.util.SerializableObject;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author samuelm
 */
public class ServiceItem extends SerializableObject implements TableModel, TableCellRenderer {
    
    final private static String NUMBER_LABEL = "#";
    final private static String ITEM_LABEL = java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Item");
    
    /**
     * Contain the slide itens
     */
    protected ArrayList<ServiceItemRenderer> slides;
    protected LinkedList<TableModelListener> tableModelListeners;
    protected TableCellEditor cellEditor;
    protected String title;
    protected String template;
    protected ServiceItemTextField slideNumberField;
    protected SlideNumberCellRenderer slideNumberRenderer;
    
    /** Creates a new instance of ServiceItem */
    public ServiceItem() {
        slides = new ArrayList<ServiceItemRenderer>();
        tableModelListeners = new LinkedList<TableModelListener>();
        this.title = "";
        this.template = ConfigObj.getInstance().getTemplateText();
        this.slides = new ArrayList<ServiceItemRenderer>();
        slideNumberField = new ServiceItemTextField();
        slideNumberField.setHorizontalAlignment( ServiceItemTextField.TRAILING );
        slideNumberField.setBorder(null);
        slideNumberRenderer = new SlideNumberCellRenderer();
    }

    public class SlideNumberCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            slideNumberField.setText(value.toString());
            slideNumberField.setShowMark( slides.get(row).getMark() );
            if (isSelected){
                slideNumberField.setBackground( table.getSelectionBackground() );
            }else{
                slideNumberField.setBackground( table.getBackground() );
            }
            return slideNumberField;
        }
        
    }
    
    public String toString(){
        return this.title;
    }
    
    /**
     * This method should be overriden by upper class to
     * inform the hieght needed to display the item of row.
     */
    protected int getRowSize(int row){
        return 15;
    }
    
    
    protected void registerProperties() {
        properties.add("Title");
        properties.add("Template");
    }

    public String getTitle(){
        return this.title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }

    public String getTemplate(){
        return this.template;
    }
    
    public void setTemplate(String template){
        this.template = template;
    }
    
    public int getRowCount() {
        if (slides != null){
            return slides.size();
        }else{
            return 0;
        }
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex == 0){
            return NUMBER_LABEL;
        }else{
            return ITEM_LABEL;
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (slides != null && slides.size()>0){
            if (columnIndex==0){
                String text = Integer.toString(rowIndex+1)+" ";
                //if (slides.get(rowIndex).getShowMark()){
                //    text = text + " *";
                //}
                return text;
            }else
                return slides.get(rowIndex);
        }else{
            return null;
        }
    }

    public int getNextMarkedSlide(int actualSlide){
        for(int i=actualSlide+1;i<this.getRowCount();i++){
            if (slides.get(i).getMark()){
                return i;
            }
        }
        return actualSlide;
    }
    
    public int getPreviousMarkedSlide(int actualSlide){
        for(int i=actualSlide-1;i>=0;i--){
            if(slides.get(i).getMark()){
                return i;
            }
        }
        return actualSlide;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void addTableModelListener(TableModelListener l) {
        tableModelListeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        tableModelListeners.remove(l);
    }

    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return slides.get(row).getComponent(isSelected, hasFocus);
    }
    
    public void registerJTable(JTable jTable){
        jTable.setModel(this);
        jTable.getColumn(NUMBER_LABEL).setMaxWidth(30);
        jTable.getColumn(NUMBER_LABEL).setCellRenderer(slideNumberRenderer);
        jTable.getColumn(ITEM_LABEL).setCellRenderer(this);
        jTable.getColumn(ITEM_LABEL).setCellEditor(cellEditor);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fireTableChanged();
    }
    
    public void updateHeights(JTable jTable){
        
        int width = jTable.getWidth() - jTable.getColumn(NUMBER_LABEL).getWidth();
        
        for (int i=0; i< this.getRowCount(); i++ ){
            ServiceItemRenderer r = slides.get(i);
            r.setWidth(width);
            jTable.setRowHeight(i, r.getHeight() );
        }

        
    }
    
    public void setZoom (int f){
        for (ServiceItemRenderer r : slides){
            r.setZoom(f);
        }
    }

    protected void fireTableChanged(){
        TableModelEvent e = new TableModelEvent(this);
        for (TableModelListener l : tableModelListeners){
            l.tableChanged(e);
            if ( l instanceof JTable){
                this.updateHeights((JTable) l);
            }
        }

    }
    
    
    
}
