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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author samuelm
 */
public class ImageTemplateItem extends TemplateItem {
    
    BufferedImage img;
    private String filename;
    
    /** Creates a new instance of ImageTemplateItem */
    public ImageTemplateItem(String filename) {
        super();
        properties.add("Filename");
        this.setFilename(filename);
    }

    
    public void draw(Graphics2D g) {
        
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()) );
            g.drawImage(img, this.getLeft(), this.getTop(), this.getWidth(), this.getHeight(), null );
        }finally{
            g.setComposite(oldComp);
        }
        
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        try {
            img = ImageIO.read( new File(filename) );
            this.setWidth( img.getWidth() );
            this.setHeight( img.getHeight() );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        firePropChanged("Filename");
    }
    
}
