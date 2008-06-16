/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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
 * DisplayControlPanel.java
 *
 * Created on 24 May 2006, 23:14
 */

package datasoul.datashow;

import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.render.ContentManager;
import datasoul.templates.TemplateComboBox;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
  
/**
 *
 * @author  samuelm
 */
public class DisplayControlPanel extends javax.swing.JPanel {
    
    private boolean isMainOnBlack = false;
    private boolean isMainOnClean = false;
    
    /**
     * Creates new form DisplayControlPanel
     */
    public DisplayControlPanel() {
        initComponents();
        
        String x = DisplayControlConfig.getInstance().getTemplateMonitor();
        templateMonitor.setSelectedItem(x);
        
        jSliderShowHideSpeed.setValue( DisplayControlConfig.getInstance().getSlideShowHideTime() / 100 );
        jSliderSlideChangeSpeed.setValue( DisplayControlConfig.getInstance().getSlideTransitionTime() / 100 );
        cbMonitorFollowMain.setSelected( DisplayControlConfig.getInstance().getMonitorFollowMainControls() );
        updateMonitorControls();
 
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put( 0, new JLabel("0s") );
        labels.put( 10, new JLabel("1s") );
        labels.put( 20, new JLabel("2s") );
        labels.put( 30, new JLabel("3s") );
        jSliderShowHideSpeed.setLabelTable(labels);
        jSliderSlideChangeSpeed.setLabelTable(labels);

        templateMonitor.setFilterType(TemplateComboBox.FILTER_MONITOR);
        
        pnlSpeed.setVisible( jSpeedCb.isSelected() );
        pnlMonitor.setVisible( jMonitorCb.isSelected() );
        
        if (!ConfigObj.getInstance().getMonitorOutput()){
            pnlMonitor.setVisible(false);
            jMonitorCb.setVisible(false);
        }
        
    }

    
    public void mainDisplayBlack(){
        
        if( !isMainOnBlack ){
            if ( ConfigObj.getInstance().getMainOutput() ){
                ContentManager.getMainDisplay().setBlack( 1 );
            }
            isMainOnBlack = true;
            btnMainBlack.setSelected(true);
        }else{
            if ( ConfigObj.getInstance().getMainOutput() ){
                ContentManager.getMainDisplay().setBlack( 0 );
            }
            isMainOnBlack = false;
            btnMainBlack.setSelected(false);
        }

        if (DisplayControlConfig.getInstance().getMonitorFollowMainControls()){
            btnMonitorBlack.setSelected( btnMainBlack.isSelected() );
            btnMonitorBlackActionPerformed(null);
        }
    }
    public void mainDisplayClean(){
        
        /*
        
        if( !isMainOnClean ){
            if ( ConfigObj.getInstance().getMainOutput() ){
                ContentManager.getMainDisplay().setClear( 1 );
            }
            isMainOnClean = true;
            btnMainClear.setSelected(true);
        }else{
            if ( ConfigObj.getInstance().getMainOutput() ){
                ContentManager.getMainDisplay().setClear( 0 );
            }
            isMainOnClean = false;
            btnMainClear.setSelected(false);
        }
        
        if (DisplayControlConfig.getInstance().getMonitorFollowMainControls()){
            btnMonitorClear.setSelected( btnMainClear.isSelected() );
            btnMonitorClearActionPerformed(null);
        }
         */
    }
    
    public void mainDisplayShow(){

        int time = DisplayControlConfig.getInstance().getSlideShowHideTime();

        if (ConfigObj.getInstance().getMainOutput()){
            // avoid re-showing animation if inveked by shortcut key
            if (!btnShow.isSelected()){
                ContentManager.getMainDisplay().slideShow( time );
            }
        }
        
        btnShow.setSelected(true);
        btnHide.setSelected(false);
        
        if (DisplayControlConfig.getInstance().getMonitorFollowMainControls()){
            monitorDisplayShow();
        }
    }
    
