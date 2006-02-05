/*
 * DisplayTemplate.java
 *
 * Created on December 31, 2005, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.util.AttributedObject;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Node;

/**
 *
 * @author samuelm
 */
public class DisplayTemplate extends AttributedObject {
    
    ArrayList<TemplateItem> items;
    
    DisplayTemplateTableModel model;
    
    static int defaultItemNameCount = 1;
    
    private String name;
    
    /** Creates a new instance of DisplayTemplate */
    public DisplayTemplate() {
        super();
        items = new ArrayList<TemplateItem>();
        model = new DisplayTemplateTableModel();
    }
    
    @Override
    protected void registerProperties(){
        properties.add("Name");
    }
    
    public DisplayTemplateTableModel getModel(){
        return model;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * items are exported as default access level to
     * allow other classes in this package access it directly.
     * This is needed by the TempalteEditorPanel.
     * But the general rule is that other components shall
     * avoid using it directly.
     */
    ArrayList<TemplateItem> getItems(){
        return items;
    }
    
    public void addItem(TemplateItem t){
        if (t.getName().equals("")){
            t.setName("Item #"+defaultItemNameCount++);
        }
        items.add(t);
        model.fireTableDataChanged();
    }
    
    public void removeItem(TemplateItem t){
        items.remove(t);
        model.fireTableDataChanged();
    }
    
    public void paint (Graphics2D g){
        
        Iterator<TemplateItem> iter = items.iterator();
        while(iter.hasNext()){
            TemplateItem t = iter.next();
            t.draw( g );
        }

    }

    public void moveUp (TemplateItem t){
        int size = items.size();
        for (int i=0; i<size-1; i++){
            if (items.get(i) == t){
                TemplateItem other = items.get(i+1);
                items.set(i+1, t);
                items.set(i, other);
                break;
            }
        }
    }

    public void moveDown (TemplateItem t){
        int size = items.size();
        for (int i=1; i<size; i++){
            if (items.get(i) == t){
                TemplateItem other = items.get(i-1);
                items.set(i-1, t);
                items.set(i, other);
                break;
            }
        }
    }
    
    
    public class DisplayTemplateTableModel extends DefaultTableModel {
        
        public int getRowCount() {
            return items.size();
        }

        public int getColumnCount() {
            return 1;
        }

        public String getColumnName(int columnIndex) {
            return "Item";
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return items.get( items.size()-1 - rowIndex).getName();
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        
    }
    
    @Override
    public Node writeObject() throws Exception{
        
        Node n = super.writeObject();
        
        Node nodeItems = n.getOwnerDocument().createElement("TemplateItems");
        
        for (int i=0; i< items.size(); i++){
            
            Node ti =  items.get(i).writeObject();
            
            nodeItems.appendChild( nodeItems.getOwnerDocument().importNode(ti, true) );
                    
        }
        
        n.appendChild(nodeItems);
        
        return n;
        
    }
    

}
