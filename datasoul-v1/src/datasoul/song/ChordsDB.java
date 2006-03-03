/*
 * ChordsDB.java
 *
 * Created on 21 de Janeiro de 2006, 17:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.util.ListTable;
import datasoul.util.SerializableItf;
import datasoul.util.SerializableObject;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrador
 */
public class ChordsDB extends ListTable{

    /** Creates a new instance of ChordsDB */
    public ChordsDB() {

    }

    public String getColumnName(int columnIndex) {
        return "Chord";
    }
    
    public Chord getChordByName(String name){
        for(int i=0;i<this.objectList.size();i++){
            if(name.equals(((Chord)this.objectList.get(i)).getName())){
                return (Chord)this.objectList.get(i);
            }
        }
        return null;
    }
    
     public Node writeObject() throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
         
        Document doc= db.newDocument();
        
        Node nodeOut = doc.createElement("ChordsDB");
        Node node; 
        String paramName;
        String paramValue;
        for(int i=0;i<objectList.size();i++){
            node = ((SerializableItf)objectList.get(i)).writeObject();
            nodeOut.appendChild(doc.importNode(node,true));
        }
              
        return nodeOut.cloneNode(true);
     }
     
     public void readObject(Node nodeIn) throws Exception{

        NodeList nodeList= nodeIn.getChildNodes();
        String paramName;
        String paramValue;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeType()==1){
                Chord chord = new Chord();
                chord.readObject(nodeList.item(i));
                this.addItem(chord);
            }
        }
        this.sortByName();
     }
    
}
