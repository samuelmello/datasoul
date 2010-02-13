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
 * ImageTemplateItem.java
 *
 * Created on December 24, 2005, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import org.w3c.dom.Node;

/**
 *
 * @author samuelm
 */
public class ImageTemplateItem extends TemplateItem {
    
    private BufferedImage img;
    private int stretch;
    private int alingment;
    private int vertAlign;
    private int content;
    
    private static JComboBox cbStrecth;
    private static JComboBox cbAlignment;
    private static JComboBox cbVerticalAlignment;
    private static JComboBox cbContent;

    public static final int IMAGE_CONTENT_STATIC = 0;
    public static final int IMAGE_CONTENT_SLIDE = 1;
    public static final int IMAGE_CONTENT_NEXT_SLIDE = 2;
    public static final String[] IMAGE_CONTENT_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Static"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Slide"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Next_Slide")};
    
    public static final int ALIGN_LEFT = 0; //"Left";
    public static final int ALIGN_CENTER = 1;//"Center";
    public static final int ALIGN_RIGHT = 2; //"Right";
    public static final String[] ALIGN_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Left"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Center"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Right")};

    public static final int VALIGN_TOP = 0; //"Top";
    public static final int VALIGN_MIDDLE = 1; //"Middle";
    public static final int VALIGN_BOTTOM = 2; //"Bottom";
    public static final String[] VALIGN_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Top"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Middle"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Bottom")};
    
