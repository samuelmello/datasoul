/*
 * AlertControlPanel.java
 *
 * Created on 24 May 2006, 23:36
 */

package datasoul.datashow;

import datasoul.config.ConfigObj;
import datasoul.templates.TemplateComboBox;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author  samuelm
 */
public class AlertControlPanel extends javax.swing.JPanel {
    
    private ArrayList<String> history;
    
    private Alert activeAlert;
    
    /** Creates new form AlertControlPanel */
    public AlertControlPanel() {
        initComponents();
        cbMainTemplate.setFilterType(TemplateComboBox.FILTER_ALERT);
        cbMonitorTemplate.setFilterType(TemplateComboBox.FILTER_ALERT);
        history = new ArrayList<String>();
        cbHistory.removeAllItems();
        if (!ConfigObj.getInstance().getMonitorOutput()){
            cbShowOnMonitor1.setVisible(false);
            cbMonitorTemplate.setVisible(false);
            lblTemplateMonitor.setVisible(false);
            cbShowOnMain1.setSelected(true);
            cbShowOnMain1.setVisible(false);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlert = new javax.swing.JTextArea();
        cbShowOnMain1 = new javax.swing.JCheckBox();
        cbShowOnMonitor1 = new javax.swing.JCheckBox();
        cbMainTemplate = new datasoul.templates.TemplateComboBox();
        jLabel1 = new javax.swing.JLabel();
        lblTemplateMonitor = new javax.swing.JLabel();
        cbMonitorTemplate = new datasoul.templates.TemplateComboBox();
        jLabel13 = new javax.swing.JLabel();
        spnAlertTime = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        btnShowAlert = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cbHistory = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel3.setText(bundle.getString("Text")); // NOI18N

        txtAlert.setColumns(20);
        txtAlert.setRows(5);
        jScrollPane1.setViewportView(txtAlert);

        cbShowOnMain1.setText(bundle.getString("Show_on_Main_Output")); // NOI18N
        cbShowOnMain1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        cbShowOnMonitor1.setText(bundle.getString("Show_on_Monitor_Output")); // NOI18N
        cbShowOnMonitor1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        jLabel1.setText(bundle.getString("Template")); // NOI18N

        lblTemplateMonitor.setText(bundle.getString("Template")); // NOI18N

        jLabel13.setText(bundle.getString("Duration")); // NOI18N

        spnAlertTime.setModel(new SpinnerNumberModel(1, 1, 999, 1));

        jLabel14.setText(bundle.getString("seconds")); // NOI18N

        btnShowAlert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/bell.png"))); // NOI18N
        btnShowAlert.setText(bundle.getString("Show_Alert")); // NOI18N
        btnShowAlert.setToolTipText(bundle.getString("Show_alert")); // NOI18N
        btnShowAlert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAlertActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("Recently_used_texts")); // NOI18N

        cbHistory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbHistoryActionPerformed(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/button_cancel.png"))); // NOI18N
        btnCancel.setText(bundle.getString("Cancel")); // NOI18N
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbHistory, 0, 230, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbMainTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTemplateMonitor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbMonitorTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                    .addComponent(cbShowOnMain1)
                    .addComponent(cbShowOnMonitor1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnAlertTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnShowAlert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbShowOnMain1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbMainTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbShowOnMonitor1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTemplateMonitor)
                    .addComponent(cbMonitorTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(spnAlertTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnShowAlert)
                    .addComponent(btnCancel))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (activeAlert != null){
            activeAlert.interrupt();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cbHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbHistoryActionPerformed
        int index = cbHistory.getSelectedIndex();
        if (index >= 0 && index < history.size()){
            txtAlert.setText( history.get( index ) );
        }
    }//GEN-LAST:event_cbHistoryActionPerformed

    public void notifyAlertEnd(){
        btnShowAlert.setEnabled(true);
        btnCancel.setEnabled(false);
    }
    
    
    private void btnShowAlertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAlertActionPerformed
        activeAlert = new Alert(this);
        String text = txtAlert.getText();
        activeAlert.setText( text );
        activeAlert.setMainTemplate( cbMainTemplate.getSelectedItem().toString() );
        activeAlert.setMonitorTemplate( cbMonitorTemplate.getSelectedItem().toString() );
        activeAlert.setShowOnMain( cbShowOnMain1.isSelected() );
        activeAlert.setShowOnMonitor( cbShowOnMonitor1.isSelected() );
        activeAlert.setTime( Integer.parseInt(spnAlertTime.getValue().toString()) * 1000 );
        activeAlert.start();
        
        history.add(0, text);
        if (text.length() > 40){
            cbHistory.insertItemAt(text.substring(0, 40) + "...", 0);
        }else{
            cbHistory.insertItemAt(text, 0);
        }
        if (history.size()==6){
            history.remove(5);
            cbHistory.removeItemAt(5);
        }
        btnShowAlert.setEnabled(false);
        btnCancel.setEnabled(true);
        
    }//GEN-LAST:event_btnShowAlertActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnShowAlert;
    private javax.swing.JComboBox cbHistory;
    private datasoul.templates.TemplateComboBox cbMainTemplate;
    private datasoul.templates.TemplateComboBox cbMonitorTemplate;
    private javax.swing.JCheckBox cbShowOnMain1;
    private javax.swing.JCheckBox cbShowOnMonitor1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTemplateMonitor;
    private javax.swing.JSpinner spnAlertTime;
    private javax.swing.JTextArea txtAlert;
    // End of variables declaration//GEN-END:variables

    public class HistoryModel extends DefaultComboBoxModel {
        
    }
    
}
