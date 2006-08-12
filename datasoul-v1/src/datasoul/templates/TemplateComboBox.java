/*
 * templateComboBox.java
 *
 * Created on 27 de Marco de 2006, 21:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.ConfigObj;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Administrador
 */
public class TemplateComboBox extends JComboBox implements TableModelListener{
    
    private TableModel model;
    
    public static final int FILTER_NONE = 0;
    public static final int FILTER_MONITOR = 1;
    public static final int FILTER_ALERT = 2;
    public static final int FILTER_GENERAL = 3;
    
    private int filterType;
    
    /** Creates a new instance of templateComboBox */
    public TemplateComboBox() {
        TemplateManager manager = TemplateManager.getInstance();
        //manager.refreshAvailableTemplates();
        this.setModel( manager );
        populateList();        
    }

    private void setModel(TableModel model){
        this.model = model;
        model.addTableModelListener(this);
    }

    public void tableChanged(TableModelEvent e) {
        populateList();
    }
    
    private void populateList(){
        
        String filter = null;
        
        switch (filterType){
            case FILTER_MONITOR:
                filter = ConfigObj.getInstance().getMonitorTemplateFilter().toLowerCase();
                break;
                
            case FILTER_ALERT:
                filter = ConfigObj.getInstance().getAlertTemplateFilter().toLowerCase();
                break;

            case FILTER_GENERAL:
                filter = ConfigObj.getInstance().getGeneralTemplateFilter().toLowerCase();
                break;

        }
        
        Object[]  objs = this.getSelectedObjects();
        this.removeAllItems();
        for(int i=0; i<model.getRowCount(); i++){
            String strItem = model.getValueAt(i,0).toString();
            if ( filter == null || filter.equals("") || strItem.toLowerCase().contains(filter)){
                this.addItem(strItem);
            }
            
        }
        for(Object obj:objs){
            this.setSelectedItem(obj);
        }
    }
    
    public void setFilterType(int filterType){
        this.filterType = filterType;
        populateList();
    }
    
    public int getFilterType(){
        return filterType;
    }
}

