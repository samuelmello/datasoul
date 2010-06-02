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
 * DisplayTemplate.java
 *
 * Created on December 31, 2005, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import datasoul.util.AttributedObject;
import datasoul.util.ObjectManager;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
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

/**
 *
 * @author samuelm
 */
public class DisplayTemplate extends AttributedObject {
    
    ArrayList<TemplateItem> items;
    
    static int defaultItemNameCount = 1;
    
    private String name;
    
    private int transitionKeepBG;
    public static final int KEEP_BG_YES = 0;
    public static final int KEEP_BG_NO = 1;
    public static final String[] KEEP_BG_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("YES"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NO")};
    private static JComboBox cbKeepBG;

    private int targetContent;
    public static final int TARGET_CONTENT_TEXT = 0;
    public static final int TARGET_CONTENT_SONG = 1;
    public static final int TARGET_CONTENT_STAGE = 2;
    public static final int TARGET_CONTENT_ALERT = 3;
    public static final int TARGET_CONTENT_IMAGES = 4;
    public static final String [] TARGET_CONTENT_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEXT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SONG"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("STAGE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ALERT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("IMAGES") };
    private static JComboBox cbTargetContent;

    private int width;
    private int height;

    /** Creates an empty DisplayTemplate */
    public DisplayTemplate() {
        super();
        items = new ArrayList<TemplateItem>();
        
        this.setTransitionKeepBGIdx(KEEP_BG_YES);
        this.setWidth(800);
        this.setHeight(600);
        
        if (cbKeepBG == null){
            cbKeepBG = new JComboBox();
            for (int i=0; i<KEEP_BG_TABLE.length; i++)
                cbKeepBG.addItem(KEEP_BG_TABLE[i]);
        }
        registerEditorComboBox("TransitionKeepBGIdx", cbKeepBG);

        if (cbTargetContent == null){
            cbTargetContent = new JComboBox();
            for (int i=0; i<TARGET_CONTENT_TABLE.length; i++)
                cbTargetContent.addItem(TARGET_CONTENT_TABLE[i]);
        }
        registerEditorComboBox("TargetContentIdx", cbTargetContent);

    }

