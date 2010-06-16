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
 * AttributedObject.java
 *
 * Created on December 31, 2005, 1:25 PM
 *
 */

package datasoul.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author samuelm
 */
public abstract class AttributedObject extends SerializableObject implements TableModel {
    
    protected HashMap<String, Component> propEditors;

    protected ArrayList<String> colorEditors;

    static private HashMap<Class, HashMap<String, String>> displayNamesTable = new HashMap<Class, HashMap<String, String>>();

    private HashMap<String, String> displayNames;
    
    protected MyTableCellEditor tableCellEditor;
    
    protected ColorTableCellRenderer colorTableCellRenderer;
    
    private ArrayList<javax.swing.event.TableModelListener> listeners;
    
    /** Creates a new instance of AttributedObject */
    public AttributedObject() {
        
        propEditors = new HashMap<String, Component>();
        
        listeners = new ArrayList<javax.swing.event.TableModelListener>();
        tableCellEditor = new MyTableCellEditor(new JTextField());
        colorTableCellRenderer = new ColorTableCellRenderer();
        colorEditors = new ArrayList<String>();
        
    }

    public void removeTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.remove(l);
    }

    public void addTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.add(l);
    }
    
  
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
        if (columnIndex != 1) return;
        
        try{
            String prop = properties.get(rowIndex);
            if (prop.endsWith("Idx")){
                prop = prop.substring(0, prop.length() - 3);
            }
            this.getClass().getMethod("set"+prop, String.class).invoke(this, aValue);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex == 0){
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("PROPERTY");
        }else{
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VALUE");
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex==0){
            return Object.class;
        }else{
            return AttributedObject.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex==1);
    }


    protected HashMap<String, String> getDisplayNames(){
        if (displayNames == null){
            
            if ( displayNamesTable.containsKey(this.getClass()) ){
                displayNames = displayNamesTable.get(this.getClass());
            }else{
                displayNames = new HashMap<String, String>();
                displayNamesTable.put(this.getClass(), displayNames);
            }
            
        }
        return displayNames;

    }
    
    public void registerDisplayString(String prop, String displayStr){
        getDisplayNames().put(prop, displayStr);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0){
            
            if (getDisplayNames().containsKey( properties.get(rowIndex) )){
                return getDisplayNames().get( properties.get(rowIndex) );
            }else{
                return properties.get(rowIndex);
            }
            
        }else{
            try{
                
                String prop = properties.get(rowIndex);
                if (prop.endsWith("Idx")){
                    prop = prop.substring(0, prop.length() - 3);
                }
                
                return this.getClass().getMethod("get"+prop).invoke(this);
            }catch(Exception e){
                e.printStackTrace();
                return "";
            }
        }
        
    }

    public int getRowCount() {
        return properties.size();
    }

    public int getColumnCount() {
        return 2;
    }
    
    protected void firePropChanged(String prop){

        int i;
        boolean found = false;
        
        for (i=0; i<properties.size(); i++){
            if (properties.get(i).equals(prop)){
                found = true;
                break;
            }
        }
        
        if (found){
            TableModelEvent evt = new TableModelEvent(this, i);
            
            for (javax.swing.event.TableModelListener l : listeners){
                l.tableChanged(evt);
            }            
        }
    }

    private class ColorTableCellRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            String prop = properties.get(row);

            if ( colorEditors.contains( prop ) && propEditors.containsKey( prop ) ){
                Component c = propEditors.get( properties.get(row) );
                if (c instanceof JColorTextField){
                    ((JColorTextField)c).setText("0x"+value.toString());
                }
                return c;
            }else{
                return super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
            }
        }

        
        
    }

    private class MyTableCellEditor extends DefaultCellEditor {
        
        public MyTableCellEditor(JTextField j){
            super(j);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
            
            String prop = properties.get(row);
            
            if ( propEditors.containsKey( prop ) ){
                Component c = propEditors.get( properties.get(row) );
                if (c instanceof JComboBox){
                    ((JComboBox)c).setSelectedItem(value);
                }
                return c;
            }else{
                return super.getTableCellEditorComponent (table, value, isSelected, row, column);
            }
        }
        
        protected void setValue (Object o){
            this.delegate.setValue(o);
            stopCellEditing();
        }
        
        
    }

    
    
    protected void registerEditorComboBox(String property, JComboBox cb){
        cb.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        
        cb.addActionListener(  new JComboBoxWrapper() );
        
        propEditors.put(property, cb);
    }
    
    protected void registerColorChooser(String property){
        propEditors.put(property, new JColorTextField(property));
        colorEditors.add(property);
    }
    
    public TableCellEditor getTableCellEditor(){
        return this.tableCellEditor;
    }
    
    
    
    protected class JComboBoxWrapper implements ActionListener, Serializable {

       /**
        * When an action is performed, editing is ended.
        * @param e the action event
        * @see #stopCellEditing
        */
        public void actionPerformed(ActionEvent e) {
            
            Object source = e.getSource();
            if (source instanceof JComboBox){
                tableCellEditor.setValue( ((JComboBox) source).getSelectedItem() );
            }
            
	}

    }
    
    protected class JColorTextField extends JTextField {
        
        boolean edited = false;
        
        String property;
        
        public JColorTextField(String property) {
            super();
            
            this.property = property;
            
            this.addFocusListener(new java.awt.event.FocusAdapter() {
                
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (!edited){
                        
                        edited = true;
                        
                        Color color =  JColorChooser.showDialog(JColorTextField.this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CHOOSE COLOR"), Color.decode(getText()));

                        if (color != null){
                            String hexcolor = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                            JColorTextField.this.setText( hexcolor );
                            tableCellEditor.setValue( hexcolor );
                        }
                        
                        if (evt.getOppositeComponent() != null)
                            evt.getOppositeComponent().requestFocusInWindow();
                        
                        
                    }
                }
                
                public void focusLost(java.awt.event.FocusEvent evt) {
                    edited = false;
                }
            });
            
        }
        
        @Override
        public void paint (Graphics g){
            
            String value = "0xFFFFFF";
            
            try {
                value = "0x" +  AttributedObject.this.getClass().getMethod("get"+property).invoke(AttributedObject.this).toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            g.setColor( Color.decode(value) );
            
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        
    }
    
   

    public ArrayList<TableModelListener> getListeners() {
        return listeners;
    }
    
    public TableCellRenderer getColorTableCellRenderer(){
        return colorTableCellRenderer;
    }
    
}


