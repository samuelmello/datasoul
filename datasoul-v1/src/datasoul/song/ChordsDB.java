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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrador
 */
public class ChordsDB extends ListTable{

    private static ChordsDB instance;
    /** Creates a new instance of ChordsDB */
    private ChordsDB() {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
        + "config"+System.getProperty("file.separator")+"datasoul.chordsdb";
        
        File chordsFile = new File(path);

        Document dom=null;
        Node node = null;
        try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(chordsFile);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("ChordsDB").item(0);

        }catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error, the file is not well formed\n"+e.getMessage(),"DataSoul Error",0);    
            return;
        }        

        try {
            this.readObject(node);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error, the file is not well formed\nErro:"+e.getMessage(),"DataSoul Error",0);    
        }
    }

    public static ChordsDB getInstance(){
        if(instance==null){
            instance = new ChordsDB();
        }
        return instance;
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

    public ArrayList<String> getChordsName(){
        ArrayList<String> al = new ArrayList<String>();
        for(Object obj:objectList)
            al.add(((Chord)obj).getName());
        return al;
    }
    
    public void save(){
        try{
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
            + "config"+System.getProperty("file.separator")+"datasoul.chordsdb";

            Node node = this.writeObject();
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document
            FileOutputStream fos = new FileOutputStream(path);
            XMLSerializer xs = new org.apache.xml.serialize.XMLSerializer();
            OutputFormat outFormat = new OutputFormat();
            outFormat.setIndenting(true);
            outFormat.setEncoding("ISO-8859-1");
            xs.setOutputFormat(outFormat);
            xs.setOutputByteStream(fos);
            xs.serialize(doc);

        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error writing file.\nErro:"+e.getMessage(),"DataSoul Error",0);    
        }        
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
