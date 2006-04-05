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
    private String textWeight;
    private String underline;
    private String content;
    
        
    private AttributedString atribStr;
    
    private DefaultCellEditor alignmentEditor;
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("Content");
        properties.add("Text");
        properties.add("FontName");
        properties.add("FontSize");
        properties.add("FontOutline");
        properties.add("FontColor");
        properties.add("OutlineColor");
        properties.add("Alignment");
        properties.add("VerticalAlignment");
        properties.add("TextWidth");
        properties.add("TextWeight");
        properties.add("Underline");

    }

    private static JComboBox cbAlignment;
    private static JComboBox cbContent;
    private static JComboBox cbVerticalAlignment;
    private static JComboBox cbTextWidth;
    private static JComboBox cbTextWeight;
    private static JComboBox cbUnderline;
    private static JComboBox cbFontName;
    
    public static final String CONTENT_TITLE = "Title";
    public static final String CONTENT_SLIDE = "Slide";
    public static final String CONTENT_NEXTSLIDE = "Next Slide";
    public static final String CONTENT_CLOCK = "Clock";
    public static final String CONTENT_TIMER = "Timer";
    public static final String CONTENT_STATIC = "Static";
    public static final String CONTENT_ALERT = "Alert";
    public static final String DEFAULT_TEXT = "TextItem";

    public TextTemplateItem () {
        
        super();
        
        this.setWidth(100);
        this.setHeight(100);
        this.setContent("Static");
        this.setText(DEFAULT_TEXT);
        this.setFontSize(16f);
        this.setAlignment("Left");
        this.setVerticalAlignment("Top");
        this.setTextWidth("Regular");
        this.setTextWeight("Regular");
        this.setUnderline("Off");
        this.setFontName("Serif");
        this.setFontColor(Color.BLACK);
        this.setOutlineColor(Color.BLACK);

        if (cbContent == null){
            cbContent = new JComboBox();
            cbContent.addItem(CONTENT_STATIC);
            cbContent.addItem(CONTENT_SLIDE);
            cbContent.addItem(CONTENT_NEXTSLIDE);
            cbContent.addItem(CONTENT_TITLE);
            cbContent.addItem(CONTENT_CLOCK);
            cbContent.addItem(CONTENT_TIMER);
            cbContent.addItem(CONTENT_ALERT);
        }
        registerEditorComboBox("Content", cbContent);
        
        if (cbAlignment == null){
            cbAlignment = new JComboBox();
            cbAlignment.addItem("Left");
            cbAlignment.addItem("Center");
            cbAlignment.addItem("Right");
        }
        registerEditorComboBox("Alignment", cbAlignment);
        
        if (cbVerticalAlignment == null) {
            cbVerticalAlignment = new JComboBox();
            cbVerticalAlignment.addItem("Top");
            cbVerticalAlignment.addItem("Middle");
            cbVerticalAlignment.addItem("Bottom");
        }
        registerEditorComboBox("VerticalAlignment", cbVerticalAlignment);
        
        if (cbTextWidth == null){
            cbTextWidth = new JComboBox();
            cbTextWidth.addItem("Condensed");
            cbTextWidth.addItem("Semi Condensed");
            cbTextWidth.addItem("Regular");
            cbTextWidth.addItem("Semi Extended");
            cbTextWidth.addItem("Extended");
        }
        registerEditorComboBox("TextWidth", cbTextWidth);

        if (cbTextWeight == null){
            cbTextWeight = new JComboBox();
            cbTextWeight.addItem("Regular");
            cbTextWeight.addItem("Extra Light");
            cbTextWeight.addItem("Light");
            cbTextWeight.addItem("Semi Bold");
            cbTextWeight.addItem("Medium");
            cbTextWeight.addItem("Bold");
            cbTextWeight.addItem("Extra Bold");
        }
        registerEditorComboBox("TextWeight", cbTextWeight);
        
        
        if (cbUnderline == null){
            cbUnderline = new JComboBox();
            cbUnderline.addItem("Off");
            cbUnderline.addItem("Simple");
            cbUnderline.addItem("TwoPixel");
            cbUnderline.addItem("Dotted");
            cbUnderline.addItem("Dashed");
        }
        registerEditorComboBox("Underline", cbUnderline);
        
        if (cbFontName == null){
            String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            cbFontName = new JComboBox();
            for ( int i = 0; i < fontList.length; i++ )
              cbFontName.addItem( fontList[i] );
        }
        registerEditorComboBox("FontName", cbFontName);
        
        registerColorChooser("FontColor");
        registerColorChooser("OutlineColor");
        
        
    }

    public void draw(Graphics2D g) {
        
         if (text == null || text.length() == 0){
             return;
         }
        
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
         int limit;
         do {

getFullWord: {
    
             this.atribStr.addAttribute(TextAttribute.SIZE, getFontSize() - fontReduction);
             lbm = new LineBreakMeasurer(aci, frc);
             fontReduction += 1f;
             drawPosY = 0;
             while (lbm.getPosition() < getText().length() ) {
                 //TextLayout layout = lbm.nextLayout(this.getWidth());
                 
                 limit = lbm.getPosition() + getText().substring( lbm.getPosition() ).indexOf('\n')+1;
                 if (limit <= lbm.getPosition()){
                     limit = getText().length();
                 }
                 TextLayout layout = lbm.nextLayout(this.getWidth(), limit, true);
                 
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

                 limit = lbm.getPosition() + getText().substring( lbm.getPosition() ).indexOf('\n')+1;
                 if (limit <= lbm.getPosition()){
                     limit = getText().length();
                 }
                 TextLayout layout = lbm.nextLayout(this.getWidth(), limit, true);

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

    private void updateAttributes(){

        if (text == null || text.length() == 0) {
            return;
        }
        
        
        atribStr = new AttributedString(text);
        
        
        if (fontName != null){
            atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        }
        atribStr.addAttribute(TextAttribute.SIZE, this.getFontSize());
        
        if ( textWidth == null) {
            // do nothing
        } else if ( textWidth.equals("Condensed")) {
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
        } else if ( textWidth.equals("Semi Condensed")) {
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_CONDENSED);
        } else if ( textWidth.equals("Regular")) {
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_REGULAR);
        } else if ( textWidth.equals("Semi Extended")) {
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_EXTENDED);
        } else if ( textWidth.equals("Extended")) {
            atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_EXTENDED);
        }

        
        if ( textWeight == null) {
            // do nothing
        } else if ( textWeight.equals("Extra Light")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);
        } else if ( textWeight.equals("Light")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
        } else if ( textWeight.equals("Semi Bold")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_DEMIBOLD);
        } else if ( textWeight.equals("Medium")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        } else if ( textWeight.equals("Bold")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        } else if ( textWeight.equals("Extra Bold")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
        } else if ( textWeight.equals("Regular")) {
            atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        }

        
        if ( underline == null ) {
            // do nothing
        } else if ( underline.equals("Off")) {
            //atribStr.addAttribute(TextAttribute.UNDERLINE, null);
        } else if ( underline.equals("Simple")) {
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        } else if ( underline.equals("Dotted")) {
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        } else if ( underline.equals("TwoPixel")) {
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL);
        } else if ( underline.equals("Dashed")) {
            atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
        }

    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateAttributes();
        firePropChanged("Text");
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        updateAttributes();
        firePropChanged("FontName");
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        updateAttributes();
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
        
        this.textWidth = width;
        updateAttributes();
        firePropChanged("TextWidth");

    }
    
    public String getTextWeight(){
        return this.textWeight;
    }

    protected void firePropChanged(String prop) {
    }
    
    public void setTextWeight(String weight){
        this.textWeight = weight;
        updateAttributes();
        firePropChanged("TextWeight");
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

        this.underline = under;
        updateAttributes();
        firePropChanged("Underline");
            
    }
    
    
    public String getContent(){
        return this.content;
    }
    
    public void setContent(String content){
        this.content = content;
    }
}
