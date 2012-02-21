/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import datasoul.bible.BibleVerseListEditorForm;
import datasoul.config.BackgroundConfigFrame;
import datasoul.config.ConfigFrame;
import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.config.WindowPropConfig;
import datasoul.datashow.ImportServiceItemForm;
import datasoul.help.HelpFrameAbout;
import datasoul.help.HelpFrameKeyboard;
import datasoul.render.ContentManager;
import datasoul.render.ContentRender;
import datasoul.render.remote.RemoteLauncherDialog;
import datasoul.serviceitems.AttachmentServiceItem;
import datasoul.serviceitems.ContentlessServiceItem;
import datasoul.serviceitems.GenericAttachmentServiceItem;
import datasoul.serviceitems.ServiceItem;
import datasoul.serviceitems.VideoServiceItem;
import datasoul.serviceitems.imagelist.ImageListEditorForm;
import datasoul.serviceitems.imagelist.ImageListServiceItem;
import datasoul.serviceitems.song.Song;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.serviceitems.text.TextServiceItemEditorForm;
import datasoul.servicelist.ServiceListColorRender;
import datasoul.servicelist.ServiceListExporterPanel;
import datasoul.servicelist.ServiceListTable;
import datasoul.templates.TemplateCellEditor;
import datasoul.templates.TemplateManagerForm;
import datasoul.util.ObjectManager;
import datasoul.util.OfficeTextExtractorFrame;
import datasoul.util.OnlinePublishFrame;

/**
 *
 * @author  Administrador
 */
