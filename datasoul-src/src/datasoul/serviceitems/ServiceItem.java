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
 * ServiceItem.java
 *
 * Created on February 10, 2006, 9:27 PM
 *
 */

package datasoul.serviceitems;

import datasoul.config.BackgroundConfig;
import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmdVideoStop;
import datasoul.util.SerializableObject;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.Icon;
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
    final private static String ITEM_LABEL = java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ITEM");
    
    /**
     * Contain the slide itens
     */
    protected ArrayList<ServiceItemRenderer> slides;
    protected LinkedList<TableModelListener> tableModelListeners;
    protected TableCellEditor cellEditor;
    protected String title;
    protected String template;
    protected int duration;
    protected String notes;
    protected int startHour;
    protected int startMinute;
    protected ServiceItemNumberLabel slideNumberField;
    protected SlideNumberCellRenderer slideNumberRenderer;
    
    /** Creates a new instance of ServiceItem */
    public ServiceItem() {
        slides = new ArrayList<ServiceItemRenderer>();
        tableModelListeners = new LinkedList<TableModelListener>();
        this.title = "";
        this.slides = new ArrayList<ServiceItemRenderer>();
        slideNumberField = new ServiceItemNumberLabel();
        slideNumberField.setHorizontalAlignment( ServiceItemNumberLabel.TRAILING );
        slideNumberField.setOpaque(true);
        slideNumberRenderer = new SlideNumberCellRenderer();
    }

    public class SlideNumberCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            slideNumberField.setText(value.toString());
            slideNumberField.setShowMark( slides.get(row).getMark() );
            if (isSelected){
                slideNumberField.setBackground( table.getSelectionBackground() );
                slideNumberField.setForeground( table.getSelectionForeground() );
            }else{
                slideNumberField.setBackground( table.getBackground() );
                slideNumberField.setForeground( table.getForeground() );
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

    public ArrayList<ServiceItemRenderer> getSlides(){
        return slides;
    }

    protected void registerProperties() {
        super.registerProperties();
        properties.add("Title");
        properties.add("Template");
        properties.add("Duration");
        properties.add("Notes");
    }

    public void assignTo(ServiceItem item){
        item.setTitle(title);
        item.setTemplate(template);
        item.setDuration(Integer.toString(duration));
        item.setNotes(notes);
    }

    public int getDuration(){
        return this.duration;
    }
    
    public void setDuration(String s){
        this.duration = Integer.parseInt(s);
    }
    
    public String getNotes(){
        return this.notes;
    }
    
    public void setNotes(String n){
        this.notes = n;
    }
    
    public void setStartHour(int x){
        startHour = x;
    }

    public void setStartMinute(int x){
        startMinute = x;
    }
    
    public String getStartTime(){
        return String.format("%d:%02d", startHour, startMinute);
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
    
    protected void fireTableChanged(){
        TableModelEvent e = new TableModelEvent(this);
        for (TableModelListener l : tableModelListeners){
            l.tableChanged(e);
            if ( l instanceof JTable){
                this.updateHeights((JTable) l);
            }
        }

    }

    public boolean isTemplateEditEnabled(){
        return true;
    }

    public String getDefaultMonitorTemplate(){
        return "";
    }

    public Color getBackgroundColor(){
        return null;
    }

    public void edit(){

    }

    public void previewItem(){
        ContentManager cm = ContentManager.getInstance();
        cm.setTemplatePreview(this.getTemplate());
        cm.setTitlePreview(this.getTitle());
        cm.setSongAuthorPreview("");
        cm.setSongSourcePreview("");
        cm.setCopyrightPreview("");
    }

    public void showItem(){
        ContentManager cm = ContentManager.getInstance();
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC){
            cm.setMainShowBackground(true);
        }else{
            cm.setMainShowBackground(false);
        }
        cm.setMainShowTemplate(true);
        cm.setTemplateLive(this.getTemplate());
        cm.setTitleLive(this.getTitle());
        cm.setSongAuthorLive("");
        cm.setSongSourceLive("");
        cm.setCopyrightLive("");
        GstManagerServer.getInstance().sendCommand(new GstDisplayCmdVideoStop());
    }

    public void dispose(){
        
    }

    public Icon getIcon(){
        return null;
    }

    public boolean getShowSlideTable(){
        return true;
    }

    public boolean getShowMediaControls(){
        return false;
    }

}


