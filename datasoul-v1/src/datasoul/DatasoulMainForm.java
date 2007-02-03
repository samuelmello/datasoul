/*
 * DatasoulMainForm.java
 *
 * Created on 13 de Dezembro de 2005, 21:59
 */

package datasoul;

import datasoul.config.BackgroundConfig;
import datasoul.config.ConfigObj;
import datasoul.config.ConfigPanel;
import datasoul.config.DisplayControlConfig;
import datasoul.datashow.DatashowPanel;
import datasoul.help.HelpPanel;
import datasoul.render.ContentManager;
import datasoul.song.AllSongsListTable;
import datasoul.song.ChordsDB;
import datasoul.song.SongsPanel;
import datasoul.templates.TemplatePanel;
import datasoul.datashow.TimerManager;
import datasoul.util.KeyListner;
import datasoul.util.ObjectManager;
import datasoul.util.Splash;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.UIManager;

/**
 *
 * @author  Administrador
 */
public class DatasoulMainForm extends javax.swing.JFrame {
    
    public SongsPanel songs = new SongsPanel();
    public DatashowPanel datashow = new DatashowPanel();
    public TemplatePanel templates = new TemplatePanel();
    public ConfigPanel config = new ConfigPanel();
    public HelpPanel help = new HelpPanel();
    
    
    public static final String DATASOUL_VERSION = "Alpha 61124";
    
    org.jdesktop.layout.GroupLayout songsLayout;
    org.jdesktop.layout.GroupLayout datashowLayout;
    org.jdesktop.layout.GroupLayout templatesLayout;
    org.jdesktop.layout.GroupLayout configLayout;
    org.jdesktop.layout.GroupLayout helpLayout;

    
    /**
     * Creates new form DatasoulMainForm
     */
    public DatasoulMainForm() {
        
        initComponents();
        
        this.setTitle("Datasoul - "+DATASOUL_VERSION);
        
        ObjectManager.getInstance().setDatasoulMainForm(this);
        
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/datasoul.gif")).getImage());
        
        // Initialize datashow Layout
        datashowLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        datashowLayout.setHorizontalGroup(
            datashowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(datashow, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .add(toolBarMain)
        );
        datashowLayout.setVerticalGroup(
            datashowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, datashowLayout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(datashow, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );

        // Initialize songs layout
        songsLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        songsLayout.setHorizontalGroup(
            songsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(songs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .add(toolBarMain)
        );
        songsLayout.setVerticalGroup(
            songsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, songsLayout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(songs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );
        
        // Initialize templates layout
        templatesLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        templatesLayout.setHorizontalGroup(
            templatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(templates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .add(toolBarMain)
        );
        templatesLayout.setVerticalGroup(
            templatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, templatesLayout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(templates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );
        
        // Initialize config layout
        configLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        configLayout.setHorizontalGroup(
            configLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(config, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .add(toolBarMain)
        );
        configLayout.setVerticalGroup(
            configLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, configLayout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(config, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );
        
        // Initialize help layout
        helpLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        helpLayout.setHorizontalGroup(
            helpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(help, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .add(toolBarMain)
        );
        helpLayout.setVerticalGroup(
            helpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, helpLayout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(help, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );

        showPanel(ObjectManager.VIEW_PROJECTOR);
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_PROJECTOR);        
    }
    
    
    public void showPanel(int panel){
        
        songs.setVisible(false);
        datashow.setVisible(false);
        templates.setVisible(false);
        config.setVisible(false);
        help.setVisible(false);
        btnDatashow.setEnabled(true);
        btnSongs.setEnabled(true);
        btnTemplates.setEnabled(true);
        btnConfig.setEnabled(true);
        btnHelp.setEnabled(true);

        switch (panel) {
            case ObjectManager.VIEW_PROJECTOR:
                datashow.setVisible(true);
                getContentPane().setLayout(datashowLayout);
                datashow.validate();
                btnDatashow.setEnabled(false);
                break;
            case ObjectManager.VIEW_SONGS:
                songs.setVisible(true);
                getContentPane().setLayout(songsLayout);
                songs.validate();
                btnSongs.setEnabled(false);
                break;        
            case ObjectManager.VIEW_TEMPLATES:
                templates.setVisible(true);
                getContentPane().setLayout(templatesLayout);
                templates.validate();
                btnTemplates.setEnabled(false);
                break;
            case ObjectManager.VIEW_CONFIG:
                config.setVisible(true);
                getContentPane().setLayout(configLayout);
                config.validate();
                btnConfig.setEnabled(false);
                break;
            case ObjectManager.VIEW_HELP:
                help.setVisible(true);
                getContentPane().setLayout(helpLayout);
                help.validate();
                btnHelp.setEnabled(false);
                break;
                
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        toolBarMain = new javax.swing.JToolBar();
        btnDatashow = new javax.swing.JButton();
        btnTemplates = new javax.swing.JButton();
        btnSongs = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Datasoul");
        toolBarMain.setFloatable(false);
        toolBarMain.setMinimumSize(new java.awt.Dimension(451, 60));
        toolBarMain.setPreferredSize(new java.awt.Dimension(442, 60));
        btnDatashow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/projector1.png")));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        btnDatashow.setText(bundle.getString("Projector")); // NOI18N
        btnDatashow.setToolTipText(bundle.getString("Projector_operation_view")); // NOI18N
        btnDatashow.setBorderPainted(false);
        btnDatashow.setFocusPainted(false);
        btnDatashow.setOpaque(false);
        btnDatashow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDatashowMouseClicked(evt);
            }
        });

        toolBarMain.add(btnDatashow);

        btnTemplates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/looknfeel.png")));
        btnTemplates.setText(bundle.getString("Templates")); // NOI18N
        btnTemplates.setToolTipText(bundle.getString("Template_edit_view")); // NOI18N
        btnTemplates.setBorderPainted(false);
        btnTemplates.setFocusPainted(false);
        btnTemplates.setMaximumSize(new java.awt.Dimension(88, 44));
        btnTemplates.setMinimumSize(new java.awt.Dimension(88, 44));
        btnTemplates.setOpaque(false);
        btnTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplatesActionPerformed(evt);
            }
        });

        toolBarMain.add(btnTemplates);

        btnSongs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/music1.png")));
        btnSongs.setText(bundle.getString("Songs")); // NOI18N
        btnSongs.setToolTipText(bundle.getString("Song_manipulation_view")); // NOI18N
        btnSongs.setBorderPainted(false);
        btnSongs.setFocusPainted(false);
        btnSongs.setOpaque(false);
        btnSongs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSongsMouseClicked(evt);
            }
        });

        toolBarMain.add(btnSongs);

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/package_settings.png")));
        btnConfig.setText(bundle.getString("Configuration")); // NOI18N
        btnConfig.setToolTipText(bundle.getString("Configuration_view")); // NOI18N
        btnConfig.setBorderPainted(false);
        btnConfig.setFocusPainted(false);
        btnConfig.setMaximumSize(new java.awt.Dimension(88, 44));
        btnConfig.setMinimumSize(new java.awt.Dimension(88, 44));
        btnConfig.setOpaque(false);
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        toolBarMain.add(btnConfig);

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/khelpcenter.png")));
        btnHelp.setText(bundle.getString("Help")); // NOI18N
        btnHelp.setBorderPainted(false);
        btnHelp.setFocusPainted(false);
        btnHelp.setOpaque(false);
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });

