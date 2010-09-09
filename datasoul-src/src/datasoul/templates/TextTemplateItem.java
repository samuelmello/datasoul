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
 * TextTemplateItem.java
 *
 * Created on December 24, 2005, 1:51 PM
 *
 */

package datasoul.templates;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
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
    private String formatedString;
    private String fontName;
    private float fontSize;
    private float fontOutline;
    private Color fontColor;
    private Color outlineColor;
    private int alingment;
    private int vertAlign;
    private int textWidth;
    private int textWeight;
    private int underline;
    private int content;
    private int uppercase;
        
    private AttributedString atribStr;
    
    private DefaultCellEditor alignmentEditor;
    
    @Override
    protected void registerProperties(){
        super.registerProperties();
        properties.add("ContentIdx");
        registerDisplayString("ContentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CONTENT"));
        properties.add("Text");
        registerDisplayString("Text", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEXT"));
        properties.add("FontName");
        registerDisplayString("FontName", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FONT NAME"));
        properties.add("FontSize");
        registerDisplayString("FontSize", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FONT SIZE"));
        properties.add("FontOutline");
        registerDisplayString("FontOutline", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FONT OUTLINE"));
        properties.add("FontColor");
        registerDisplayString("FontColor", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FONT COLOR"));
        properties.add("OutlineColor");
        registerDisplayString("OutlineColor", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("OUTLINE COLOR"));
        properties.add("AlignmentIdx");
        registerDisplayString("AlignmentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ALIGNMENT"));
        properties.add("VerticalAlignmentIdx");
        registerDisplayString("VerticalAlignmentIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VERTICAL ALIGNMENT"));
        properties.add("TextWidthIdx");
        registerDisplayString("TextWidthIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEXT WIDTH"));
        properties.add("TextWeightIdx");
        registerDisplayString("TextWeightIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEXT WEIGHT"));
        properties.add("UnderlineIdx");
        registerDisplayString("UnderlineIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNDERLINE"));
        properties.add("UppercaseIdx");
        registerDisplayString("UppercaseIdx", java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UPPERCASE"));
        
    }

    private static JComboBox cbAlignment;
    private static JComboBox cbContent;
    private static JComboBox cbVerticalAlignment;
    private static JComboBox cbTextWidth;
    private static JComboBox cbTextWeight;
    private static JComboBox cbUnderline;
    private static JComboBox cbFontName;
    private static JComboBox cbUppercase;
    
    public static final int CONTENT_TITLE = 0;// "Title";
    public static final int CONTENT_SLIDE = 1;// "Slide";
    public static final int CONTENT_NEXTSLIDE = 2; //"Next Slide";
    public static final int CONTENT_CLOCK = 3;//"Clock";
    public static final int CONTENT_TIMER = 4;//"Timer";
    public static final int CONTENT_STATIC = 5;//"Static";
    public static final int CONTENT_ALERT = 6;//"Alert";
    public static final int CONTENT_SONGAUTHOR = 7;//"Author";
    public static final int CONTENT_COPYRIGHT = 8;//"Copyright";
    public static final int CONTENT_SONGSOURCE = 9;//"Source";
    public static final String[] CONTENT_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TITLE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SLIDE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NEXT SLIDE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CLOCK"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TIMER"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("STATIC"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ALERT"),java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SONG AUTHOR"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("COPYRIGHT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SOURCE")};
    
    public static final int ALIGN_LEFT = 0; //"Left";
    public static final int ALIGN_CENTER = 1;//"Center";
    public static final int ALIGN_RIGHT = 2; //"Right";
    public static final String[] ALIGN_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("LEFT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CENTER"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("RIGHT")};

    public static final int VALIGN_TOP = 0; //"Top";
    public static final int VALIGN_MIDDLE = 1; //"Middle";
    public static final int VALIGN_BOTTOM = 2; //"Bottom";
    public static final String[] VALIGN_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TOP"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("MIDDLE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("BOTTOM")};

    public static final int TEXTWIDTH_CONDENSED = 0;
    public static final int TEXTWIDTH_SEMICONDENSED = 1;
    public static final int TEXTWIDTH_REGULAR = 2;
    public static final int TEXTWIDTH_SEMIEXTENDED = 3;
    public static final int TEXTWIDTH_EXTENDED = 4;
    public static final String[] TEXTWIDTH_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CONDENSED"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SEMI CONDENSED"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("REGULAR"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SEMI EXTENDED"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("EXTENDED")};
    
    public static final int TEXTWEIGHT_REGULAR = 0;
    public static final int TEXTWEIGHT_EXTRALIGHT = 1;
    public static final int TEXTWEIGHT_LIGHT = 2;
    public static final int TEXTWEIGHT_SEMIBOLD = 3;
    public static final int TEXTWEIGHT_MEDIUM = 4;
    public static final int TEXTWEIGHT_BOLD = 5;
    public static final int TEXTWEIGHT_EXTRABOLD = 6;
    public static final String[] TEXTWEIGHT_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("REGULAR"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("EXTRA LIGHT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("LIGHT"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SEMI BOLD"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("MEDIUM"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("BOLD"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("EXTRA BOLD")};

    public static final int UNDERLINE_OFF = 0;
    public static final int UNDERLINE_SIMPLE = 1;
    public static final int UNDERLINE_TWOPIXEL = 2;
    public static final int UNDERLINE_DOTTED = 3;
    public static final int UNDERLINE_DASHED = 4;
    public static final String[] UNDERLINE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("OFF"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SIMPLE"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TWOPIXEL"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DOTTED"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DASHED")};
    
    public static final int UPPERCASE_NO = 0;
    public static final int UPPERCASE_YES = 1;
    public static final String[] UPPERCASE_TABLE = {java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NO"), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("YES")};
    
    public static final String DEFAULT_TEXT = java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEXTITEM");

    public TextTemplateItem () {
        
        super();
        
        int i;
        
        this.setWidth(100);
        this.setHeight(100);
        this.setContentIdx(CONTENT_STATIC);
        this.setText(DEFAULT_TEXT);
        this.setFontSize(16f);
        this.setAlignmentIdx(ALIGN_LEFT);
        this.setVerticalAlignmentIdx(VALIGN_TOP);
        this.setTextWidthIdx(TEXTWIDTH_REGULAR);
        this.setTextWeightIdx(TEXTWEIGHT_REGULAR);
        this.setUnderlineIdx(UNDERLINE_OFF);
        this.setFontName("Serif");
        this.setFontColor(Color.BLACK);
        this.setOutlineColor(Color.BLACK);
        this.setUppercaseIdx(UPPERCASE_NO);

    }

    @Override
    public void setUpEdit(){
        int i;

        super.setUpEdit();

        if (cbContent == null){
            cbContent = new JComboBox();
            for (i=0; i<CONTENT_TABLE.length; i++)
                cbContent.addItem(CONTENT_TABLE[i]);
        }
        registerEditorComboBox("ContentIdx", cbContent);

        if (cbAlignment == null){
            cbAlignment = new JComboBox();
            for (i=0; i<ALIGN_TABLE.length; i++){
                cbAlignment.addItem(ALIGN_TABLE[i]);
            }
        }
        registerEditorComboBox("AlignmentIdx", cbAlignment);

        if (cbVerticalAlignment == null) {
            cbVerticalAlignment = new JComboBox();
            for (i=0; i<VALIGN_TABLE.length; i++){
                cbVerticalAlignment.addItem(VALIGN_TABLE[i]);
            }

        }
        registerEditorComboBox("VerticalAlignmentIdx", cbVerticalAlignment);

        if (cbTextWidth == null){
            cbTextWidth = new JComboBox();
            for (i=0; i<TEXTWIDTH_TABLE.length; i++){
                cbTextWidth.addItem(TEXTWIDTH_TABLE[i]);
            }
        }
        registerEditorComboBox("TextWidthIdx", cbTextWidth);

        if (cbTextWeight == null){
            cbTextWeight = new JComboBox();
            for (i=0; i<TEXTWEIGHT_TABLE.length; i++)
                cbTextWeight.addItem(TEXTWEIGHT_TABLE[i]);
        }
        registerEditorComboBox("TextWeightIdx", cbTextWeight);


        if (cbUnderline == null){
            cbUnderline = new JComboBox();
            for (i=0; i<UNDERLINE_TABLE.length; i++)
                cbUnderline.addItem(UNDERLINE_TABLE[i]);
        }
        registerEditorComboBox("UnderlineIdx", cbUnderline);

        if (cbUppercase == null){
            cbUppercase = new JComboBox();
            for (i=0; i<UPPERCASE_TABLE.length; i++)
                cbUppercase.addItem(UPPERCASE_TABLE[i]);
        }
        registerEditorComboBox("UppercaseIdx", cbUppercase);



        if (cbFontName == null){
            String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            cbFontName = new JComboBox();
            for ( i = 0; i < fontList.length; i++ )
              cbFontName.addItem( fontList[i] );
        }
        registerEditorComboBox("FontName", cbFontName);

        registerColorChooser("FontColor");
        registerColorChooser("OutlineColor");

    }
    
    public void assign(TextTemplateItem from){
        super.assign(from);
        this.setContentIdx(from.getContentIdx());
        this.setText(from.getText());
        this.setFontSize(from.getFontSize());
        this.setAlignmentIdx(from.getAlignmentIdx());
        this.setVerticalAlignmentIdx(from.getVerticalAlignmentIdx());
        this.setTextWidthIdx(from.getTextWidthIdx());
        this.setTextWeightIdx(from.getTextWeightIdx());
        this.setUnderlineIdx(from.getUnderlineIdx());
        this.setFontName(from.getFontName());
        this.setFontColor(from.getFontColor());
        this.setOutlineColor(from.getOutlineColor());
        this.setFontOutline(from.getFontOutline());
        this.setUppercaseIdx(from.getUppercaseIdx());
    }
    

    @Override
    public void draw(Graphics2D g, float time) {
        
         if (formatedString == null || formatedString.length() == 0){
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
             while (lbm.getPosition() < formatedString.length() ) {
                 //TextLayout layout = lbm.nextLayout(this.getWidth());
                 
                 limit = lbm.getPosition() + formatedString.substring( lbm.getPosition() ).indexOf('\n')+1;
                 if (limit <= lbm.getPosition()){
                     limit = formatedString.length();
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

         }while ( (lbm.getPosition() < formatedString.length()) || ( drawPosY > this.getHeight() && fontReduction < getFontSize()));
        
         // do the paiting
         aci = this.atribStr.getIterator();
         lbm.setPosition(0);
         if ( vertAlign == VALIGN_MIDDLE ){
             drawPosY = (this.getHeight() - drawPosY) / 2;
         } else if (vertAlign == VALIGN_BOTTOM){
             drawPosY = this.getHeight() - drawPosY;
         } else {
             drawPosY = 0;
         }
         
         Color oldColor = g.getColor();
         Composite oldComposite = g.getComposite();
         try{
             g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
             g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
             g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha() * time ) );
             while (lbm.getPosition() < formatedString.length() ) {

                 limit = lbm.getPosition() + formatedString.substring( lbm.getPosition() ).indexOf('\n')+1;
                 if (limit <= lbm.getPosition()){
                     limit = formatedString.length();
                 }
                 TextLayout layout = lbm.nextLayout(this.getWidth(), limit, true);

                 drawPosY += layout.getAscent();

                 // default alignment = Left
                 float drawPosX = this.getLeft();
                 
                 if ( alingment == ALIGN_RIGHT)
                     drawPosX = this.getLeft() + ( this.getWidth() - layout.getAdvance() ) ;
                 
                 if ( alingment == ALIGN_CENTER)
                     drawPosX = this.getLeft() + ( this.getWidth() - layout.getAdvance() )/2 ;
                 
                 // draw the outline
                 if (this.getFontOutline() > 0.1f){
                     g.setColor(outlineColor);
                     layout.draw(g, drawPosX + this.getFontOutline(), drawPosY + this.getTop());
                     layout.draw(g, drawPosX - this.getFontOutline(), drawPosY + this.getTop());
                     layout.draw(g, drawPosX, drawPosY + this.getTop() + this.getFontOutline());
                     layout.draw(g, drawPosX, drawPosY + this.getTop() - this.getFontOutline());
                     layout.draw(g, drawPosX + this.getFontOutline(), drawPosY + this.getTop() + this.getFontOutline());
                     layout.draw(g, drawPosX + this.getFontOutline(), drawPosY + this.getTop() - this.getFontOutline());
                     layout.draw(g, drawPosX - this.getFontOutline(), drawPosY + this.getTop() + this.getFontOutline());
                     layout.draw(g, drawPosX - this.getFontOutline(), drawPosY + this.getTop() - this.getFontOutline());
                 }

                 // draw the text
                 g.setColor(fontColor);
                 layout.draw(g, drawPosX, drawPosY + this.getTop());
                 
                 drawPosY += layout.getDescent(); 
                 drawPosY += layout.getLeading();
             }
         }finally{
             g.setColor(oldColor);
             g.setComposite(oldComposite);
         }

    }

    private void updateAttributes(){

        if (text == null || text.length() == 0) {
            return;
        }

        String tmpText;
        
        // uppercase?
        if (uppercase == UPPERCASE_YES){
            tmpText = text.toUpperCase();
        }else{
            tmpText = text;
        }
        
        // The string contain something to be displayed in italic?
        
        if (tmpText.contains("[") && tmpText.contains("]")) {
            int a = tmpText.indexOf("[");
            int b = tmpText.lastIndexOf("]");
            formatedString = tmpText.substring(0, a) + tmpText.substring(a+1, b) + tmpText.substring(b+1);
            atribStr = new AttributedString(formatedString);
            atribStr.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, a, b-1);
        }else{
            formatedString = tmpText;
            atribStr = new AttributedString(formatedString);
        }        
        
        
        if (fontName != null){
            atribStr.addAttribute( TextAttribute.FAMILY, fontName );
        }
        atribStr.addAttribute(TextAttribute.SIZE, this.getFontSize());
        
        switch (textWidth){
            
            case TEXTWIDTH_CONDENSED:
                atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
                break;
                
            case TEXTWIDTH_SEMICONDENSED:
                atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_CONDENSED);
                break;
            
            case TEXTWIDTH_REGULAR:
                atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_REGULAR);
                break;
            
            case TEXTWIDTH_SEMIEXTENDED:
                atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_EXTENDED);
                break;
            
            case TEXTWIDTH_EXTENDED:
                atribStr.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_EXTENDED);
                break;
        }

        switch (textWeight){
            case TEXTWEIGHT_EXTRALIGHT:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);
                break;
            case TEXTWEIGHT_LIGHT:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
                break;
            case TEXTWEIGHT_SEMIBOLD:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_DEMIBOLD);
                break;
            case TEXTWEIGHT_MEDIUM:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
                break;
            case TEXTWEIGHT_BOLD:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                break;
            case TEXTWEIGHT_EXTRABOLD:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
                break;
            case TEXTWEIGHT_REGULAR:
                atribStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                break;
        }
        
        switch(underline){
            case UNDERLINE_SIMPLE:
                atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
                break;
            case UNDERLINE_TWOPIXEL:
                atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL);
                break;
            case UNDERLINE_DOTTED:
                atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
                break;
            case UNDERLINE_DASHED:
                atribStr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
                break;
        }

    }
    
    public String getText() {
        return text;
    }

    public synchronized void setText(String text) {
        if (text == null || text.equals("")){
            this.text = " ";
        }else{
            this.text = text;
        }
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
        return ALIGN_TABLE[this.alingment];
    }

    public int getAlignmentIdx(){
        return this.alingment;
    }

    public void setAlignmentIdx(String s){
        setAlignmentIdx(Integer.parseInt(s));
    }
    
    public void setAlignmentIdx(int a){
        this.alingment = a;
        firePropChanged("AlignmentIdx");
    }
    
    public void setAlignment(String alignment){
        
        for (int j=0; j<ALIGN_TABLE.length; j++){
            if (alignment.equalsIgnoreCase(ALIGN_TABLE[j])){
                setAlignmentIdx(j);
            }
        }
        
    }
    
    public String getVerticalAlignment(){
        return VALIGN_TABLE[this.vertAlign];
    }

    public int getVerticalAlignmentIdx(){
        return this.vertAlign;
    }
    
    public void setVerticalAlignment(String vertAlign){
        for (int j=0; j<ALIGN_TABLE.length; j++){
            if (vertAlign.equalsIgnoreCase(VALIGN_TABLE[j])){
                setVerticalAlignmentIdx(j);
            }
        }
    }
    
    public void setVerticalAlignmentIdx(int x){
        this.vertAlign = x;
        firePropChanged("VerticalAlignmentIdx");
    }
     
    public void setVerticalAlignmentIdx(String x){
        setVerticalAlignmentIdx(Integer.parseInt(x));
    }

    
    public int getTextWidthIdx(){
        return this.textWidth;
    }
    
    public String getTextWidth(){
        return TEXTWIDTH_TABLE[this.textWidth];
    }

    public void setTextWidthIdx(int width){
        this.textWidth = width;
        updateAttributes();
        firePropChanged("TextWidthIdx");
    }
    
    public void setTextWidthIdx(String width){
        setTextWidthIdx(Integer.parseInt(width));
    }
    
    public void setTextWidth(String str){
        for (int i=0; i<TEXTWIDTH_TABLE.length; i++){
            if (str.equalsIgnoreCase(TEXTWIDTH_TABLE[i])){
                setTextWidthIdx(i);
            }
        }
    }
    
    
    public String getTextWeight(){
        return TEXTWEIGHT_TABLE[this.textWeight];
    }

    public int getTextWeightIdx(){
        return this.textWeight;
    }
    
    public void setTextWeightIdx(String weight){
        setTextWeightIdx(Integer.parseInt(weight));
    }

    public void setTextWeightIdx(int weight){
        this.textWeight = weight;
        updateAttributes();
        firePropChanged("TextWeightIdx");
    }
            
    public void setTextWeight(String str){
        for (int j=0; j<TEXTWEIGHT_TABLE.length; j++){
            if (str.equalsIgnoreCase(TEXTWEIGHT_TABLE[j])){
                setTextWeightIdx(j);
            }
        }
    }
    
    public String getUnderline(){
        return UNDERLINE_TABLE[this.underline];
    }

    public int getUnderlineIdx(){
        return this.underline;
    }
    
    public void setUnderlineIdx(String under){
        setUnderlineIdx(Integer.parseInt(under));
    }

    public void setUnderlineIdx(int under){
        this.underline = under;
        updateAttributes();
        firePropChanged("UnderlineIdx");
    }
    
    public void setUnderline(String str){
        for (int j=0; j<UNDERLINE_TABLE.length; j++){
            if (str.equalsIgnoreCase(UNDERLINE_TABLE[j])){
                setUnderlineIdx(j);
            }
        }
    }    
    
    public int getContentIdx(){
        return content;
    }
    
    public void setContentIdx(String content){
        setContentIdx(Integer.parseInt(content));
    }
    
    public void setContentIdx(int content){
        this.content = content;
        firePropChanged("ContentIdx");
    }
    
    public String getContent(){
        return CONTENT_TABLE[this.content];
    }
    
    public void setContent(String content){
        for (int j=0; j<CONTENT_TABLE.length; j++){
            if (content.equalsIgnoreCase(CONTENT_TABLE[j])){
                setContentIdx(j);
            }
        }
    }
    
    public String getUppercase(){
        return UPPERCASE_TABLE[uppercase];
    }
    
    public int getUppercaseIdx(){
        return uppercase;
    }
    
    public void setUppercaseIdx(int x){
        this.uppercase = x;
        updateAttributes();
        firePropChanged("UppercaseIdx");
    }
    
    public void setUppercaseIdx(String s){
        this.setUppercaseIdx(Integer.parseInt(s));
    }
    
    public void setUppercase(String s){
        for (int j=0; j<UPPERCASE_TABLE.length; j++){
            if (s.equalsIgnoreCase(UPPERCASE_TABLE[j])){
                setUppercaseIdx(j);
            }
        }
    }
}


