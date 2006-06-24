/*
 * ConfigObj.java
 *
 * Created on 22 de Marco de 2006, 21:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul;

import datasoul.render.ContentManager;
import datasoul.render.SDLContentRender;
import datasoul.util.SerializableObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public class ConfigObj extends SerializableObject {
    
    static ConfigObj instance;
    
    private String mainOutput;
    private String mainOutputPositionLeft;
    private String mainOutputPositionTop;
    private String mainOutputSizeWidth;
    private String mainOutputSizeHeight;
    private String monitorOutput;
    private String monitorOutputPositionLeft;
    private String monitorOutputPositionTop;
    private String monitorOutputSizeWidth;
    private String monitorOutputSizeHeight;
    private String templateMonitor;
    private String templateText;
    private String videoInput;
    private String videoMode;
    private String videoDeintrelace;
    private String clockMode;    
    private String videoDebugMode;
    private String mainDisplayEngine;
    private String monitorDisplayEngine;
    private int slideTransitionTime;
    private int slideShowHideTime;
    private boolean monitorFollowMainControls;
    
    /** Creates a new instance of ConfigObj */
    private ConfigObj() {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
        + "config" + System.getProperty("file.separator") + "datasoul.config";

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
                node = dom.getElementsByTagName("ConfigObj").item(0);

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
            + "config" + System.getProperty("file.separator") + "datasoul.config";

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
            JOptionPane.showMessageDialog(null,"Error writing file.\nErro:"+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
        }
    }
    
    public static synchronized ConfigObj getInstance(){
        if(instance==null){
            instance = new ConfigObj();
        }
        return instance;
    }

    protected void registerProperties() {
        properties.add("MainDisplayEngine");
        properties.add("MonitorDisplayEngine");
        properties.add("MainOutput");
        properties.add("MainOutputPositionLeft");
        properties.add("MainOutputPositionTop");
        properties.add("MainOutputSizeWidth");
        properties.add("MainOutputSizeHeight");
        properties.add("MonitorOutput");
        properties.add("MonitorOutputPositionLeft");
        properties.add("MonitorOutputPositionTop");
        properties.add("MonitorOutputSizeWidth");
        properties.add("MonitorOutputSizeHeight");
        properties.add("TemplateMonitor");
        properties.add("TemplateText");
        properties.add("VideoInput");
        properties.add("VideoMode");
        properties.add("VideoDeintrelace");
        properties.add("ClockMode");        
        properties.add("VideoDebugMode");
        properties.add("SlideTransitionTime");
        properties.add("SlideShowHideTime");
        properties.add("MonitorFollowMainControls");
    }
    
    public ArrayList<String> getProperties(){
        return properties;
    }

    public String getMainOutput() {
        return mainOutput;
    }

    public void setMainOutput(String mainOutput) {
        this.mainOutput = mainOutput;
    }

    public String getMainOutputPositionLeft() {
        return mainOutputPositionLeft;
    }

    public void setMainOutputPositionLeft(String mainOutputPositionLeft) {
        this.mainOutputPositionLeft = mainOutputPositionLeft;
    }

    public String getMainOutputPositionTop() {
        return mainOutputPositionTop;
    }

    public void setMainOutputPositionTop(String mainOutputPositionTop) {
        this.mainOutputPositionTop = mainOutputPositionTop;
    }

    public String getMainOutputSizeWidth() {
        return mainOutputSizeWidth;
    }

    public void setMainOutputSizeWidth(String mainOutputSizeWidth) {
        this.mainOutputSizeWidth = mainOutputSizeWidth;
    }

    public String getMainOutputSizeHeight() {
        return mainOutputSizeHeight;
    }

    public void setMainOutputSizeHeight(String mainOutputSizeHeight) {
        this.mainOutputSizeHeight = mainOutputSizeHeight;
    }

    public String getMonitorOutput() {
        return monitorOutput;
    }

    public void setMonitorOutput(String monitorOutput) {
        this.monitorOutput = monitorOutput;
    }

    public String getMonitorOutputPositionLeft() {
        return monitorOutputPositionLeft;
    }

    public void setMonitorOutputPositionLeft(String monitorOutputPositionLeft) {
        this.monitorOutputPositionLeft = monitorOutputPositionLeft;
    }

    public String getMonitorOutputPositionTop() {
        return monitorOutputPositionTop;
    }

    public void setMonitorOutputPositionTop(String monitorOutputPositionTop) {
        this.monitorOutputPositionTop = monitorOutputPositionTop;
    }
    
    public String getMonitorOutputSizeWidth() {
        return monitorOutputSizeWidth;
    }

    public void setMonitorOutputSizeWidth(String monitorOutputSizeWidth) {
        this.monitorOutputSizeWidth = monitorOutputSizeWidth;
    }

    public String getMonitorOutputSizeHeight() {
        return monitorOutputSizeHeight;
    }

    public void setMonitorOutputSizeHeight(String monitorOutputSizeHeight) {
        this.monitorOutputSizeHeight = monitorOutputSizeHeight;
    }

    public String getTemplateMonitor() {
        return templateMonitor;
    }

    public void setTemplateMonitor(String templateMonitor) {
        this.templateMonitor = templateMonitor;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public String getVideoInput() {
        return videoInput;
    }

    public void setVideoInput(String videoInput) {

        this.videoInput = videoInput;
        if ( this.getMainOutput().equalsIgnoreCase("TRUE") ){
            int index = 1;
            if (videoInput.equalsIgnoreCase("Tuner")){
                index = 0;
            }else if (videoInput.equalsIgnoreCase("S-Video")){
                index = 3;
            }else if (videoInput.equalsIgnoreCase("Composite")){
                index = 1;
            }
            if ( ContentManager.getMainDisplay() instanceof SDLContentRender ){
                ((SDLContentRender)ContentManager.getMainDisplay()).setInputSrc(index);
            }
        }
        
        
    }

    public String getVideoMode() {
        return videoMode;
    }

    public void setVideoMode(String videoMode) {
        
        this.videoMode = videoMode;
        if ( this.getMainOutput().equalsIgnoreCase("TRUE") ){
            int index = 1;
            if (videoMode.equalsIgnoreCase("PAL")){
                index = 0;
            }else if (videoMode.equalsIgnoreCase("NTSC")){
                index = 1;
            }else if (videoMode.equalsIgnoreCase("SECAM")){
                index = 2;
            }
            if ( ContentManager.getMainDisplay() instanceof SDLContentRender ){
                ((SDLContentRender)ContentManager.getMainDisplay()).setInputMode(index);
            }
        }
        
    }

    public String getVideoDeintrelace() {
        return videoDeintrelace;
    }

    public void setVideoDeintrelace(String videoDeintrelace) {
        this.videoDeintrelace = videoDeintrelace;
        if ( this.getMainOutput().equalsIgnoreCase("TRUE") ){
            int index = 1;
            if (videoDeintrelace.equalsIgnoreCase("None")){
                index = 0;
            }else if (videoDeintrelace.equalsIgnoreCase("Blend")){
                index = 1;
            }else if (videoDeintrelace.equalsIgnoreCase("Smart Blend")){
                index = 2;
            }else if (videoDeintrelace.equalsIgnoreCase("Smooth Blend")){
                index = 3;
            }
            if ( ContentManager.getMainDisplay() instanceof SDLContentRender ){
                ((SDLContentRender)ContentManager.getMainDisplay()).setDeintrelaceMode( index );
            }
        }        
        
    }

    public String getClockMode() {
        return clockMode;
    }

    public void setClockMode(String clockMode) {
        this.clockMode = clockMode;
    }
    
    public String getVideoDebugMode() {
        return this.videoDebugMode;
    }

    public void setVideoDebugMode(String videoDebugMode) {
        this.videoDebugMode = videoDebugMode;
        if (this.getMainOutput().equalsIgnoreCase("TRUE")){
            if (videoDebugMode.equalsIgnoreCase("Yes")){
                ContentManager.getMainDisplay().setDebugMode( 1 );
            }else{
                ContentManager.getMainDisplay().setDebugMode( 0 );
            }
        }
    }

    public String getMainDisplayEngine(){
        return this.mainDisplayEngine;
    }
    
    public void setMainDisplayEngine(String engine){
        this.mainDisplayEngine = engine;
        ContentManager.setMainDisplayEngine(engine);
    }
    
    public String getMonitorDisplayEngine(){
        return this.monitorDisplayEngine;
    }
    
    public void setMonitorDisplayEngine(String engine){
        this.monitorDisplayEngine = engine;
        ContentManager.setMonitorDisplayEngine(engine);
    }

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
    }
    
    public boolean getMonitorFollowMainControls(){
        return monitorFollowMainControls;
    }
    
    public void setMonitorFollowMainControls(boolean b){
        monitorFollowMainControls = b;
    }
    
    public void setMonitorFollowMainControls(String str){
        try{
            setMonitorFollowMainControls( Boolean.parseBoolean(str) );
        }catch(Exception e){
            // ignore exception
        }
    }
}
