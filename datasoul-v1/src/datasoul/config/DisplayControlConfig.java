/*
 * DisplayControlConfig.java
 *
 * Created on 6 November 2006, 23:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.util.SerializableObject;
import java.io.File;
import java.io.FileOutputStream;
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
public class DisplayControlConfig extends SerializableObject {
    
    
    private static DisplayControlConfig instance = null; 
    
    public static synchronized DisplayControlConfig getInstance(){
        if (instance == null){
            instance = new DisplayControlConfig();
        }
        return instance;
    }
    
    /** Creates a new instance of ConfigObj */
    private DisplayControlConfig() {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
        + "config" + System.getProperty("file.separator") + "displayControl.config";

        File configFile = new File(path);

        Document dom=null;
        Node node = null;
        try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(configFile);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("DisplayControlConfig").item(0);

        }catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error parsing config file\n"+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
            return;
        }        

        try {
            this.readObject(node);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error parsing config file\nError: "+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
        }
    }
    
    public void save(){
        try{
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
            + "config" + System.getProperty("file.separator") + "displayControl.config";

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
            JOptionPane.showMessageDialog(null,"Error writing file.\nError:"+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
        }
    }
    
    private int slideTransitionTime;
    private int slideShowHideTime;
    private boolean monitorFollowMainControls;
    private String templateMonitor;

    public int getSlideTransitionTime() {
        return slideTransitionTime;
    }

    public void setSlideTransitionTime(String slideTransitionTime) {
        int x = -1;
        try{
            x = Integer.parseInt(slideTransitionTime);
        }catch(Exception e){
            // do nothing
        }
        setSlideTransitionTime(x);
    }
    
    public void setSlideTransitionTime(int slideTransitionTime) {
        if (slideTransitionTime >= 0){
            this.slideTransitionTime = slideTransitionTime;
        }else{
            this.slideTransitionTime = 0;
        }
        save();
    }

    public int getSlideShowHideTime() {
        return slideShowHideTime;
    }

    public void setSlideShowHideTime(String slideShowHideTime) {
        int x = -1;
        try{
            x = Integer.parseInt(slideShowHideTime);
        }catch(Exception e){
            // do nothing
        }
        setSlideShowHideTime(x);
    }
    
    public void setSlideShowHideTime(int slideShowHideTime) {
        if (slideShowHideTime >= 0){
            this.slideShowHideTime = slideShowHideTime;
        }else{
            this.slideShowHideTime = 0;
        }
        save();
    }
    
    public boolean getMonitorFollowMainControls(){
        return monitorFollowMainControls;
    }

    public String getMonitorFollowMainControlsIdx(){
        if (monitorFollowMainControls==false){
            return "0";
        }else{
            return "1";
        }
    }
    
    public void setMonitorFollowMainControls(boolean b){
        monitorFollowMainControls = b;
        save();
    }
    
    public void setMonitorFollowMainControlsIdx(String str){
        setMonitorFollowMainControls( str.equals("1") );
    }

    public String getTemplateMonitor() {
        return templateMonitor;
    }

    public void setTemplateMonitor(String templateMonitor) {
        this.templateMonitor = templateMonitor;
        save();
    }

    
    
    protected void registerProperties() {
        properties.add("SlideTransitionTime");
        properties.add("SlideShowHideTime");
        properties.add("MonitorFollowMainControlsIdx");
        properties.add("TemplateMonitor");
    }

    
}
