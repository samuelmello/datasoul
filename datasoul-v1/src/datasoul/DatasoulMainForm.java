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
 * DatasoulMainForm.java
 *
 * Created on 13 de Dezembro de 2005, 21:59
 */

package datasoul;

import datasoul.config.BackgroundConfig;
import datasoul.config.ConfigFrame;
import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.config.WindowPropConfig;
import datasoul.datashow.DatashowPanel;
import datasoul.datashow.ServiceListTable;
import datasoul.render.ContentManager;
import datasoul.song.AllSongsListTable;
import datasoul.song.ChordsDB;
import datasoul.song.SongsPanel;
import datasoul.datashow.TimerManager;
import datasoul.help.HelpFrame;
import datasoul.servicelist.ExtServiceListPanel;
import datasoul.servicelist.ServiceListExporterPanel;
import datasoul.templates.TemplateManager;
import datasoul.templates.TemplateManagerForm;
import datasoul.util.KeyListner;
import datasoul.util.ObjectManager;
import datasoul.util.Splash;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;

/**
 *
 * @author  Administrador
 */
public class DatasoulMainForm extends javax.swing.JFrame {

    public SongsPanel songs = new SongsPanel();
    public DatashowPanel datashow = new DatashowPanel();
    public ExtServiceListPanel service = new ExtServiceListPanel();

    private boolean updateSize = false;
    
    GroupLayout songsLayout;
    GroupLayout datashowLayout;
    GroupLayout templatesLayout;
    GroupLayout configLayout;
    GroupLayout helpLayout;
    GroupLayout serviceLayout;

    public static final int FILE_FORMAT_VERSION = 1;
      
