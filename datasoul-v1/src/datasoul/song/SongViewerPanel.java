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
import java.awt.image.BufferedImage;
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
import javax.print.attribute.AttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
    
    /**
     * Creates new form SongViewerPanel
     */
    public SongViewerPanel() {
        initComponents();

        songTemplate = new SongTemplate();
        
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
        StyleConstants.setForeground(nameStyle,Color.decode("0x000055"));
        StyleConstants.setBackground(nameStyle,Color.white);
        StyleConstants.setFontFamily(nameStyle,"Arial");
        StyleConstants.setFontSize(nameStyle,20);                

        StyleConstants.setForeground(authorStyle,Color.black);
        StyleConstants.setBackground(authorStyle,Color.white);
        StyleConstants.setFontFamily(authorStyle,"Arial");
        StyleConstants.setFontSize(authorStyle,12);                

        StyleConstants.setForeground(chordsStyle,Color.decode("0x8888cc"));
        StyleConstants.setBackground(chordsStyle,Color.white);
        StyleConstants.setFontFamily(chordsStyle,"Arial");
        StyleConstants.setFontSize(chordsStyle,12);                

        StyleConstants.setForeground(lyricsStyle,Color.black);
        StyleConstants.setBackground(lyricsStyle,Color.white);
        StyleConstants.setFontFamily(lyricsStyle,"Arial");
        StyleConstants.setFontSize(lyricsStyle,12);                
        
    }
    
    public void viewSong(Song song){
        String strSong="";
        String line="";
        String nextline="";
        StringBuffer html = new StringBuffer();
        StringReader sr = null;
        BufferedReader buff= null;
        
        chordsName.clear();
        
        editorSong.setContentType("text/rtf");
        
        javax.swing.text.Document doc = editorSong.getDocument();

        if(comboVersion.getSelectedItem().equals("Complete")){
            strSong = song.getChordsComplete();
        }else{
            strSong = song.getChordsSimplified();
        }
        sr = new StringReader(strSong);
        buff = new BufferedReader(sr);
        
        try {            
            doc.remove(0,doc.getLength());
            
            doc.insertString(doc.getLength(),song.getSongName()+"\n",nameStyle);
            doc.insertString(doc.getLength(),song.getSongAuthor()+"\n\n\n",authorStyle);

            line = buff.readLine();
            while((nextline = buff.readLine())!=null){
                addFormattedSongLine(doc,line,nextline);
                line = nextline;
            }
            addFormattedSongLine(doc,line,"");

            drawChords();            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        for(int i=0;i<chords.length;i++){
            if(!chords[i].equals(""))
            {
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
        Font font = new Font("Arial", Font.PLAIN, 12);
        FontMetrics fontMetrics = getFontMetrics(font);
        spaceSize = fontMetrics.stringWidth(" ");
        
        for(int i=0;i<chords.length;i++){
            if(!chords[i].equals("")){
                if(!chordsName.contains(chords[i]))
                    chordsName.add(chords[i]);
                index = strAux.length();
                if(index<nextline.length()){
                    widthNextLine = fontMetrics.stringWidth(nextline.substring(0,index));                
                    widthNewLine = fontMetrics.stringWidth(newLine);                
                    neededWidth = (widthNextLine - widthNewLine);
                    spacesNedded = Math.round(neededWidth/spaceSize);
                    spaces = "";
                    for(int j=0;j<spacesNedded;j++)
                        spaces = spaces + " ";
                    newLine = newLine + spaces + chords[i];
                }else{
                    newLine = newLine + spaces + chords[i];                    
                }
                strAux += chords[i] + " ";                    
            }else{
                strAux += " ";
                spaces = spaces + " ";
            }
        }
        
        return newLine;        
    }
    
    private void drawChords() throws BadLocationException {

        editorSongChords.setContentType("text/rtf");
        
        javax.swing.text.Document doc = editorSongChords.getDocument();
        
        doc.remove(0,doc.getLength());
        
        ChordsDB chordsDB = objectManager.getChordsManagerPanel().getChordsDB();
        for(int i=0; i<chordsName.size();i++){
            Chord chord = chordsDB.getChordByName(chordsName.get(i));
            if(chord!=null){
                ChordShapePanel csp = new ChordShapePanel(chord.getName(),chord.getShape());
                
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
        labelVersion = new javax.swing.JLabel();
        comboVersion = new javax.swing.JComboBox();

        split1.setDividerLocation(375);
        scroolSong.setViewportView(editorSong);

        org.jdesktop.layout.GroupLayout panelSongLayout = new org.jdesktop.layout.GroupLayout(panelSong);
        panelSong.setLayout(panelSongLayout);
        panelSongLayout.setHorizontalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );
        panelSongLayout.setVerticalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        split1.setLeftComponent(panelSong);

        scroolSongChords.setViewportView(editorSongChords);

        org.jdesktop.layout.GroupLayout panelSongChordsLayout = new org.jdesktop.layout.GroupLayout(panelSongChords);
        panelSongChords.setLayout(panelSongChordsLayout);
        panelSongChordsLayout.setHorizontalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroolSongChords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
        );
        panelSongChordsLayout.setVerticalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongChords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        split1.setRightComponent(panelSongChords);

        jToolBar1.setFloatable(false);
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/print.gif")));
        btnPrint.setText("print");
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

        jToolBar1.add(jSeparator2);

        labelVersion.setFont(new java.awt.Font("Arial", 1, 11));
        labelVersion.setText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VERSION"));
        jToolBar1.add(labelVersion);

        comboVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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
            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFormatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFormatMouseClicked

        SongFormatFrame sff = new SongFormatFrame(songTemplate);
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
                        ChordShapePanel csp = new ChordShapePanel(chord.getName(),chord.getShape());

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
    private javax.swing.JComboBox comboVersion;
    private javax.swing.JEditorPane editorSong;
    private javax.swing.JEditorPane editorSongChords;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelVersion;
    private javax.swing.JPanel panelSong;
    private javax.swing.JPanel panelSongChords;
    private javax.swing.JScrollPane scroolSong;
    private javax.swing.JScrollPane scroolSongChords;
    private javax.swing.JSplitPane split1;
    // End of variables declaration//GEN-END:variables
    
}
