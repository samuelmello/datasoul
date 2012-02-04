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

/*
 * BackgroundConfigFrame.java
 *
 * Created on Mar 7, 2010, 11:23:04 PM
 */

package datasoul.datashow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import datasoul.DatasoulMainForm;
import datasoul.config.BackgroundConfig;
import datasoul.config.ConfigObj;
import datasoul.templates.ImageTemplateItem;
import datasoul.util.ObjectManager;

/**
 *
 * @author samuel
 */
public class BackgroundConfigFrame extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5552735972332154608L;
	/** Creates new form BackgroundConfigFrame */
    public BackgroundConfigFrame() {
        initComponents();

        DatasoulMainForm.setDatasoulIcon(this);

        BufferedImage img1 = BackgroundConfig.getInstance().getBackgroundImg();

        imageDisplay.getImageTemplateItem().setStretchIdx(ImageTemplateItem.STRETCH_YES);
        imageDisplay.getImageTemplateItem().setAlignmentIdx(ImageTemplateItem.ALIGN_CENTER);
        imageDisplay.getImageTemplateItem().setVerticalAlignmentIdx(ImageTemplateItem.VALIGN_MIDDLE);
        imageDisplay.updateSize();
        imageDisplay.getImageTemplateItem().setImage(img1);

        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC){
            rbStatic.setSelected(true);
        }
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_VIDEO){
            rbVideo.setSelected(true);
        }
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_LIVE){
            rbLive.setSelected(true);
        }

        lblVideo.setText(BackgroundConfig.getInstance().getVideoFile());

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        imageDisplay = new datasoul.util.ImageDisplay();
        btnColorMain = new javax.swing.JButton();
        btnChangeMain = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        rbStatic = new javax.swing.JRadioButton();
        rbVideo = new javax.swing.JRadioButton();
        lblVideo = new javax.swing.JLabel();
        btnChangeVideo = new javax.swing.JButton();
        rbLive = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        btnDiscard = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        setTitle(bundle.getString("DATASOUL BACKGROUND")); // NOI18N

        imageDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        imageDisplay.setPreferredSize(new java.awt.Dimension(160, 120));

        javax.swing.GroupLayout imageDisplayLayout = new javax.swing.GroupLayout(imageDisplay);
        imageDisplay.setLayout(imageDisplayLayout);
        imageDisplayLayout.setHorizontalGroup(
            imageDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        imageDisplayLayout.setVerticalGroup(
            imageDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        btnColorMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_bucketfill.png"))); // NOI18N
        btnColorMain.setText(bundle.getString("COLOR")); // NOI18N
        btnColorMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColorMainActionPerformed(evt);
            }
        });

        btnChangeMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/image-x-generic.png"))); // NOI18N
        btnChangeMain.setText(bundle.getString("IMAGE")); // NOI18N
        btnChangeMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeMainActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("CHOOSE BACKGROUND:")); // NOI18N

        buttonGroup1.add(rbStatic);
        rbStatic.setText(bundle.getString("STATIC")); // NOI18N
        rbStatic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbStaticActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbVideo);
        rbVideo.setText(bundle.getString("VIDEO")); // NOI18N

        lblVideo.setText("jLabel2");

        btnChangeVideo.setText(bundle.getString("CHANGE")); // NOI18N
        btnChangeVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeVideoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbLive);
        rbLive.setText(bundle.getString("LIVE VIDEO")); // NOI18N

        btnDiscard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/window-close.png"))); // NOI18N
        btnDiscard.setText(bundle.getString("DISCARD AND CLOSE")); // NOI18N
        btnDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscardActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-save.png"))); // NOI18N
        btnSave.setText(bundle.getString("SAVE AND CLOSE")); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDiscard)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDiscard)
                    .addComponent(btnSave))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(imageDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnColorMain)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnChangeMain))))
                            .addComponent(rbStatic)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnChangeVideo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVideo, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                            .addComponent(rbVideo)
                            .addComponent(rbLive))
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbStatic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnChangeMain, 0, 0, Short.MAX_VALUE)
                    .addComponent(btnColorMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addComponent(rbVideo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeVideo)
                    .addComponent(lblVideo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLive)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnColorMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColorMainActionPerformed

        Color color =  JColorChooser.showDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CHOOSE COLOR"), Color.BLACK);
        if (color != null){ // color is null if the user choose cancel.
            BufferedImage img = new BufferedImage(ConfigObj.getActiveInstance().getMainRenderWidth(), ConfigObj.getActiveInstance().getMainRenderHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = img.createGraphics();
            g.setColor(color);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            imageDisplay.getImageTemplateItem().setImage(img);
            imageDisplay.updateSize();
            imageDisplay.repaint();
        }
    }//GEN-LAST:event_btnColorMainActionPerformed

    private void btnChangeMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeMainActionPerformed

        JFileChooser fc = new JFileChooser();
        File dir = new File(System.getProperty("user.dir"));
        fc.setCurrentDirectory(dir);
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setControlButtonsAreShown(true);
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SELECT IMAGE"));
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION && fc.getSelectedFile().exists() ){
            String filename = fc.getSelectedFile().getAbsolutePath();
            imageDisplay.getImageTemplateItem().loadImage(filename);
            imageDisplay.getImageTemplateItem().assertImageSize(ConfigObj.getActiveInstance().getMainRenderWidth(), ConfigObj.getActiveInstance().getMainRenderHeight());
            imageDisplay.updateSize();
            imageDisplay.repaint();
        }
    }//GEN-LAST:event_btnChangeMainActionPerformed

    private void rbStaticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbStaticActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbStaticActionPerformed

    private void btnDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscardActionPerformed
        ObjectManager.getInstance().setBackgroundConfigFrame(null);
        dispose();
    }//GEN-LAST:event_btnDiscardActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        BackgroundConfig bg = BackgroundConfig.getInstance();
        bg.setBackgroundImg( imageDisplay.getImageTemplateItem().getImage() );
        bg.setVideoFile(lblVideo.getText());
        if (rbStatic.isSelected()){
            bg.setMode(BackgroundConfig.MODE_STATIC);
        }else if (rbVideo.isSelected()){
            bg.setMode(BackgroundConfig.MODE_VIDEO);
        }else if (rbLive.isSelected()){
            bg.setMode(BackgroundConfig.MODE_LIVE);
        }
        bg.save();
        ObjectManager.getInstance().getMainVideoFrame().playBackground();
        ObjectManager.getInstance().setBackgroundConfigFrame(null);
        dispose();
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnChangeVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeVideoActionPerformed
        JFileChooser fc = new JFileChooser();
        File dir = new File(System.getProperty("user.dir"));
        fc.setCurrentDirectory(dir);
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setControlButtonsAreShown(true);
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SELECT VIDEO"));
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION && fc.getSelectedFile().exists() ){
            String filename = fc.getSelectedFile().getAbsolutePath();
            lblVideo.setText(filename);
        }
    }//GEN-LAST:event_btnChangeVideoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangeMain;
    private javax.swing.JButton btnChangeVideo;
    private javax.swing.JButton btnColorMain;
    private javax.swing.JButton btnDiscard;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private datasoul.util.ImageDisplay imageDisplay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblVideo;
    private javax.swing.JRadioButton rbLive;
    private javax.swing.JRadioButton rbStatic;
    private javax.swing.JRadioButton rbVideo;
    // End of variables declaration//GEN-END:variables

}


