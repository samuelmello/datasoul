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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
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
        String fs = System.getProperty("file.separator");
        String path = System.getProperty("user.home") + fs + ".datasoul"+fs+"config" + System.getProperty("file.separator") + fileName;
        File configFile = new File(path);
        
        if (!configFile.exists()){
            // Ensure directory exists
            String dirpath = System.getProperty("user.home") + fs + ".datasoul"+fs+"config";
            File configdir = new File(dirpath);
            configdir.mkdirs();
            
            // check if there is old config file
            String oldpath = System.getProperty("user.dir") + fs + "config" + System.getProperty("file.separator") + fileName;
            File oldfile = new File(oldpath);
            if (oldfile.exists()){
                try {
                    writeFileFromStream(configFile, new FileInputStream(oldfile));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to create config file "+path+": "+ex.getMessage());
                }
            }else{
                // Get it from jar
                InputStream is = AbstractConfig.class.getResourceAsStream("defaults/"+fileName);
                try {
                    writeFileFromStream(configFile, is);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to create config file "+path+": "+ex.getMessage());
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
            this.readObject(node);
        } catch (Exception e) {
            ShowDialog.showReadFileError(configFile, e);
            e.printStackTrace();
        }
    }
    
    protected void save(String nodeName){
        String fs = System.getProperty("file.separator");
        String path = System.getProperty("user.home") + fs + ".datasoul"+fs+"config" + System.getProperty("file.separator") + nodeName;
        
        try{

            Node node = this.writeObject();
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document
            FileOutputStream fos = new FileOutputStream(path);
            org.apache.xml.serialize.XMLSerializer xs = new org.apache.xml.serialize.XMLSerializer();
            OutputFormat outFormat = new OutputFormat();
            outFormat.setIndenting(true);
            outFormat.setEncoding("ISO-8859-1");
            xs.setOutputFormat(outFormat);
            xs.setOutputByteStream(fos);
            xs.serialize(doc);

        } catch(Exception e){
            ShowDialog.showWriteFileError(path, e);
            e.printStackTrace();
        }

    }
    
}
