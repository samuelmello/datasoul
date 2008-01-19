/*
 * ImportServiceItemForm.java
 *
 * Created on 7 de Abril de 2006, 23:32
 */

package datasoul.datashow;

import datasoul.util.ShowDialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
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

        setColorRender();
        center();
    }
    
    private void setColorRender(){
        ServiceListColorRender cr = new ServiceListColorRender();
        
        this.tableServiceList.getColumnModel().getColumn(0).setCellRenderer(cr);
        this.tableServiceList.getColumnModel().getColumn(1).setCellRenderer(cr);        
    }
    
    public void center(){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width)/2, (screen.height - frame.height)/2);
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
        btnImport = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        setTitle(bundle.getString("Import_Service_Item")); // NOI18N
        setAlwaysOnTop(true);

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
        tableServiceList.setCellSelectionEnabled(true);
        scroolServiceList.setViewportView(tableServiceList);

        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/additem.png"))); // NOI18N
        btnImport.setText(bundle.getString("Import")); // NOI18N
        btnImport.setToolTipText(bundle.getString("Import_selected_item_to_actual_service_list")); // NOI18N
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/fileopen.png"))); // NOI18N
        btnOpen.setText(bundle.getString("Open")); // NOI18N
        btnOpen.setToolTipText(bundle.getString("Open_service_list_...")); // NOI18N
        btnOpen.setMaximumSize(new java.awt.Dimension(89, 29));
        btnOpen.setMinimumSize(new java.awt.Dimension(89, 29));
        btnOpen.setPreferredSize(new java.awt.Dimension(89, 29));
        btnOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpenMouseClicked(evt);
            }
        });

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/fileclose.png"))); // NOI18N
        btnClose.setText(bundle.getString("Close")); // NOI18N
        btnClose.setToolTipText(bundle.getString("Close_window")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("Select_another_service_list_to_import_an_item")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroolServiceList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose))
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroolServiceList, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        for(int item:tableServiceList.getSelectedRows())
            ServiceListTable.getActiveInstance().addItem(tableServiceList.getModel().getValueAt(item,0));        
    }//GEN-LAST:event_btnImportActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpenMouseClicked
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
        File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "servicelists");
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
    }//GEN-LAST:event_btnOpenMouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnOpen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scroolServiceList;
    private datasoul.util.DnDTable tableServiceList;
    // End of variables declaration//GEN-END:variables

}