    /**
     * Creates new form DatasoulMainForm
     */
    public DatasoulMainForm() {
        
        initComponents();
        
        this.setTitle("Datasoul - "+getVersion());
        DatasoulMainForm.setDatasoulIcon(this);
        
        ObjectManager.getInstance().setDatasoulMainForm(this);
        
        
        // Initialize datashow Layout
        datashowLayout = new GroupLayout(getContentPane());
        datashowLayout.setHorizontalGroup(
            datashowLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(datashow, GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .addComponent(toolBarMain)
        );
        datashowLayout.setVerticalGroup(
            datashowLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.LEADING, datashowLayout.createSequentialGroup()
                .addComponent(toolBarMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datashow, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );

        // Initialize songs layout
        songsLayout = new GroupLayout(getContentPane());
        songsLayout.setHorizontalGroup(
            songsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(songs, GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .addComponent(toolBarMain)
        );
        songsLayout.setVerticalGroup(
            songsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.LEADING, songsLayout.createSequentialGroup()
                .addComponent(toolBarMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(songs, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );
        
        // Initialize service layout
        serviceLayout = new GroupLayout(getContentPane());
        serviceLayout.setHorizontalGroup(
            serviceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(service, GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
            .addComponent(toolBarMain)
        );
        serviceLayout.setVerticalGroup(
            serviceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.LEADING, serviceLayout.createSequentialGroup()
                .addComponent(toolBarMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(service, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
        );
        
        
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        
        tabbedInterface.addTab(
                bundle.getString("Service_Plan"),
                new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_task.png")),
                service,
                bundle.getString("Service_Plan_Tip"));

        tabbedInterface.addTab(
                bundle.getString("Song_Library"),
                new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_effects-sound_small.png")),
                songs,
                bundle.getString("Song_Library_Tip"));

        tabbedInterface.addTab(
                bundle.getString("Projector"),
                new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_effects-preview_small.png")),
                datashow,
                bundle.getString("Projector_Tip"));

        
        ObjectManager.getInstance().setViewActive(ObjectManager.VIEW_PROJECTOR);        
        
        WindowPropConfig.getInstance().getMainForm(this);
        updateSize = true;
    }

    public static void setDatasoulIcon(JFrame frame){
        frame.setIconImage(new javax.swing.ImageIcon(DatasoulMainForm.class.getResource("/datasoul/icons/datasoul.png")).getImage());
    }

    public void showPanel(int panel){
        if ((panel >= 0) && (panel < ObjectManager.VIEW_TAB_COUNT)) {
            tabbedInterface.setSelectedIndex(panel);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBarMain = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSaveAs = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnTemplates = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnClose = new javax.swing.JButton();
        tabbedInterface = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Datasoul");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        toolBarMain.setFloatable(false);
        toolBarMain.setMinimumSize(new java.awt.Dimension(451, 36));
        toolBarMain.setOpaque(false);
        toolBarMain.setPreferredSize(new java.awt.Dimension(442, 36));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-new.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.setFocusable(false);
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        toolBarMain.add(btnNew);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-open.png"))); // NOI18N
        btnOpen.setText("Open");
        btnOpen.setFocusable(false);
        btnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        toolBarMain.add(btnOpen);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        toolBarMain.add(btnSave);

        btnSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-save-as.png"))); // NOI18N
        btnSaveAs.setText("Save As");
        btnSaveAs.setFocusable(false);
        btnSaveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAsActionPerformed(evt);
            }
        });
        toolBarMain.add(btnSaveAs);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/x-office-document.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.setFocusable(false);
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        toolBarMain.add(btnExport);
        toolBarMain.add(jSeparator2);

        btnTemplates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/applications-graphics_small.png"))); // NOI18N
        btnTemplates.setText("Templates");
        btnTemplates.setFocusable(false);
        btnTemplates.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTemplates.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplatesActionPerformed(evt);
            }
        });
        toolBarMain.add(btnTemplates);

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/preferences-system-small.png"))); // NOI18N
        btnConfig.setText("Configuration");
        btnConfig.setFocusable(false);
        btnConfig.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfig.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });
        toolBarMain.add(btnConfig);

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/help-browser-small.png"))); // NOI18N
        btnHelp.setText("Help");
        btnHelp.setFocusable(false);
        btnHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });
        toolBarMain.add(btnHelp);
        toolBarMain.add(jSeparator1);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/application-exit-small.png"))); // NOI18N
        btnClose.setText("Exit");
        btnClose.setFocusable(false);
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        toolBarMain.add(btnClose);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBarMain, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addComponent(tabbedInterface, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBarMain, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedInterface, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (updateSize){
            WindowPropConfig.getInstance().setMainForm(this);
        }
    }//GEN-LAST:event_formComponentResized

