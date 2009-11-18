/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.templates.ImageTemplateItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author samuel
 */
public class ImageListServiceRenderer implements ServiceItemRenderer {

    public class ImageListRendererLabel extends JLabel {

        public void paint(Graphics g) {
            super.paint(g);
            image.draw((Graphics2D) g, 1.0f);

        }

    }


    private static Color selectedBackground;
    private static Color notSelectedBackground;

    private ImageListRendererLabel label;
    private ImageTemplateItem image;

    private int height = 125;

    static {
        JTable aux = new JTable();
        selectedBackground = new Color( aux.getSelectionBackground().getRGB()) ;
        notSelectedBackground = new Color( aux.getBackground().getRGB());
    }

    /** Creates a new instance of TextServiceItemRenderer */
    public ImageListServiceRenderer() {
        label = new ImageListRendererLabel();
        label.setOpaque(true);
        label.setDoubleBuffered(true);
        image = new ImageTemplateItem();
        image.setStretchIdx(0);
    }

    public void setWidth(int width) {
        label.setSize(width, height);
        image.setHeight(height);
        image.setWidth(width);
    }

    public int getHeight() {
        return height;
    }

    public Component getComponent(boolean selected, boolean hasFocus) {

        if (selected){
            label.setBackground(selectedBackground);
        }else{
            label.setBackground(notSelectedBackground);
        }

        return label;
    }


    public void setMark(boolean showMark){
        // ignore marks for images
    }

    public boolean getMark() {
        // ignore marks for images
        return false;
    }

    public boolean getShowMark() {
        // ignore marks for images
        return false;
    }

    public void setImage(BufferedImage bi){
        image.setImage(bi);
    }

    public BufferedImage getImage(){
        return image.getImage();
    }

}
