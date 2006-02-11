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
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    
    /**
     * Loads an existing DisplayTemplate
     */
    public DisplayTemplate(String filename) throws Exception {
        
        super();
        items = new ArrayList<TemplateItem>();
        model = new DisplayTemplateTableModel();
        
        
        Document dom=null;
        Node node=null;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        //parse using builder to get DOM representation of the XML file
        dom = db.parse(filename);
        
        //node = dom.getDocumentElement().getChildNodes().item(0);
        node = dom.getElementsByTagName("DisplayTemplate").item(0);
        
        this.readObject(node);
        
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
    
    public void paint(Graphics2D g){
        
        for (TemplateItem t : items){
            t.draw( g );
        }
        
    }
    
    public void moveUp(TemplateItem t){
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
    
    public void moveDown(TemplateItem t){
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
    
    @Override
            public void readObject(Node nodeIn) throws Exception {
        
        // read the properties
        super.readObject(nodeIn);
        
        // now, read the template Items
        NodeList nodeList= nodeIn.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            // find the templateItem tag in document
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
                    nodeList.item(i).getNodeName().equals("TemplateItems") ){
                
                // for each templateItem, create it and add it to the list
                NodeList templateItemsNodes = nodeList.item(i).getChildNodes();
                for (int j=0; j<templateItemsNodes.getLength(); j++){
                    if ( templateItemsNodes.item(j).getNodeType() == Node.ELEMENT_NODE ){
                        
                        // Determine the type for the TemplateItem
                        String className = templateItemsNodes.item(j).getNodeName();
                        className = this.getClass().getPackage().getName() + "." + className;
                        
                        // Create the Object
                        Class cl = Class.forName(className);
                        Object ti = cl.newInstance();
                        
                        // set the properties
                        if (ti instanceof TemplateItem){
                            ((TemplateItem) ti).readObject( templateItemsNodes.item(j) );
                            this.addItem( (TemplateItem) ti );
                        }
                    }
                }
                
            }
        }
        
        
    }
    
    public void setTemplateItems(String s){
        // just to conformance with super's readObject.
        // The TemplateItems will be readed lately by the overriden readObject
    }
    
    
    public void save(String filename) throws Exception {
        Node node = this.writeObject();
        Document doc = node.getOwnerDocument();
        doc.appendChild( node);                        // Add Root to Document
        FileOutputStream fos = new FileOutputStream( filename );
        org.apache.xml.serialize.XMLSerializer xs = new org.apache.xml.serialize.XMLSerializer();
        OutputFormat outFormat = new OutputFormat();
        outFormat.setIndenting(true);
        outFormat.setEncoding("ISO-8859-1");
        xs.setOutputFormat(outFormat);
        xs.setOutputByteStream(fos);
        xs.serialize(doc);
    }
    
    
}
