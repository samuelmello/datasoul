/*
 * SongViewerPanel.java
 *
 * Created on 14 de Dezembro de 2005, 22:09
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.print.attribute.AttributeSet;
import javax.swing.ImageIcon;
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
    
    /**
     * Creates new form SongViewerPanel
     */
    public SongViewerPanel() {
        initComponents();

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
            doc.insertString(doc.getLength(),song.getSongAuthor()+"\n",authorStyle);

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
        labelTemplate = new javax.swing.JLabel();
        comboTemplate = new javax.swing.JComboBox();
        split1 = new javax.swing.JSplitPane();
        panelSong = new javax.swing.JPanel();
        scroolSong = new javax.swing.JScrollPane();
        editorSong = new javax.swing.JEditorPane();
        panelSongChords = new javax.swing.JPanel();
        scroolSongChords = new javax.swing.JScrollPane();
        editorSongChords = new javax.swing.JEditorPane();
        labelVersion = new javax.swing.JLabel();
        comboVersion = new javax.swing.JComboBox();
        toolBar = new javax.swing.JToolBar();
        btnPrint = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();

        labelTemplate.setFont(new java.awt.Font("Arial", 1, 11));
        labelTemplate.setText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEMPLATE"));

        comboTemplate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        split1.setDividerLocation(375);
        scroolSong.setViewportView(editorSong);

        org.jdesktop.layout.GroupLayout panelSongLayout = new org.jdesktop.layout.GroupLayout(panelSong);
        panelSong.setLayout(panelSongLayout);
        panelSongLayout.setHorizontalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
        );
        panelSongLayout.setVerticalGroup(
            panelSongLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroolSong, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        split1.setLeftComponent(panelSong);

        scroolSongChords.setViewportView(editorSongChords);

        org.jdesktop.layout.GroupLayout panelSongChordsLayout = new org.jdesktop.layout.GroupLayout(panelSongChords);
        panelSongChords.setLayout(panelSongChordsLayout);
        panelSongChordsLayout.setHorizontalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scroolSongChords)
        );
        panelSongChordsLayout.setVerticalGroup(
            panelSongChordsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroolSongChords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        split1.setRightComponent(panelSongChords);

        labelVersion.setFont(new java.awt.Font("Arial", 1, 11));
        labelVersion.setText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VERSION"));

        comboVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/print.gif")));
        btnPrint.setAlignmentY(0.0F);
        toolBar.add(btnPrint);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/generatePraiseDoc.gif")));
        btnExport.setAlignmentY(0.0F);
        toolBar.add(btnExport);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(toolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 67, Short.MAX_VALUE)
                        .add(labelVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(comboVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(17, 17, 17)
                        .add(labelTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(comboTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(labelVersion)
                        .add(comboVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(labelTemplate)
                        .add(comboTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(toolBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(split1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox comboTemplate;
    private javax.swing.JComboBox comboVersion;
    private javax.swing.JEditorPane editorSong;
    private javax.swing.JEditorPane editorSongChords;
    private javax.swing.JLabel labelTemplate;
    private javax.swing.JLabel labelVersion;
    private javax.swing.JPanel panelSong;
    private javax.swing.JPanel panelSongChords;
    private javax.swing.JScrollPane scroolSong;
    private javax.swing.JScrollPane scroolSongChords;
    private javax.swing.JSplitPane split1;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    
}
