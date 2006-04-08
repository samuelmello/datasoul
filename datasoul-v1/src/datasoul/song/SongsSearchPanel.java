/*
 * SongsSearchPanel.java
 *
 * Created on 22 de Dezembro de 2005, 21:32
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author  Administrador
 */
public class SongsSearchPanel extends javax.swing.JPanel implements javax.swing.event.TableModelListener{

    private Object objectManager;    
    private AllSongsListTable allSongsListTable;
    private JFrame frameParent;
    /**
     * Creates new form SongsSearchPanel
     */
    public SongsSearchPanel() {
        initComponents();

        tableSongList.setDroppable(false);
        
        comboField.removeAllItems();
        comboField.addItem("All");
        comboField.addItem("FileName");
        comboField.addItem("SongName");
        comboField.addItem("SongAuthor");
        comboField.addItem("Lyrics");
        comboField.setSelectedIndex(0);
        
        allSongsListTable = AllSongsListTable.getInstance();

        tableSongList.setModel(allSongsListTable);

        this.btnClose.setVisible(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        dnDTable1 = new datasoul.util.DnDTable();
        fieldString = new javax.swing.JTextField();
        comboField = new javax.swing.JComboBox();
        labelString = new javax.swing.JLabel();
        labelField = new javax.swing.JLabel();
        toolBar = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAddToList = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        scroolSongList = new javax.swing.JScrollPane();
        tableSongList = new datasoul.util.DnDTable();

        dnDTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(dnDTable1);

        fieldString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldStringKeyPressed(evt);
            }
        });

        comboField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelString.setText("Search");

        labelField.setText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FIELD"));

        toolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/new.gif")));
        btnNew.setAlignmentY(0.0F);
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });

        toolBar.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/edit.gif")));
        btnEdit.setAlignmentY(0.0F);
        btnEdit.setName("btnEdit");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });

        toolBar.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/delete.gif")));
        btnDelete.setAlignmentY(0.0F);
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeletebtnRemoveMouseClicked(evt);
            }
        });

        toolBar.add(btnDelete);

        btnAddToList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/addToList.gif")));
        btnAddToList.setAlignmentY(0.0F);
        btnAddToList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToListActionPerformed(evt);
            }
        });

        toolBar.add(btnAddToList);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/delete.gif")));
        btnClose.setText("Close");
        btnClose.setAlignmentY(0.0F);
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });

        toolBar.add(btnClose);

        tableSongList.setModel(new javax.swing.table.DefaultTableModel(
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
        tableSongList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableSongListKeyPressed(evt);
            }
        });
        tableSongList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSongListMouseClicked(evt);
            }
        });

        scroolSongList.setViewportView(tableSongList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labelString)
                    .add(labelField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(fieldString, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .add(comboField, 0, 277, Short.MAX_VALUE))
                .addContainerGap())
            .add(toolBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(labelString)
                    .add(fieldString, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(comboField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(labelField))
                .add(10, 10, 10)
                .add(scroolSongList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(toolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        if(frameParent!=null)
            frameParent.dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        SongEditorForm songEditor = new SongEditorForm();
        
        songEditor.setVisible(true);
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        SongEditorForm songEditor = new SongEditorForm((Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
        songEditor.setVisible(true);
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnDeletebtnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeletebtnRemoveMouseClicked
        Song song = (Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0);
        String filePath = song.getFilePath();
        
        File file = new File(filePath);
        if(JOptionPane.showConfirmDialog(this,"Are you shure that you want to delete the file "+filePath+" ?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            if(file.delete()){
                tableSongList.removeItem();
            }
        }
        this.repaint();
    }//GEN-LAST:event_btnDeletebtnRemoveMouseClicked

    private void btnAddToListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToListActionPerformed
        for(int item:tableSongList.getSelectedRows())
            ServiceListTable.getInstance().addItem(tableSongList.getModel().getValueAt(item,0));        
    }//GEN-LAST:event_btnAddToListActionPerformed

    public void usingInAddSongItemPanel(JFrame frameParent){
        this.btnClose.setVisible(true);
        this.frameParent = frameParent;
        this.btnDelete.setVisible(false);
        this.btnEdit.setVisible(false);
        this.btnNew.setVisible(false);
        this.btnAddToList.setText("Add");
    }
    public void addItem(java.awt.event.ActionEvent evt) {                                          
        btnAddToListActionPerformed(evt);
    }
    
    private void tableSongListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSongListMouseClicked
        showItem();
    }//GEN-LAST:event_tableSongListMouseClicked

    private void tableSongListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableSongListKeyPressed
        showItem();
    }//GEN-LAST:event_tableSongListKeyPressed

    private void showItem(){
        if(objectManager instanceof DatashowPanel){
            DatashowPanel om = (DatashowPanel)objectManager;
            if (om != null){
                om.getPreviewPanel().previewItem((ServiceItem)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
            }
        }else if(objectManager instanceof SongsPanel){
            SongsPanel om = (SongsPanel)objectManager;     
            if (om != null){
                om.getSongViewerPanel().viewSong((Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
            }
        }else if(objectManager instanceof AddSongForm){
            AddSongForm om = (AddSongForm)objectManager;     
            if (om != null){
                om.viewSong((Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
            }
        }
    }    
    
    private void fieldStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldStringKeyPressed
        
        SongListTable foundSongTable = new SongListTable();
        
        for(int i=0; i<allSongsListTable.getRowCount();i++){
            try {
                if(fieldString.getText().length()==0){
                    foundSongTable.addItem(allSongsListTable.getValueAt(i,0));
                }else{
                    String searchStr;
                    if(String.valueOf(evt.getKeyChar()).equals("\b")){
                        searchStr = fieldString.getText().substring(0,fieldString.getText().length()-1);
                    }else{
                        searchStr = fieldString.getText()+evt.getKeyChar();
                    }
                    if(comboField.getSelectedItem().toString().equals("All")){
                        for(int j=1; j<comboField.getItemCount();j++){
                            if(((Song)allSongsListTable.getValueAt(i,0)).containsStringInField(comboField.getItemAt(j).toString(),searchStr)){
                                foundSongTable.addItem(allSongsListTable.getValueAt(i,0));
                                break;
                            }                        
                        }
                    }else if(((Song)allSongsListTable.getValueAt(i,0)).containsStringInField(comboField.getSelectedItem().toString(),searchStr)){
                        foundSongTable.addItem(allSongsListTable.getValueAt(i,0));
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Error in searching.\nErro:"+ex.getMessage(),"DataSoul Error",0);    
            }
        }
        foundSongTable.addTableModelListener(this);        

        tableSongList.setModel(foundSongTable);

    }//GEN-LAST:event_fieldStringKeyPressed

    public void tableChanged(TableModelEvent e) {
        this.repaint();
    }

    public Object getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(Object objectManager) {
        this.objectManager = objectManager;
    }
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddToList;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox comboField;
    private datasoul.util.DnDTable dnDTable1;
    private javax.swing.JTextField fieldString;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelField;
    private javax.swing.JLabel labelString;
    private javax.swing.JScrollPane scroolSongList;
    private datasoul.util.DnDTable tableSongList;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    
}
