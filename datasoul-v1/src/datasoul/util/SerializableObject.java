/*
 * SerializableObject.java
 *
 * Created on 3 de Janeiro de 2006, 21:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public abstract class SerializableObject  implements Transferable, SerializableItf{

    protected ArrayList<String> properties;
    static public DataFlavor serializableObjectFlavor = new DataFlavor(datasoul.util.SerializableObject.class,"serializableObjectFlavor");;
    
    static private HashMap<Class, ArrayList<String>> propertiesTable = new HashMap<Class, ArrayList<String>>();
    
    protected abstract void registerProperties();
    
    /** Creates a new instance of SerializableObject */
    public SerializableObject() {
        
        if ( propertiesTable.containsKey(this.getClass()) ){
            properties = propertiesTable.get(this.getClass());
        }else{
            properties = new ArrayList<String>();
            this.registerProperties();
            propertiesTable.put(this.getClass(), properties);
        }
        
    }

     public Node writeObject() throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
         
        Document doc= db.newDocument();
        
        Node nodeOut = doc.createElement(this.getClass().getName().replace(this.getClass().getPackage().getName()+".",""));
        Node node; 
        String paramName;
        Object paramValue;
        
         for(int i=0;i<properties.size();i++){
            paramName = properties.get(i);
            
            paramValue = this.getClass().getMethod("get"+properties.get(i)).invoke(this);         
            node = doc.createElement(paramName);
            if (paramValue != null){
                node.setTextContent(paramValue.toString());
            }
            nodeOut.appendChild(node);
        }
              
        return nodeOut.cloneNode(true);
     }
     
     public void readObject(Node nodeIn) throws Exception{

        NodeList nodeList= nodeIn.getChildNodes();
        String paramName;
        String paramValue;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeType()==1){
                paramName = nodeList.item(i).getNodeName(); 
                paramValue = nodeList.item(i).getTextContent();
                this.getClass().getMethod("set"+paramName, String.class).invoke(this, paramValue);
            }
        }
     }

    public DataFlavor[] getTransferDataFlavors() {

        
        DataFlavor[] df = new DataFlavor[1];
        df[0] = SerializableObject.serializableObjectFlavor;
        return df;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if(flavor == SerializableObject.serializableObjectFlavor) {
                return true;
        }
        return false;
    }

  public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {

    if (flavor.equals(SerializableObject.serializableObjectFlavor)) {
        return (SerializableObject)this;      
    }
    return null;
  }    


}
