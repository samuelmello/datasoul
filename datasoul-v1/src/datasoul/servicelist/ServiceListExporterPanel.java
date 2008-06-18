/*
 * ServiceListExporterPanel.java
 *
 * Created on June 4, 2008, 11:44 PM
 */

package datasoul.servicelist;

import datasoul.datashow.ServiceListTable;
import datasoul.datashow.TextServiceItem;
import datasoul.song.Song;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author  samuel
 */
public class ServiceListExporterPanel extends javax.swing.JFrame {
    
    /** Creates new form ServiceListExporterPanel */
    public ServiceListExporterPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbServicePlan = new javax.swing.JCheckBox();
        cbLyrics = new javax.swing.JCheckBox();
        cbChordsSimple = new javax.swing.JCheckBox();
        cbChordsComplete = new javax.swing.JCheckBox();
        cbOk = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbFormat = new javax.swing.JComboBox();
        cbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export Service List");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setText("Select sessions to export:");

        cbServicePlan.setSelected(true);
        cbServicePlan.setText("Service Plan");
        cbServicePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbServicePlanActionPerformed(evt);
            }
        });

        cbLyrics.setSelected(true);
        cbLyrics.setText("Song Lyrics and Texts Items");
        cbLyrics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLyricsActionPerformed(evt);
            }
        });

        cbChordsSimple.setSelected(true);
        cbChordsSimple.setText("Songs Chords Simple");

        cbChordsComplete.setSelected(true);
        cbChordsComplete.setText("Songs Chords Complete");

        cbOk.setText("OK");
        cbOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOkActionPerformed(evt);
            }
        });

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel2.setText("Format:");

        cbFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PDF", "RTF" }));

        cbCancel.setText("Cancel");
        cbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(269, Short.MAX_VALUE)
                .addComponent(cbCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOk)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbServicePlan)
                            .addComponent(cbLyrics)
                            .addComponent(cbChordsSimple)
                            .addComponent(cbChordsComplete)))
                    .addComponent(jLabel1))
                .addContainerGap(145, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbServicePlan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLyrics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbChordsSimple)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbChordsComplete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbOk)
                    .addComponent(cbCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbServicePlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbServicePlanActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbServicePlanActionPerformed

    private void cbLyricsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLyricsActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbLyricsActionPerformed

    private void cbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOkActionPerformed
        try{
            
            String fileName = null;
            
            // Choose PDF or RTF
            int type =  ServiceListExporterDocument.TYPE_PDF;
            String fileextension = ".pdf";
            if (cbFormat.getSelectedIndex() == 1){
                type = ServiceListExporterDocument.TYPE_RTF;
                fileextension = ".rtf";
            }

            // Ask for file to save
            JFileChooser fc = new JFileChooser();
//            File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
//            fc.setCurrentDirectory(dir);
            fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Select_the_file_to_save."));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileName = fc.getSelectedFile().getPath();
                if (!fileName.contains(fileextension)) {
                    fileName = fileName + fileextension;
                }
            }

            
            // Process it
            if (fileName != null){

                ServiceListExporterDocument sled = new ServiceListExporterDocument(type, fileName);
                
                if (cbServicePlan.isSelected()){
                    sled.addServicePlan();
                }

                ServiceListTable slt =  ServiceListTable.getActiveInstance();
                for (int i=0; i<slt.getRowCount(); i++){
                    Object o = slt.getServiceItem(i);
                    if (o instanceof Song){
                        if (cbLyrics.isSelected()){
                            sled.addSongLyrics((Song)o);
                        }
                        if (cbChordsSimple.isSelected()){
                            sled.addSongChordsSimple((Song)o);
                        }
                        if (cbChordsComplete.isSelected()){
                            sled.addSongChordsComplete((Song)o);
                        }
                    }else if (o instanceof TextServiceItem){
                        if (cbLyrics.isSelected()){
                            sled.addTextItem((TextServiceItem)o);
                        }
                    }
                            
                }

                sled.write();
            }
            
            JOptionPane.showMessageDialog(this, "Document created successfully");
            dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error creating document: "+e.getMessage());
        }
}//GEN-LAST:event_cbOkActionPerformed

    private void cbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCancelActionPerformed
        dispose();
    }//GEN-LAST:event_cbCancelActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cbCancel;
    private javax.swing.JCheckBox cbChordsComplete;
    private javax.swing.JCheckBox cbChordsSimple;
    private javax.swing.JComboBox cbFormat;
    private javax.swing.JCheckBox cbLyrics;
    private javax.swing.JButton cbOk;
    private javax.swing.JCheckBox cbServicePlan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
    
}