    public static final int STRETCH_NO = 0;
    public static final int STRETCH_YES = 1;
    public static final String[] STRETCH_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("No"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Yes")};

    private ZipReader activeZipReader;
    
    public ImageTemplateItem() {
        super();
        int i;
        this.setStretchIdx(STRETCH_YES);
        this.setAlignmentIdx(ALIGN_CENTER);
        this.setVerticalAlignmentIdx(VALIGN_MIDDLE);
        
        if (cbAlignment == null){
            cbAlignment = new JComboBox();
            for (i=0; i<ALIGN_TABLE.length; i++){
                cbAlignment.addItem(ALIGN_TABLE[i]);
            }
        }
        registerEditorComboBox("AlignmentIdx", cbAlignment);
        
        if (cbVerticalAlignment == null) {
            cbVerticalAlignment = new JComboBox();
            for (i=0; i<VALIGN_TABLE.length; i++){
                cbVerticalAlignment.addItem(VALIGN_TABLE[i]);
            }

        }
        registerEditorComboBox("VerticalAlignmentIdx", cbVerticalAlignment);

        if (cbStrecth == null){
            cbStrecth = new JComboBox();
            for (i=0; i<STRETCH_TABLE.length; i++){
                cbStrecth.addItem(STRETCH_TABLE[i]);
            }
        }
        registerEditorComboBox("StretchIdx", cbStrecth);

        if (cbContent == null){
            cbContent = new JComboBox();
            for (i=0; i<IMAGE_CONTENT_TABLE.length; i++)
                cbContent.addItem(IMAGE_CONTENT_TABLE[i]);
        }
        registerEditorComboBox("ContentIdx", cbContent);

    }
    
    public void assign(ImageTemplateItem from){
        super.assign(from);
        this.setStretchIdx(from.getStretchIdx());
        this.setAlignmentIdx(from.getAlignmentIdx());
        this.setVerticalAlignmentIdx(from.getVerticalAlignmentIdx());
        this.setContentIdx(from.getContentIdx());
        
        // ok, here we are getting just a reference for the image.
        // this will cause the assignment to do not really clone the object
        // but the images are not expected to change in runtime, so it should
        // be faster and save some memory
        this.setImage(from.getImage());
    }
    
    
    /** Creates a new instance of ImageTemplateItem */
    public ImageTemplateItem(String filename) {
        this();
        this.loadImage(filename);       
    }

    public void loadImage(String filename){
        if (filename != null){
            try {
                img = ImageIO.read( new File(filename) );
                assertImageSize();
                this.setWidth( img.getWidth() );
                this.setHeight( img.getHeight() );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void setImage(BufferedImage img){
        this.img = img;
        if (img != null){
            assertImageSize();
        }
    }
    
    public BufferedImage getImage(){
        return img;
    }

    /**
     * Ensures that the image is not larger than the screen size to 
     * avoid running out of memory
     */
    private void assertImageSize(){

        float fw = (float) this.img.getWidth() / (float) DisplayTemplate.TEMPLATE_WIDTH;
        float fh = (float) this.img.getHeight() / (float) DisplayTemplate.TEMPLATE_HEIGHT;
        
        // any of the sides is greater than the output size?
        // using 1.1 to allow some tolerance and avoid 
        // resizing multiple times
        if (fw > 1.1 || fh > 1.1){
            int neww, newh;
            if (fw > fh){
                neww = (int) (this.img.getWidth() * (1.0/fh));
                newh = (int) (this.img.getHeight() * (1.0/fh));
            }else{
                neww = (int) (this.img.getWidth() * (1.0/fw));
                newh = (int) (this.img.getHeight() * (1.0/fw));
            }
            
            BufferedImage img2 = new BufferedImage(neww, newh, BufferedImage.TYPE_4BYTE_ABGR);
            img2.getGraphics().drawImage(img, 0, 0, neww, newh, null);
            this.img = img2;
            setWidth(neww);
            setHeight(newh);
        }
    } 
    
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("ContentIdx");
        registerDisplayString("ContentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Content"));
        properties.add("AlignmentIdx");
        registerDisplayString("AlignmentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Alignment"));
        properties.add("VerticalAlignmentIdx");
        registerDisplayString("VerticalAlignmentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Vertical_Alignment"));
        properties.add("StretchIdx");
        registerDisplayString("StretchIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Stretch"));

    }
    
    @Override
    public void draw(Graphics2D g, float time) {
        
        if (img==null)
            return;
        
        int x, y, w, h;
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha() * time ) );
            
            if (stretch == STRETCH_YES){
                x = this.getLeft();
                y = this.getTop();
                w = this.getWidth();
                h = this.getHeight();
            }else{
                float ratio_w = (float) this.getWidth() / (float) img.getWidth();
                float ratio_h = (float) this.getHeight() / (float) img.getHeight();
                if (ratio_w < ratio_h){
                    w = (int) (img.getWidth() * ratio_w);
                    h = (int) (img.getHeight() * ratio_w);
                }else{
                    w = (int) (img.getWidth() * ratio_h);
                    h = (int) (img.getHeight() * ratio_h);
                }
                
                if (alingment == ALIGN_LEFT){
                    x = this.getLeft();
                }else if (alingment == ALIGN_RIGHT){
                    x = this.getLeft() + ( this.getWidth() - w);
                }else{
                    x = this.getLeft() + ( this.getWidth() - w) / 2;
                }
                
                if (vertAlign == VALIGN_TOP){
                    y = this.getTop();
                }else if (vertAlign == VALIGN_BOTTOM){
                    y = this.getTop() + ( this.getHeight() - h );
                }else{
                    y = this.getTop() + ( this.getHeight() - h ) / 2;
                }

            }
            
            g.drawImage(img, x, y, w, h, null );
        }finally{
            g.setComposite(oldComp);
        }
        
    }

    public String getImageHash() {
        return "img-"+Integer.toString(img.hashCode())+".png";
    }

    public void setImageHash(String strImage) {

        if (activeZipReader != null){
            InputStream is = null;
            try {
                is = activeZipReader.getInputStream(strImage);
                img = ImageIO.read(is);
                assertImageSize();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Node writeObject(ZipWriter zip) throws Exception{

        properties.add("ImageHash");
              
        Node node = super.writeObject(zip);

        zip.appendImage(img, getImageHash());
        
        properties.remove("ImageHash");
        
        return node;
    }


    @Override
    public synchronized void readObject(Node nodeIn, ZipReader zip) {

        properties.add("ImageHash");

        activeZipReader = zip;
              
        super.readObject(nodeIn, zip);

        activeZipReader = null;

        properties.remove("ImageHash");
        
        return;
    }
     
     public int getStretchIdx (){
         return stretch;
     }
     
     public String getStretch (){
         return STRETCH_TABLE[stretch];
     }
     
     
     public void setStretch(String stretch){
        for (int j=0; j<STRETCH_TABLE.length; j++){
            if (stretch.equalsIgnoreCase(STRETCH_TABLE[j])){
                setStretchIdx(j);
            }
        }
     }
     
     public void setStretchIdx(String stretch){
         setStretchIdx(Integer.parseInt(stretch));
     }
     
     public void setStretchIdx(int stretch){
         this.stretch = stretch;
         firePropChanged("StretchIdx");
     }

     
    public String getAlignment(){
        return ALIGN_TABLE[this.alingment];
    }

    public int getAlignmentIdx(){
        return this.alingment;
    }

    public void setAlignmentIdx(String s){
        setAlignmentIdx(Integer.parseInt(s));
    }
    
    public void setAlignmentIdx(int a){
        this.alingment = a;
        firePropChanged("AlignmentIdx");
    }
    
    public void setAlignment(String alignment){
        
        for (int j=0; j<ALIGN_TABLE.length; j++){
            if (alignment.equalsIgnoreCase(ALIGN_TABLE[j])){
                setAlignmentIdx(j);
            }
        }
        
    }
    
    public String getVerticalAlignment(){
        return VALIGN_TABLE[this.vertAlign];
    }

    public int getVerticalAlignmentIdx(){
        return this.vertAlign;
    }
    
    public void setVerticalAlignment(String vertAlign){
        for (int j=0; j<ALIGN_TABLE.length; j++){
            if (vertAlign.equalsIgnoreCase(VALIGN_TABLE[j])){
                setVerticalAlignmentIdx(j);
            }
        }
    }
    
    public void setVerticalAlignmentIdx(int x){
        this.vertAlign = x;
        firePropChanged("VerticalAlignmentIdx");
    }
     
    public void setVerticalAlignmentIdx(String x){
        setVerticalAlignmentIdx(Integer.parseInt(x));
    }

    public int getContentIdx(){
        return content;
    }

    public void setContentIdx(String content){
        setContentIdx(Integer.parseInt(content));
    }

    public void setContentIdx(int content){
        this.content = content;
        firePropChanged("ContentIdx");
    }

    public String getContent(){
        return IMAGE_CONTENT_TABLE[this.content];
    }

    public void setContent(String content){
        for (int j=0; j<IMAGE_CONTENT_TABLE.length; j++){
            if (content.equalsIgnoreCase(IMAGE_CONTENT_TABLE[j])){
                setContentIdx(j);
            }
        }
    }

     
}
