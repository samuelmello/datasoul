/*
 * SwingDisplay.java
 *
 * Created on March 20, 2006, 11:45 PM
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author  root
 */
public class SwingDisplay extends javax.swing.JFrame {
    

    /**
     * Creates new form SwingDisplay
     */
    public SwingDisplay() {
        initComponents();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        swingDisplayPanel1 = new datasoul.render.SwingDisplayPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        setFocusCycleRoot(false);
        setResizable(false);
        setUndecorated(true);

        javax.swing.GroupLayout swingDisplayPanel1Layout = new javax.swing.GroupLayout(swingDisplayPanel1);
        swingDisplayPanel1.setLayout(swingDisplayPanel1Layout);
        swingDisplayPanel1Layout.setHorizontalGroup(
            swingDisplayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );
        swingDisplayPanel1Layout.setVerticalGroup(
            swingDisplayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swingDisplayPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swingDisplayPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void initDisplay(int width, int height, int top, int left, boolean isMonitor) {
        this.setSize(width, height);
        this.setLocation(left, top);
        swingDisplayPanel1.initDisplay(width, height, 0, 0);
        this.setVisible(true);
    }

    public void shutdown(){
        this.setVisible(false);
    }
    
    public void setBackgroundMode(int mode) {
        swingDisplayPanel1.setBackgroundMode(mode);
    }

    public void setClear(int mode) {
        swingDisplayPanel1.setClear(mode);
    }

    public void setBlack(int mode) {
        swingDisplayPanel1.setBlack(mode);
    }

    public void paintBackground(BufferedImage img) {
        swingDisplayPanel1.paintBackground(img);
    }

    void flip() {
        swingDisplayPanel1.flip();
    }

    void clear(){
        swingDisplayPanel1.clear();
    }

    void paint(BufferedImage img, Composite rule) {
        swingDisplayPanel1.paint(img, rule);
    }

    void setWindowTitle(String title) {
        this.setTitle(title);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datasoul.render.SwingDisplayPanel swingDisplayPanel1;
    // End of variables declaration//GEN-END:variables
    
}