    private void btnTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplatesActionPerformed
        TemplateManagerForm tmf = new TemplateManagerForm();
        tmf.setLocationRelativeTo(this);
        tmf.setVisible(true);
    }//GEN-LAST:event_btnTemplatesActionPerformed

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        HelpFrame hf = new HelpFrame();
        hf.setLocationRelativeTo(this);
        hf.setVisible(true);
    }//GEN-LAST:event_btnHelpActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        ConfigFrame cf = new ConfigFrame();
        cf.setLocationRelativeTo(this);
        cf.setVisible(true);
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        int resp = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Confirm_Close")+ "?", "Datasoul", JOptionPane.YES_NO_OPTION );

        if (resp == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        ServiceListTable.getActiveInstance().fileNew();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        ServiceListTable.getActiveInstance().openServiceList();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        ServiceListTable.getActiveInstance().saveServiceList();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAsActionPerformed
        ServiceListTable.getActiveInstance().saveServiceListAs();
    }//GEN-LAST:event_btnSaveAsActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        ServiceListExporterPanel p = new ServiceListExporterPanel();
        p.setLocationRelativeTo(this);
        p.setVisible(true);
    }//GEN-LAST:event_btnExportActionPerformed
    
    public static void checkStorageLocation(){
        String stgloc = ConfigObj.getInstance().getStorageLoc();
        if (stgloc == null || stgloc.trim().equals("")){
            stgloc = System.getProperty("user.home")+System.getProperty("file.separator")+".datasoul"+System.getProperty("file.separator")+"data";
            ConfigObj.getInstance().setStorageLoc(stgloc);
            ConfigObj.getInstance().save();
        }
        System.setProperty("datasoul.stgloc", stgloc);
        
        File stglocdir  = new File(stgloc);
        if (!stglocdir.exists()){
            stglocdir.mkdirs();
        }
        
        File templates  = new File(stgloc+System.getProperty("file.separator")+"templates");
        if (!templates.exists()){
            templates.mkdirs();
            copySampleTemplates(templates.getAbsolutePath());
        }
        
        File songs  = new File(stgloc+System.getProperty("file.separator")+"songs");
        if (!songs.exists()){
            songs.mkdirs();
            copySampleSongs(songs.getAbsolutePath());
        }
    
        File serviceslist  = new File(stgloc+System.getProperty("file.separator")+"servicelists");
        if (!serviceslist.exists()){
            serviceslist.mkdirs();
            copySampleServices(serviceslist.getAbsolutePath());
        }

    }
    
    protected static void copyFile(String resource, String targetName) throws IOException{
        InputStream is = DatasoulMainForm.class.getResourceAsStream(resource);
        FileOutputStream fos = new FileOutputStream(targetName);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();
    }
    
    protected static void copySampleTemplates(String dir){
        String files[] = {"alert-general.template",
            "alert-nursery.template",
            "alert-parking.template",
            "bible.template",
            "monitor-preaching.template",
            "monitor-song.template",
            "default.template",
            "song.template",
            "subtitle.template"
        };
        
        for (String f: files){
            try{
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    protected static void copySampleSongs(String dir){
        String files[] = {"Amazing Grace.song",
            "How Great Thou Art.song",
            "It Is Well With My Soul.song",
            "Joyful Joyful We Adore Thee.song",
            };
        
        for (String f: files){
            try{
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
    

    protected static void copySampleServices(String dir){
        String files[] = {"SampleService.servicelist"};
        
        for (String f: files){
            try{
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // Enable anti-aliasing
        System.setProperty("swing.aatext","true");
        
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            //ignore and fall back to java look and feel
        }

        // Put the AvailableFontFamilyName list
        Thread t = new Thread(){
            @Override
          public void run(){
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
          }
        };
        t.start();
        
        //start splashscreen
        final Splash splash = new Splash();
        splash.setVisible(true);
        
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_configuration..."));
        ConfigObj.getInstance();
        checkStorageLocation();
        
        BackgroundConfig.getInstance();
        TemplateManager.getInstance().refreshAvailableTemplates();
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

        // Join the FontFamily cache thread
        try{
            t.join();
        }catch(Exception e){
            // Do nothing
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainForm.setVisible(true);
                splash.setVisible(false);
                splash.dispose();
            }
        });
        
    }

    public static String getVersion(){
        try {
            Properties prop = new Properties();
            prop.load(DatasoulMainForm.class.getResourceAsStream("version.properties"));
            return prop.getProperty("version");
        } catch (IOException ex) {
            Logger.getLogger(DatasoulMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Unknown Private Build";
    }

    public static String getFileFormatVersion() {
        return Integer.toString(FILE_FORMAT_VERSION);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnClose;
    javax.swing.JButton btnConfig;
    javax.swing.JButton btnExport;
    javax.swing.JButton btnHelp;
    javax.swing.JButton btnNew;
    javax.swing.JButton btnOpen;
    javax.swing.JButton btnSave;
    javax.swing.JButton btnSaveAs;
    javax.swing.JButton btnTemplates;
    javax.swing.JToolBar.Separator jSeparator1;
    javax.swing.JToolBar.Separator jSeparator2;
    javax.swing.JTabbedPane tabbedInterface;
    javax.swing.JToolBar toolBarMain;
    // End of variables declaration//GEN-END:variables
    
}
