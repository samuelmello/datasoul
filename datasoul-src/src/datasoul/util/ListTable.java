/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * ListTable.java
 *
 * Created on 8 de Janeiro de 2006, 19:47
 *
 */

package datasoul.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 *
 * @author Administrador
 */
public class ListTable extends SerializableObject implements TableModel {

    private ArrayList<javax.swing.event.TableModelListener> listeners;
    protected ArrayList<Object> objectList;
    
    /**
     * Creates a new instance of ListTable
     */
    public ListTable() {
        objectList = new ArrayList<Object>();
        listeners = new ArrayList<javax.swing.event.TableModelListener>();
        
    }

    public int getRowCount() {
        return objectList.size();
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int columnIndex) {
        return "FileName";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object object = objectList.get(rowIndex);
        return  object;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        objectList.set(rowIndex,(String)aValue.toString());
        tableModelChanged();
    }

    public void removeTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.remove(l);
    }

    public void addTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.add(l);
    }

    public void addItem(Object item) {
        objectList.add(item);
        tableModelChanged();
    }

    public void removeItem(int index) {
        objectList.remove(index);
        tableModelChanged();
    }

    private class StringComparator implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo( o2.toString() );
        }
    }
    private StringComparator stringComparator = new StringComparator();
    
    public void sortByName() {

        Collections.sort(objectList, stringComparator);
        
        tableModelChanged();
    }
    
    public void upItem(int row){
        if(row>0){
            Object obj1 = objectList.get(row-1);
            Object obj2 = objectList.get(row);
            objectList.set(row,obj1);    
            objectList.set(row-1,obj2);
        }
        tableModelChanged();
    }

    public void downItem(int row){
        if(row<objectList.size()-1){
            Object obj1 = objectList.get(row+1);
            Object obj2 = objectList.get(row);
            objectList.set(row,obj1);    
            objectList.set(row+1,obj2);
        }
        tableModelChanged();
    }

    protected void tableModelChanged(){
        TableModelEvent tme = new TableModelEvent(this);
        for(int i=0;i<this.listeners.size();i++){
            this.listeners.get(i).tableChanged(tme);            
        }
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("List");
    }
    
}