    public static void importTemplate(String filename){
        DisplayTemplate t = new DisplayTemplate();
        try {
            t.loadFromFile(filename);
            if (ObjectManager.getInstance().getDatasoulMainForm() != null){
                t.save(ObjectManager.getInstance().getDatasoulMainForm().getRootPane());
            }else{
                // During initial conversion, at startup
                t.save(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadFromFile(String filename) throws Exception{
        
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();

        ZipReader zip;
        Document dom;

        // Open the file
        if (filename.endsWith(".template")){
            // Older version
            zip = new ZipReader(null, 1);
            FileInputStream fis = new FileInputStream(filename);
            GZIPInputStream zis = null;
            try{
                zis = new GZIPInputStream(fis);
            }catch(IOException e){
                zis = null;
            }
            //parse using builder to get DOM representation of the XML file
            if (zis != null){
                // file in GZIP format
                dom = db.parse(zis);
            }else{
                // uncompressed file
                dom = db.parse(filename);
            }

        }else{
            // Newer format, zip
            zip = new ZipReader(filename, DatasoulMainForm.FILE_FORMAT_VERSION);
            dom = db.parse(zip.getMainInputStream());
        }
        
        Node node = dom.getElementsByTagName("DisplayTemplate").item(0);

        this.readObject(node, zip);
        
        // Close the input stream
        zip.close();

        // upgrade from older versions
        
        // version up to 1.2 had fixed resolution at 640x480
        if (getDatasoulFileVersion() < 1){
            setWidth(640);
            setHeight(480);
        }
        // version 1.3 had fixed resolution at 800x600
        if (getDatasoulFileVersion() == 1){
            setWidth(800);
            setHeight(600);
        }

        // version 1.x don't have target content
        if (getDatasoulFileVersion() < 2){
            guessTargetContent();
        }

        setDatasoulFileVersion(DatasoulMainForm.getFileFormatVersion());
    }

    public void setResolution(int newWidth, int newHeight){
        float fWidth = (float) newWidth / (float) width;
        float fHeight = (float) newHeight / (float) height;
        for (TemplateItem it : items){
            it.setWidth((int) (fWidth * it.getWidth()));
            it.setHeight((int)(fHeight * it.getHeight()));
            it.setTop((int) (fHeight * it.getTop()));
            it.setLeft((int)(fWidth * it.getLeft()));
            if (it instanceof TextTemplateItem){
                TextTemplateItem text = ((TextTemplateItem)it);
                text.setFontSize( text.getFontSize() * fHeight );
            }
        }
        setWidth(newWidth);
        setHeight(newHeight);
    }
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("Name");
        registerDisplayString("Name", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEMPLATE NAME"));
        properties.add("TransitionKeepBGIdx");
        registerDisplayString("TransitionKeepBGIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TRANSITION KEEP BACKGROUND"));
        properties.add("TargetContentIdx");
        registerDisplayString("TargetContentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TARGET CONTENT"));
        properties.add("Width");
        registerDisplayString("Width", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("WIDTH"));
        properties.add("Height");
        registerDisplayString("Height", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("HEIGHT"));
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        
        this.name = name;
        firePropChanged("Name");
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    
    /**
     * items are exported as default access level to
     * allow other classes in this package access it directly.
     * This is needed by the TempalteEditorPanel.
     * But the general rule is that other components shall
     * avoid using it directly.
     */
    public ArrayList<TemplateItem> getItems(){
        return items;
    }
    
    public void addItem(TemplateItem t){
        if (t.getName().equals("")){
            t.setName(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ITEM #")+defaultItemNameCount++);
        }
        
        items.add(t);
    }
    
    public void removeItem(TemplateItem t){
        items.remove(t);
    }
    
    /**
     * Paint the template in the given Graphics2D g.
     * The 'time' parameter is used for transiction effects. The value range
     * is 0 to 1, where 0 is hidden and 1 is shown.
     * 
     * @param g The graphic to paint to
     * @param time valid values are between 0 (hidden) and 1 (shown). 
     */
    public synchronized void paint(Graphics2D g, float time){
        
        for (TemplateItem t : items){
            t.draw( g, time );
        }
        
    }
    
    public void moveUp(TemplateItem t){
        int size = items.size();
        for (int i=0; i<size-1; i++){
            if (items.get(i) == t){
                TemplateItem other = items.get(i+1);
                items.set(i+1, t);
                items.set(i, other);
                break;
            }
        }
    }
    
    public void moveDown(TemplateItem t){
        int size = items.size();
        for (int i=1; i<size; i++){
            if (items.get(i) == t){
                TemplateItem other = items.get(i-1);
                items.set(i-1, t);
                items.set(i, other);
                break;
            }
        }
    }
    
    @Override
    public Node writeObject(ZipWriter zip) throws Exception{
        
        Node n = super.writeObject(zip);
        
        Node nodeItems = n.getOwnerDocument().createElement("TemplateItems");
        
        for (int i=0; i< items.size(); i++){
            
            Node ti =  items.get(i).writeObject(zip);
            
            nodeItems.appendChild( nodeItems.getOwnerDocument().importNode(ti, true) );
            
        }
        
        n.appendChild(nodeItems);
        
        return n;
        
    }
    
    @Override
    public void readObject(Node nodeIn, ZipReader zip) {
        
        // read the properties
        super.readObject(nodeIn, zip);
        
        // now, read the template Items
        NodeList nodeList= nodeIn.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            // find the templateItem tag in document
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
                    nodeList.item(i).getNodeName().equals("TemplateItems") ){
                
                // for each templateItem, create it and add it to the list
                NodeList templateItemsNodes = nodeList.item(i).getChildNodes();
                for (int j=0; j<templateItemsNodes.getLength(); j++){
                    if ( templateItemsNodes.item(j).getNodeType() == Node.ELEMENT_NODE ){
                        
                        // Determine the type for the TemplateItem
                        String className = templateItemsNodes.item(j).getNodeName();
                        className = this.getClass().getPackage().getName() + "." + className;
                        Class cl;
                        Object ti = null;
                        
                        try {
                            cl = Class.forName(className);
                            ti = cl.newInstance();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        
                        // set the properties
                        if (ti instanceof TemplateItem){
                            ((TemplateItem) ti).readObject( templateItemsNodes.item(j), zip );
                            this.addItem( (TemplateItem) ti );
                        }
                    }
                }
                
            }
        }
        
        
    }
    
    public void setTemplateItems(String s){
        // just to conformance with super's readObject.
        // The TemplateItems will be readed lately by the overriden readObject
    }

    public void saveAs(JComponent owner) throws Exception {

        String newname = JOptionPane.showInputDialog("Enter new template name:", getName());
        if (newname != null && !newname.trim().equals("")){
            this.setName(newname.trim());
            this.save(owner);
        }

    }

    
    public void save(JComponent owner) throws Exception {

        if (this.getName() == null){
            saveAs(owner);
        }else{

            String path = ConfigObj.getActiveInstance().getStoragePathTemplates();
            String filename = path + File.separator + this.getName()+".templatez";

            ZipWriter zip = new ZipWriter(filename, DatasoulMainForm.FILE_FORMAT_VERSION);
            Node node = this.writeObject(zip);
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document

            Source source = new DOMSource(doc);

            // Prepare the output file
            Result result = new StreamResult(zip.getOutputStream());

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

            // Now, store meta data
            DisplayTemplateMetadata meta = new DisplayTemplateMetadata(this);
            meta.setFilename(filename);
            zip.startMetadata();
            meta.save(zip);

            TemplateManager.getInstance().addTemplateMetadata(meta);

            // Done, write images and close it
            zip.close();

        }
    }
    
    public void setTransitionKeepBGIdx(int i){
        this.transitionKeepBG = i;
        firePropChanged("TransitionKeepBGIdx");
    }

    public void setTransitionKeepBGIdx(String i){
        setTransitionKeepBGIdx(Integer.parseInt(i));
    }
    
    public void setTransitionKeepBG(String str){
        for (int i=0; i<KEEP_BG_TABLE.length; i++){
            if (str.equalsIgnoreCase(KEEP_BG_TABLE[i])){
                setTransitionKeepBGIdx(i);
            }
        }
    }
    
    public int getTransitionKeepBGIdx(){
        return transitionKeepBG;
    }

    public String getTransitionKeepBG(){
        return KEEP_BG_TABLE[transitionKeepBG];
    }

    public void setTargetContentIdx(int i){
        this.targetContent = i;
        firePropChanged("TargetContentIdx");
    }

    public void setTargetContentIdx(String i){
        setTargetContentIdx(Integer.parseInt(i));
    }

    public void setTargetContent(String str){
        for (int i=0; i<TARGET_CONTENT_TABLE.length; i++){
            if (str.equalsIgnoreCase(TARGET_CONTENT_TABLE[i])){
                setTargetContentIdx(i);
            }
        }
    }

    public int getTargetContentIdx(){
        return this.targetContent;
    }

    public String getTargetContent(){
        return TARGET_CONTENT_TABLE[this.targetContent];
    }

    /* used to set target content for templates created with older versions of datasoul */
    private void guessTargetContent(){
        for (TemplateItem t : items){
            if (t instanceof TextTemplateItem){
                int content = ((TextTemplateItem) t).getContentIdx();

                if (content == TextTemplateItem.CONTENT_ALERT){
                    this.setTargetContentIdx(TARGET_CONTENT_ALERT);
                    return;
                }
                if (content == TextTemplateItem.CONTENT_CLOCK ||
                    content == TextTemplateItem.CONTENT_TIMER){
                    this.setTargetContentIdx(TARGET_CONTENT_STAGE);
                    return;
                }
                if (content == TextTemplateItem.CONTENT_SONGAUTHOR ||
                    content == TextTemplateItem.CONTENT_SONGSOURCE ||
                    content == TextTemplateItem.CONTENT_COPYRIGHT){
                    this.setTargetContentIdx(TARGET_CONTENT_SONG);
                    return;
                }
            }
        }
        /* For format version < 2 we do not have Images templates yet */
        /* If none of the above matched, leave it as text */
    }


    public boolean useTimer(){
        for (TemplateItem ti : items){
            if (ti instanceof TextTemplateItem){
                switch (((TextTemplateItem)ti).getContentIdx()){
                    case TextTemplateItem.CONTENT_CLOCK:
                    case TextTemplateItem.CONTENT_TIMER:
                        return true;
                }
            }
        }
        return false;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setWidth(int w){
        this.width = w;
        firePropChanged("Width");
    }

    public void setHeight(int h){
        this.height = h;
        firePropChanged("Height");
    }

    public void setWidth(String w){
        setWidth(Integer.parseInt(w));
    }

    public void setHeight(String h){
        setHeight(Integer.parseInt(h));
    }

}


