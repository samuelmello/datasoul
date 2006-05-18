/*
 * ImageTemplateItem.java
 *
 * Created on December 24, 2005, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import org.w3c.dom.Node;

/**
 *
 * @author samuelm
 */
public class ImageTemplateItem extends TemplateItem {
    
    BufferedImage img;
    private String imageInStr;
    private String filename;
    private String stretch;
    private String alingment;
    private String vertAlign;
    
    private static JComboBox cbStrecth;
    private static JComboBox cbAlignment;
    private static JComboBox cbVerticalAlignment;
    
    public static final String ALIGN_LEFT = "Left";
    public static final String ALIGN_CENTER = "Center";
    public static final String ALIGN_RIGHT = "Right";

    public static final String VALIGN_TOP = "Top";
    public static final String VALIGN_MIDDLE = "Middle";
    public static final String VALIGN_BOTTOM = "Bottom";
    
    
    public ImageTemplateItem() {
        super();
        this.setStretch("Yes");
        this.setAlignment(ALIGN_CENTER);
        this.setVerticalAlignment(VALIGN_MIDDLE);
        if (cbAlignment == null){
            cbAlignment = new JComboBox();
            cbAlignment.addItem(ALIGN_LEFT);
            cbAlignment.addItem(ALIGN_CENTER);
            cbAlignment.addItem(ALIGN_RIGHT);
        }
        registerEditorComboBox("Alignment", cbAlignment);
        
        if (cbVerticalAlignment == null) {
            cbVerticalAlignment = new JComboBox();
            cbVerticalAlignment.addItem(VALIGN_TOP);
            cbVerticalAlignment.addItem(VALIGN_MIDDLE);
            cbVerticalAlignment.addItem(VALIGN_BOTTOM);
        }
        registerEditorComboBox("VerticalAlignment", cbVerticalAlignment);

        if (cbStrecth == null){
            cbStrecth = new JComboBox();
            cbStrecth.addItem("Yes");
            cbStrecth.addItem("No");
        }
        registerEditorComboBox("Stretch", cbStrecth);
    }
    
    /** Creates a new instance of ImageTemplateItem */
    public ImageTemplateItem(String filename) {
        this();
        this.loadImage(filename);       
    }

    private void loadImage(String filename){
        if (filename != null){
            this.filename = filename;
            try {

                img = ImageIO.read( new File(filename) );
                this.setWidth( img.getWidth() );
                this.setHeight( img.getHeight() );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("Alignment");
        properties.add("VerticalAlignment");
        properties.add("Stretch");

    }
    
    @Override
    public void draw(Graphics2D g, float time) {
        
        int x, y, w, h;
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha() * time ) );
            
            if (stretch.equals("Yes")){
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
                
                if (alingment.equals(ALIGN_LEFT)){
                    x = this.getLeft();
                }else if (alingment.equals(ALIGN_RIGHT)){
                    x = this.getLeft() + ( this.getWidth() - w);
                }else{
                    x = this.getLeft() + ( this.getWidth() - w) / 2;
                }
                
                if (vertAlign.equals(VALIGN_TOP)){
                    y = this.getTop();
                }else if (vertAlign.equals(VALIGN_BOTTOM)){
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

    public String getImageInStr() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( this.img, "png", baos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        byte[] ba = baos.toByteArray();

        int len=ba.length,j;
        StringBuffer sb=new StringBuffer(len*2);
        for (j=0;j<len;j++) {
            String sByte=Integer.toHexString((int)(ba[j] & 0xFF));
            sb.append(stringAlign2chars(sByte));
        }
        return sb.toString();

    }

    private String stringAlign2chars(String str){
        if (str.length()!=2)
            return '0'+str;
        else
            return str;
    }
    
    public void setImageInStr(String strImage) {

        String str="";
        int intAux=0;
        byte[] bytes = new byte[strImage.length()/2];
        for(int i=0; i< strImage.length()-1;i=i+2){
            str = strImage.substring(i,i+2);
            intAux = Integer.parseInt(str,16);
            bytes[i/2]=(byte)intAux;
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            this.img = ImageIO.read(bais);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }        
    
     public Node writeObject() throws Exception{

        properties.add("ImageInStr");
              
        Node node = super.writeObject();
        
        properties.remove("ImageInStr");
        
        return node;
     }
     
     public void readObject(Node nodeIn) throws Exception {

        properties.add("ImageInStr");
              
        super.readObject(nodeIn);
        
        properties.remove("ImageInStr");
        
        return;
     }
     
     public String getStretch (){
         return stretch;
     }
     
     public void setStretch(String stretch){
         this.stretch = stretch;
         firePropChanged("Stretch");
     }

    public String getAlignment(){
        return this.alingment;
    }
    
    public void setAlignment(String alignment){
        if ( alignment.equals(ALIGN_LEFT) || alignment.equals(ALIGN_CENTER) || alignment.equals(ALIGN_RIGHT)){
            this.alingment = alignment;
            firePropChanged("Alignment");
        }else{
            System.out.println("Align: "+alignment);
        }
    }
    
    public String getVerticalAlignment(){
        return this.vertAlign;
    }
    
    public void setVerticalAlignment(String vertAlign){
        if ( vertAlign.equals(VALIGN_TOP) || vertAlign.equals(VALIGN_MIDDLE) || vertAlign.equals(VALIGN_BOTTOM)){
            this.vertAlign = vertAlign;
            firePropChanged("VerticalAlignment");
        }
    }

     
     
}
