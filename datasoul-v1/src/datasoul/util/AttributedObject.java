/*
 * AttributedObject.java
 *
 * Created on December 31, 2005, 1:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

/**
 *
 * @author samuelm
 */
public class AttributedObject extends SerializableObject implements TableModel {
    
    protected HashMap<String, Component> propEditors;
    protected MyTableCellEditor tableCellEditor;
    
    private ArrayList<javax.swing.event.TableModelListener> listeners;
    
    /** Creates a new instance of AttributedObject */
    public AttributedObject() {
        propEditors = new HashMap<String, Component>();
        listeners = new ArrayList<javax.swing.event.TableModelListener>();
        
        tableCellEditor = new MyTableCellEditor(new JTextField());
        
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
            this.getClass().getMethod("set"+properties.get(rowIndex), String.class).invoke(this, aValue);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex == 0){
            return "Property";
        }else{
            return "Value";
        }
    }

    public Class<Object> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex==1);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0){
            return properties.get(rowIndex);
        }else{
            try{
                return this.getClass().getMethod("get"+properties.get(rowIndex)).invoke(this);
            }catch(Exception e){
                e.printStackTrace();
                return "Deu erro";
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
            
            Iterator<javax.swing.event.TableModelListener> iter = listeners.iterator();
            while(iter.hasNext()){
                javax.swing.event.TableModelListener l = iter.next();
                l.tableChanged(evt);
            }            
        }
    }
    

    private class MyTableCellEditor extends DefaultCellEditor {
        
        public MyTableCellEditor(JTextField j){
            super(j);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
            
            if (  propEditors.containsKey( properties.get(row) ) ){
                super.getTableCellEditorComponent (table, value, isSelected, row, column);
                Component c = propEditors.get( properties.get(row) );
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
        propEditors.put(property, new JColorTextField());
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
        
        public JColorTextField() {
            super();
            this.addFocusListener(new java.awt.event.FocusAdapter() {
                
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (!edited){
                        JColorChooser cc = new JColorChooser();
                        Color color =  cc.showDialog(JColorTextField.this, "Choose color", Color.BLACK);
                        
                        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
                        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
                        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
                        
                        JColorTextField.this.setText( hexcolor );
                        tableCellEditor.setValue( hexcolor );
                        edited = true;
                        
                        evt.getOppositeComponent().requestFocusInWindow();
                        
                    }
                }
                
                public void focusLost(java.awt.event.FocusEvent evt) {
                    edited = false;
                }
            });
            
        }

        
    }
    
   

    public ArrayList<TableModelListener> getListeners() {
        return listeners;
    }
    
}
