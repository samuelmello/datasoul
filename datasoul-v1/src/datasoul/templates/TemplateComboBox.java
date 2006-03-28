/*
 * templateComboBox.java
 *
 * Created on 27 de Março de 2006, 21:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

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
    
    /** Creates a new instance of templateComboBox */
    public TemplateComboBox() {
        TemplateManager manager = TemplateManager.getInstance();
        manager.refreshAvailableTemplates();
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
        this.removeAllItems();
        for(int i=0; i<model.getRowCount(); i++){
            this.addItem(model.getValueAt(i,0));
        }
    }
}
