/*
 * ServiceItem.java
 *
 * Created on February 10, 2006, 9:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.util.SerializableObject;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author samuelm
 */
public class ServiceItem extends SerializableObject implements TableModel, TableCellRenderer {
    
    final private static String NUMBER_LABEL = "#";
    final private static String ITEM_LABEL = "Item";
    
    /**
     * Contain the slide itens
     */
    ArrayList<ServiceItemRenderer> slides;
    
    /** Creates a new instance of ServiceItem */
    public ServiceItem() {
        slides = new ArrayList<ServiceItemRenderer>();
    }

    /**
     * This method should be overriden by upper class to
     * inform the hieght needed to display the item of row.
     */
    protected int getRowSize(int row){
        return 15;
    }
    
    
    protected void registerProperties() {
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
        if (slides != null){
            if (columnIndex==0)
                return Integer.toString(rowIndex+1);
            else
                return slides.get(rowIndex);
        }else{
            return null;
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return slides.get(row).getComponent(isSelected);
    }
    
    public void registerJTable(JTable jTable){
        jTable.setModel(this);
        jTable.getColumn(NUMBER_LABEL).setMaxWidth(30);
        jTable.getColumn(ITEM_LABEL).setCellRenderer(this);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void updateHeights(JTable jTable){
        
        int width = jTable.getWidth() - jTable.getColumn(NUMBER_LABEL).getWidth();
        
        for (int i=0; i< this.getRowCount(); i++ ){
            ServiceItemRenderer r = slides.get(i);
            r.setWidth(width);
            jTable.setRowHeight(i, r.getHeight() );
        }

        
    }
    
}
