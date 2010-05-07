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
 * AbstractConfig.java
 *
 * Created on 18 November 2006, 11:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.util.SerializableObject;
import datasoul.util.ShowDialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

/**
 *
 * @author samuelm
 */
public abstract class AbstractConfig extends SerializableObject {
    
    private void writeFileFromStream(File f, InputStream is) throws IOException{
        
        FileOutputStream fos = new FileOutputStream(f);
        
        int x;
        while ((x = is.read()) != -1){
            fos.write((byte)x);
        }
        fos.close();
        is.close();
    }
    
    protected void load(String fileName){
        
        /* Find the configuration file. If its not in ~/.datasoul/config, copy it there */
        String path = getConfigPath() + fileName;
        File configFile = new File(path);
        
        if (!configFile.exists()){
            // Ensure directory exists
            String dirpath = System.getProperty("user.home") + File.separator + ".datasoul"+File.separator+"config";
            File configdir = new File(dirpath);
            configdir.mkdirs();
            
            // check if there is old config file
            String oldpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + fileName;
            File oldfile = new File(oldpath);
            if (oldfile.exists()){
                try {
                    writeFileFromStream(configFile, new FileInputStream(oldfile));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO CREATE CONFIG FILE ")+path+": "+ex.getMessage());
                }
            }else{
                // Get it from jar
                InputStream is = AbstractConfig.class.getResourceAsStream("defaults/"+fileName);
                try {
                    writeFileFromStream(configFile, is);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO CREATE CONFIG FILE ")+path+": "+ex.getMessage());
                }
            }
            
            // Reopen the file
            configFile = new File(path); 
        }

        Document dom=null;
        Node node = null;
        try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(configFile);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName( this.getClass().getSimpleName() ).item(0);

        }catch(Exception e) {
            ShowDialog.showReadFileError(configFile, e);
            return;
        }        

        try {
            this.readObject(node, null);
        } catch (Exception e) {
            ShowDialog.showReadFileError(configFile, e);
            e.printStackTrace();
        }
        
    }
    
    protected String getConfigPath(){
        String path = System.getProperty("user.home") + File.separator + ".datasoul"+File.separator+"config" + File.separator;
        return path;
    }

    protected void save(String nodeName){

        String path = getConfigPath() + nodeName;

        try{

            Node node = this.writeObject(null);
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document

            Source source = new DOMSource(doc);

            // Prepare the output file
            File file = new File(path);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

        } catch(Exception e){
            ShowDialog.showWriteFileError(path, e);
            e.printStackTrace();
        }

    }
    
}

