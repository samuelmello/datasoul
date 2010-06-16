/* 
 * Copyright 2005-2010 Samuel Mello
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

package datasoul.templates;

import datasoul.DatasoulMainForm;
import datasoul.util.SerializableObject;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
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
 * @author samuel
 */
public class DisplayTemplateMetadata extends SerializableObject {

    private String name;
    private int targetContent;
    private String exampleImageName;

    private DisplayTemplate template;
    private String filename;

    public DisplayTemplateMetadata (DisplayTemplate template){
        this.template = template;
        this.name = template.getName();
        this.targetContent = template.getTargetContentIdx();
    }

    public DisplayTemplateMetadata (String filename) throws Exception{
        this.filename = filename;
        ZipReader zip = new ZipReader(filename, DatasoulMainForm.FILE_FORMAT_VERSION);
        loadFromFile(zip);
        zip.close();
    }

    public BufferedImage getMiniImage(){
        BufferedImage ret = null;
        try{
            ZipReader zip = new ZipReader(filename, DatasoulMainForm.FILE_FORMAT_VERSION);
            ret = ImageIO.read(zip.getInputStream(this.exampleImageName));
            zip.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public DisplayTemplate newDisplayTemplate() throws Exception{
        // Load original copy in TemplateManager
        DisplayTemplate tpl = new DisplayTemplate();
        tpl.loadFromFile(filename);
        return tpl;
    }


    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("Name");
        properties.add("TargetContentIdx");
        properties.add("ExampleImageName");
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTargetContentIdx(int i){
        this.targetContent = i;
    }

    public void setTargetContentIdx(String i){
        setTargetContentIdx(Integer.parseInt(i));
    }

    public void setTargetContent(String str){
        for (int i=0; i<DisplayTemplate.TARGET_CONTENT_TABLE.length; i++){
            if (str.equalsIgnoreCase(DisplayTemplate.TARGET_CONTENT_TABLE[i])){
                setTargetContentIdx(i);
            }
        }
    }

    public int getTargetContentIdx(){
        return this.targetContent;
    }

    public String getTargetContent(){
        return DisplayTemplate.TARGET_CONTENT_TABLE[this.targetContent];
    }

    public String getExampleImageName(){
        return this.exampleImageName;
    }

    public void setExampleImageName(String s){
        this.exampleImageName = s;
    }

    private void createExampleImage(ZipWriter zip){

        // Paint the template
        BufferedImage img = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = img.createGraphics();
        template.paint(g, 1.0f);

        // Resize it
        int newWidth = 100;
        int newHeight = (int) (100.0f * ((float) template.getHeight() / (float) template.getWidth()));
        BufferedImage miniimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = miniimg.createGraphics();
        g2.drawImage(img, 0, 0, newWidth, newHeight, null);

        // Store name and queue it
        this.exampleImageName = "mini-"+miniimg.hashCode()+".png";
        zip.appendImage(miniimg, exampleImageName);

    }

    @Override
    public Node writeObject(ZipWriter zip) throws Exception{
        createExampleImage(zip);
        return super.writeObject(zip);
    }

    public void save (ZipWriter zip) throws Exception{
        Node node = this.writeObject(zip);
        Document doc = node.getOwnerDocument();
        doc.appendChild(node);                        // Add Root to Document

        Source source = new DOMSource(doc);

        // Prepare the output file
        Result result = new StreamResult(zip.getOutputStream());

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
    }

    public void loadFromFile(ZipReader zip) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document dom = db.parse(zip.getMetadataInputStream());

        Node node = dom.getElementsByTagName("DisplayTemplateMetadata").item(0);

        this.readObject(node, zip);

    }

    public void setFilename(String f){
        this.filename = f;
    }

}

