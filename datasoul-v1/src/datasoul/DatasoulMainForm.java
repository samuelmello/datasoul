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
import datasoul.render.ContentManager;
import datasoul.song.AllSongsListTable;
import datasoul.song.ChordsDB;
import datasoul.song.SongsPanel;
import datasoul.templates.TemplatePanel;
import datasoul.datashow.TimerManager;
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
    
    org.jdesktop.layout.GroupLayout songsLayout;
    org.jdesktop.layout.GroupLayout datashowLayout;
    org.jdesktop.layout.GroupLayout templatesLayout;
    org.jdesktop.layout.GroupLayout configLayout;

    
    /**
     * Creates new form DatasoulMainForm
     */
    public DatasoulMainForm() {
        
        initComponents();

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
        

        showPanel(ObjectManager.VIEW_PROJECTOR);
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_PROJECTOR);        
    }
    
    
    public void showPanel(int panel){
        
        songs.setVisible(false);
        datashow.setVisible(false);
        templates.setVisible(false);
        config.setVisible(false);
        btnDatashow.setEnabled(true);
        btnSongs.setEnabled(true);
        btnTemplates.setEnabled(true);
        btnConfig.setEnabled(true);

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
        btnSongs = new javax.swing.JButton();
        btnTemplates = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DataSoul - Alpha 61111");
        toolBarMain.setFloatable(false);
        toolBarMain.setMinimumSize(new java.awt.Dimension(451, 60));
        toolBarMain.setPreferredSize(new java.awt.Dimension(442, 60));
        btnDatashow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/projector1.png")));
        btnDatashow.setText("Projector");
        btnDatashow.setToolTipText("Projector operation view");
        btnDatashow.setBorderPainted(false);
        btnDatashow.setFocusPainted(false);
        btnDatashow.setOpaque(false);
        btnDatashow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDatashowMouseClicked(evt);
            }
        });

        toolBarMain.add(btnDatashow);

        btnSongs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/music1.png")));
        btnSongs.setText("Songs");
        btnSongs.setToolTipText("Song manipulation view");
        btnSongs.setBorderPainted(false);
        btnSongs.setFocusPainted(false);
        btnSongs.setOpaque(false);
        btnSongs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSongsMouseClicked(evt);
            }
        });

        toolBarMain.add(btnSongs);

        btnTemplates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/looknfeel.png")));
        btnTemplates.setText("Templates");
        btnTemplates.setToolTipText("template view");
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

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/package_settings.png")));
        btnConfig.setText("Configuration");
        btnConfig.setToolTipText("Configuration view");
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

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/exit.png")));
        btnClose.setText("Close");
        btnClose.setToolTipText("Close the program");
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

        jSeparator1.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
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
        try{
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        }catch(Exception e){
            
        }

        //start splashscreen
        //SplashScreen splashScreen = new SplashScreen("/datasoul/icons/splashScreen.gif");
        //splashScreen.splashShow();
        final Splash splash = new Splash();
        splash.setVisible(true);

        splash.setStatusText("Loading configuration...");
        ConfigObj.getInstance();
        BackgroundConfig.getInstance();
        DisplayControlConfig.getInstance();

        splash.setStatusText("Loading songs...");
        AllSongsListTable.getInstance();
        splash.setStatusText("Loading chords database...");
        ChordsDB.getInstance();
        splash.setStatusText("Starting content manager...");
        ContentManager.getInstance();
        TimerManager.getInstance();
        splash.setStatusText("Creating user interface...");

        //stop splashscreen
        //splashScreen.splashHide();        

        Toolkit.getDefaultToolkit().addAWTEventListener( new KeyListner(), AWTEvent.KEY_EVENT_MASK);        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DatasoulMainForm().setVisible(true);
                splash.setVisible(false);
                splash.dispose();
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnDatashow;
    private javax.swing.JButton btnSongs;
    private javax.swing.JButton btnTemplates;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar toolBarMain;
    // End of variables declaration//GEN-END:variables
    
}
