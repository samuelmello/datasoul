/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.servicelist;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import datasoul.DatasoulMainForm;
import datasoul.datashow.ServiceItem;
import datasoul.datashow.ServiceListTable;
import datasoul.datashow.TextServiceItem;
import datasoul.song.Chord;
import datasoul.song.ChordShapePanel;
import datasoul.song.ChordsDB;
import datasoul.song.Song;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author samuel
 */
public class ServiceListExporterDocument {

    private Document document;
    public static final int TYPE_PDF = 0;
    public static final int TYPE_RTF = 1;
    
    public ServiceListExporterDocument(int type, String filename) throws FileNotFoundException, DocumentException{
        document = new Document();
        
        // ensure file do not exist to avoid garbage at the end of the file
        File f = new File(filename);
        if (f.exists()){
            f.delete();
        }
        
        if (type == TYPE_RTF){
            RtfWriter2.getInstance(document, new FileOutputStream(filename));
        }else{
            PdfWriter.getInstance(document, new FileOutputStream(filename));
        }
        document.open();
        document.addCreator("Datasoul "+DatasoulMainForm.getVersion());
        document.addCreationDate();
    }
    
    public void write() {
        document.close();
    }
    
    public void addServicePlan() throws DocumentException{
        
        ServiceListTable slt = ServiceListTable.getActiveInstance();
        
        Paragraph p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Service_Plan"), FontFactory.getFont(FontFactory.HELVETICA, 12));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        
        p = new Paragraph(slt.getTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        
        Table t = new Table(4);
        t.setWidths(new int[]{10,5, 50, 35});
        t.setPadding(2.0f);
        t.setWidth(100.0f);
        
        t.addCell(createHeaderCell(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Time")));
        t.addCell(createHeaderCell(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Min")));
        t.addCell(createHeaderCell(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Title")));
        t.addCell(createHeaderCell(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Notes")));
        
        
        for (int i=0; i<slt.getRowCount(); i++){
            
            ServiceItem si = (ServiceItem) slt.getServiceItem(i);
            Cell c;

            // Start time
            c = new Cell(si.getStartTime());
            c.setMaxLines(1);
            c.setVerticalAlignment(Cell.ALIGN_MIDDLE);
            c.setHorizontalAlignment(Cell.ALIGN_CENTER);
            t.addCell(c);
            
            // Duration
            c = new Cell(Integer.toString(si.getDuration()));
            c.setMaxLines(1);
            c.setVerticalAlignment(Cell.ALIGN_MIDDLE);
            c.setHorizontalAlignment(Cell.ALIGN_RIGHT);
            t.addCell(c);
            
            // Title
            c = new Cell(si.getTitle());
            c.setVerticalAlignment(Cell.ALIGN_MIDDLE);
            t.addCell(c);

            // Notes
            c = new Cell(si.getNotes());
            c.setVerticalAlignment(Cell.ALIGN_MIDDLE);
            t.addCell(c);

        }
        
        document.add(t);
        
        p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Notes"), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        document.add(p);
        
        p = new Paragraph(slt.getNotes(), FontFactory.getFont(FontFactory.HELVETICA));
        p.setIndentationLeft(10.0f);
        document.add(p);

        document.newPage();
        
        
    }
    
    
    private Cell createHeaderCell(String text) throws BadElementException{

        Cell c = new Cell(new Chunk(text,FontFactory.getFont(FontFactory.HELVETICA_BOLD) ));
        c.setMaxLines(1);
        c.setHeader(true);
        c.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        c.setHorizontalAlignment(Cell.ALIGN_CENTER);
        return c;

    }
    
    private void addSongHeader(Song s) throws DocumentException {
        Paragraph p;

        p = new Paragraph(s.getTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        document.add(p);
        
        if (!s.getSongAuthor().trim().equals("")){
            p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Author")+s.getSongAuthor(), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
            document.add(p);
        }
        
        if (!s.getCopyright().trim().equals("")){
            p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Copyright")+s.getCopyright(), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
            document.add(p);
        }

        if (!s.getSongSource().trim().equals("")){
            p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Source")+s.getSongSource(), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
            document.add(p);
        }
        

        
    }
    
    public void addSongLyrics(Song s) throws DocumentException{

        addSongHeader(s);

        Paragraph p;

        p = new Paragraph("("+java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Lyrics")+")", FontFactory.getFont(FontFactory.HELVETICA, 8));
        document.add(p);
        
        String text = s.getText().replace(Song.CHORUS_MARK , "").replace(Song.SLIDE_BREAK, "");

        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        
        p = new Paragraph(text, FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        
        document.newPage();
        
    }
    
    public void addTextItem(TextServiceItem t) throws DocumentException{

        String text = t.getText().replace(Song.CHORUS_MARK , "").replace(Song.SLIDE_BREAK, "");
        
        Paragraph p;
        
        p = new Paragraph(t.getTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        document.add(p);

        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        
        p = new Paragraph(text, FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        
        document.newPage();
    }
    
    public void addSongChordsSimple(Song s) throws DocumentException{

        
        // If we don't have the chords saved, skip it
        if (s.getChordsSimplified().trim().equals(""))
            return;
        
        addSongHeader(s);

        Paragraph p;

        p = new Paragraph("("+java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Chords_Simple")+")", FontFactory.getFont(FontFactory.HELVETICA, 8));
        document.add(p);

        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);

        String text[] = s.getChordsSimplified().replace(Song.CHORUS_MARK , " ").replace(Song.SLIDE_BREAK, " ").split("\n");
        
        addSongChords(text);
        
        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        addChordsShape(s.getChordsUsedSimple());
        document.add(p);

        document.newPage();
        
    }


    public void addSongChordsComplete(Song s) throws DocumentException{

        
        // If we don't have the chords saved, skip it
        if (s.getChordsComplete().trim().equals(""))
            return;
        
        addSongHeader(s);

        Paragraph p;

        p = new Paragraph("("+java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Chords_Complete")+")", FontFactory.getFont(FontFactory.HELVETICA, 8));
        document.add(p);
        
        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);

        String text[] = s.getChordsComplete().replace(Song.CHORUS_MARK , " ").replace(Song.SLIDE_BREAK, " ").split("\n");
        addSongChords(text);

        
        p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA));
        document.add(p);
        addChordsShape(s.getChordsUsedComplete());
        document.add(p);
                
        document.newPage();
        
    }

    private void addChordsShape(ArrayList<String> chordsName) throws DocumentException{
        ChordsDB chordsDB = ChordsDB.getInstance();
        String notCatalogued = "";
        LinkedList<Chunk> images = new LinkedList<Chunk>();

        for(int i=0; i<chordsName.size();i++){
            Chord chord = chordsDB.getChordByName(chordsName.get(i));
            if(chord!=null){
                ChordShapePanel csp = new ChordShapePanel(2, chord.getName(),chord.getShape());
                BufferedImage im = csp.createImage();
                try{
                    File tmp = File.createTempFile("datasoul-img", ".jpg");
                    tmp.deleteOnExit();
                    
                    ImageIO.write(im, "jpeg", tmp);
                    Chunk c = new Chunk(Image.getInstance(tmp.getAbsolutePath()), 0, 0, true);
                    
                    images.add(c);
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Internal_error:_")+e.getMessage());
                }
            }else{
                notCatalogued += chordsName.get(i);
            }
        }
        
        Paragraph p;

        if (!images.isEmpty()){
            p = new Paragraph();
            for (Chunk c : images){
                p.add(c);
            }
            document.add(p);
        }
        
        if (!notCatalogued.equals("") ){
            p = new Paragraph(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("The_following_chords_are_not_cataloged:_")+notCatalogued, FontFactory.getFont(FontFactory.HELVETICA, 8));
            document.add(p);
        }

        
    }
    

    private void addSongChords(String[] text) throws DocumentException{
        Font chordfont  = new Font(Font.COURIER, 12, Font.BOLD, Color.BLUE.darker());
        Font lyricfont  = new Font(Font.COURIER, 12, Font.NORMAL);
        Paragraph p;

                
        for (int i=0; i<text.length; i++){
            if (text[i].startsWith("=")){
                p = new Paragraph(text[i].substring(1), chordfont);
            }else{
                p = new Paragraph(text[i], lyricfont);
            }
            document.add(p);
        }
        
    }
    
}
