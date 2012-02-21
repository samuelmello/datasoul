/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * ChordsDB.java
 *
 * Created on 21 de Janeiro de 2006, 17:51
 *
 */

package datasoul.serviceitems.song;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import datasoul.config.AbstractConfig;
import datasoul.util.ListTable;
import datasoul.util.SerializableItf;
import datasoul.util.ShowDialog;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;

/**
 *
 * @author Administrador
 */
public class ChordsDB extends ListTable{

    private void writeFileFromStream(File f, InputStream is) throws IOException{
        
        FileOutputStream fos = new FileOutputStream(f);
        
        int x;
        while ((x = is.read()) != -1){
            fos.write((byte)x);
        }
        fos.close();
        is.close();
    }
    
    
    private static ChordsDB instance;
    /** Creates a new instance of ChordsDB */
    private ChordsDB() {
        /* Find the configuration file. If its not in ~/.datasoul/config, copy it there */
        String fileName = "datasoul.chordsdb";
        String fs = System.getProperty("file.separator");
        String path = System.getProperty("user.home") + fs + ".datasoul"+fs+"config" + System.getProperty("file.separator") + fileName;
        File chordsFile = new File(path);
        
        if (!chordsFile.exists()){
            // Ensure directory exists
            String dirpath = System.getProperty("user.home") + fs + ".datasoul"+fs+"config";
            File configdir = new File(dirpath);
            configdir.mkdirs();
            
            // check if there is old config file
            String oldpath = System.getProperty("user.dir") + fs + "config" + System.getProperty("file.separator") + fileName;
            File oldfile = new File(oldpath);
            if (oldfile.exists()){
                try {
                    writeFileFromStream(chordsFile, new FileInputStream(oldfile));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO CREATE CONFIG FILE ")+path+": "+ex.getMessage());
                }
            }else{
                // Get it from jar
                InputStream is = AbstractConfig.class.getResourceAsStream("defaults/"+fileName);
                try {
                    writeFileFromStream(chordsFile, is);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO CREATE CONFIG FILE ")+path+": "+ex.getMessage());
                }

            }
            
            // Reopen the file
            chordsFile = new File(path); 
        }

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
            ShowDialog.showReadFileError(chordsFile, e);
            return;
        }        

        try {
            this.readObject(node, null);
        } catch (Exception e) {
            ShowDialog.showReadFileError(chordsFile, e);
        }
    }

    public static ChordsDB getInstance(){
        if(instance==null){
            instance = new ChordsDB();
        }
        return instance;
    }
    
    public String getColumnName(int columnIndex) {
        return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CHORD");
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
        String fileName = "datasoul.chordsdb";
        String fs = System.getProperty("file.separator");
        String path = System.getProperty("user.home") + fs + ".datasoul"+fs+"config" + System.getProperty("file.separator") + fileName;
        try{

            Node node = this.writeObject(null);
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document
            FileOutputStream fos = new FileOutputStream(path);

            Source source = new DOMSource(doc);

            // Prepare the output file
            Result result = new StreamResult(fos);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

            fos.close();

        } catch(Exception e){
            ShowDialog.showWriteFileError(path, e);
        }        
    }


    @Override
    public Node writeObject(ZipWriter zip) throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
         
        Document doc= db.newDocument();
        
        Node nodeOut = doc.createElement("ChordsDB");
        Node node; 
        for(int i=0;i<objectList.size();i++){
            node = ((SerializableItf)objectList.get(i)).writeObject(zip);
            nodeOut.appendChild(doc.importNode(node,true));
        }
              
        return nodeOut.cloneNode(true);
     }

    @Override
    public void readObject(Node nodeIn, ZipReader zip) {

        NodeList nodeList= nodeIn.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            if(nodeList.item(i).getNodeType()==1){
                Chord chord = new Chord();
                chord.readObject(nodeList.item(i), zip);
                this.addItem(chord);
            }
        }
        this.sortByName();
     }
    
}


