/*
 * SongViewerPanel.java
 *
 * Created on 14 de Dezembro de 2005, 22:09
 */

package datasoul.song;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.text.Style;

/**
 *
 * @author  Administrador
 */
public class SongViewerPanel extends javax.swing.JPanel {

    private SongsPanel objectManager;
    private ArrayList<String> chordsName;
    
    private Style nameStyle;
    private Style authorStyle;
    private Style lyricsStyle;
    private Style chordsStyle;
    private Style chordShapeStyle;
    
    private SongTemplate songTemplate;
    private Song song;
    
    private String keyOrig="";
    private String keyActual="";
    
    private Vector<String> notes = new Vector<String>();

    private static ArrayList<String> specialWords = new ArrayList<String>();    

    /**
     * Creates new form SongViewerPanel
     */
    public SongViewerPanel() {
        initComponents();
        
        this.songViewer1.setView(SongViewer.VIEW_LYRICS);
        this.songViewer2.setView(SongViewer.VIEW_CHORDS_COMPLETE);
        this.songViewer3.setView(SongViewer.VIEW_CHORDS_SIMPLIFIED);
    }

    public void viewSong(Song song){
        keyOrig="";
        keyActual="";
        
        this.song = song;
        lblSongName.setText("   "+song.getTitle());

        this.songViewer1.viewSong(song);
        this.songViewer2.viewSong(song);
        this.songViewer3.viewSong(song);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        songViewer4 = new datasoul.song.SongViewer();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblSongName = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        songViewer1 = new datasoul.song.SongViewer();
        songViewer2 = new datasoul.song.SongViewer();
        songViewer3 = new datasoul.song.SongViewer();

        jToolBar2.setFloatable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/playsound.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel1.setText(bundle.getString("Song:")); // NOI18N
        jToolBar2.add(jLabel1);

        lblSongName.setText("   ");
        jToolBar2.add(lblSongName);

        jTabbedPane1.addTab(bundle.getString("Lyrics"), songViewer1); // NOI18N
        jTabbedPane1.addTab(bundle.getString("Chords_Complete"), songViewer2); // NOI18N
        jTabbedPane1.addTab(bundle.getString("Chords_Simplified"), songViewer3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblSongName;
    private datasoul.song.SongViewer songViewer1;
    private datasoul.song.SongViewer songViewer2;
    private datasoul.song.SongViewer songViewer3;
    private datasoul.song.SongViewer songViewer4;
    // End of variables declaration//GEN-END:variables

}
