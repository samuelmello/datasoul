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
 * templateComboBox.java
 *
 * Created on 27 de Marco de 2006, 21:05
 *
 */

package datasoul.templates;

import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Administrador
 */
public class TemplateComboBox extends JComboBox implements TableModelListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7833643012768256674L;

	private int targetContent;
    
    private boolean updating;
    
    /** Creates a new instance of templateComboBox */
    public TemplateComboBox() {
        targetContent = -1;
        TemplateManager.getInstance().addTableModelListener(this);
        populateList();        
    }

    public void tableChanged(TableModelEvent e) {
        populateList();
    }
    
    private void populateList(){
        
        updating = true;
                
        Object[]  objs = this.getSelectedObjects();
        this.removeAllItems();
        for(int i=0; i<TemplateManager.getInstance().getRowCount(); i++){
            DisplayTemplateMetadata meta = TemplateManager.getInstance().getDisplayTemplateMetadata(i);
            if (targetContent == -1 || targetContent == meta.getTargetContentIdx())
                this.addItem(meta.getName());

            // special case: for Songs, show also all Text templates
            if (targetContent == DisplayTemplate.TARGET_CONTENT_SONG &&
                    meta.getTargetContentIdx() == DisplayTemplate.TARGET_CONTENT_TEXT)
                this.addItem(meta.getName());
            
        }
        for(Object obj:objs){
            this.setSelectedItem(obj);
        }
        updating = false;
    }
    
    public boolean isUpdating(){
        return updating;
    }
    
    public void setTargetContent(int content){
        this.targetContent = content;
        populateList();
    }
    
    public int getTargetContent(){
        return targetContent;
    }
}


