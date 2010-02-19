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
import datasoul.datashow.ImageListEditorForm;
import datasoul.datashow.ImageListServiceItem;
import datasoul.datashow.ImportServiceItemForm;
import datasoul.datashow.ServiceItem;
import datasoul.datashow.ServiceListColorRender;
import datasoul.datashow.ServiceListTable;
import datasoul.datashow.TextServiceItem;
import datasoul.datashow.TextServiceItemEditorForm;
import datasoul.render.ContentManager;
import datasoul.song.AllSongsListTable;
import datasoul.song.ChordsDB;
import datasoul.datashow.TimerManager;
import datasoul.help.HelpFrame;
import datasoul.servicelist.ContentlessServiceItem;
import datasoul.servicelist.ServiceListExporterPanel;
import datasoul.song.AddSongForm;
import datasoul.song.Song;
import datasoul.song.SongEditorForm;
import datasoul.templates.DisplayTemplate;
import datasoul.templates.TemplateComboBox;
import datasoul.templates.TemplateManagerForm;
import datasoul.util.KeyListner;
import datasoul.util.ObjectManager;
import datasoul.util.Splash;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Administrador
 */
public class DatasoulMainForm extends javax.swing.JFrame {

    private boolean updateSize = false;
    
    public static final int FILE_FORMAT_VERSION = 2;
      
    /**
     * Creates new form DatasoulMainForm
     */
    public DatasoulMainForm() {
        
        initComponents();
        
        this.setTitle("Datasoul - "+getVersion());
        DatasoulMainForm.setDatasoulIcon(this);
        
        ObjectManager.getInstance().setDatasoulMainForm(this);
        ObjectManager.getInstance().setPreviewPanel(preview);
        ObjectManager.getInstance().setAuxiliarPanel(auxiliar);
        ObjectManager.getInstance().setLivePanel(live);


        tableServiceList.setModel( ServiceListTable.getActiveInstance() );

        tableServiceList.setDraggable(false);

        //TemplateComboBox comboBox = new TemplateComboBox();
        //this.tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TEMPLATE).setCellEditor(new DefaultCellEditor(comboBox));
        this.tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TEMPLATE).setCellEditor(new TemplateCellEditor());


        ServiceListColorRender cr = new ServiceListColorRender();
        for (int i=0; i<ServiceListTable.COLUMN_COUNT; i++){
            this.tableServiceList.getColumnModel().getColumn(i).setCellRenderer(cr);
        }


