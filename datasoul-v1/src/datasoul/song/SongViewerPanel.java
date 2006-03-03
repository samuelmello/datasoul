/*
 * SongViewerPanel.java
 *
 * Created on 14 de Dezembro de 2005, 22:09
 */

package datasoul.song;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.print.attribute.AttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
    
    private void loadSongTemplate(){
        songTemplate = new SongTemplate();
        
        File songTemplateFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "songTemplate.st");
        
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
                JOptionPane.showMessageDialog(this,"Error, the file is not well formed\nErro:"+e.getMessage(),"DataSoul Error",0);
            }
        }
    }
    
    /**
     * Creates new form SongViewerPanel
     */
    public SongViewerPanel() {
        initComponents();

        loadSongTemplate();

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

        specialWords.add("Intro");
        specialWords.add("(1x)");
        specialWords.add("(2x)");
        specialWords.add("(3x)");
        specialWords.add("(4x)");
        specialWords.add("(5x)");        
        specialWords.add("(6x)");
        specialWords.add("(7x)");
        specialWords.add("(8x)");        
        specialWords.add("(9x)");        
        
        comboVersion.removeAllItems();
        comboVersion.addItem("Complete");
        comboVersion.addItem("Simplified");
        comboVersion.setSelectedIndex(0);
        
        chordsName = new ArrayList<String>();
        
        StyleContext sc = new StyleContext();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
    //prop font
        nameStyle = sc.addStyle("nameStyle",defaultStyle);
        authorStyle = sc.addStyle("authorStyle",defaultStyle);
        lyricsStyle = sc.addStyle("lyricsStyle",defaultStyle);
        chordsStyle = sc.addStyle("chordsStyle",defaultStyle);
        chordShapeStyle = sc.addStyle("chordShapeStyle",null);

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

        if(comboVersion.getSelectedItem().equals("Complete")){
            strSong = song.getChordsComplete();
        }else{
            strSong = song.getChordsSimplified();
        }
        sr = new StringReader(strSong);
        buff = new BufferedReader(sr);
        
        if(clearBeforeAdd){
            doc.remove(0,doc.getLength());
        }

        doc.insertString(doc.getLength(),song.getSongName()+"\n",nameStyle);
        doc.insertString(doc.getLength(),song.getSongAuthor()+"\n\n\n",authorStyle);

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
            doc.insertString(doc.getLength(),getFormattedChordLine(line,nextline)+"\n",chordsStyle);
        }else{
            doc.insertString(doc.getLength(),getFormattedLyricLine(line)+"\n",lyricsStyle);
        }
        return;
    }
    
    private boolean isChordsLine(String line){
        String[] chords = line.split(" ");        
        ChordsDB chordsDB = objectManager.getChordsManagerPanel().getChordsDB();
  loop: for(int i=0;i<chords.length;i++){
            if(!chords[i].equals(""))
            {
                for(int j=0;j<specialWords.size();j++){
                    if(chords[i].toLowerCase().contains(specialWords.get(j).toLowerCase())){
                        continue loop;
                    }
                }
                
                Chord chord = chordsDB.getChordByName(chords[i]);
                if(chord==null)
                    return false;
            }
        }

        return true;
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
        
        String[] chords = line.split(" ");
        Font fontChords = new Font(songTemplate.getChordsFontName(), Font.PLAIN, songTemplate.getChordsFontSize());
        FontMetrics fontChordsMetrics = getFontMetrics(fontChords);
        Font fontLyrics = new Font(songTemplate.getLyricsFontName(), Font.PLAIN, songTemplate.getLyricsFontSize());
        FontMetrics fontLyricsMetrics = getFontMetrics(fontLyrics);
        spaceSize = fontChordsMetrics.stringWidth(" ");
        
        for(int i=0;i<chords.length;i++){
            if(!chords[i].equals("")){
                boolean specialWord = false;
                for(int j=0;j<specialWords.size();j++){
                    if(chords[i].toLowerCase().contains(specialWords.get(j).toLowerCase())){
                        specialWord = true;
                    }
                }
                
                String thisChord = changeKey(chords[i]);
                if(!chordsName.contains(thisChord)&&!specialWord)
                    chordsName.add(thisChord);
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
        
        ChordsDB chordsDB = objectManager.getChordsManagerPanel().getChordsDB();
        for(int i=0; i<chordsName.size();i++){
            Chord chord = chordsDB.getChordByName(chordsName.get(i));
            if(chord!=null){
                int shapeSize = 1;
                if(songTemplate.getChordShapeSize().equals("Small")){
                    shapeSize = 3;
                }else if(songTemplate.getChordShapeSize().equals("Medium")){
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        split1 = new javax.swing.JSplitPane();
        panelSong = new javax.swing.JPanel();
        scroolSong = new javax.swing.JScrollPane();
        editorSong = new javax.swing.JEditorPane();
        panelSongChords = new javax.swing.JPanel();
        scroolSongChords = new javax.swing.JScrollPane();
        editorSongChords = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnPrint = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnFormat = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        labelKey = new javax.swing.JLabel();
        comboKey = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        labelVersion = new javax.swing.JLabel();
        comboVersion = new javax.swing.JComboBox();

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
            .add(scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        split1.setLeftComponent(panelSong);

        editorSongChords.setEditable(false);
        scroolSongChords.setViewportView(editorSongChords);

        org.jdesktop.layout.GroupLayout panelSongChordsLayout = new org.jdesktop.layout.GroupLayout(panelSongChords);
        panelSongChords.setLayout(panelSongChordsLayout);
        panelSongChordsLayout.setHorizontalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroolSongChords)
        );
        panelSongChordsLayout.setVerticalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongChords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        split1.setRightComponent(panelSongChords);

        jToolBar1.setFloatable(false);
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/print.gif")));
        btnPrint.setText("print");
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });

        jToolBar1.add(btnPrint);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/generatePraiseDoc.gif")));
        btnExport.setText("Export");
        btnExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExportMouseClicked(evt);
            }
        });

        jToolBar1.add(btnExport);

        btnFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/format.gif")));
        btnFormat.setText("Format");
        btnFormat.setMaximumSize(new java.awt.Dimension(80, 32));
        btnFormat.setMinimumSize(new java.awt.Dimension(72, 32));
        btnFormat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFormatMouseClicked(evt);
            }
        });

        jToolBar1.add(btnFormat);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.add(jSeparator2);

        labelKey.setFont(new java.awt.Font("Arial", 1, 11));
        labelKey.setText("Key");
        jToolBar1.add(labelKey);

        comboKey.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboKey.setMinimumSize(new java.awt.Dimension(25, 18));
        comboKey.setPreferredSize(new java.awt.Dimension(25, 22));
        comboKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboKeyActionPerformed(evt);
            }
        });

        jToolBar1.add(comboKey);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.add(jSeparator1);

        labelVersion.setFont(new java.awt.Font("Arial", 1, 11));
        labelVersion.setText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VERSION"));
        jToolBar1.add(labelVersion);

        comboVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboVersionActionPerformed(evt);
            }
        });

        jToolBar1.add(comboVersion);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
            .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboVersionActionPerformed
        this.refresh();
    }//GEN-LAST:event_comboVersionActionPerformed

    private void comboKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboKeyActionPerformed
        if(keyOrig.equals(""))
            return;
        
        keyActual = (String)comboKey.getSelectedItem();
        this.refresh();
    }//GEN-LAST:event_comboKeyActionPerformed

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        JEditorPane jep = new JEditorPane();
        try {            
            drawLyrics(jep,true);
            drawChords(jep,false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
      
        Content content = new Content(jep);
        pj.setPrintable(content, pf);
        try {
            if (pj.printDialog())
                pj.print();
        } catch (Exception e) {}

    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnFormatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFormatMouseClicked

        SongFormatFrame sff = new SongFormatFrame(this, songTemplate);
        sff.setVisible(true);
    }//GEN-LAST:event_btnFormatMouseClicked

        public ByteArrayOutputStream exportRTFSong(ByteArrayOutputStream os) throws Exception{
            ByteArrayOutputStream os2 = exportRTFLyrics(os);
            ByteArrayOutputStream os3 = exportRTFChords(os2);
            return os3;
        }
        public ByteArrayOutputStream exportRTFLyrics(ByteArrayOutputStream os) throws Exception{
              //writes the lyrics and its chords
              ByteArrayOutputStream osOut = new ByteArrayOutputStream();
              if(os.toByteArray().length>0){
                osOut.write(os.toByteArray(),0,os.toByteArray().length-2);
              }

              javax.swing.text.Document doc = this.editorSong.getDocument();
              int length = doc.getLength();
              doc.getDefaultRootElement().getElement(0);

              this.editorSong.getEditorKit().write(osOut, doc, 0, length);

              if(os.toByteArray().length>0){
                  osOut.write("\n}".getBytes());
              }
              
              osOut.close();
              
              return osOut;
        }

        public ByteArrayOutputStream exportRTFChords(ByteArrayOutputStream os) throws IOException{

              ByteArrayOutputStream osOut = new ByteArrayOutputStream();
              osOut.write(os.toByteArray(),0,os.toByteArray().length-2);
              
              javax.swing.text.Document docChords = this.editorSongChords.getDocument();
              int chordsLength = docChords.getLength();
 
                ChordsDB chordsDB = objectManager.getChordsManagerPanel().getChordsDB();
                for(int i=0; i<chordsName.size();i++){
                    Chord chord = chordsDB.getChordByName(chordsName.get(i));
                    if(chord!=null){
                        int shapeSize = 1;
                        if(songTemplate.getChordShapeSize().equals("Small")){
                            shapeSize = 3;
                        }else if(songTemplate.getChordShapeSize().equals("Medium")){
                            shapeSize = 2;
                        }
                        ChordShapePanel csp = new ChordShapePanel(shapeSize, chord.getName(),chord.getShape());

                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        BufferedImage bi= csp.createImage();

                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
                        encoder.encode(bi);

                        byte[] ba=baos.toByteArray();

                        int len=ba.length,j;
                        StringBuffer sb=new StringBuffer(len*2);
                        for (j=0;j<len;j++) {
                            String sByte=Integer.toHexString((int)(ba[j] & 0xFF));
                            if (sByte.length()!=2)
                                sb.append('0'+sByte);
                            else
                                sb.append(sByte);
                        }
                        String s="{\\pict\\jpegblip " + sb.toString()+"}";

                        osOut.write(s.getBytes());
                    }else{
                    }

                }
                osOut.write("\n}".getBytes());
                osOut.close();
                
                return osOut;
        }
    
    private void btnExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportMouseClicked
        JFileChooser fc = new JFileChooser();
        if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            try {

                ByteArrayOutputStream osOut = exportRTFSong(new ByteArrayOutputStream());    
                String filePath = fc.getSelectedFile().getPath();
                if(!filePath.contains(".rtf"))
                    filePath = filePath + ".rtf";
                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(osOut.toByteArray());
                fos.close();

            } catch (Exception ex) {
            }
        }

    }//GEN-LAST:event_btnExportMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnFormat;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox comboKey;
    private javax.swing.JComboBox comboVersion;
    private javax.swing.JEditorPane editorSong;
    private javax.swing.JEditorPane editorSongChords;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelKey;
    private javax.swing.JLabel labelVersion;
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