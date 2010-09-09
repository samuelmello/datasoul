/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * SerializableObject.java
 *
 * Created on 3 de Janeiro de 2006, 21:37
 *
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import datasoul.DatasoulMainForm;

/**
 *
 * @author Administrador
 */
public abstract class SerializableObject  implements Transferable, SerializableItf{

    protected ArrayList<String> properties;

    static public DataFlavor serializableObjectFlavor = new DataFlavor(datasoul.util.SerializableObject.class,"serializableObjectFlavor");;
    
    // we hold just one properties array instance for each class
    static private HashMap<Class, ArrayList<String>> propertiesTable = new HashMap<Class, ArrayList<String>>();

    private int datasoulFileVersion;
    
    /** Creates a new instance of SerializableObject */
    public SerializableObject() {

        // if this is the first object of this class, we register the static 
        // array of properties
        if ( propertiesTable.containsKey(this.getClass()) ){
            properties = propertiesTable.get(this.getClass());
        }else{
            properties = new ArrayList<String>();
            this.registerProperties();
            propertiesTable.put(this.getClass(), properties);
        }
        
    }

    protected void registerProperties(){
    }
    
    @Override
    public Node writeObject(ZipWriter zip) throws Exception{

        // update version stamp
        this.setDatasoulFileVersion(DatasoulMainForm.getFileFormatVersion());
         
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
         
        Document doc= db.newDocument();
        
        Node nodeOut = doc.createElement(this.getClass().getName().replace(this.getClass().getPackage().getName()+".",""));
        Node node; 
        String paramName;
        Object paramValue;

        // Add file version
        node = doc.createElement("DatasoulFileVersion");
        node.setTextContent( Integer.toString(DatasoulMainForm.FILE_FORMAT_VERSION) );
        nodeOut.appendChild(node);

        // Add properties
        for(int i=0;i<properties.size();i++){
            paramName = properties.get(i);
            
            paramValue = this.getClass().getMethod("get"+paramName).invoke(this);         
            node = doc.createElement(paramName);
            if (paramValue != null){
                node.setTextContent(paramValue.toString());
            }
            nodeOut.appendChild(node);
        }
              
        return nodeOut.cloneNode(true);
     }

    @Override
    public void readObject(Node nodeIn, ZipReader zip)  {

        NodeList nodeList= nodeIn.getChildNodes();
        String paramName;
        String paramValue;
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ){
                paramName = nodeList.item(i).getNodeName(); 
                paramValue = nodeList.item(i).getTextContent();
                
                try {
                    this.getClass().getMethod("set"+paramName, String.class).invoke(this, paramValue);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
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

    public int getDatasoulFileVersion(){
        return datasoulFileVersion;
    }
    
    public void setDatasoulFileVersion(String s){
        datasoulFileVersion = 0;
        try{
            if (s != null && s.length() > 0 ){
                if (s.equals("1.2")){
                    datasoulFileVersion = 0;
                }else{
                    datasoulFileVersion = Integer.parseInt(s);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
  

}

