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

package datasoul.serviceitems.imagelist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTable;

import datasoul.serviceitems.GenericAttachmentServiceItem;
import datasoul.serviceitems.ServiceItemRenderer;
import datasoul.templates.ImageTemplateItem;

/**
 *
 * @author samuel
 */
public class ImageListServiceRenderer implements ServiceItemRenderer {

    public class ImageListRendererLabel extends JLabel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 3968632277212059372L;

		public void paint(Graphics g) {
            super.paint(g);
            image.draw((Graphics2D) g, 1.0f);

        }

    }


    private static Color selectedBackground;
    private static Color notSelectedBackground;

    private ImageListRendererLabel label;
    private ImageTemplateItem image;
    private File tmpFile;

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

    public void setImageWithoutTempFile(BufferedImage img){
        image.setImage(img);
    }

    public void setImage(BufferedImage bi) throws IOException{
        image.setImage(bi);
        
        // Ensure maximum image size is Full HD
        image.assertImageSize(0, 1080);

        // Clean up any previous
        if (tmpFile != null)
            tmpFile.delete();

        // Save temporary file
        tmpFile = GenericAttachmentServiceItem.createTemporaryFile("img-"+this.hashCode()+".png");
        tmpFile.deleteOnExit();
        ImageIO.write(image.getImage(), "png", tmpFile);
    }

    public void dispose(){
        if (tmpFile != null){
            tmpFile.delete();
        }
    }

    public File getTmpFile(){
        return tmpFile;
    }

    public BufferedImage getImage(){
        return image.getImage();
    }

}