    public void mainDisplayHide(){
        
        int time = DisplayControlConfig.getInstance().getSlideShowHideTime();
        
        if (ConfigObj.getInstance().getMainOutput()){
            // avoid re-showing animation if inveked by shortcut key
            if (!btnHide.isSelected()){
                ContentManager.getMainDisplay().slideHide( time );
            }
        }

        btnShow.setSelected(false);
        btnHide.setSelected(true);
        
        if (DisplayControlConfig.getInstance().getMonitorFollowMainControls()){
            monitorDisplayHide();
        }
    }
    
    public void monitorDisplayShow(){
        
        int time = DisplayControlConfig.getInstance().getSlideShowHideTime();
        
        btnMonitorShow.setSelected(true);
        btnMonitorHide.setSelected(false);
        
        if ( ConfigObj.getInstance().getMonitorOutput() ){
            // avoid re-showing animation if inveked by shortcut key
            if (!btnMonitorShow.isSelected()){
                ContentManager.getMonitorDisplay().slideShow( time );
            }
        }
    }
    
    public void monitorDisplayHide(){

        int time = DisplayControlConfig.getInstance().getSlideShowHideTime();

        btnMonitorShow.setSelected(false);
        btnMonitorHide.setSelected(true);

        if ( ConfigObj.getInstance().getMonitorOutput() ){
            // avoid re-showing animation if inveked by shortcut key
            if (!btnMonitorHide.isSelected()){
                ContentManager.getMonitorDisplay().slideHide( time );
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        pnlSpeed = new javax.swing.JPanel();
        jSliderSlideChangeSpeed = new javax.swing.JSlider();
        jLabel16 = new javax.swing.JLabel();
        jSliderShowHideSpeed = new javax.swing.JSlider();
        jLabel15 = new javax.swing.JLabel();
        pnlMonitor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        templateMonitor = new datasoul.templates.TemplateComboBox();
        jPanel1 = new javax.swing.JPanel();
        btnMonitorBlack = new javax.swing.JToggleButton();
        btnMonitorHide = new javax.swing.JToggleButton();
        btnMonitorShow = new javax.swing.JToggleButton();
        cbMonitorFollowMain = new javax.swing.JCheckBox();
        jMonitorCb = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnMainBlack = new javax.swing.JToggleButton();
        btnHide = new javax.swing.JToggleButton();
        btnShow = new javax.swing.JToggleButton();
        jSpeedCb = new javax.swing.JCheckBox();

        setAutoscrolls(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel10.setText(bundle.getString("Main_Output")); // NOI18N

        jSliderSlideChangeSpeed.setMajorTickSpacing(5);
        jSliderSlideChangeSpeed.setMaximum(30);
        jSliderSlideChangeSpeed.setMinorTickSpacing(1);
        jSliderSlideChangeSpeed.setPaintLabels(true);
        jSliderSlideChangeSpeed.setPaintTicks(true);
        jSliderSlideChangeSpeed.setValue(2);
        jSliderSlideChangeSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderSlideChangeSpeedStateChanged(evt);
            }
        });

        jLabel16.setText(bundle.getString("Slide_Change_Transition_Speed")); // NOI18N

        jSliderShowHideSpeed.setMajorTickSpacing(5);
        jSliderShowHideSpeed.setMaximum(30);
        jSliderShowHideSpeed.setMinorTickSpacing(1);
        jSliderShowHideSpeed.setPaintLabels(true);
        jSliderShowHideSpeed.setPaintTicks(true);
        jSliderShowHideSpeed.setValue(10);
        jSliderShowHideSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderShowHideSpeedStateChanged(evt);
            }
        });

        jLabel15.setText(bundle.getString("Show/Hide_Speed")); // NOI18N

        org.jdesktop.layout.GroupLayout pnlSpeedLayout = new org.jdesktop.layout.GroupLayout(pnlSpeed);
        pnlSpeed.setLayout(pnlSpeedLayout);
        pnlSpeedLayout.setHorizontalGroup(
            pnlSpeedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSliderShowHideSpeed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
            .add(pnlSpeedLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlSpeedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel15)
                    .add(jLabel16))
                .addContainerGap(112, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSliderSlideChangeSpeed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
        );
        pnlSpeedLayout.setVerticalGroup(
            pnlSpeedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlSpeedLayout.createSequentialGroup()
                .add(jLabel15)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSliderShowHideSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel16)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSliderSlideChangeSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setText(bundle.getString("Template")); // NOI18N

        templateMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateMonitorActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 3, 0));

        btnMonitorBlack.setText(bundle.getString("Black")); // NOI18N
        btnMonitorBlack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitorBlackActionPerformed(evt);
            }
        });
        jPanel1.add(btnMonitorBlack);

        btnMonitorHide.setSelected(true);
        btnMonitorHide.setText(bundle.getString("Hide")); // NOI18N
        btnMonitorHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitorHideActionPerformed(evt);
            }
        });
        jPanel1.add(btnMonitorHide);

        btnMonitorShow.setText(bundle.getString("Show")); // NOI18N
        btnMonitorShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitorShowActionPerformed(evt);
            }
        });
        jPanel1.add(btnMonitorShow);

        cbMonitorFollowMain.setText(bundle.getString("Monitor_follows_Main_Output_controls")); // NOI18N
        cbMonitorFollowMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbMonitorFollowMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMonitorFollowMainActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnlMonitorLayout = new org.jdesktop.layout.GroupLayout(pnlMonitor);
        pnlMonitor.setLayout(pnlMonitorLayout);
        pnlMonitorLayout.setHorizontalGroup(
            pnlMonitorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMonitorLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(templateMonitor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
            .add(pnlMonitorLayout.createSequentialGroup()
                .addContainerGap()
                .add(cbMonitorFollowMain)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        pnlMonitorLayout.setVerticalGroup(
            pnlMonitorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMonitorLayout.createSequentialGroup()
                .add(cbMonitorFollowMain)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMonitorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(templateMonitor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMonitorCb.setText(bundle.getString("Show_Monitor_Controls")); // NOI18N
        jMonitorCb.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jMonitorCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMonitorCbActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 3, 0));

        btnMainBlack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/black1.png"))); // NOI18N
        btnMainBlack.setText(bundle.getString("Black")); // NOI18N
        btnMainBlack.setToolTipText(bundle.getString("Makes_the_main_output_black_(F12)")); // NOI18N
        btnMainBlack.setFocusPainted(false);
        btnMainBlack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainBlackActionPerformed(evt);
            }
        });
        jPanel2.add(btnMainBlack);

        btnHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/jabber_offline.png"))); // NOI18N
        btnHide.setSelected(true);
        btnHide.setText(bundle.getString("Hide")); // NOI18N
        btnHide.setToolTipText(bundle.getString("Hide_the_main_output_content_(F9)")); // NOI18N
        btnHide.setFocusPainted(false);
        btnHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHideActionPerformed(evt);
            }
        });
        jPanel2.add(btnHide);

        btnShow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/jabber_online.png"))); // NOI18N
        btnShow.setText(bundle.getString("Show")); // NOI18N
        btnShow.setToolTipText(bundle.getString("Show_the_main_output_content_(F10)")); // NOI18N
        btnShow.setFocusPainted(false);
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });
        jPanel2.add(btnShow);

        jSpeedCb.setText(bundle.getString("Show_Speed_Controls")); // NOI18N
        jSpeedCb.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jSpeedCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSpeedCbActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .add(jLabel10)
                    .add(jMonitorCb)
                    .add(pnlMonitor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jSpeedCb)
                    .add(pnlSpeed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jMonitorCb)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMonitor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSpeedCb)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpeedCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSpeedCbActionPerformed
        pnlSpeed.setVisible( jSpeedCb.isSelected() );
    }//GEN-LAST:event_jSpeedCbActionPerformed

    private void jMonitorCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMonitorCbActionPerformed
        pnlMonitor.setVisible( jMonitorCb.isSelected() );
    }//GEN-LAST:event_jMonitorCbActionPerformed

    private void btnMonitorShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonitorShowActionPerformed
        if (btnMonitorShow.isSelected()){
            this.monitorDisplayShow();
        }else{
            btnMonitorShow.setSelected(true);
        }
    }//GEN-LAST:event_btnMonitorShowActionPerformed

    private void btnMonitorHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonitorHideActionPerformed
        if (btnMonitorHide.isSelected()){
            this.monitorDisplayHide();
        }else{
            btnMonitorHide.setSelected(true);
        }
    }//GEN-LAST:event_btnMonitorHideActionPerformed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        if (btnShow.isSelected()){
            this.mainDisplayShow();
        }else{
            btnShow.setSelected(true);
        }
    }//GEN-LAST:event_btnShowActionPerformed

    private void btnHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHideActionPerformed
        if (btnHide.isSelected()){
            this.mainDisplayHide();
        }else{
            btnHide.setSelected(true);
        }
        
    }//GEN-LAST:event_btnHideActionPerformed

    private void cbMonitorFollowMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMonitorFollowMainActionPerformed

        DisplayControlConfig.getInstance().setMonitorFollowMainControls( cbMonitorFollowMain.isSelected() );
        updateMonitorControls();
        
    }//GEN-LAST:event_cbMonitorFollowMainActionPerformed

    private void updateMonitorControls(){
        
        boolean b = ! DisplayControlConfig.getInstance().getMonitorFollowMainControls();
        btnMonitorBlack.setEnabled(b);
        //btnMonitorClear.setEnabled(b);
        btnMonitorHide.setEnabled(b);
        btnMonitorShow.setEnabled(b);
        
    }
    
    private void jSliderSlideChangeSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderSlideChangeSpeedStateChanged

        DisplayControlConfig.getInstance().setSlideTransitionTime( jSliderSlideChangeSpeed.getValue() * 100 );
        
    }//GEN-LAST:event_jSliderSlideChangeSpeedStateChanged

    private void jSliderShowHideSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderShowHideSpeedStateChanged
        
        DisplayControlConfig.getInstance().setSlideShowHideTime( jSliderShowHideSpeed.getValue() * 100 );
        
    }//GEN-LAST:event_jSliderShowHideSpeedStateChanged

    private void btnMonitorBlackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonitorBlackActionPerformed
        if ( ConfigObj.getInstance().getMonitorOutput() ){
            if( btnMonitorBlack.isSelected() ){
                ContentManager.getMonitorDisplay().setBlack( 1 );
            }else{
                ContentManager.getMonitorDisplay().setBlack( 0 );
            }
        }
        
    }//GEN-LAST:event_btnMonitorBlackActionPerformed

    private void templateMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateMonitorActionPerformed

        if (templateMonitor.getSelectedItem() != null && !templateMonitor.isUpdating()){
            ContentManager.getInstance().setTemplateMonitorLive( templateMonitor.getSelectedItem().toString() );
            ContentManager.getInstance().slideChange(-1);
            DisplayControlConfig.getInstance().setTemplateMonitor( templateMonitor.getSelectedItem().toString() );                    
        }
        
    }//GEN-LAST:event_templateMonitorActionPerformed

    private void btnMainBlackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainBlackActionPerformed

        this.mainDisplayBlack();
    }//GEN-LAST:event_btnMainBlackActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnHide;
    private javax.swing.JToggleButton btnMainBlack;
    private javax.swing.JToggleButton btnMonitorBlack;
    private javax.swing.JToggleButton btnMonitorHide;
    private javax.swing.JToggleButton btnMonitorShow;
    private javax.swing.JToggleButton btnShow;
    private javax.swing.JCheckBox cbMonitorFollowMain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JCheckBox jMonitorCb;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSliderShowHideSpeed;
    private javax.swing.JSlider jSliderSlideChangeSpeed;
    private javax.swing.JCheckBox jSpeedCb;
    private javax.swing.JPanel pnlMonitor;
    private javax.swing.JPanel pnlSpeed;
    private datasoul.templates.TemplateComboBox templateMonitor;
    // End of variables declaration//GEN-END:variables
    
}
