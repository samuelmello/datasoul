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
 * TemplateManager.java
 *
 * Created on February 25, 2006, 1:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.config.ConfigObj;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 *
 * @author samuelm
 */
public class TemplateManager implements TableModel {
    
    private static ArrayList<String> availableTemplates = new ArrayList<String>();
    private static TemplateManager instance = null;
    private ArrayList<javax.swing.event.TableModelListener> listeners;
   
    
    /** Creates a new instance of TemplateManager */
    private TemplateManager() {
        listeners = new ArrayList<javax.swing.event.TableModelListener>();
    }
    
    public static synchronized TemplateManager getInstance(){
        if (instance == null){
            instance = new TemplateManager();
            // Ensure ConfigObj already loaded.
            // If any template combo box register to the TemplateManager before 
            // ConfigObj, it will cause a dead lock
            ConfigObj.getInstance();
        }
        return instance;
    }
    

    public int getRowCount() {
        return availableTemplates.size();
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int columnIndex) {
        return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Template");
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return availableTemplates.get(rowIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void removeTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.remove(l);
    }

    public void addTableModelListener(javax.swing.event.TableModelListener l) {
        this.listeners.add(l);
    }
    
    public void tableModelChanged(){
        TableModelEvent tme = new TableModelEvent(this);
        for(int i=0;i<this.listeners.size();i++){
            this.listeners.get(i).tableChanged(tme);            
        }
    }
    
    
    public void refreshAvailableTemplates(){
        
        availableTemplates.clear();
        String path = System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "templates";
        
        File file = new File(path);
        String[] files = file.list();
        
        // there is at least one file in the directroy?
        if (files!=null){
            int size = files.length;
            
            for(int i=0; i<size;i++){
                if(files[i].endsWith(".template")){
                    availableTemplates.add( files[i].substring(0, files[i].indexOf(".template"))) ;
                }
            }
        }
        
        tableModelChanged();
        
    }
    
    
    public void deleteTemplate(String templateName) throws Exception {

        String path = System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "templates";
        File f = new File(path + System.getProperty("file.separator") + templateName + ".template");
        f.delete();
        DisplayTemplate.deleteTemplate(templateName);
        refreshAvailableTemplates();
        
    }
   
    
}
