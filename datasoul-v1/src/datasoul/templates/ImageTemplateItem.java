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
    private int stretch;
    private int alingment;
    private int vertAlign;
    
    private static JComboBox cbStrecth;
    private static JComboBox cbAlignment;
    private static JComboBox cbVerticalAlignment;
    
    
    
    public static final int ALIGN_LEFT = 0; //"Left";
    public static final int ALIGN_CENTER = 1;//"Center";
    public static final int ALIGN_RIGHT = 2; //"Right";
    public static final String[] ALIGN_TABLE = {"Left", "Center", "Right"};

    public static final int VALIGN_TOP = 0; //"Top";
    public static final int VALIGN_MIDDLE = 1; //"Middle";
    public static final int VALIGN_BOTTOM = 2; //"Bottom";
    public static final String[] VALIGN_TABLE = {"Top", "Middle", "Bottom"};
    
    public static final int STRETCH_NO = 0;
    public static final int STRETCH_YES = 1;
    public static final String[] STRETCH_TABLE = {"No", "Yes"};
    
    
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
        registerEditorComboBox("int.Alignment", cbAlignment);
        
        if (cbVerticalAlignment == null) {
            cbVerticalAlignment = new JComboBox();
            for (i=0; i<VALIGN_TABLE.length; i++){
                cbVerticalAlignment.addItem(VALIGN_TABLE[i]);
            }

        }
        registerEditorComboBox("int.VerticalAlignment", cbVerticalAlignment);

        if (cbStrecth == null){
            cbStrecth = new JComboBox();
            for (i=0; i<STRETCH_TABLE.length; i++){
                cbStrecth.addItem(STRETCH_TABLE[i]);
            }
        }
        registerEditorComboBox("int.Stretch", cbStrecth);
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
        properties.add("int.Alignment");
        registerDisplayString("int.Alignment", "Alignment");
        properties.add("int.VerticalAlignment");
        registerDisplayString("int.VerticalAlignment", "Vertical Alignment");
        properties.add("int.Stretch");
        registerDisplayString("int.Stretch", "Stretch");

    }
    
    @Override
    public void draw(Graphics2D g, float time) {
        
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
         firePropChanged("int.Stretch");
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
        firePropChanged("int.Alignment");
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
        firePropChanged("int.VerticalAlignment");
    }
     
    public void setVerticalAlignmentIdx(String x){
        setVerticalAlignmentIdx(Integer.parseInt(x));
    }
     
}
