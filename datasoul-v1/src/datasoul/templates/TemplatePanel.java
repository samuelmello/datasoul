/*
 * TemplatePanel.java
 *
 * Created on February 25, 2006, 10:11 AM
 */

package datasoul.templates;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author  samuelm
 */
public class TemplatePanel extends javax.swing.JPanel {
    
    /** Creates new form TemplatePanel */
    public TemplatePanel() {
        initComponents();
        
        TemplateManager manager = TemplateManager.getInstance();
        manager.refreshAvailableTemplates();
        jTableTemplates.setModel( manager );
        
        templateEditorPanel1.setPropertiesTable(jTableProperties);
        
        lblTemplateName.setText( templateEditorPanel1.getTemplate().getName() );
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        btnAddText = new javax.swing.JButton();
        btnMoveUp = new javax.swing.JButton();
        btnAddImage = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnMoveDown = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblTemplateName = new javax.swing.JLabel();
        templateEditorPanel1 = new datasoul.templates.TemplateEditorPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTemplates = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnLoad = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnDeleteTemplate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableProperties = new javax.swing.JTable();

        jSplitPane1.setDividerLocation(250);
        btnAddText.setText("Add Text");
        btnAddText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTextActionPerformed(evt);
            }
        });

        btnMoveUp.setText("Move Up");
        btnMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveUpActionPerformed(evt);
            }
        });

        btnAddImage.setText("Add Image");
        btnAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImageActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDeleteItem.setText("Delete");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        btnMoveDown.setText("Move Down");
        btnMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveDownActionPerformed(evt);
            }
        });

        jLabel1.setText("Template:");

        lblTemplateName.setText("jLabel2");

        org.jdesktop.layout.GroupLayout templateEditorPanel1Layout = new org.jdesktop.layout.GroupLayout(templateEditorPanel1);
        templateEditorPanel1.setLayout(templateEditorPanel1Layout);
        templateEditorPanel1Layout.setHorizontalGroup(
            templateEditorPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 636, Short.MAX_VALUE)
        );
        templateEditorPanel1Layout.setVerticalGroup(
            templateEditorPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 476, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(btnAddImage)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnAddText)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnDeleteItem)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnMoveUp)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnMoveDown)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSave))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblTemplateName))
                    .add(templateEditorPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(lblTemplateName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(templateEditorPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnAddImage)
                    .add(btnAddText)
                    .add(btnDeleteItem)
                    .add(btnMoveUp)
                    .add(btnMoveDown)
                    .add(btnSave))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jSplitPane1.setRightComponent(jPanel1);

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jTableTemplates.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableTemplates);

        jToolBar1.setFloatable(false);
        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        jToolBar1.add(btnLoad);

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jToolBar1.add(btnNew);

        btnDeleteTemplate.setText("Delete");
        btnDeleteTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTemplateActionPerformed(evt);
            }
        });

        jToolBar1.add(btnDeleteTemplate);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jSplitPane2.setTopComponent(jPanel2);

        jTableProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTableProperties);

        jSplitPane2.setRightComponent(jScrollPane2);

        jSplitPane1.setLeftComponent(jSplitPane2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTemplateActionPerformed


        if ( jTableTemplates.getSelectedRowCount() == 1){
            Object x = jTableTemplates.getValueAt( jTableTemplates.getSelectedRow(), jTableTemplates.getSelectedColumn() );
            if (x instanceof String){
                String str = (String) x;
                int confirm = JOptionPane.showConfirmDialog(this, "Delete template "+x+"?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION){
                    try{
                        TemplateManager.getInstance().deleteTemplate(str);
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(this,"Unable to save template:\n"+e.getMessage(),"DataSoul Error",0);    
                    }// try
                }//if confirm
            }// if instance of
        }// if selected

        
    }//GEN-LAST:event_btnDeleteTemplateActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

        if ( jTableTemplates.getSelectedRowCount() == 1){
            Object x = jTableTemplates.getValueAt( jTableTemplates.getSelectedRow(), jTableTemplates.getSelectedColumn() );
            if (x instanceof String){
                String str = (String) x;
                templateEditorPanel1.open( str );
                lblTemplateName.setText( str );
            }
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        templateEditorPanel1.openNewTemplate();
        lblTemplateName.setText( templateEditorPanel1.getTemplate().getName() );
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        templateEditorPanel1.save();
        TemplateManager.getInstance().refreshAvailableTemplates();
        lblTemplateName.setText( templateEditorPanel1.getTemplate().getName() );
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        templateEditorPanel1.deleteSelectedItem();
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnAddTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTextActionPerformed
        TextTemplateItem txt = new TextTemplateItem();
        templateEditorPanel1.addItem(txt);
    }//GEN-LAST:event_btnAddTextActionPerformed

    private void btnAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImageActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setControlButtonsAreShown(true);
        fc.setDialogTitle("Select Image");
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION && fc.getSelectedFile().exists() ){
            String filename = fc.getSelectedFile().getAbsolutePath();
            ImageTemplateItem img = new ImageTemplateItem( filename );
            templateEditorPanel1.addItem(img);
        }
    }//GEN-LAST:event_btnAddImageActionPerformed

    private void btnMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveUpActionPerformed
        templateEditorPanel1.moveUpSelectedItem();
    }//GEN-LAST:event_btnMoveUpActionPerformed

    private void btnMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveDownActionPerformed
        templateEditorPanel1.moveDownSelectedItem();
    }//GEN-LAST:event_btnMoveDownActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddImage;
    private javax.swing.JButton btnAddText;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnDeleteTemplate;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnMoveDown;
    private javax.swing.JButton btnMoveUp;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTableProperties;
    private javax.swing.JTable jTableTemplates;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblTemplateName;
    private datasoul.templates.TemplateEditorPanel templateEditorPanel1;
    // End of variables declaration//GEN-END:variables
    
}
