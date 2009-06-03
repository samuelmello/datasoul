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
 * ImportServiceItemForm.java
 *
 * Created on 7 de Abril de 2006, 23:32
 */

package datasoul.datashow;

import datasoul.DatasoulMainForm;
import datasoul.util.ShowDialog;
import java.io.File;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author  Administrador
 */
public class ImportServiceItemForm extends javax.swing.JFrame {
    
    /** Creates new form ImportServiceItemForm */
    public ImportServiceItemForm() {
        initComponents();
        DatasoulMainForm.setDatasoulIcon(this);
        setColorRender();
    }
    
    private void setColorRender(){
        ServiceListColorRender cr = new ServiceListColorRender();
        
        this.tableServiceList.getColumnModel().getColumn(0).setCellRenderer(cr);
        this.tableServiceList.getColumnModel().getColumn(0).setPreferredWidth(150);
        this.tableServiceList.getColumnModel().getColumn(1).setCellRenderer(cr);        
        this.tableServiceList.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.tableServiceList.getColumnModel().getColumn(2).setCellRenderer(cr);        
        this.tableServiceList.getColumnModel().getColumn(2).setPreferredWidth(40);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroolServiceList = new javax.swing.JScrollPane();
        tableServiceList = new datasoul.util.DnDTable();
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnOpen = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        setTitle(bundle.getString("Import_Service_Item")); // NOI18N

        tableServiceList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scroolServiceList.setViewportView(tableServiceList);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/window-close.png"))); // NOI18N
        btnClose.setText(bundle.getString("Close")); // NOI18N
        btnClose.setToolTipText(bundle.getString("Close_window")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("Select_another_service_list_to_import_an_item")); // NOI18N

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-open.png"))); // NOI18N
        btnOpen.setText("Open");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_task-assigned.png"))); // NOI18N
        btnImport.setText("Import");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(btnOpen)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnImport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 327, Short.MAX_VALUE)
                        .add(btnClose))
                    .add(layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(jLabel1))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(scroolServiceList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scroolServiceList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnClose)
                    .add(btnOpen)
                    .add(btnImport))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName();
                if (name.endsWith(".servicelist")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return ".servicelist";
            }
        });
        File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
        fc.setCurrentDirectory(dir);
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            String fileName = fc.getSelectedFile().getPath();

            File file = new File(fileName);

            Document dom=null;
            Node node = null;
            ServiceListTable slt;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(file);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("ServiceListTable").item(0);

            }catch(Exception e) {
                ShowDialog.showReadFileError(file, e);
            }

            slt = new ServiceListTable();
            try {
                slt.readObject(node);
            } catch (Exception e) {
                ShowDialog.showReadFileError(file, e);
            }

            tableServiceList.setModel(slt);
            setColorRender();
        }
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        for(int item:tableServiceList.getSelectedRows())
            ServiceListTable.getActiveInstance().addItem(tableServiceList.getModel().getValueAt(item,0));        
    }//GEN-LAST:event_btnImportActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnOpen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scroolServiceList;
    private datasoul.util.DnDTable tableServiceList;
    // End of variables declaration//GEN-END:variables

}
