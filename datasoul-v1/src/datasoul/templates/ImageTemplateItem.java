/*
 * ImageTemplateItem.java
 *
 * Created on December 24, 2005, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.png.PNGImageReader;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author samuelm
 */
public class ImageTemplateItem extends TemplateItem {
    
    BufferedImage img;
    private String imageInStr;
    private String filename;
    
    public ImageTemplateItem() {
        super();
    }
    
    /** Creates a new instance of ImageTemplateItem */
    public ImageTemplateItem(String filename) {
        super();
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
    }
    
    @Override
    public void draw(Graphics2D g) {
        
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()) );
            g.drawImage(img, this.getLeft(), this.getTop(), this.getWidth(), this.getHeight(), null );
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
    
}
