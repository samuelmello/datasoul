/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * UsageStatsGrantDialog.java
 *
 * Created on Apr 2, 2010, 10:13:41 PM
 */

package datasoul;

import java.util.Random;

import datasoul.config.ConfigObj;
import datasoul.config.UsageStatsConfig;

/**
 *
 * @author samuel
 */
public class UsageStatsGrantDialog extends javax.swing.JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6788939686746288779L;
	/** Creates new form UsageStatsGrantDialog */
    public UsageStatsGrantDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        DatasoulMainForm.setDatasoulIcon(this);
        setSize(520,230);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        cbOnlineUpdate = new javax.swing.JCheckBox();
        cbOnlineStats = new javax.swing.JCheckBox();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Datasoul");
        setLocationByPlatform(true);
        setModal(true);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()-2f));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/web-browser.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel2.setText(bundle.getString("ALLOW THESE FUNCTIONS TO ACCESS INTERNET")); // NOI18N

        cbOnlineUpdate.setSelected(true);
        cbOnlineUpdate.setText(bundle.getString("NOTIFY ME WHEN A NEW VERSION OF DATASOUL IS AVAILABLE")); // NOI18N

        cbOnlineStats.setSelected(true);
        cbOnlineStats.setText(bundle.getString("SEND ANONYMOUS USAGE STATISTICS")); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("SansSerif", 1, 13));
        jTextArea1.setLineWrap(true);
        jTextArea1.setText(bundle.getString("IF YOU LIVE IN A COUNTRY WHERE CHRISTIANS ARE PERSECUTED AND DO NOT WISH TO RISK DETECTION YOU SHOULD NOT USE ANY OF THESE FUNCTIONS.")); // NOI18N
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jTextArea1.setFocusable(false);
        jTextArea1.setOpaque(false);
        jTextArea1.setRequestFocusEnabled(false);
        jTextArea1.setVerifyInputWhenFocusTarget(false);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
        jLabel1.setText(bundle.getString("WELCOME TO DATASOUL")); // NOI18N

        btnOk.setText(bundle.getString("OK")); // NOI18N
        btnOk.setMargin(new java.awt.Insets(2, 14, 2, 14));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cbOnlineUpdate)
                    .addComponent(cbOnlineStats)
                    .addComponent(jLabel1)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(453, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOnlineUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOnlineStats)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed

        if (UsageStatsConfig.getInstance().getID() == null || UsageStatsConfig.getInstance().getID().length() == 0){
            long r = new Random().nextLong();
            if (r < 0){
                r *= -1;
            }
            UsageStatsConfig.getInstance().setID(Long.toString(r));
        }

        if (cbOnlineStats.isSelected()){
            ConfigObj.getActiveInstance().setOnlineUsageStats("1");
        }else{
            ConfigObj.getActiveInstance().setOnlineUsageStats("0");
        }

        if (cbOnlineUpdate.isSelected()){
            ConfigObj.getActiveInstance().setOnlineCheckUpdate("1");
        }else{
            ConfigObj.getActiveInstance().setOnlineCheckUpdate("0");
        }

        ConfigObj.getActiveInstance().save();

        dispose();

    }//GEN-LAST:event_btnOkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox cbOnlineStats;
    private javax.swing.JCheckBox cbOnlineUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}

