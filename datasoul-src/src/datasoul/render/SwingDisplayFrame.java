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

package datasoul.render;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SwingDisplayFrame extends javax.swing.JFrame {

    private ContentDisplayRenderer contentDisplay;

    /**
     * Creates new form SwingDisplayFrame
     */
    public SwingDisplayFrame() {
        initComponents();
        DatasoulMainForm.setDatasoulIcon(this);
        setBackground(new Color(0,0,0,0));
        contentDisplay = new ContentDisplayRenderer() {
            @Override
            public void repaint() {
                    SwingDisplayFrame.this.repaint();
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public void registerAsMain(){
        int w = ConfigObj.getActiveInstance().getMainOutputDeviceObj().getWidth();
        int h = ConfigObj.getActiveInstance().getMainOutputDeviceObj().getProportionalHeight(w);
        contentDisplay.initDisplay(w, h);
        ContentRender r = new ContentRender(w, h, contentDisplay);
        ContentManager.getInstance().registerMainRender(r);
        this.setSize(w, h);
    }

    public void registerAsMonitor(){
        int w = ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getWidth();
        int h = ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getProportionalHeight(w);
        contentDisplay.initDisplay(w, h);
        ContentRender r = new ContentRender(w, h, contentDisplay);
        ContentManager.getInstance().registerMonitorRender(r);
        this.setSize(w, h);
    }
    
    
    @Override
    public void paint (Graphics g){
        BufferedImage img = contentDisplay.getActiveImage();
        if (img != null){
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.Src);
            synchronized(img){
                g.drawImage(img, 0,0, img.getWidth(), img.getHeight(), null);
            }
        }
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