        toolBarMain.add(btnHelp);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/exit.png")));
        btnClose.setText(bundle.getString("Close")); // NOI18N
        btnClose.setToolTipText(bundle.getString("Close_the_program")); // NOI18N
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setMaximumSize(new java.awt.Dimension(88, 44));
        btnClose.setMinimumSize(new java.awt.Dimension(88, 44));
        btnClose.setOpaque(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        toolBarMain.add(btnClose);

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        toolBarMain.add(jSeparator1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(toolBarMain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1044, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(toolBarMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(657, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_HELP);        
        showPanel(ObjectManager.VIEW_HELP);

    }//GEN-LAST:event_btnHelpActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed

        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_CONFIG);        
        showPanel(ObjectManager.VIEW_CONFIG);

    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplatesActionPerformed

        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_TEMPLATES);        
        showPanel(ObjectManager.VIEW_TEMPLATES);
        
    }//GEN-LAST:event_btnTemplatesActionPerformed

    private void btnSongsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSongsMouseClicked
    
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_SONGS);        
        showPanel(ObjectManager.VIEW_SONGS);
  
    }//GEN-LAST:event_btnSongsMouseClicked

    private void btnDatashowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDatashowMouseClicked
        
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_PROJECTOR);        
        showPanel(ObjectManager.VIEW_PROJECTOR);
        
    }//GEN-LAST:event_btnDatashowMouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        //start splashscreen
        final Splash splash = new Splash();
        splash.setVisible(true);

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_configuration..."));
        ConfigObj.getInstance();
        BackgroundConfig.getInstance();
        DisplayControlConfig.getInstance();

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_songs..."));
        AllSongsListTable.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_chords_database..."));
        ChordsDB.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Starting_content_manager..."));
        ContentManager.getInstance();
        TimerManager.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Creating_user_interface..."));
        final DatasoulMainForm mainForm = new DatasoulMainForm();
        
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Starting_application..."));


        Toolkit.getDefaultToolkit().addAWTEventListener( new KeyListner(), AWTEvent.KEY_EVENT_MASK);        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainForm.setVisible(true);
                splash.setVisible(false);
                splash.dispose();
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnDatashow;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnSongs;
    private javax.swing.JButton btnTemplates;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar toolBarMain;
    // End of variables declaration//GEN-END:variables
    
}
