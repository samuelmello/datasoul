/*
 * ImageDisplayer.java
 *
 * Created on March 11, 2006, 3:53 PM
 */

package datasoul.datashow;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author  samuelm
 */
public class ImageDisplayer extends javax.swing.JPanel {
    
    private BufferedImage img;
    
    /** Creates new form ImageDisplayer */
    public ImageDisplayer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 204));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setPreferredSize(new java.awt.Dimension(160, 120));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 296, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void paint(Graphics g) {
    
        super.paint(g);
        
        if (img != null){
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null );
        }
        
    }

    public void setImage(BufferedImage newimg){
        this.img = newimg;
        this.repaint();
    }
    
    public void setImage(String filename) throws IOException{
        if (filename != null){
            img = ImageIO.read( new File(filename) );
            this.repaint();
        }
    }
    
    public BufferedImage getImage(){
        return this.img;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}