public class DatasoulMainForm extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2585781540789945771L;
	private boolean updateSize = false;
    public static final int FILE_FORMAT_VERSION = 2;

    /**
     * Creates new form DatasoulMainForm
     */
    public DatasoulMainForm() {

        initComponents();

        this.setTitle("Datasoul - " + getVersion());
        DatasoulMainForm.setDatasoulIcon(this);

        ObjectManager.getInstance().setDatasoulMainForm(this);
        ObjectManager.getInstance().setPreviewPanel(preview);
        ObjectManager.getInstance().setAuxiliarPanel(auxiliar);
        ObjectManager.getInstance().setLivePanel(live);


        tableServiceList.setModel(ServiceListTable.getActiveInstance());
        tableServiceList.setDraggable(false);
        tableServiceList.getSelectionModel().addListSelectionListener(new ServiceListSelectionListener());

        this.tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TEMPLATE).setCellEditor(new TemplateCellEditor());

        ServiceListColorRender cr = new ServiceListColorRender();
        for (int i = 0; i < tableServiceList.getColumnModel().getColumnCount(); i++) {
            this.tableServiceList.getColumnModel().getColumn(i).setCellRenderer(cr);
        }

        // adjust columns
        tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TITLE).setPreferredWidth(200);

        if (ConfigObj.getActiveInstance().getTrackDurationBool()) {
            tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_TIME).setPreferredWidth(70);
            tableServiceList.getColumnModel().getColumn(ServiceListTable.COLUMN_DURATION).setPreferredWidth(40);
            pnlServiceStart.setVisible(true);
        } else {
            pnlServiceStart.setVisible(false);
        }

        initLive();

        tbInfo.setVisible(false);

        WindowPropConfig wpc = WindowPropConfig.getInstance();
        wpc.getMainForm(this);
        wpc.getSplMain(splMain);
        wpc.getSplService(splService);
        wpc.getSplSongLibrary(splSongLibrary);
        wpc.getSplDisplayControl(splDisplayControl);

        updatePreviewHeight();

        updateSize = true;
    }

    private void initLive() {


        Dimension liveSize = new Dimension(ContentManager.PREVIEW_WIDTH, ContentManager.getInstance().getPreviewHeight());
        liveDisplayPanel.setSize(liveSize);
        liveDisplayPanel.setPreferredSize(liveSize);
        liveDisplayPanel.setMaximumSize(liveSize);
        liveDisplayPanel.setMinimumSize(liveSize);
        liveDisplayPanel.initDisplay((int) liveSize.getWidth(), (int) liveSize.getHeight());
        ContentRender liveRender = new ContentRender((int) liveSize.getWidth(), (int) liveSize.getHeight(), liveDisplayPanel.getContentDisplay());
        ContentManager.getInstance().registerMainRender(liveRender);

        if (ConfigObj.getActiveInstance().getMonitorOutput()) {

            Dimension monitorSize = new Dimension(ContentManager.PREVIEW_WIDTH, ContentManager.getInstance().getPreviewMonitorHeight());
            monitorDisplayPanel.setSize(monitorSize);
            monitorDisplayPanel.setPreferredSize(monitorSize);
            monitorDisplayPanel.setMaximumSize(monitorSize);
            monitorDisplayPanel.setMinimumSize(monitorSize);
            monitorDisplayPanel.initDisplay((int) liveSize.getWidth(), (int) liveSize.getHeight());
            ContentRender monitorRender = new ContentRender((int) liveSize.getWidth(), (int) liveSize.getHeight(), monitorDisplayPanel.getContentDisplay());
            ContentManager.getInstance().registerMonitorRender(monitorRender);

        } else {
            btnShowMonitor.setSelected(false);
            btnShowMonitor.setVisible(false);
            monitorDisplayPanel.setVisible(false);
        }

    }

    public static void setDatasoulIcon(JWindow win) {
        win.setIconImage(new javax.swing.ImageIcon(DatasoulMainForm.class.getResource("/datasoul/icons/datasoul.png")).getImage());
    }
    
    public static void setDatasoulIcon(JFrame frame) {
        frame.setIconImage(new javax.swing.ImageIcon(DatasoulMainForm.class.getResource("/datasoul/icons/datasoul.png")).getImage());
    }

    public static void setDatasoulIcon(JDialog dialog) {
        dialog.setIconImage(new javax.swing.ImageIcon(DatasoulMainForm.class.getResource("/datasoul/icons/datasoul.png")).getImage());
    }

    public void previewItem() {

        try {
            ObjectManager.getInstance().setBusyCursor();

            if (tableServiceList.getSelectedRow() != -1) {
                ServiceItem item = (ServiceItem) ServiceListTable.getActiveInstance().getServiceItem(tableServiceList.getSelectedRow());
                if (ObjectManager.getInstance().getPreviewPanel() != null) {
                    ObjectManager.getInstance().getPreviewPanel().previewItem(item);
                }
            }
        } finally {
            ObjectManager.getInstance().setDefaultCursor();
        }
    }

    private void goLiveItem() {

        previewItem();

        if (!DisplayControlConfig.getInstance().getAutomaticGoLiveBool()) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    preview.goLive(false);
                }
            });

        }
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
        actAddText = new javax.swing.JMenuItem();
        actAddVerseList = new javax.swing.JMenuItem();
        actAddContentlessItem = new javax.swing.JMenuItem();
        actAddImageList = new javax.swing.JMenuItem();
        actAddVideo = new javax.swing.JMenuItem();
        actAddAttachment = new javax.swing.JMenuItem();
        actImportItem = new javax.swing.JMenuItem();
        toolBarMain = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSaveAs = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnPublish = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnShow = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnBackground = new javax.swing.JButton();
        btnTemplates = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnConfig = new javax.swing.JButton();
        splMain = new javax.swing.JSplitPane();
        splService = new javax.swing.JSplitPane();
        preview = new datasoul.datashow.PreviewPanel();
        pnlServiceList = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableServiceList = new datasoul.util.DnDTable();
        toolBar = new javax.swing.JToolBar();
        btnAddWizard = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        pnlServiceStart = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtHours = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMinutes = new javax.swing.JTextField();
        tabbedRightSide = new javax.swing.JTabbedPane();
        splSongLibrary = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textSong = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblSongName = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        songsSearchPanel1 = new datasoul.serviceitems.song.SongsSearchPanel();
        splDisplayControl = new javax.swing.JSplitPane();
        live = new datasoul.datashow.LivePanel();
        jPanel3 = new javax.swing.JPanel();
        auxiliar = new datasoul.datashow.AuxiliarPanel();
        pnlLiveBox = new javax.swing.JPanel();
        liveDisplayPanel = new datasoul.render.SwingDisplayPanel();
        monitorDisplayPanel = new datasoul.render.SwingDisplayPanel();
        btnShowMonitor = new javax.swing.JToggleButton();
        tbInfo = new javax.swing.JToolBar();
        lblInfo = new javax.swing.JLabel();
        btnCloseInfo = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        actFileNew = new javax.swing.JMenuItem();
        actFileOpen = new javax.swing.JMenuItem();
        actFileSave = new javax.swing.JMenuItem();
        actFileSaveAs = new javax.swing.JMenuItem();
        actFileExport = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        actFileExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        actEditBackground = new javax.swing.JMenuItem();
        actEditTemplates = new javax.swing.JMenuItem();
        actEditConfig = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        actImportSongFromOffice = new javax.swing.JMenuItem();
        actRemoteDisplay = new javax.swing.JMenuItem();
        actPublishOnline = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        actHelpAbout = new javax.swing.JMenuItem();
        actHelpKeyboard = new javax.swing.JMenuItem();

        actAddText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/font-x-generic.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        actAddText.setText(bundle.getString("ADD TEXT")); // NOI18N
        actAddText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddTextActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddText);

        actAddVerseList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_new-master-document.png"))); // NOI18N
        actAddVerseList.setText(bundle.getString("ADD_VERSE_LIST")); // NOI18N
        actAddVerseList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddVerseListActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddVerseList);

        actAddContentlessItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_insert-note.png"))); // NOI18N
        actAddContentlessItem.setText(bundle.getString("ADD CONTENTLESS ITEM")); // NOI18N
        actAddContentlessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddContentlessItemActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddContentlessItem);

        actAddImageList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/image-x-generic.png"))); // NOI18N
        actAddImageList.setText(bundle.getString("ADD IMAGE LIST")); // NOI18N
        actAddImageList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddImageListActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddImageList);

        actAddVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/video-x-generic.png"))); // NOI18N
        actAddVideo.setText(bundle.getString("ADD VIDEO")); // NOI18N
        actAddVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddVideoActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddVideo);

        actAddAttachment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/mail-attachment.png"))); // NOI18N
        actAddAttachment.setText(bundle.getString("ADD ATTACHMENT")); // NOI18N
        actAddAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actAddAttachmentActionPerformed(evt);
            }
        });
        ppmAddItem.add(actAddAttachment);

        actImportItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_task-assigned.png"))); // NOI18N
        actImportItem.setText(bundle.getString("IMPORT ITEM")); // NOI18N
        actImportItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actImportItemActionPerformed(evt);
            }
        });
        ppmAddItem.add(actImportItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(bundle.getString("DATASOUL")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        toolBarMain.setFloatable(false);
        toolBarMain.setOpaque(false);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-new_big.png"))); // NOI18N
        btnNew.setToolTipText(bundle.getString("NEW")); // NOI18N
        btnNew.setBorderPainted(false);
        btnNew.setFocusable(false);
        btnNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        toolBarMain.add(btnNew);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-open_big.png"))); // NOI18N
        btnOpen.setToolTipText(bundle.getString("OPEN")); // NOI18N
        btnOpen.setBorderPainted(false);
        btnOpen.setFocusable(false);
        btnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        toolBarMain.add(btnOpen);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-save_big.png"))); // NOI18N
        btnSave.setToolTipText(bundle.getString("SAVE")); // NOI18N
        btnSave.setBorderPainted(false);
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        toolBarMain.add(btnSave);

        btnSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-save-as_big.png"))); // NOI18N
        btnSaveAs.setToolTipText(bundle.getString("SAVE AS")); // NOI18N
        btnSaveAs.setBorderPainted(false);
        btnSaveAs.setFocusable(false);
        btnSaveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAsActionPerformed(evt);
            }
        });
        toolBarMain.add(btnSaveAs);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/document-print.png"))); // NOI18N
        btnPrint.setToolTipText(bundle.getString("PRINT")); // NOI18N
        btnPrint.setBorderPainted(false);
        btnPrint.setFocusable(false);
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        toolBarMain.add(btnPrint);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/x-office-document_big.png"))); // NOI18N
        btnExport.setText(bundle.getString("EXPORT")); // NOI18N
        btnExport.setBorderPainted(false);
        btnExport.setFocusable(false);
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        toolBarMain.add(btnExport);

        btnPublish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/web-browser-big.png"))); // NOI18N
        btnPublish.setText(bundle.getString("PUBLISH")); // NOI18N
        btnPublish.setBorderPainted(false);
        btnPublish.setFocusable(false);
        btnPublish.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPublishActionPerformed(evt);
            }
        });
        toolBarMain.add(btnPublish);
        toolBarMain.add(jSeparator2);

        btnShow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_effects-preview.png"))); // NOI18N
        btnShow.setText(bundle.getString("SHOW OUTPUT")); // NOI18N
        btnShow.setBorderPainted(false);
        btnShow.setEnabled(false);
        btnShow.setFocusable(false);
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });
        toolBarMain.add(btnShow);
        toolBarMain.add(jSeparator3);

        btnBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/preferences-desktop-wallpaper.png"))); // NOI18N
        btnBackground.setText(bundle.getString("BACKGROUND")); // NOI18N
        btnBackground.setBorderPainted(false);
        btnBackground.setFocusable(false);
        btnBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackgroundActionPerformed(evt);
            }
        });
        toolBarMain.add(btnBackground);

        btnTemplates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/applications-graphics.png"))); // NOI18N
        btnTemplates.setText(bundle.getString("TEMPLATES")); // NOI18N
        btnTemplates.setBorderPainted(false);
        btnTemplates.setFocusable(false);
        btnTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplatesActionPerformed(evt);
            }
        });
        toolBarMain.add(btnTemplates);
        toolBarMain.add(jSeparator1);

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/preferences-system.png"))); // NOI18N
        btnConfig.setText(bundle.getString("CONFIGURATION")); // NOI18N
        btnConfig.setBorderPainted(false);
        btnConfig.setFocusable(false);
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });
        toolBarMain.add(btnConfig);

        splMain.setDividerLocation(420);
        splMain.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splMainPropertyChange(evt);
            }
        });

        splService.setDividerLocation(200);
        splService.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splService.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                splServiceComponentResized(evt);
            }
        });
        splService.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splServicePropertyChange(evt);
            }
        });
        splService.setRightComponent(preview);

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
        tableServiceList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableServiceListKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tableServiceList);

        toolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolBar.setFloatable(false);
        toolBar.setMinimumSize(new java.awt.Dimension(30, 25));
        toolBar.setOpaque(false);

        btnAddWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/list-add.png"))); // NOI18N
        btnAddWizard.setText(bundle.getString("ADD")); // NOI18N
        btnAddWizard.setToolTipText(bundle.getString("ADD ITEM ...")); // NOI18N
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

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/edit-delete.png"))); // NOI18N
        btnRemove.setText(bundle.getString("DELETE")); // NOI18N
        btnRemove.setToolTipText(bundle.getString("DELETE ITEM")); // NOI18N
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
        btnEdit.setText(bundle.getString("EDIT")); // NOI18N
        btnEdit.setToolTipText(bundle.getString("EDIT SELECTED ITEM ...")); // NOI18N
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
        btnUp.setText(bundle.getString("MOVE UP")); // NOI18N
        btnUp.setToolTipText(bundle.getString("CHANGE ITEM ORDER TO UPPER POSITION")); // NOI18N
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
        btnDown.setText(bundle.getString("MOVE DOWN")); // NOI18N
        btnDown.setToolTipText(bundle.getString("CHANGE ITEM ORDER TO LOWER POSITION")); // NOI18N
        btnDown.setAlignmentY(0.0F);
        btnDown.setBorderPainted(false);
        btnDown.setFocusPainted(false);
        btnDown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDownMouseClicked(evt);
            }
        });
        toolBar.add(btnDown);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        txtNotes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNotesFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(txtNotes);

        jLabel4.setText(bundle.getString("SERVICE NOTES:")); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout(3, 0));

        jLabel1.setText(bundle.getString("SERVICE TITLE:")); // NOI18N
        jPanel2.add(jLabel1, java.awt.BorderLayout.WEST);

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
        jPanel2.add(txtTitle, java.awt.BorderLayout.CENTER);

        pnlServiceStart.setLayout(new javax.swing.BoxLayout(pnlServiceStart, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText(bundle.getString("START TIME:")); // NOI18N
        pnlServiceStart.add(jLabel2);

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
        pnlServiceStart.add(txtHours);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(":");
        pnlServiceStart.add(jLabel3);

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
        pnlServiceStart.add(txtMinutes);

        jPanel2.add(pnlServiceStart, java.awt.BorderLayout.EAST);

        javax.swing.GroupLayout pnlServiceListLayout = new javax.swing.GroupLayout(pnlServiceList);
        pnlServiceList.setLayout(pnlServiceListLayout);
        pnlServiceListLayout.setHorizontalGroup(
            pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServiceListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(315, Short.MAX_VALUE))
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                        .addGap(18, 18, 18))))
        );
        pnlServiceListLayout.setVerticalGroup(
            pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServiceListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlServiceListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlServiceListLayout.createSequentialGroup()
                        .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        splService.setLeftComponent(pnlServiceList);

        splMain.setLeftComponent(splService);

        splSongLibrary.setDividerLocation(200);
        splSongLibrary.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splSongLibrary.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splSongLibraryPropertyChange(evt);
            }
        });

        textSong.setEditable(false);
        jScrollPane3.setViewportView(textSong);

        jLabel6.setText(bundle.getString("AUTHOR:")); // NOI18N

        jLabel7.setText(bundle.getString("SONG:")); // NOI18N

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
                .addContainerGap(405, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );

        splSongLibrary.setRightComponent(jPanel6);
        splSongLibrary.setLeftComponent(songsSearchPanel1);

        tabbedRightSide.addTab(bundle.getString("SONG LIBRARY"), splSongLibrary); // NOI18N

        splDisplayControl.setDividerLocation(240);
        splDisplayControl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splDisplayControlPropertyChange(evt);
            }
        });
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
        btnShowMonitor.setText(bundle.getString("SHOW STAGE")); // NOI18N
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
                .addContainerGap(60, Short.MAX_VALUE))
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
            .addComponent(auxiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(auxiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLiveBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        splDisplayControl.setRightComponent(jPanel3);

        tabbedRightSide.addTab(bundle.getString("DISPLAY CONTROLS"), splDisplayControl); // NOI18N

        splMain.setRightComponent(tabbedRightSide);

        tbInfo.setFloatable(false);
        tbInfo.setRollover(true);

        lblInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/system-software-update.png"))); // NOI18N
        lblInfo.setText("jLabel5");
        tbInfo.add(lblInfo);

        btnCloseInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/window-close.png"))); // NOI18N
        btnCloseInfo.setBorderPainted(false);
        btnCloseInfo.setFocusable(false);
        btnCloseInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCloseInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseInfoActionPerformed(evt);
            }
        });
        tbInfo.add(btnCloseInfo);

        jMenu1.setText(bundle.getString("FILE")); // NOI18N

        actFileNew.setText(bundle.getString("NEW")); // NOI18N
        actFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileNewActionPerformed(evt);
            }
        });
        jMenu1.add(actFileNew);

        actFileOpen.setText(bundle.getString("OPEN")); // NOI18N
        actFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileOpenActionPerformed(evt);
            }
        });
        jMenu1.add(actFileOpen);

        actFileSave.setText(bundle.getString("SAVE")); // NOI18N
        actFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileSaveActionPerformed(evt);
            }
        });
        jMenu1.add(actFileSave);

        actFileSaveAs.setText(bundle.getString("SAVE AS")); // NOI18N
        actFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(actFileSaveAs);

        actFileExport.setText(bundle.getString("EXPORT")); // NOI18N
        actFileExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileExportActionPerformed(evt);
            }
        });
        jMenu1.add(actFileExport);
        jMenu1.add(jSeparator4);

        actFileExit.setText(bundle.getString("EXIT")); // NOI18N
        actFileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actFileExitActionPerformed(evt);
            }
        });
        jMenu1.add(actFileExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("EDIT")); // NOI18N

        actEditBackground.setText(bundle.getString("BACKGROUND")); // NOI18N
        actEditBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actEditBackgroundActionPerformed(evt);
            }
        });
        jMenu2.add(actEditBackground);

        actEditTemplates.setText(bundle.getString("TEMPLATES")); // NOI18N
        actEditTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actEditTemplatesActionPerformed(evt);
            }
        });
        jMenu2.add(actEditTemplates);

        actEditConfig.setText(bundle.getString("CONFIGURATION")); // NOI18N
        actEditConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actEditConfigActionPerformed(evt);
            }
        });
        jMenu2.add(actEditConfig);

        jMenuBar1.add(jMenu2);

        jMenu4.setText(bundle.getString("TOOLS")); // NOI18N

        actImportSongFromOffice.setText(bundle.getString("IMPORT SONG FROM OFFICE PRESENTATION")); // NOI18N
        actImportSongFromOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actImportSongFromOfficeActionPerformed(evt);
            }
        });
        jMenu4.add(actImportSongFromOffice);

        actRemoteDisplay.setText(bundle.getString("CONNECT AS REMOTE DISPLAY")); // NOI18N
        actRemoteDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actRemoteDisplayActionPerformed(evt);
            }
        });
        jMenu4.add(actRemoteDisplay);

        actPublishOnline.setText(bundle.getString("PUBLISH SERVICE PLAN ONLINE")); // NOI18N
        actPublishOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actPublishOnlineActionPerformed(evt);
            }
        });
        jMenu4.add(actPublishOnline);

        jMenuBar1.add(jMenu4);

        jMenu3.setText(bundle.getString("HELP")); // NOI18N

        actHelpAbout.setText(bundle.getString("ABOUT")); // NOI18N
        actHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actHelpAboutActionPerformed(evt);
            }
        });
        jMenu3.add(actHelpAbout);

        actHelpKeyboard.setText(bundle.getString("KEYBOARD SHORTCUTS")); // NOI18N
        actHelpKeyboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actHelpKeyboardActionPerformed(evt);
            }
        });
        jMenu3.add(actHelpKeyboard);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBarMain, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE))
            .addComponent(splMain, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBarMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splMain, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (updateSize) {
            WindowPropConfig.getInstance().setMainForm(this);
        }
    }//GEN-LAST:event_formComponentResized

    private void btnTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplatesActionPerformed
        TemplateManagerForm tmf = ObjectManager.getInstance().getTemplateManagerForm();
        tmf.setVisible(true);
    }//GEN-LAST:event_btnTemplatesActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        ConfigFrame cf = ObjectManager.getInstance().getConfigFrame();
        cf.setVisible(true);
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            ObjectManager.getInstance().setBusyCursor();
            ServiceListTable.getActiveInstance().fileNew();
            txtHours.setText(Integer.toString(ServiceListTable.getActiveInstance().getStartHour()));
            txtMinutes.setText(String.format("%02d", ServiceListTable.getActiveInstance().getStartMinute()));
            txtTitle.setText(ServiceListTable.getActiveInstance().getTitle());
            txtNotes.setText(ServiceListTable.getActiveInstance().getNotes());
        } finally {
            ObjectManager.getInstance().setDefaultCursor();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        File f = ServiceListTable.getActiveInstance().openServiceList();
        if (f != null){
            openServiceList(f.getAbsolutePath());
        }
    }//GEN-LAST:event_btnOpenActionPerformed

    public void openServiceList(final String path){
        // If there is an active service list, ask the user to save
        if (ServiceListTable.getActiveInstance().askForSaveOrCancel() == false) {
            return;
        }

        // Now, open the file
        ObjectManager.getInstance().setBusyCursor();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    ServiceListTable.getActiveInstance().openFile(path);
                    txtHours.setText(Integer.toString(ServiceListTable.getActiveInstance().getStartHour()));
                    txtMinutes.setText(String.format("%02d", ServiceListTable.getActiveInstance().getStartMinute()));
                    txtTitle.setText(ServiceListTable.getActiveInstance().getTitle());
                    txtNotes.setText(ServiceListTable.getActiveInstance().getNotes());
                } finally {
                    ObjectManager.getInstance().setDefaultCursor();
                }
            }
        };
        t.start();
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        ObjectManager.getInstance().setBusyCursor();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    ServiceListTable.getActiveInstance().saveServiceList();
                } finally {
                    ObjectManager.getInstance().setDefaultCursor();
                }

            }
        };
        t.start();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAsActionPerformed
        ObjectManager.getInstance().setBusyCursor();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    ServiceListTable.getActiveInstance().saveServiceListAs();
                } finally {
                    ObjectManager.getInstance().setDefaultCursor();
                }

            }
        };
        t.start();
    }//GEN-LAST:event_btnSaveAsActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        ServiceListExporterPanel p = new ServiceListExporterPanel();
        p.setLocationRelativeTo(this);
        p.setMode(ServiceListExporterPanel.MODE_EXPORT);
        p.setVisible(true);
    }//GEN-LAST:event_btnExportActionPerformed

    private void txtTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTitleActionPerformed

    private void txtTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTitleFocusLost
        ServiceListTable.getActiveInstance().setTitle(txtTitle.getText());
}//GEN-LAST:event_txtTitleFocusLost

    private void tableServiceListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableServiceListMouseClicked

        if (evt.getClickCount() > 1) {
            int col = tableServiceList.getColumnModel().getColumnIndexAtX(evt.getX());
            if (col == ServiceListTable.COLUMN_TITLE) {
                btnEditActionPerformed(null);
            }
        }

}//GEN-LAST:event_tableServiceListMouseClicked

    private void btnAddWizardbtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddWizardbtnAddActionPerformed
        ppmAddItem.show(this.btnAddWizard, 0, btnAddWizard.getHeight());
}//GEN-LAST:event_btnAddWizardbtnAddActionPerformed

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
        tableServiceList.removeItem();
}//GEN-LAST:event_btnRemoveMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tableServiceList.getSelectedRow() == -1) {
            return;
        }

        ServiceItem item = (ServiceItem) ServiceListTable.getActiveInstance().getServiceItem(tableServiceList.getSelectedRow());
        item.edit();

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
        try {
            int x = Integer.parseInt(txtHours.getText());
            if (x < 0 || x > 24) {
                throw new Exception();
            }
            ServiceListTable.getActiveInstance().setStartHour(x);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("INVALID VALUE"));
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
        try {
            int x = Integer.parseInt(txtMinutes.getText());
            if (x < 0 || x > 60) {
                throw new Exception();
            }
            ServiceListTable.getActiveInstance().setStartMinute(x);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("INVALID VALUE"));
            txtMinutes.setText(Integer.toString(ServiceListTable.getActiveInstance().getStartMinute()));
        }
}//GEN-LAST:event_txtMinutesFocusLost

    private void txtNotesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNotesFocusLost
        ServiceListTable.getActiveInstance().setNotes(txtNotes.getText());
}//GEN-LAST:event_txtNotesFocusLost

    private void actAddTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddTextActionPerformed
        TextServiceItemEditorForm tsief = new TextServiceItemEditorForm(new TextServiceItem());
        tsief.setLocationRelativeTo(this);
        tsief.setVisible(true);
}//GEN-LAST:event_actAddTextActionPerformed

    private void actAddContentlessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddContentlessItemActionPerformed
        String s = JOptionPane.showInputDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SERVICE ITEM NAME:"), "");
        if (s != null && !s.trim().equals("")) {
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
        form.setLocationRelativeTo(this);
        form.setVisible(true);
}//GEN-LAST:event_actAddImageListActionPerformed

    private void actImportItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actImportItemActionPerformed
        ImportServiceItemForm isif = new ImportServiceItemForm();
        isif.setLocationRelativeTo(this);
        isif.setVisible(true);
}//GEN-LAST:event_actImportItemActionPerformed

    private void splSongLibraryPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splSongLibraryPropertyChange
        if (updateSize && evt.getPropertyName().equals(javax.swing.JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
            WindowPropConfig.getInstance().setSplSongLibrary(Integer.toString(splSongLibrary.getDividerLocation()));
        }
}//GEN-LAST:event_splSongLibraryPropertyChange

    private void liveDisplayPanelpreviewDisplayResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_liveDisplayPanelpreviewDisplayResized
    }//GEN-LAST:event_liveDisplayPanelpreviewDisplayResized

    private void monitorDisplayPanelpreviewDisplayResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_monitorDisplayPanelpreviewDisplayResized
        // TODO add your handling code here:
}//GEN-LAST:event_monitorDisplayPanelpreviewDisplayResized

    private void btnShowMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMonitorActionPerformed
        monitorDisplayPanel.setVisible(btnShowMonitor.isSelected());
}//GEN-LAST:event_btnShowMonitorActionPerformed

    private void tableServiceListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableServiceListKeyPressed
    }//GEN-LAST:event_tableServiceListKeyPressed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        ObjectManager.getInstance().setOutputVisible(btnShow.isSelected());
    }//GEN-LAST:event_btnShowActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        ServiceListExporterPanel p = new ServiceListExporterPanel();
        p.setLocationRelativeTo(this);
        if (p.setMode(ServiceListExporterPanel.MODE_PRINT) == false) {
            p.dispose();
        } else {
            p.setVisible(true);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackgroundActionPerformed
        BackgroundConfigFrame bcf = ObjectManager.getInstance().getBackgroundConfigFrame();
        bcf.setVisible(true);
    }//GEN-LAST:event_btnBackgroundActionPerformed

    private void actAddAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddAttachmentActionPerformed

        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean isLink = GenericAttachmentServiceItem.askForLink();
            try {
                ObjectManager.getInstance().setBusyCursor();
                AttachmentServiceItem asi = new AttachmentServiceItem(fc.getSelectedFile(), isLink);
                ServiceListTable.getActiveInstance().addItem(asi);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ERROR ATTACHING FILE:") + " " + fc.getSelectedFile().getName() + "\n" + ex.getLocalizedMessage());
            } finally {
                ObjectManager.getInstance().setDefaultCursor();
            }
        }


    }//GEN-LAST:event_actAddAttachmentActionPerformed

    private void actAddVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddVideoActionPerformed

        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            boolean isLink = GenericAttachmentServiceItem.askForLink();
            try {
                ObjectManager.getInstance().setBusyCursor();
                VideoServiceItem asi = new VideoServiceItem(fc.getSelectedFile(), isLink);
                ServiceListTable.getActiveInstance().addItem(asi);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ERROR ATTACHING FILE:") + " " + fc.getSelectedFile().getName() + "\n" + ex.getLocalizedMessage());
            } finally {
                ObjectManager.getInstance().setDefaultCursor();
            }
        }


    }//GEN-LAST:event_actAddVideoActionPerformed

    private void splMainPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splMainPropertyChange
        if (updateSize && evt.getPropertyName().equals(javax.swing.JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
            WindowPropConfig.getInstance().setSplMain(Integer.toString(splMain.getDividerLocation()));
        }
    }//GEN-LAST:event_splMainPropertyChange

    private void splServicePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splServicePropertyChange
        if (updateSize && evt.getPropertyName().equals(javax.swing.JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
            WindowPropConfig.getInstance().setSplService(Integer.toString(splService.getDividerLocation()));
        }
    }//GEN-LAST:event_splServicePropertyChange

    private void splDisplayControlPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_splDisplayControlPropertyChange
        if (updateSize && evt.getPropertyName().equals(javax.swing.JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
            WindowPropConfig.getInstance().setSplDisplayControl(Integer.toString(splDisplayControl.getDividerLocation()));
        }
    }//GEN-LAST:event_splDisplayControlPropertyChange

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actFileExitActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void btnCloseInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseInfoActionPerformed
        tbInfo.setVisible(false);
    }//GEN-LAST:event_btnCloseInfoActionPerformed

    private void actFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_actFileNewActionPerformed

    private void actFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileOpenActionPerformed
        btnOpenActionPerformed(evt);
    }//GEN-LAST:event_actFileOpenActionPerformed

    private void actFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_actFileSaveActionPerformed

    private void actFileSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileSaveAsActionPerformed
        btnSaveAsActionPerformed(evt);
    }//GEN-LAST:event_actFileSaveAsActionPerformed

    private void actFileExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileExportActionPerformed
        btnExportActionPerformed(evt);
    }//GEN-LAST:event_actFileExportActionPerformed

    private void actEditBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actEditBackgroundActionPerformed
        btnBackgroundActionPerformed(evt);
    }//GEN-LAST:event_actEditBackgroundActionPerformed

    private void actEditTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actEditTemplatesActionPerformed
        btnTemplatesActionPerformed(evt);
    }//GEN-LAST:event_actEditTemplatesActionPerformed

    private void actEditConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actEditConfigActionPerformed
        btnConfigActionPerformed(evt);
    }//GEN-LAST:event_actEditConfigActionPerformed

    private void actFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actFileExitActionPerformed
        int resp = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CONFIRM CLOSE?"), "Datasoul", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_actFileExitActionPerformed

    private void actHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actHelpAboutActionPerformed
        HelpFrameAbout about = new HelpFrameAbout();
        about.setLocationRelativeTo(this);
        about.setVisible(true);
    }//GEN-LAST:event_actHelpAboutActionPerformed

    private void actHelpKeyboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actHelpKeyboardActionPerformed
        HelpFrameKeyboard key = new HelpFrameKeyboard();
        key.setLocationRelativeTo(this);
        key.setVisible(true);
    }//GEN-LAST:event_actHelpKeyboardActionPerformed

    private void splServiceComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_splServiceComponentResized
        if (preview.isPreviewActive()) {
            splService.setDividerLocation(splService.getHeight() - splService.getDividerSize() - preview.getPreferedHeight());
        }
    }//GEN-LAST:event_splServiceComponentResized

    private void actAddVerseListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actAddVerseListActionPerformed
        BibleVerseListEditorForm bvlef = new BibleVerseListEditorForm();
        bvlef.setLocationRelativeTo(this);
        bvlef.setVisible(true);
    }//GEN-LAST:event_actAddVerseListActionPerformed

    private void actImportSongFromOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actImportSongFromOfficeActionPerformed

        OfficeTextExtractorFrame otef = new OfficeTextExtractorFrame();
        otef.setLocationRelativeTo(this);
        otef.showConvertDialog();

    }//GEN-LAST:event_actImportSongFromOfficeActionPerformed

    private void actRemoteDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actRemoteDisplayActionPerformed

        RemoteLauncherDialog dialog = new RemoteLauncherDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    }//GEN-LAST:event_actRemoteDisplayActionPerformed

    private void btnPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPublishActionPerformed

        OnlinePublishFrame opf = new OnlinePublishFrame();
        opf.setLocationRelativeTo(this);
        opf.setVisible(true);
        
    }//GEN-LAST:event_btnPublishActionPerformed

    private void actPublishOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actPublishOnlineActionPerformed
        btnPublishActionPerformed(evt);
    }//GEN-LAST:event_actPublishOnlineActionPerformed

    public void closeOutputs() {

        if (btnShow.isSelected()) {
            ObjectManager.getInstance().setOutputVisible(false);
            btnShow.setSelected(false);
        }
    }

    public void showOutputs() {
        if (!btnShow.isSelected()) {
            ObjectManager.getInstance().setOutputVisible(true);
            btnShow.setSelected(true);
        }

    }

    public static String getVersion() {
        try {
            Properties prop = new Properties();
            prop.load(DatasoulMainForm.class.getResourceAsStream("version.properties"));
            return prop.getProperty("version");
        } catch (IOException ex) {
            Logger.getLogger(DatasoulMainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNKNOWN PRIVATE BUILD");
    }

    public static String getFileFormatVersion() {
        return Integer.toString(FILE_FORMAT_VERSION);
    }

    public boolean goToNextServiceItem() {
        int currentRow = tableServiceList.getSelectedRow();
        int maxRow = tableServiceList.getRowCount();
        while (currentRow < maxRow - 1) {
            tableServiceList.setRowSelectionInterval(currentRow + 1, currentRow + 1);
            goLiveItem();
            return true;
        }
        return false;
    }

    public boolean goToPreviousServiceItem() {
        int currentRow = tableServiceList.getSelectedRow();
        while (currentRow > 0) {
            tableServiceList.setRowSelectionInterval(currentRow - 1, currentRow - 1);
            goLiveItem();
            return true;
        }
        return false;
    }

    public void showDisplayControls() {
        tabbedRightSide.setSelectedComponent(splDisplayControl);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JMenuItem actAddAttachment;
    javax.swing.JMenuItem actAddContentlessItem;
    javax.swing.JMenuItem actAddImageList;
    javax.swing.JMenuItem actAddText;
    javax.swing.JMenuItem actAddVerseList;
    javax.swing.JMenuItem actAddVideo;
    javax.swing.JMenuItem actEditBackground;
    javax.swing.JMenuItem actEditConfig;
    javax.swing.JMenuItem actEditTemplates;
    javax.swing.JMenuItem actFileExit;
    javax.swing.JMenuItem actFileExport;
    javax.swing.JMenuItem actFileNew;
    javax.swing.JMenuItem actFileOpen;
    javax.swing.JMenuItem actFileSave;
    javax.swing.JMenuItem actFileSaveAs;
    javax.swing.JMenuItem actHelpAbout;
    javax.swing.JMenuItem actHelpKeyboard;
    javax.swing.JMenuItem actImportItem;
    javax.swing.JMenuItem actImportSongFromOffice;
    javax.swing.JMenuItem actPublishOnline;
    javax.swing.JMenuItem actRemoteDisplay;
    datasoul.datashow.AuxiliarPanel auxiliar;
    javax.swing.JButton btnAddWizard;
    javax.swing.JButton btnBackground;
    javax.swing.JButton btnCloseInfo;
    javax.swing.JButton btnConfig;
    javax.swing.JButton btnDown;
    javax.swing.JButton btnEdit;
    javax.swing.JButton btnExport;
    javax.swing.JButton btnNew;
    javax.swing.JButton btnOpen;
    javax.swing.JButton btnPrint;
    javax.swing.JButton btnPublish;
    javax.swing.JButton btnRemove;
    javax.swing.JButton btnSave;
    javax.swing.JButton btnSaveAs;
    javax.swing.JToggleButton btnShow;
    javax.swing.JToggleButton btnShowMonitor;
    javax.swing.JButton btnTemplates;
    javax.swing.JButton btnUp;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabel4;
    javax.swing.JLabel jLabel6;
    javax.swing.JLabel jLabel7;
    javax.swing.JMenu jMenu1;
    javax.swing.JMenu jMenu2;
    javax.swing.JMenu jMenu3;
    javax.swing.JMenu jMenu4;
    javax.swing.JMenuBar jMenuBar1;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel3;
    javax.swing.JPanel jPanel6;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JToolBar.Separator jSeparator1;
    javax.swing.JToolBar.Separator jSeparator2;
    javax.swing.JToolBar.Separator jSeparator3;
    javax.swing.JPopupMenu.Separator jSeparator4;
    javax.swing.JLabel lblAuthor;
    javax.swing.JLabel lblInfo;
    javax.swing.JLabel lblSongName;
    datasoul.datashow.LivePanel live;
    datasoul.render.SwingDisplayPanel liveDisplayPanel;
    datasoul.render.SwingDisplayPanel monitorDisplayPanel;
    javax.swing.JPanel pnlLiveBox;
    javax.swing.JPanel pnlServiceList;
    javax.swing.JPanel pnlServiceStart;
    javax.swing.JPopupMenu ppmAddItem;
    datasoul.datashow.PreviewPanel preview;
    datasoul.serviceitems.song.SongsSearchPanel songsSearchPanel1;
    javax.swing.JSplitPane splDisplayControl;
    javax.swing.JSplitPane splMain;
    javax.swing.JSplitPane splService;
    javax.swing.JSplitPane splSongLibrary;
    javax.swing.JTabbedPane tabbedRightSide;
    datasoul.util.DnDTable tableServiceList;
    javax.swing.JToolBar tbInfo;
    javax.swing.JTextPane textSong;
    javax.swing.JToolBar toolBar;
    javax.swing.JToolBar toolBarMain;
    javax.swing.JTextField txtHours;
    javax.swing.JTextField txtMinutes;
    javax.swing.JTextArea txtNotes;
    javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables

    public void updatePreviewHeight() {
        splService.setDividerLocation(splService.getHeight() - splService.getDividerSize() - preview.getPreferedHeight());
    }

    public void setInfoText(String txt) {
        lblInfo.setText(txt);
        tbInfo.setVisible(true);
    }

    public void setOutputEnabled(boolean b) {
        btnShow.setEnabled(b);
    }

    public class ServiceListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            previewItem();
        }
    }
}


