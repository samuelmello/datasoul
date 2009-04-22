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
 * SongViewer.java
 *
 * Created on 14 de Dezembro de 2005, 22:09
 */

package datasoul.song;

import datasoul.servicelist.ServiceListExporterPanel;
import datasoul.util.ShowDialog;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author  Administrador
 */
public class SongViewer extends javax.swing.JPanel {

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

    public static String VIEW_LYRICS = "Lyrics";
    public static String VIEW_CHORDS_COMPLETE = "ChordsComplete";
    public static String VIEW_CHORDS_SIMPLIFIED = "ChordsSimplified";

    public String activeView = VIEW_LYRICS;
    
    private void loadSongTemplate(){
        songTemplate = new SongTemplate();
        
        String filepath = System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") 
        + "config"+ System.getProperty("file.separator") + "datasoul.song"+this.activeView+"template";

        File songTemplateFile = new File(filepath);
        
        Document dom=null;
        Node node = null;
        Song song;
        if(songTemplateFile.exists()){
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(songTemplateFile);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("SongTemplate").item(0);

                songTemplate.readObject(node);
               
            }catch(Exception e) {
                ShowDialog.showReadFileError(songTemplateFile, e);
            }
        }
    }
    
    /**
     * Creates new form SongViewer
     */
    public SongViewer() {
        initComponents();

        notes.add("C");
        notes.add("C#");
        notes.add("D");
        notes.add("D#");
        notes.add("E");
        notes.add("F");
        notes.add("F#");
        notes.add("G");
        notes.add("G#");
        notes.add("A");
        notes.add("A#");
        notes.add("B");
        comboKey.removeAllItems();
        comboKey.addItem("C");
        comboKey.addItem("C#");
        comboKey.addItem("D");
        comboKey.addItem("D#");
        comboKey.addItem("E");
        comboKey.addItem("F");
        comboKey.addItem("F#");
        comboKey.addItem("G");
        comboKey.addItem("G#");
        comboKey.addItem("A");
        comboKey.addItem("A#");
        comboKey.addItem("B");


        chordsName = new ArrayList<String>();
        
        StyleContext sc = new StyleContext();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
    //prop font
        nameStyle = sc.addStyle("nameStyle",defaultStyle);
        authorStyle = sc.addStyle("authorStyle",defaultStyle);
        lyricsStyle = sc.addStyle("lyricsStyle",defaultStyle);
        chordsStyle = sc.addStyle("chordsStyle",defaultStyle);
        chordShapeStyle = sc.addStyle("chordShapeStyle",null);

        loadSongTemplate();
        setStyles();
    }
    
    private void setStyles(){
        StyleConstants.setForeground(nameStyle,Color.decode("0x"+songTemplate.getTitleFontColor()));
        StyleConstants.setBackground(nameStyle,Color.white);
        StyleConstants.setFontFamily(nameStyle,songTemplate.getTitleFontName());
        StyleConstants.setFontSize(nameStyle,songTemplate.getTitleFontSize());                

        StyleConstants.setForeground(authorStyle,Color.decode("0x"+songTemplate.getAuthorFontColor()));
        StyleConstants.setBackground(authorStyle,Color.white);
        StyleConstants.setFontFamily(authorStyle,songTemplate.getAuthorFontName());
        StyleConstants.setFontSize(authorStyle,songTemplate.getAuthorFontSize());                

        StyleConstants.setForeground(chordsStyle,Color.decode("0x"+songTemplate.getChordsFontColor()));
        StyleConstants.setBackground(chordsStyle,Color.white);
        StyleConstants.setFontFamily(chordsStyle,songTemplate.getChordsFontName());
        StyleConstants.setFontSize(chordsStyle,songTemplate.getChordsFontSize());       

        StyleConstants.setForeground(lyricsStyle,Color.decode("0x"+songTemplate.getLyricsFontColor()));
        StyleConstants.setBackground(lyricsStyle,Color.white);
        StyleConstants.setFontFamily(lyricsStyle,songTemplate.getLyricsFontName());
        StyleConstants.setFontSize(lyricsStyle,songTemplate.getLyricsFontSize());
        
    }
    
    public void refresh(){
        if(song!=null){
            showSong();
        }
    }
    
    public void setView(String item){
        activeView = item;
        
        if(this.activeView.equals(this.VIEW_LYRICS)){
            this.labelKey.setVisible(false);
            this.comboKey.setVisible(false);
            this.btnChords.setVisible(false);
            this.panelSongChords.setVisible(false);
        }else{
            this.labelKey.setVisible(true);
            this.comboKey.setVisible(true);
            this.btnChords.setVisible(true);
            this.panelSongChords.setVisible(true);
        }
        
        loadSongTemplate();
        setStyles();
    }

    public String getView(){
        return activeView;
    }
    
    
    public void viewSong(Song song){
        keyOrig="";
        keyActual="";
        
        this.song = song;
        showSong();
    }
    
    public void showSong(){
        setStyles();        
        
        chordsName.clear();

        try {            
            drawLyrics(this.editorSong,true);
            drawChords(this.editorSongChords,true);
            this.editorSong.setCaretPosition(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLyrics(JEditorPane jep, boolean clearBeforeAdd) throws Exception{
        String strSong="";
        String line="";
        String nextline="";
        StringBuffer html = new StringBuffer();
        StringReader sr = null;
        BufferedReader buff= null;

       
        jep.setContentType("text/rtf");
        
        javax.swing.text.Document doc = jep.getDocument();

        if(this.getView().equals(this.VIEW_CHORDS_COMPLETE)){
            strSong = song.getChordsComplete().replace(Song.CHORUS_MARK,"").replace(Song.SLIDE_BREAK,"");

            /*
            if(strSong.length()==0){
                strSong = song.getText().replace(Song.CHORUS_MARK,"").replace(Song.SLIDE_BREAK,"");
            }
             */
        }else if(this.getView().equals(this.VIEW_CHORDS_SIMPLIFIED)){
            strSong = song.getChordsSimplified().replace(Song.CHORUS_MARK,"").replace(Song.SLIDE_BREAK,"");
            
            /*
            if(strSong.length()==0){
                strSong = song.getText().replace(Song.CHORUS_MARK,"").replace(Song.SLIDE_BREAK,"");
            }
             */
        }else{
            strSong = song.getText().replace(Song.CHORUS_MARK,"").replace(Song.SLIDE_BREAK,"");
        }
        
        sr = new StringReader(strSong);
        buff = new BufferedReader(sr);
        
        if(clearBeforeAdd){
            doc.remove(0,doc.getLength());
        }

        doc.insertString(doc.getLength(),song.getTitle()+"\n",nameStyle);
        if (song.getSongAuthor()!=null && song.getSongAuthor().length() > 0)
            doc.insertString(doc.getLength(),song.getSongAuthor()+"\n",authorStyle);
        if (song.getCopyright() != null && song.getCopyright().length() > 0)
            doc.insertString(doc.getLength(),song.getCopyright()+"\n",authorStyle);
        if (song.getSongSource() != null && song.getSongSource().length() > 0)
            doc.insertString(doc.getLength(),song.getSongSource()+"\n",authorStyle);
        
        doc.insertString(doc.getLength(),"\n\n",authorStyle);

        line = buff.readLine();
        while((nextline = buff.readLine())!=null){
            addFormattedSongLine(doc,line,nextline);
            line = nextline;
        }
        addFormattedSongLine(doc,line,"");
       
    }
    
    
    private void addFormattedSongLine(javax.swing.text.Document doc, String line, String nextline) throws BadLocationException{
        if(line==null)
            return;
        
        if(isChordsLine(line)){
            line = line.substring(1,line.length());
            doc.insertString(doc.getLength(),getFormattedChordLine(line,nextline)+"\n",chordsStyle);
        }else{
            doc.insertString(doc.getLength(),getFormattedLyricLine(line)+"\n",lyricsStyle);
        }
        return;
    }
    
    private boolean isChordsLine(String line){

        if(line.startsWith("=")){
            return true;
        }else{
            return false;
        }
    }

    private String getFormattedLyricLine(String line){
        return line;
    }

    private String getFormattedChordLine(String line,String nextline){
        String newLine="";
        String strAux="";
        String spaces="";
        int index=0;
        int widthNextLine=0;
        int widthNewLine=0;
        int neededWidth=0;
        int spaceSize=0;
        int spacesNedded=0;
        
        String[] chords = line.replace("\t"," ").split(" ");
        Font fontChords = new Font(songTemplate.getChordsFontName(), Font.PLAIN, songTemplate.getChordsFontSize());
        FontMetrics fontChordsMetrics = getFontMetrics(fontChords);
        Font fontLyrics = new Font(songTemplate.getLyricsFontName(), Font.PLAIN, songTemplate.getLyricsFontSize());
        FontMetrics fontLyricsMetrics = getFontMetrics(fontLyrics);
        spaceSize = fontChordsMetrics.stringWidth(" ");
        
        for(int i=0;i<chords.length;i++){
            if(!chords[i].equals("")){
                
                String thisChord = changeKey(chords[i]);
                
                String thisChordCleaned = thisChord;
                //if there is some comments in the line like (C/E  E  F), these part ignores the "(" and consider it a chord line                
                if(thisChordCleaned.startsWith("(")){
                    thisChordCleaned = thisChordCleaned.replace("(","");
                }
                if( ( thisChordCleaned.endsWith(")")) &&
                    (!thisChordCleaned.contains("("))    ){
                    thisChordCleaned = thisChordCleaned.replace(")","");
                }
                
                if(!chordsName.contains(thisChordCleaned)&&
                    (thisChordCleaned.startsWith("C")||
                     thisChordCleaned.startsWith("D")||
                     thisChordCleaned.startsWith("E")||
                     thisChordCleaned.startsWith("F")||
                     thisChordCleaned.startsWith("G")||
                     thisChordCleaned.startsWith("A")||
                     thisChordCleaned.startsWith("B") 
                    )
                 ){
                    chordsName.add(thisChordCleaned);
                }
                
                index = strAux.length();
                if(index<nextline.length()){
                    widthNextLine = fontLyricsMetrics.stringWidth(nextline.substring(0,index));                
                    widthNewLine = fontChordsMetrics.stringWidth(newLine);                
                    neededWidth = (widthNextLine - widthNewLine);
                    spacesNedded = Math.round(neededWidth/spaceSize);
                    spaces = "";
                    for(int j=0;j<spacesNedded;j++)
                        spaces = spaces + " ";
                    newLine = newLine + spaces + thisChord;
                }else{
                    spaces = "";
                    int indexAux= index-1;
                    while(indexAux>1 && line.substring(indexAux, indexAux+1).equals(" "))
                    {
                        spaces = spaces + " ";                        
                        indexAux = indexAux-1;
                    }
                    newLine = newLine + spaces + thisChord;                    
                }
                strAux += thisChord + " ";                    
            }else{
                strAux += " ";
                spaces = spaces + " ";
            }
        }
        
        return newLine;        
    }
    
    private String changeKey(String chord){
        String newChord = chord;
        
        if(keyActual.equals("")){
            keyActual = getNote(chord);        
            comboKey.setSelectedItem(keyActual);
        }
        if(keyOrig.equals(""))
            keyOrig = getNote(chord);

        //if key was changed
        if(!keyActual.equals(keyOrig)){
            int diff = notes.indexOf(keyActual)-notes.indexOf(keyOrig);
            //main note
            String note = getNote(chord);
            int index = notes.indexOf(note);
            int newIndex = index + diff;
            if(newIndex<0)
                newIndex = 12 - newIndex;
            if(newIndex>11)
                newIndex = newIndex-12;
            
            newChord =  notes.get(newIndex);
            
            if(chord.contains("/")){
                //main note
                note = getNote(chord.substring(chord.indexOf("/")+1,chord.length()));
                index = notes.indexOf(note);
                newIndex = index + diff;
                if(newIndex<0)
                    newIndex = 12 - newIndex;
                if(newIndex>11)
                    newIndex = newIndex-12;
                
                newChord =  newChord+chord.substring(getNote(chord).length(),chord.indexOf("/")+1)+notes.get(newIndex);                
            }else{
                newChord =  newChord+chord.substring(note.length(),chord.length());                
            }
           
        }

        return newChord;
    }
    
    private String getNote(String chord){
        if((chord.length()>1)&&(chord.substring(1,2).equals("#"))){
            return chord.substring(0,2);
        }else if((chord.length()>1)&&(chord.substring(1,2).equals("b"))){
            int index = notes.indexOf(chord.substring(0,1))-1;
            if(index==-1){
                index=11;
            }
            return notes.get(index);
        }else{
            return chord.substring(0,1);
        }
    }
    
    private void drawChords(JEditorPane jep, boolean clearBeforeAdd) throws BadLocationException {

        jep.setContentType("text/rtf");
        
        javax.swing.text.Document doc = jep.getDocument();
        
        if(clearBeforeAdd){
            doc.remove(0,doc.getLength());
        }
        
        ChordsDB chordsDB = ChordsDB.getInstance();
        for(int i=0; i<chordsName.size();i++){
            Chord chord = chordsDB.getChordByName(chordsName.get(i));
            if(chord!=null){
                int shapeSize = 1;
                if(songTemplate.getChordShapeSizeIdx() == SongTemplate.CHORDSIZE_SMALL){
                    shapeSize = 3;
                }else if(songTemplate.getChordShapeSizeIdx() == SongTemplate.CHORDSIZE_MEDIUM){
                    shapeSize = 2;
                }
                ChordShapePanel csp = new ChordShapePanel(shapeSize, chord.getName(),chord.getShape());
                
                StyleConstants.setIcon(chordShapeStyle, new ImageIcon(csp.createImage()));
                doc.insertString(doc.getLength(),"text to be ignored", chordShapeStyle);

            }else{
                doc.insertString(doc.getLength(),"\nChord not cataloged "+chordsName.get(i)+"\n", lyricsStyle);                
            }
                
        }
    }


    public SongsPanel getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(SongsPanel objectManager) {
        this.objectManager = objectManager;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        split1 = new javax.swing.JSplitPane();
        panelSong = new javax.swing.JPanel();
        scroolSong = new javax.swing.JScrollPane();
        editorSong = new javax.swing.JEditorPane();
        panelSongChords = new javax.swing.JPanel();
        scroolSongChords = new javax.swing.JScrollPane();
        editorSongChords = new javax.swing.JEditorPane();
        jToolBar2 = new javax.swing.JToolBar();
        btnExport = new javax.swing.JButton();
        btnChords = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        labelKey = new javax.swing.JLabel();
        comboKey = new javax.swing.JComboBox();

        split1.setDividerLocation(375);

        editorSong.setEditable(false);
        scroolSong.setViewportView(editorSong);

        org.jdesktop.layout.GroupLayout panelSongLayout = new org.jdesktop.layout.GroupLayout(panelSong);
        panelSong.setLayout(panelSongLayout);
        panelSongLayout.setHorizontalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSong)
        );
        panelSongLayout.setVerticalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        split1.setLeftComponent(panelSong);

        editorSongChords.setEditable(false);
        scroolSongChords.setViewportView(editorSongChords);

        org.jdesktop.layout.GroupLayout panelSongChordsLayout = new org.jdesktop.layout.GroupLayout(panelSongChords);
        panelSongChords.setLayout(panelSongChordsLayout);
        panelSongChordsLayout.setHorizontalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongChords)
        );
        panelSongChordsLayout.setVerticalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongChords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        split1.setRightComponent(panelSongChords);

        jToolBar2.setFloatable(false);
        jToolBar2.setOpaque(false);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/x-office-document.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        btnExport.setText(bundle.getString("Export")); // NOI18N
        btnExport.setBorderPainted(false);
        btnExport.setFocusPainted(false);
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        jToolBar2.add(btnExport);

        btnChords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_midi.png"))); // NOI18N
        btnChords.setText("Chord Manager");
        btnChords.setBorderPainted(false);
        btnChords.setFocusPainted(false);
        btnChords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChordsActionPerformed(evt);
            }
        });
        jToolBar2.add(btnChords);

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jToolBar2.add(jSeparator1);

        labelKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_effects-sound_small.png"))); // NOI18N
        labelKey.setText(bundle.getString("Key_")); // NOI18N
        jToolBar2.add(labelKey);

        comboKey.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboKey.setToolTipText(bundle.getString("Select_the_key_to_song")); // NOI18N
        comboKey.setMaximumSize(new java.awt.Dimension(50, 32767));
        comboKey.setMinimumSize(new java.awt.Dimension(50, 18));
        comboKey.setPreferredSize(new java.awt.Dimension(50, 22));
        comboKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboKeyActionPerformed(evt);
            }
        });
        jToolBar2.add(comboKey);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnChordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChordsActionPerformed
        ChordsManagerFrame cmf = new ChordsManagerFrame();
        cmf.setVisible(true);
    }//GEN-LAST:event_btnChordsActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed

        ServiceListExporterPanel slep = new ServiceListExporterPanel();
        slep.setLocationRelativeTo(this);
        slep.setSingleSong(song);
        slep.setVisible(true);
        
}//GEN-LAST:event_btnExportActionPerformed

    private void comboKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboKeyActionPerformed
        if(keyOrig.equals(""))
            return;
        
        keyActual = (String)comboKey.getSelectedItem();
        this.refresh();
    }//GEN-LAST:event_comboKeyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChords;
    private javax.swing.JButton btnExport;
    private javax.swing.JComboBox comboKey;
    private javax.swing.JEditorPane editorSong;
    private javax.swing.JEditorPane editorSongChords;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel labelKey;
    private javax.swing.JPanel panelSong;
    private javax.swing.JPanel panelSongChords;
    private javax.swing.JScrollPane scroolSong;
    private javax.swing.JScrollPane scroolSongChords;
    private javax.swing.JSplitPane split1;
    // End of variables declaration//GEN-END:variables

    class Content implements Printable {
        JEditorPane jep;
        
        public Content(JEditorPane jep){
            this.jep = jep; 
        }
        
        public int print(Graphics g, PageFormat pf, int pageIndex) {
            jep.setSize((int)pf.getWidth(), (int)pf.getHeight());
            jep.invalidate();

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());

            if (pageIndex==0){
                jep.paint(g2d);
            } else{
                return Printable.NO_SUCH_PAGE;
            }
            
            return Printable.PAGE_EXISTS;
        }
    }
}
