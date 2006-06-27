/*
 * SongsListPanel.java
 *
 * Created on 22 de Dezembro de 2005, 21:32
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.io.File;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
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
public class SongsListPanel extends javax.swing.JPanel implements javax.swing.event.TableModelListener{

    /**
     * Creates new form SongsListPanel
     */
    public SongsListPanel() {
        initComponents();
  
        tableSongList.setDroppable(false);
        
        loadMusics();
    }
    
    public void loadMusics(){

        SongListTable songListTable = AllSongsListTable.getInstance();
        songListTable.addTableModelListener(this);
        
        tableSongList.setModel(songListTable);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        toolBar = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAddToList = new javax.swing.JButton();
        scroolSongList = new javax.swing.JScrollPane();
        tableSongList = new datasoul.util.DnDTable();

        toolBar.setBorder(null);
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/new.gif")));
        btnNew.setToolTipText("Create a new song ...");
        btnNew.setAlignmentY(0.0F);
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });

        toolBar.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/edit.gif")));
        btnEdit.setToolTipText("Edit song ...");
        btnEdit.setAlignmentY(0.0F);
        btnEdit.setName("btnEdit");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });

        toolBar.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/delete.gif")));
        btnDelete.setToolTipText("Delete song");
        btnDelete.setAlignmentY(0.0F);
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });

        toolBar.add(btnDelete);

        btnAddToList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/addToList.gif")));
        btnAddToList.setToolTipText("Add song to Service List");
        btnAddToList.setAlignmentY(0.0F);
        btnAddToList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddToListMouseClicked(evt);
            }
        });

        toolBar.add(btnAddToList);

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
            .add(scroolSongList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
            .add(toolBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(scroolSongList, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(toolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddToListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddToListMouseClicked
        for(int item:tableSongList.getSelectedRows())
            ServiceListTable.getActiveInstance().addItem(tableSongList.getModel().getValueAt(item,0));        
    }//GEN-LAST:event_btnAddToListMouseClicked

    private void tableSongListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSongListMouseClicked
        showItem();
    }//GEN-LAST:event_tableSongListMouseClicked

    private void tableSongListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableSongListKeyPressed
        showItem();
    }//GEN-LAST:event_tableSongListKeyPressed

    private void showItem(){
        if(ObjectManager.getInstance().getViewActive()==ObjectManager.VIEW_PROJECTOR){
            if(ObjectManager.getInstance().getPreviewPanel()!=null)
                ObjectManager.getInstance().getPreviewPanel().previewItem((ServiceItem)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
        }
        if(ObjectManager.getInstance().getViewActive()==ObjectManager.VIEW_SONGS){
            if(ObjectManager.getInstance().getSongViewerPanel()!=null)
                ObjectManager.getInstance().getSongViewerPanel().viewSong((Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));
        }
    }
    
    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        Song song = (Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0);
        String filePath = song.getFilePath();
        
        File file = new File(filePath);
        if(JOptionPane.showConfirmDialog(this,"Are you shure that you want to delete the file "+filePath+" ?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            if(file.delete()){
                tableSongList.removeItem();                
            }
        }
        this.repaint();
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked

        SongEditorForm songEditor = new SongEditorForm((Song)tableSongList.getModel().getValueAt(tableSongList.getSelectedRow(),0));        
        songEditor.setVisible(true);
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        SongEditorForm songEditor = new SongEditorForm();

        songEditor.setVisible(true);
    }//GEN-LAST:event_btnNewMouseClicked

    public void tableChanged(TableModelEvent e) {
        this.repaint();
    }

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddToList;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JScrollPane scroolSongList;
    private datasoul.util.DnDTable tableSongList;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    
}
