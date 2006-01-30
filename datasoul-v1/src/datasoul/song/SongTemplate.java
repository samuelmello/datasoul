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
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;

/**
 *
 * @author Administrador
 */
public class SongTemplate extends TemplateItem{

    private String titleFontName;

    
    /**
     * Creates a new instance of SongTemplate
     */
    public SongTemplate() {
        super();

        JComboBox cb = new JComboBox();
        String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        cb = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb.addItem( fontList[i] );
        
        registerEditorComboBox("TitleFontName", cb);
    }

    protected void registerProperties() {
//        super.registerProperties();
        properties.add("TitleFontName");
    }

    public String getTitleFontName() {
        return titleFontName;
    }

    public void setTitleFontName(String titleFontName) {
        this.titleFontName = titleFontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("titleFontName");
    }

    public void draw(Graphics2D g) {
    }

}
