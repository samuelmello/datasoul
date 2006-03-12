/*
 * ListTable.java
 *
 * Created on 8 de Janeiro de 2006, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import com.sun.org.apache.xml.internal.utils.StringVector;
import java.util.ArrayList;
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
    
    //to do, implement the serialize
    public String getList(){
        return "";
    }
    //to do, implement the serialize    
    public void setList(String string){
        
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

    public Class<Object> getColumnClass(int columnIndex) {
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

    public void sortByName() {
        ArrayList<Object> objectListSorted = new ArrayList<Object>();

mainloop: for(int i=0;i<objectList.size();i++){
            int j;
            for(j=0;j<objectListSorted.size();j++){
                if(isBefore(objectList.get(i),objectListSorted.get(j))){
                    objectListSorted.add(j,objectList.get(i));
                    continue mainloop;
                }
            }
            objectListSorted.add(j,objectList.get(i));
        }
        
        objectList = objectListSorted;
        tableModelChanged();
    }
    
    private boolean isBefore(Object obj1, Object obj2){
        String str1 = obj1.toString();
        String str2 = obj2.toString();
        
        StringVector sv = new StringVector();
        int max = str2.length();
        if(str1.length()<str2.length()){
            max = str1.length();
        }
        for(int i=0;i<max;i++){
            if(Character.valueOf(str1.charAt(i))<Character.valueOf(str2.charAt(i))){
                return true;
            }
            if(Character.valueOf(str1.charAt(i))>Character.valueOf(str2.charAt(i))){
                return false;
            }
        }
        if(str1.length()>str2.length())
            return false;
        else
            return true;
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
        properties.add("List");
    }
    
}