        // adjust columns
        tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TIME).setPreferredWidth(30);
        tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_DURATION).setPreferredWidth(20);
        tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TITLE).setPreferredWidth(200);

        initLive();

        WindowPropConfig.getInstance().getMainForm(this);
        updateSize = true;
    }

    private void initLive(){


        ContentManager.getInstance().registerMainDisplay(liveDisplayPanel);
        Dimension liveSize = new Dimension(ContentManager.PREVIEW_WIDTH, ContentManager.getInstance().getPreviewHeight());
        liveDisplayPanel.setSize(liveSize);
        liveDisplayPanel.setPreferredSize(liveSize);
        liveDisplayPanel.setMaximumSize(liveSize);
        liveDisplayPanel.setMinimumSize(liveSize);

        if (ConfigObj.getInstance().getMonitorOutput()){

            ContentManager.getInstance().registerMonitorDisplay(monitorDisplayPanel);
            Dimension monitorSize = new Dimension(ContentManager.PREVIEW_WIDTH, ContentManager.getInstance().getPreviewMonitorHeight());
            monitorDisplayPanel.setSize(monitorSize);
            monitorDisplayPanel.setPreferredSize(monitorSize);
            monitorDisplayPanel.setMaximumSize(monitorSize);
            monitorDisplayPanel.setMinimumSize(monitorSize);

        }else{
            btnShowMonitor.setSelected(false);
            btnShowMonitor.setVisible(false);
            monitorDisplayPanel.setVisible(false);
        }

    }


    public static void setDatasoulIcon(JFrame frame){
        frame.setIconImage(new javax.swing.ImageIcon(DatasoulMainForm.class.getResource("/datasoul/icons/datasoul.png")).getImage());
    }

    private void previewItem(){

        try{
            ObjectManager.getInstance().setBusyCursor();

            if (tableServiceList.getSelectedRow() != -1){
                ServiceItem item = (ServiceItem) ServiceListTable.getActiveInstance().getServiceItem(tableServiceList.getSelectedRow());
                if (!(item instanceof ContentlessServiceItem)){
                    if(ObjectManager.getInstance().getPreviewPanel()!=null)
                        ObjectManager.getInstance().getPreviewPanel().previewItem(item);
                }
            }
        }finally{
            ObjectManager.getInstance().setDefaultCursor();
        }
    }

    private void goLiveItem(){

        previewItem();

        SwingUtilities.invokeLater(new Runnable(){ 
            @Override
            public void run(){
                preview.goLive();
            }});

    }


    public void viewSong(Song song) {
        lblSongName.setText(song.getTitle());
        lblAuthor.setText(song.getSongAuthor());
        textSong.setText(song.getText().replace(Song.CHORUS_MARK, "").replace(Song.SLIDE_BREAK, ""));
        textSong.setCaretPosition(0);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ppmAddItem = new javax.swing.JPopupMenu();
        actAddSong = new javax.swing.JMenuItem();
        actAddText = new javax.swing.JMenuItem();
        actAddBible = new javax.swing.JMenuItem();
        actAddContentlessItem = new javax.swing.JMenuItem();
        actAddImageList = new javax.swing.JMenuItem();
        actImportItem = new javax.swing.JMenuItem();
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
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        pnlPreview = new javax.swing.JPanel();
        preview = new datasoul.datashow.PreviewPanel();
        pnlServiceList = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableServiceList = new datasoul.util.DnDTable();
        toolBar = new javax.swing.JToolBar();
        btnAddWizard = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnGoLive = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtHours = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMinutes = new javax.swing.JTextField();
        tabbedRightSide = new javax.swing.JTabbedPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textSong = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblSongName = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        songsSearchPanel1 = new datasoul.song.SongsSearchPanel();
        splDisplayControl = new javax.swing.JSplitPane();
        live = new datasoul.datashow.LivePanel();
        jPanel3 = new javax.swing.JPanel();
        auxiliar = new datasoul.datashow.AuxiliarPanel();
        pnlLiveBox = new javax.swing.JPanel();
        liveDisplayPanel = new datasoul.render.SwingDisplayPanel();
        monitorDisplayPanel = new datasoul.render.SwingDisplayPanel();
        btnShowMonitor = new javax.swing.JToggleButton();

        actAddSong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_playlist.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        actAddSong.setText(bundle.getString("Add_Song")); // NOI18N
        actAddSong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddSongActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddSong);

        actAddText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/font-x-generic.png"))); // NOI18N
        actAddText.setText(bundle.getString("Add_Text")); // NOI18N
        actAddText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddTextActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddText);

        actAddBible.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_bookmark.png"))); // NOI18N
        actAddBible.setText(bundle.getString("Add_Bible_Text")); // NOI18N
        actAddBible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddBibleActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddBible);

        actAddContentlessItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_insert-note.png"))); // NOI18N
        actAddContentlessItem.setText(bundle.getString("Add_Contentless_Item")); // NOI18N
        actAddContentlessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddContentlessItemActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddContentlessItem);

        actAddImageList.setText("Add Image List");
        actAddImageList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddImageListActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddImageList);

        actImportItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_task-assigned.png"))); // NOI18N
        actImportItem.setText(bundle.getString("Import_Item")); // NOI18N
        actImportItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actImportItemActionPerformed(evt);
            }
        });
        ppmAddItem.add(actImportItem);

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

        jSplitPane1.setDividerLocation(500);

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        txtNotes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNotesFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(txtNotes);

        jLabel4.setText(bundle.getString("Service_Notes:")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane3.setRightComponent(jPanel2);

        preview.setMinimumSize(new java.awt.Dimension(10, 10));

        javax.swing.GroupLayout pnlPreviewLayout = new javax.swing.GroupLayout(pnlPreview);
        pnlPreview.setLayout(pnlPreviewLayout);
        pnlPreviewLayout.setHorizontalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(preview, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );
        pnlPreviewLayout.setVerticalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(preview, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
        );

        jSplitPane3.setTopComponent(pnlPreview);

        jSplitPane2.setBottomComponent(jSplitPane3);

        jLabel2.setText(bundle.getString("Start_Time:")); // NOI18N

        jLabel1.setText(bundle.getString("Service_Title:")); // NOI18N

        txtTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitleActionPerformed(evt);
            }
        });
        txtTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTitleFocusLost(evt);
            }
        });

        tableServiceList.setModel(new javax.swing.table.DefaultTableModel(
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
        tableServiceList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableServiceListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableServiceList);

        toolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolBar.setFloatable(false);
        toolBar.setMinimumSize(new java.awt.Dimension(30, 25));
        toolBar.setOpaque(false);

        btnAddWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/list-add.png"))); // NOI18N
        btnAddWizard.setText("Add");
        btnAddWizard.setToolTipText("Add Item ...");
        btnAddWizard.setAlignmentY(0.0F);
        btnAddWizard.setBorderPainted(false);
        btnAddWizard.setFocusPainted(false);
        btnAddWizard.setFocusable(false);
        btnAddWizard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddWizardbtnAddActionPerformed(evt);
            }
        });
        toolBar.add(btnAddWizard);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/list-remove.png"))); // NOI18N
        btnRemove.setText(bundle.getString("Delete")); // NOI18N
        btnRemove.setToolTipText(bundle.getString("Delete_item")); // NOI18N
        btnRemove.setAlignmentY(0.0F);
        btnRemove.setBorderPainted(false);
        btnRemove.setFocusPainted(false);
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        toolBar.add(btnRemove);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/gtk-edit.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setToolTipText("Edit selected item ...");
        btnEdit.setAlignmentY(0.0F);
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        toolBar.add(btnEdit);

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/go-up.png"))); // NOI18N
        btnUp.setText(bundle.getString("Move_Up")); // NOI18N
        btnUp.setToolTipText(bundle.getString("Change_item_order_to_upper_position")); // NOI18N
        btnUp.setAlignmentY(0.0F);
        btnUp.setBorderPainted(false);
        btnUp.setFocusPainted(false);
        btnUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpMouseClicked(evt);
            }
        });
        toolBar.add(btnUp);

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/go-down.png"))); // NOI18N
        btnDown.setText(bundle.getString("Move_Down")); // NOI18N
        btnDown.setToolTipText(bundle.getString("Change_item_order_to_lower_position")); // NOI18N
        btnDown.setAlignmentY(0.0F);
        btnDown.setBorderPainted(false);
        btnDown.setFocusPainted(false);
        btnDown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDownMouseClicked(evt);
            }
        });
        toolBar.add(btnDown);

        btnGoLive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/media-playback-start.png"))); // NOI18N
        btnGoLive.setText(bundle.getString("GO_LIVE")); // NOI18N
        btnGoLive.setToolTipText(bundle.getString("Send_slides_to_live")); // NOI18N
        btnGoLive.setBorderPainted(false);
        btnGoLive.setFocusPainted(false);
        btnGoLive.setFocusable(false);
        btnGoLive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoLiveActionPerformed(evt);
            }
        });
        toolBar.add(btnGoLive);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        txtHours.setColumns(2);
        txtHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHours.setText("00");
        txtHours.setMinimumSize(new java.awt.Dimension(30, 27));
        txtHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoursActionPerformed(evt);
            }
        });
        txtHours.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHoursFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHoursFocusLost(evt);
            }
        });
        txtHours.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHoursKeyPressed(evt);
            }
        });
        jPanel1.add(txtHours);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(":");
        jPanel1.add(jLabel3);

        txtMinutes.setColumns(2);
        txtMinutes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMinutes.setText("00");
        txtMinutes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMinutesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinutesFocusLost(evt);
            }
        });
        jPanel1.add(txtMinutes);

        javax.swing.GroupLayout pnlServiceListLayout = new javax.swing.GroupLayout(pnlServiceList);
        pnlServiceList.setLayout(pnlServiceListLayout);
        pnlServiceListLayout.setHorizontalGroup(
            pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
            .addGroup(pnlServiceListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlServiceListLayout.setVerticalGroup(
            pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlServiceListLayout.createSequentialGroup()
                .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane2.setLeftComponent(pnlServiceList);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane4.setDividerLocation(350);
        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSplitPane4PropertyChange(evt);
            }
        });

        jScrollPane3.setViewportView(textSong);

        jLabel6.setText(bundle.getString("Author:")); // NOI18N

        jLabel7.setText(bundle.getString("Song:")); // NOI18N

        lblSongName.setText("     ");

        lblAuthor.setText("     ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSongName)
                    .addComponent(lblAuthor))
                .addContainerGap(507, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblSongName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblAuthor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
        );

        jSplitPane4.setRightComponent(jPanel6);
        jSplitPane4.setLeftComponent(songsSearchPanel1);

        tabbedRightSide.addTab("Song Library", jSplitPane4);

        splDisplayControl.setDividerLocation(250);
        splDisplayControl.setLeftComponent(live);

        liveDisplayPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        liveDisplayPanel.setPreferredSize(new java.awt.Dimension(160, 120));
        liveDisplayPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                liveDisplayPanelpreviewDisplayResized(evt);
            }
        });

        javax.swing.GroupLayout liveDisplayPanelLayout = new javax.swing.GroupLayout(liveDisplayPanel);
        liveDisplayPanel.setLayout(liveDisplayPanelLayout);
        liveDisplayPanelLayout.setHorizontalGroup(
            liveDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );
        liveDisplayPanelLayout.setVerticalGroup(
            liveDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );

        monitorDisplayPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        monitorDisplayPanel.setPreferredSize(new java.awt.Dimension(160, 120));
        monitorDisplayPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                monitorDisplayPanelpreviewDisplayResized(evt);
            }
        });

        javax.swing.GroupLayout monitorDisplayPanelLayout = new javax.swing.GroupLayout(monitorDisplayPanel);
        monitorDisplayPanel.setLayout(monitorDisplayPanelLayout);
        monitorDisplayPanelLayout.setHorizontalGroup(
            monitorDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );
        monitorDisplayPanelLayout.setVerticalGroup(
            monitorDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );

        btnShowMonitor.setSelected(true);
        btnShowMonitor.setText(bundle.getString("Show_Monitor")); // NOI18N
        btnShowMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMonitorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLiveBoxLayout = new javax.swing.GroupLayout(pnlLiveBox);
        pnlLiveBox.setLayout(pnlLiveBoxLayout);
        pnlLiveBoxLayout.setHorizontalGroup(
            pnlLiveBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLiveBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLiveBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(monitorDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(liveDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowMonitor))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        pnlLiveBoxLayout.setVerticalGroup(
            pnlLiveBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLiveBoxLayout.createSequentialGroup()
                .addComponent(btnShowMonitor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(liveDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monitorDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(pnlLiveBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addComponent(auxiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(auxiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLiveBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splDisplayControl.setRightComponent(jPanel3);

        tabbedRightSide.addTab("Display Controls", splDisplayControl);

        jSplitPane1.setRightComponent(tabbedRightSide);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBarMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBarMain, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE))
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

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTitleActionPerformed

    private void txtTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTitleFocusLost
        ServiceListTable.getActiveInstance().setTitle(txtTitle.getText());
}//GEN-LAST:event_txtTitleFocusLost

    private void tableServiceListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableServiceListMouseClicked
        if (evt.getClickCount() > 1){
            btnEditActionPerformed(null);
        }else{
            previewItem();
        }
}//GEN-LAST:event_tableServiceListMouseClicked

    private void btnAddWizardbtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddWizardbtnAddActionPerformed
        ppmAddItem.show(this.btnAddWizard, 0, btnAddWizard.getHeight());
}//GEN-LAST:event_btnAddWizardbtnAddActionPerformed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        tableServiceList.removeItem();
}//GEN-LAST:event_btnRemoveMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tableServiceList.getSelectedRow() == -1){
            return;
        }

        ServiceItem item = (ServiceItem)ServiceListTable.getActiveInstance().getServiceItem( tableServiceList.getSelectedRow() );
        if(item instanceof Song){
            SongEditorForm sef = new SongEditorForm((Song)item);
            sef.setVisible(true);
        }else if(item instanceof TextServiceItem){
            TextServiceItemEditorForm tsief = new TextServiceItemEditorForm((TextServiceItem)item);
            tsief.setLocationRelativeTo(this);
            tsief.setVisible(true);
        }else if(item instanceof ContentlessServiceItem){
            String s = JOptionPane.showInputDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Service_Item_Name:"), item.getTitle());
            if (s != null && !s.trim().equals("")){
                item.setTitle(s);
            }
            tableServiceList.repaint();
        }else if (item instanceof ImageListServiceItem){
            ImageListEditorForm ilsi = new ImageListEditorForm((ImageListServiceItem)item);
            ilsi.setVisible(true);
        }

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpMouseClicked
        tableServiceList.upItem();
}//GEN-LAST:event_btnUpMouseClicked

    private void btnDownMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDownMouseClicked
        tableServiceList.downItem();
}//GEN-LAST:event_btnDownMouseClicked

    private void txtHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoursActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtHoursActionPerformed

    private void txtHoursFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHoursFocusGained
        txtHours.selectAll();
}//GEN-LAST:event_txtHoursFocusGained

    private void txtHoursFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHoursFocusLost
        try{
            int x = Integer.parseInt(txtHours.getText());
            if (x < 0 || x > 24){
                throw new Exception();
            }
            ServiceListTable.getActiveInstance().setStartHour(x);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Invalid_value"));
            txtHours.setText(Integer.toString(ServiceListTable.getActiveInstance().getStartHour()));
        }
        pnlServiceList.revalidate();
}//GEN-LAST:event_txtHoursFocusLost

    private void txtHoursKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoursKeyPressed

}//GEN-LAST:event_txtHoursKeyPressed

    private void txtMinutesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinutesFocusGained
        txtMinutes.selectAll();
}//GEN-LAST:event_txtMinutesFocusGained

    private void txtMinutesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinutesFocusLost
        try{
            int x = Integer.parseInt(txtMinutes.getText());
            if (x < 0 || x > 60){
                throw new Exception();
            }
            ServiceListTable.getActiveInstance().setStartMinute(x);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Invalid_value"));
            txtMinutes.setText(Integer.toString(ServiceListTable.getActiveInstance().getStartMinute()));
        }
}//GEN-LAST:event_txtMinutesFocusLost

    private void txtNotesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNotesFocusLost
        ServiceListTable.getActiveInstance().setNotes(txtNotes.getText());
}//GEN-LAST:event_txtNotesFocusLost

    private void actAddSongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddSongActionPerformed
        AddSongForm asf = new AddSongForm();
        asf.setLocationRelativeTo(this);
        asf.setVisible(true);
}//GEN-LAST:event_actAddSongActionPerformed

    private void actAddTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddTextActionPerformed
        TextServiceItemEditorForm tsief = new TextServiceItemEditorForm(new TextServiceItem());
        tsief.setLocationRelativeTo(this);
        tsief.setVisible(true);
}//GEN-LAST:event_actAddTextActionPerformed

    private void actAddBibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddBibleActionPerformed
        TextServiceItemEditorForm tsief = new TextServiceItemEditorForm(new TextServiceItem());
        tsief.setLocationRelativeTo(this);
        tsief.setBibleVisible(true);
        tsief.setVisible(true);
}//GEN-LAST:event_actAddBibleActionPerformed

    private void actAddContentlessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddContentlessItemActionPerformed
        String s = JOptionPane.showInputDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Service_Item_Name:"), "");
        if (s != null && !s.trim().equals("")){
            ContentlessServiceItem csi = new ContentlessServiceItem();
            csi.setTitle(s);
            csi.setTemplate("");
            ServiceListTable.getActiveInstance().addItem(csi);
        }
}//GEN-LAST:event_actAddContentlessItemActionPerformed

    private void actAddImageListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddImageListActionPerformed
        ImageListServiceItem newitem = new ImageListServiceItem();
        ServiceListTable.getActiveInstance().addItem(newitem);
        ImageListEditorForm form = new ImageListEditorForm(newitem);
        form.setVisible(true);
}//GEN-LAST:event_actAddImageListActionPerformed

    private void actImportItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actImportItemActionPerformed
        ImportServiceItemForm isif = new ImportServiceItemForm();
        isif.setLocationRelativeTo(this);
        isif.setVisible(true);
}//GEN-LAST:event_actImportItemActionPerformed

    private void jSplitPane4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSplitPane4PropertyChange
        if (updateSize && evt.getPropertyName().equals(javax.swing.JSplitPane.DIVIDER_LOCATION_PROPERTY)){
            WindowPropConfig.getInstance().setServiceSplit2(Integer.toString(jSplitPane2.getDividerLocation()));
        }
}//GEN-LAST:event_jSplitPane4PropertyChange

    private void liveDisplayPanelpreviewDisplayResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_liveDisplayPanelpreviewDisplayResized

    }//GEN-LAST:event_liveDisplayPanelpreviewDisplayResized

    private void monitorDisplayPanelpreviewDisplayResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_monitorDisplayPanelpreviewDisplayResized
        // TODO add your handling code here:
}//GEN-LAST:event_monitorDisplayPanelpreviewDisplayResized

    private void btnShowMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMonitorActionPerformed
        monitorDisplayPanel.setVisible(btnShowMonitor.isSelected());
}//GEN-LAST:event_btnShowMonitorActionPerformed

    private void btnGoLiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoLiveActionPerformed
        preview.goLive();
        showDisplayControls();
    }//GEN-LAST:event_btnGoLiveActionPerformed
    
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
        String files[] = {"alert-general.templatez",
            "alert-nursery.templatez",
            "alert-parking.templatez",
            "bible.templatez",
            "monitor-preaching.templatez",
            "monitor-song.templatez",
            "default.templatez",
            "song.templatez",
            "subtitle.templatez"
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
        ContentManager.getInstance();
        checkStorageLocation();
        
        BackgroundConfig.getInstance();
        DisplayControlConfig.getInstance();

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_songs..."));
        AllSongsListTable.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_chords_database..."));
        ChordsDB.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Starting_content_manager..."));
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

        ContentManager.getInstance().getMainDisplay();
        ContentManager.getInstance().getMonitorDisplay();
        
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

    public boolean goToNextServiceItem() {
       int currentRow = tableServiceList.getSelectedRow();
       int maxRow = tableServiceList.getRowCount();
       while (currentRow < maxRow-1){
           if (tableServiceList.getModel().getValueAt(currentRow+1, 0) instanceof ContentlessServiceItem){
               currentRow++;
           }else{
               tableServiceList.setRowSelectionInterval(currentRow+1,currentRow+1);
               goLiveItem();
               return true;
           }
        }
        return false;
    }

    public boolean goToPreviousServiceItem() {
        int currentRow = tableServiceList.getSelectedRow();
        while (currentRow > 0) {
           if (tableServiceList.getModel().getValueAt(currentRow-1, 0) instanceof ContentlessServiceItem){
              currentRow--;
           }else{
               tableServiceList.setRowSelectionInterval(currentRow - 1, currentRow - 1);
               goLiveItem();
               return true;
           }
        }
        return false;
    }


    public void showDisplayControls(){
        tabbedRightSide.setSelectedComponent(splDisplayControl);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JMenuItem actAddBible;
    javax.swing.JMenuItem actAddContentlessItem;
    javax.swing.JMenuItem actAddImageList;
    javax.swing.JMenuItem actAddSong;
    javax.swing.JMenuItem actAddText;
    javax.swing.JMenuItem actImportItem;
    datasoul.datashow.AuxiliarPanel auxiliar;
    javax.swing.JButton btnAddWizard;
    javax.swing.JButton btnClose;
    javax.swing.JButton btnConfig;
    javax.swing.JButton btnDown;
    javax.swing.JButton btnEdit;
    javax.swing.JButton btnExport;
    javax.swing.JButton btnGoLive;
    javax.swing.JButton btnHelp;
    javax.swing.JButton btnNew;
    javax.swing.JButton btnOpen;
    javax.swing.JButton btnRemove;
    javax.swing.JButton btnSave;
    javax.swing.JButton btnSaveAs;
    javax.swing.JToggleButton btnShowMonitor;
    javax.swing.JButton btnTemplates;
    javax.swing.JButton btnUp;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabel4;
    javax.swing.JLabel jLabel6;
    javax.swing.JLabel jLabel7;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel3;
    javax.swing.JPanel jPanel6;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JToolBar.Separator jSeparator1;
    javax.swing.JToolBar.Separator jSeparator2;
    javax.swing.JSplitPane jSplitPane1;
    javax.swing.JSplitPane jSplitPane2;
    javax.swing.JSplitPane jSplitPane3;
    javax.swing.JSplitPane jSplitPane4;
    javax.swing.JLabel lblAuthor;
    javax.swing.JLabel lblSongName;
    datasoul.datashow.LivePanel live;
    datasoul.render.SwingDisplayPanel liveDisplayPanel;
    datasoul.render.SwingDisplayPanel monitorDisplayPanel;
    javax.swing.JPanel pnlLiveBox;
    javax.swing.JPanel pnlPreview;
    javax.swing.JPanel pnlServiceList;
    javax.swing.JPopupMenu ppmAddItem;
    datasoul.datashow.PreviewPanel preview;
    datasoul.song.SongsSearchPanel songsSearchPanel1;
    javax.swing.JSplitPane splDisplayControl;
    javax.swing.JTabbedPane tabbedRightSide;
    datasoul.util.DnDTable tableServiceList;
    javax.swing.JTextPane textSong;
    javax.swing.JToolBar toolBar;
    javax.swing.JToolBar toolBarMain;
    javax.swing.JTextField txtHours;
    javax.swing.JTextField txtMinutes;
    javax.swing.JTextArea txtNotes;
    javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables


    private class TemplateCellEditor extends DefaultCellEditor implements ActionListener {

        private TemplateComboBox cbSong;
        private TemplateComboBox cbText;
        private TemplateComboBox cbImage;
        private TemplateComboBox cbAll;

        @Override
        public void actionPerformed(ActionEvent e) {

            Object source = e.getSource();
            if (source instanceof JComboBox){
                this.setValue( ((JComboBox) source).getSelectedItem() );
            }

        }

        public TemplateCellEditor(){
            super(new JTextField());

            cbSong = new TemplateComboBox();
            cbSong.setTargetContent(DisplayTemplate.TARGET_CONTENT_SONG);
            cbSong.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            cbSong.addActionListener(this);

            cbText = new TemplateComboBox();
            cbText.setTargetContent(DisplayTemplate.TARGET_CONTENT_TEXT);
            cbText.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            cbText.addActionListener(this);

            cbImage = new TemplateComboBox();
            cbImage.setTargetContent(DisplayTemplate.TARGET_CONTENT_IMAGES);
            cbImage.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            cbImage.addActionListener(this);

            cbAll = new TemplateComboBox();
            cbAll.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            cbAll.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
            
            TemplateComboBox cbRet;
            ServiceItem item = (ServiceItem) ServiceListTable.getActiveInstance().getServiceItem(row);

            if ( item instanceof ImageListServiceItem ){
                cbRet = cbImage;
            }else if ( item instanceof Song ){
                cbRet = cbSong;
            }else if ( item instanceof TextServiceItem ){
                cbRet = cbText;
            }else{
                cbRet = cbAll;
            }

            cbRet.setSelectedItem(value);

            return cbRet;
        }

        protected void setValue (Object o){
            this.delegate.setValue(o);
            stopCellEditing();
        }

    }

}
