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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public class SerializableObject  implements Transferable, SerializableItf{

    protected ArrayList<String> properties;
    static public DataFlavor serializableObjectFlavor = new DataFlavor(datasoul.util.SerializableObject.class,"serializableObjectFlavor");;
    
    /** Creates a new instance of SerializableObject */
    public SerializableObject() {
        properties = new ArrayList<String>();
        
    }

     public Node writeObject() throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
         
        Document doc= db.newDocument();
        
        Node nodeOut = doc.createElement(this.getClass().getName().replace(this.getClass().getPackage().getName()+".",""));
        Node node; 
        String paramName;
        String paramValue;
         for(int i=0;i<properties.size();i++){
            paramName = properties.get(i);
            paramValue = (String)this.getClass().getMethod("get"+properties.get(i)).invoke(this);         
            node = doc.createElement(paramName);
            node.setTextContent(paramValue);
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
