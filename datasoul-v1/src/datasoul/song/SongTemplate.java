/*
 * SongTemplate.java
 *
 * Created on 29 de Janeiro de 2006, 21:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.templates.TemplateItem;
import datasoul.util.AttributedObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;

/**
 *
 * @author Administrador
 */
public class SongTemplate extends TemplateItem{

    private String titleFontName;
    private Color titleFontColor;
    private int titleFontSize;    
    private String authorFontName;
    private Color authorFontColor;
    private int authorFontSize;
    private String chordsFontName;
    private Color chordsFontColor;
    private int chordsFontSize;
    private String lyricsFontName;    
    private Color lyricsFontColor;    
    private int lyricsFontSize;    

    
    /**
     * Creates a new instance of SongTemplate
     */
    public SongTemplate() {
        super();

        this.setTitleFontName("Arial");
        this.setTitleFontColor(Color.BLACK);
        this.setTitleFontSize(20);
        this.setAuthorFontName("Arial");
        this.setAuthorFontColor(Color.BLACK);
        this.setAuthorFontSize(10);
        this.setChordsFontName("Arial");
        this.setChordsFontColor(Color.BLACK);
        this.setChordsFontSize(12);
        this.setLyricsFontName("Arial");
        this.setLyricsFontColor(Color.BLACK);
        this.setLyricsFontSize(12);

        String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        JComboBox cb1 = new JComboBox();
        cb1 = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb1.addItem( fontList[i] );

        JComboBox cb2 = new JComboBox();
        cb2 = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb2.addItem( fontList[i] );

        JComboBox cb3 = new JComboBox();
        cb3 = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb3.addItem( fontList[i] );

        JComboBox cb4 = new JComboBox();
        cb4 = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb4.addItem( fontList[i] );
        
        registerEditorComboBox("TitleFontName", cb1);
        registerEditorComboBox("AuthorFontName", cb2);
        registerEditorComboBox("ChordsFontName", cb3);
        registerEditorComboBox("LyricsFontName", cb4);
        
        registerColorChooser("TitleFontColor");
        registerColorChooser("AuthorFontColor");
        registerColorChooser("ChordsFontColor");
        registerColorChooser("LyricsFontColor");
    }

    protected void registerProperties() {
//        super.registerProperties();
        properties.add("TitleFontName");
        properties.add("TitleFontColor");
        properties.add("TitleFontSize");        
        properties.add("AuthorFontName");
        properties.add("AuthorFontColor");
        properties.add("AuthorFontSize");
        properties.add("ChordsFontName");
        properties.add("ChordsFontColor");
        properties.add("ChordsFontSize");
        properties.add("LyricsFontName");
        properties.add("LyricsFontColor");
        properties.add("LyricsFontSize");
    }

    public String getTitleFontName() {
        return titleFontName;
    }

    public void setTitleFontName(String titleFontName) {
        this.titleFontName = titleFontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("titleFontName");
    }

    public String getAuthorFontName() {
        return authorFontName;
    }

    public void setAuthorFontName(String authorFontName) {
        this.authorFontName = authorFontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("authorFontName");
    }
    public String getChordsFontName() {
        return chordsFontName;
    }

    public void setChordsFontName(String chordsFontName) {
        this.chordsFontName = chordsFontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("chordsFontName");
    }
    public String getLyricsFontName() {
        return lyricsFontName;
    }

    public void setLyricsFontName(String lyricsFontName) {
        this.lyricsFontName = lyricsFontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("lyricsFontName");
    }

    public String getTitleFontColor() {
        Color color = titleFontColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }
   
    public void setTitleFontColor(Color fontColor) {
        this.titleFontColor = fontColor;
        firePropChanged("TitleFontColor");
    }

    public void setTitleFontColor(String fontColor) {
        this.setTitleFontColor(stringToColor(fontColor));
    }
    
    public String getAuthorFontColor() {
        Color color = authorFontColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }
   
    public void setAuthorFontColor(Color fontColor) {
        this.authorFontColor = fontColor;
        firePropChanged("AuthorFontColor");
    }

    public void setAuthorFontColor(String fontColor) {
        this.setAuthorFontColor(stringToColor(fontColor));
    }
    
    public String getChordsFontColor() {
        Color color = chordsFontColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }
   
    public void setChordsFontColor(Color fontColor) {
        this.chordsFontColor = fontColor;
        firePropChanged("ChordsFontColor");
    }
    
    public void setChordsFontColor(String fontColor) {
        this.setChordsFontColor(stringToColor(fontColor));
    }
    
    public String getLyricsFontColor() {
        Color color = lyricsFontColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }
   
    public void setLyricsFontColor(Color fontColor) {
        this.lyricsFontColor = fontColor;
        firePropChanged("LyricsFontColor");
    }

    public void setLyricsFontColor(String fontColor) {
        this.setLyricsFontColor(stringToColor(fontColor));
    }
    
    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int fontSize) {
        this.titleFontSize = fontSize;
        ///atribStr.addAttribute(TextAttribute.SIZE, this.getTitleFontSize());
        firePropChanged("TitleFontSize");
    }

    public void setTitleFontSize(String fontSize) {
        this.setTitleFontSize(Integer.parseInt(fontSize));
    }
    
    public int getAuthorFontSize() {
        return authorFontSize;
    }

    public void setAuthorFontSize(int fontSize) {
        this.authorFontSize = fontSize;
        ///atribStr.addAttribute(TextAttribute.SIZE, this.getAuthorFontSize());
        firePropChanged("AuthorFontSize");
    }

    public void setAuthorFontSize(String fontSize) {
        this.setAuthorFontSize(Integer.parseInt(fontSize));
    }

    public int getChordsFontSize() {
        return chordsFontSize;
    }

    public void setChordsFontSize(int fontSize) {
        this.chordsFontSize = fontSize;
        ///atribStr.addAttribute(TextAttribute.SIZE, this.getChordsFontSize());
        firePropChanged("ChordsFontSize");
    }

    public void setChordsFontSize(String fontSize) {
        this.setChordsFontSize(Integer.parseInt(fontSize));
    }

    public int getLyricsFontSize() {
        return lyricsFontSize;
    }

    public void setLyricsFontSize(int fontSize) {
        this.lyricsFontSize = fontSize;
        ///atribStr.addAttribute(TextAttribute.SIZE, this.getFontSize());
        firePropChanged("LyricsFontSize");
    }

    public void setLyricsFontSize(String fontSize) {
        this.setLyricsFontSize(Integer.parseInt(fontSize));
    }
    
    public void draw(Graphics2D g) {
    }

    private Color stringToColor(String strColor){
        
        try{
            
            String[] str = strColor.split("");
            
            // the split("") produces a first empty element on the array.
            // we desconsider it an use from 1 to 6.
            if (str.length != 7){
                throw new IllegalArgumentException("Invalid Color: "+strColor);
            }
            
            
            int r = Integer.parseInt(str[1]+str[2], 16);
            int g = Integer.parseInt(str[3]+str[4], 16);
            int b = Integer.parseInt(str[5]+str[6], 16);
            
            return new Color(r,g,b);
            
        }catch(Exception e){
            throw new IllegalArgumentException("Invalid Color: "+strColor);
        }
        
    }
    
}
