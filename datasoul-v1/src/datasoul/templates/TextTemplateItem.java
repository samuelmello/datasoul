/*
 * TextTemplateItem.java
 *
 * Created on December 24, 2005, 1:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author samuelm
 */
public class TextTemplateItem extends TemplateItem {
    
    private String text;
    private String fontName;
    private float fontSize;
    private float fontOutline;
    private Color fontColor;
    private Color outlineColor;
    private String alingment;
    private String vertAlign;
    private String textWidth;
    private String underline;
    
    
        
    private AttributedString atribStr;
    
    private DefaultCellEditor alignmentEditor;
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("Text");
        properties.add("FontName");
        properties.add("FontSize");
        properties.add("FontOutline");
        properties.add("FontColor");
        properties.add("OutlineColor");
        properties.add("Alignment");
        properties.add("VerticalAlignment");
        properties.add("TextWidth");
        properties.add("Underline");

    }


    public TextTemplateItem () {
        
        super();
        
        this.setWidth(100);
        this.setHeight(100);
        this.setText("TextItem");
        this.setFontSize(16f);
        this.setAlignment("Left");
        this.setVerticalAlignment("Top");
        this.setTextWidth("Regular");
        this.setUnderline("Off");
        this.setFontName("Serif");
        this.setFontColor(Color.BLACK);
        this.setOutlineColor(Color.BLACK);

        JComboBox cb = new JComboBox();
        cb.addItem("Left");
        cb.addItem("Center");
        cb.addItem("Right");
        registerEditorComboBox("Alignment", cb);
        
        cb = new JComboBox();
        cb.addItem("Top");
        cb.addItem("Middle");
        cb.addItem("Bottom");
        registerEditorComboBox("VerticalAlignment", cb);
        
        cb = new JComboBox();
        cb.addItem("Condensed");
        cb.addItem("SemiCondensed");
        cb.addItem("Regular");
        cb.addItem("SemiExtended");
        cb.addItem("Extended");
        registerEditorComboBox("TextWidth", cb);

        cb = new JComboBox();
        cb.addItem("Off");
        cb.addItem("Simple");
        cb.addItem("TwoPixel");
        cb.addItem("Dotted");
        cb.addItem("Dashed");
        registerEditorComboBox("Underline", cb);
        
        String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        cb = new JComboBox();
        for ( int i = 0; i < fontList.length; i++ )
          cb.addItem( fontList[i] );
        registerEditorComboBox("FontName", cb);
        
        registerColorChooser("FontColor");
        registerColorChooser("OutlineColor");
        
        
    }

    public void draw(Graphics2D g) {
        
         AttributedCharacterIterator aci = this.atribStr.getIterator();
         FontRenderContext frc = g.getFontRenderContext();
         LineBreakMeasurer lbm;
         try{
            lbm = new LineBreakMeasurer(aci, frc);
         }catch(Exception e){
             e.printStackTrace();
             return;
         }

         // Compute text font size
         float drawPosY;
         float fontReduction = 0f;
         do {

getFullWord: {
    
             this.atribStr.addAttribute(TextAttribute.SIZE, getFontSize() - fontReduction);
             lbm = new LineBreakMeasurer(aci, frc);
             fontReduction += 1f;
             drawPosY = 0;
             while (lbm.getPosition() < getText().length() ) {
                 //TextLayout layout = lbm.nextLayout(this.getWidth());
                 
                 TextLayout layout = lbm.nextLayout(this.getWidth(), getText().length(), true);
                 
                 if (layout == null){
                     break getFullWord;
                 }
                 
                 drawPosY += layout.getAscent();
                 drawPosY += layout.getDescent(); 
                 drawPosY += layout.getLeading();
             }
}

         }while ( (lbm.getPosition() < getText().length()) || ( drawPosY > this.getHeight() && fontReduction < getFontSize()));
        
         // do the paiting
         aci = this.atribStr.getIterator();
         lbm.setPosition(0);
         if ( vertAlign.equals("Middle") ){
             drawPosY = (this.getHeight() - drawPosY) / 2;
         } else if (vertAlign.equals("Bottom")){
             drawPosY = this.getHeight() - drawPosY;
         } else {
             drawPosY = 0;
         }
         
         Color oldColor = g.getColor();
         Stroke oldStroke = g.getStroke();
         Composite oldComposite = g.getComposite();
         try{
             g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
             g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
             g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()) );
             while (lbm.getPosition() < getText().length() ) {
                 
                 TextLayout layout = lbm.nextLayout(this.getWidth(), getText().length(), true );
                 drawPosY += layout.getAscent();

                 // default alignment = Left
                 float drawPosX = this.getLeft();
                 
                 if (getAlignment().equals("Right"))
                     drawPosX = this.getLeft() + ( this.getWidth() - layout.getAdvance() ) ;
                 
                 if (getAlignment().equals("Center"))
                     drawPosX = this.getLeft() + ( this.getWidth() - layout.getAdvance() )/2 ;
                 
                 // draw the text
                 g.setStroke( oldStroke );
                 
                 g.setColor(fontColor);
                 
                 layout.draw(g, drawPosX, drawPosY + this.getTop());
                 
                 // draw the outline
                 if (this.getFontOutline() > 0.01f){
                     g.setStroke ( new BasicStroke(this.getFontOutline()) );
                     Shape shp = layout.getOutline( AffineTransform.getTranslateInstance(drawPosX, drawPosY + this.getTop()) ); 
                     g.setColor(outlineColor);
                     g.draw(shp);
                 }

                 drawPosY += layout.getDescent(); 
                 drawPosY += layout.getLeading();
             }
         }finally{
             g.setColor(oldColor);
             g.setStroke(oldStroke);
             g.setComposite(oldComposite);
         }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        atribStr = new AttributedString(text);
        firePropChanged("Text");
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        //atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        firePropChanged("FontName");
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        atribStr.addAttribute(TextAttribute.SIZE, this.getFontSize());
        firePropChanged("FontSize");
    }

    public void setFontSize(String fontSize) {
        this.setFontSize(Float.parseFloat(fontSize));
    }

    
    public float getFontOutline() {
        return fontOutline;
    }

    public void setFontOutline(float fontOutline) {
        this.fontOutline = fontOutline;
        firePropChanged("FontOutline");
    }

    public void setFontOutline(String fontOutline) {
        this.setFontOutline(Float.parseFloat(fontOutline));
    }


    public String getFontColor() {
        Color color = fontColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }
   
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        firePropChanged("FontColor");
    }

    public void setFontColor(String fontColor) {
        this.setFontColor(stringToColor(fontColor));
    }


    public String getOutlineColor() {
        Color color = outlineColor;
        String hexcolor = (color.getRed()>0x09) ? Integer.toHexString(color.getRed()) : Integer.toHexString(color.getRed())+"0";
        hexcolor = hexcolor + ((color.getGreen()>0x09) ? Integer.toHexString(color.getGreen()) : Integer.toHexString(color.getGreen())+"0");
        hexcolor = hexcolor + ((color.getBlue()>0x09)?Integer.toHexString(color.getBlue()) : Integer.toHexString(color.getBlue())+"0");
        return hexcolor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        firePropChanged("OutlineColor");
    }

    public void setOutlineColor(String outlineColor) {
        this.setOutlineColor(stringToColor(outlineColor));
    }
    
    public String getAlignment(){
        return this.alingment;
    }
    
    public void setAlignment(String alignment){
        if ( alignment.equals("Left") || alignment.equals("Right") || alignment.equals("Center")){
            this.alingment = alignment;
            firePropChanged("Alignment");
        }else{
            System.out.println("Align: "+alignment);
        }
    }
    
    public String getVerticalAlignment(){
        return this.vertAlign;
    }
    
    public void setVerticalAlignment(String vertAlign){
        if ( vertAlign.equals("Top") || vertAlign.equals("Middle") || vertAlign.equals("Bottom")){
            this.vertAlign = vertAlign;
            firePropChanged("VerticalAlignment");
        }
    }

    
    public String getTextWidth(){
        return this.textWidth;
    }
    
    public void setTextWidth(String width){
        
        /*
         *  WIDTH_CONDENSED = 0.75,
            WIDTH_SEMI_CONDENSED = 0.875,
            WIDTH_REGULAR = 1.0,
            WIDTH_SEMI_EXTENDED = 1.25,
            WIDTH_EXTENDED = 1.5
         */
        
        if ( width.equals("Condensed")) {
            this.textWidth = width;
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
            firePropChanged("TextWidth");
        }
        
        if ( width.equals("SemiCondensed")) {
            this.textWidth = width;
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_CONDENSED);
            firePropChanged("TextWidth");
        }
        
        if ( width.equals("Regular")) {
            this.textWidth = width;
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_REGULAR);
            firePropChanged("TextWidth");
        }

        if ( width.equals("SemiExtended")) {
            this.textWidth = width;
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_EXTENDED);
            firePropChanged("TextWidth");
        }

        if ( width.equals("Extended")) {
            this.textWidth = width;
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_EXTENDED);
            firePropChanged("TextWidth");
        }
        
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

    public String getUnderline(){
        return this.underline;
                
    }
    
    public void setUnderline(String under){
        /*
         UNDERLINE_LOW_ONE_PIXEL, 
         UNDERLINE_LOW_TWO_PIXEL, 
         UNDERLINE_LOW_DOTTED, 
         UNDERLINE_LOW_GRAY, 
         UNDERLINE_LOW_DASHED
         */

        if ( under.equals("Off")) {
            this.underline = under;
            atribStr.addAttribute(TextAttribute.UNDERLINE, null);
            firePropChanged("Underline");
        }
        
        if ( under.equals("Simple")) {
            this.underline = under;
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            firePropChanged("Underline");
        }

        if ( under.equals("Dotted")) {
            this.underline = under;
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
            firePropChanged("Underline");
        }

        if ( under.equals("TwoPixel")) {
            this.underline = under;
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL);
            firePropChanged("Underline");
        }

        if ( under.equals("Dashed")) {
            this.underline = under;
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
            firePropChanged("Underline");
        }
        
        
    }
    
}
