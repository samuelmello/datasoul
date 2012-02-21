/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * Song.java
 *
 * Created on 3 de Janeiro de 2006, 20:45
 *
 */

package datasoul.serviceitems.song;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import datasoul.config.DisplayControlConfig;
import datasoul.render.ContentManager;
import datasoul.serviceitems.ServiceItem;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.util.ObjectManager;

/**
 *
 * @author Administrador
 */
public class Song extends TextServiceItem implements Cloneable {

    private static final String SINGLE_CHORD_REGEX = "[A-G]{1}([a-z0-9#+/-]*\\(*\\)*)*";
    private static final String CHORDS_REGEX = "^=?\\s*("+SINGLE_CHORD_REGEX+"\\s*)+$";


    public static final Pattern CHORDS_REGEX_PATTERN = Pattern.compile(CHORDS_REGEX);

    private String songAuthor="";
    private String chordsComplete="";
    private String chordsSimplified="";
    private String obs="";
    private String copyright = "";
    private String songSource = "";

    private String filePath="";        
    private String view="Title";
    private String searchStr = null;

    private boolean isClone;
    
    /** Creates a new instance of Song */
    public Song() {
        super();
        this.template = DisplayControlConfig.getInstance().getDefaultTemplateSong();
        isClone = false;
    }

    public Song getClone(){
        Song s = null;
        try {
            s = (Song) this.clone();
            s.setClone(true);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    public void setClone(boolean b){
        this.isClone = b;
    }

    public boolean isClone(){
        return this.isClone;
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("SongAuthor");
        properties.add("ChordsComplete");
        properties.add("ChordsSimplified");
        properties.add("Obs");
        properties.add("Copyright");
        properties.add("SongSource");
    }

    public void setSongAuthor(String songAuthor){
        this.songAuthor = songAuthor;
    }

    public ImageIcon getSongIcon(){
            Image icon1 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp1.png")).getImage();
            Image icon2 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp2.png")).getImage();
            Image icon3 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp3.png")).getImage();
            int icon1w = icon1.getWidth(null);
            int icon1h = icon1.getHeight(null);
            int icon2w = icon2.getWidth(null);
            int icon2h = icon2.getHeight(null);
            int icon3w = icon3.getWidth(null);
            int icon3h = icon3.getHeight(null);
            int width = icon1w+icon2w+icon3w;
            int height = icon1h;
            
            BufferedImage image = new BufferedImage(width,height,java.awt.image.BufferedImage.TRANSLUCENT);
            Graphics g = image.getGraphics();
            
            if(!this.getText().equals("")){
                g.drawImage(icon1,0,0,icon1w,icon1h,null);
            }
            
            if(!this.getChordsComplete().equals("")){
                g.drawImage(icon2,icon1w,0,icon2w,icon2h,null);
            }
            
            if(!this.getChordsSimplified().equals("")){
                g.drawImage(icon3,icon1w+icon2w,0,icon3w,icon3h,null);
            }

            return new ImageIcon(image);
    }
    
    //just to backward compatibility
    public void setLyrics(String lyrics){
        this.setText(lyrics);
    }
    
    public void setChordsComplete(String chordsComplete){
        this.chordsComplete = chordsComplete;
    }
    public void setChordsSimplified(String chordsSimplified){
        this.chordsSimplified = chordsSimplified;
    }
    public void setObs(String obs){
        this.obs = obs;
    }

    public String getSongAuthor(){
        return this.songAuthor;
    }
    
    public String getChordsComplete(){
        return this.chordsComplete;
    }
    
    public String getChordsSimplified(){
        return this.chordsSimplified;
    }
    
    public String getObs(){
        return this.obs;
    }
    
    public String getCopyright(){
        return copyright;
    }
    
    public void setCopyright(String s){
        this.copyright = s;
    }
    
    public String getSongSource(){
        return this.songSource;
    }
    
    public void setSongSource(String s){
        this.songSource = s;
    }
    public String getFileName(){
        return this.getTitle();
    }
    public void setFilePath(String file){
        this.filePath = file;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public void setView(String view){
        this.view = view;
    }

    public String getView(){
        return this.view;
    }
    
    public String toString(){
        String ret=this.getTitle();
        try {
            ret = (String)this.getClass().getMethod("get"+this.view).invoke(this);  
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return ret;
    }

    public String getSearchText(){

        if (searchStr == null){
            StringBuffer sb = new StringBuffer();
            sb.append(getText());
            sb.append(" ");
            sb.append(getCopyright());
            sb.append(" ");
            sb.append(getSongAuthor());
            sb.append(" ");
            sb.append(getSongSource());
            sb.append(" ");
            sb.append(getTitle());
            searchStr = sb.toString().toUpperCase();
        }
        return searchStr;

    }

    public static ArrayList<String> getUsedChords(String text){
        
        ArrayList<String> usedChords = new ArrayList<String>();

        for (String line : text.split("\n")){
            Matcher matcher = CHORDS_REGEX_PATTERN.matcher(line);
            /* Check if this is a chords line */
            if (matcher.find()){
                /* Backward compatibility */
                if (line.charAt(0) == '='){
                    line = line.substring(1);
                }

                /* Split in tokens */
                for (String tok : line.split("\\s")){
                    if (tok.length() > 0 && !usedChords.contains(tok) && !tok.equals("=")){
                        usedChords.add(tok);
                    }
                }
            }
        }

        return usedChords;

    }

    @Override
    public String getDefaultMonitorTemplate(){
        return DisplayControlConfig.getInstance().getMonitorTemplateSong();
    }

    @Override
    public Color getBackgroundColor(){
        return Color.decode("0xddddff");
    }

    @Override
    public void edit(){
        SongEditorForm sef = new SongEditorForm(this);
        sef.setLocationRelativeTo(ObjectManager.getInstance().getDatasoulMainForm());
        sef.setVisible(true);
    }

    @Override
    public void previewItem(){
        super.previewItem();
        ContentManager cm = ContentManager.getInstance();
        cm.setSongAuthorPreview(this.getSongAuthor());
        cm.setSongSourcePreview(this.getSongSource());
        cm.setCopyrightPreview(this.getCopyright());
    }

    @Override
    public void showItem(){
        super.showItem();
        ContentManager cm = ContentManager.getInstance();
        cm.setSongAuthorLive(this.getSongAuthor());
        cm.setSongSourceLive(this.getSongSource());
        cm.setCopyrightLive(this.getCopyright());
    }

    private static final Icon icon = new ImageIcon(Song.class .getResource("/datasoul/icons/v2/stock_effects-sound_small2.png"));
    @Override
    public Icon getIcon(){
        return icon;
    }
    
    @Override
    public String getJSon(){
        
        StringBuffer sb = new StringBuffer();
        sb.append(super.getJSon());
        
        if (this.getChordsSimplified().trim().length() > 0){
            sb.append(",");
            sb.append("chordsimpl: '");
            String chordsimpl = this.getChordsSimplified();
            chordsimpl = ServiceItem.escapeJson(chordsimpl);
            chordsimpl = chordsimpl.replaceAll("<br>=", "<br> ");
            if (chordsimpl.charAt(0) == '='){
                chordsimpl = chordsimpl.replace("=", "");
            }
            sb.append(chordsimpl);
            sb.append("'\n");
        }
        
        if (this.getChordsComplete().trim().length() > 0){
            sb.append(",");
            sb.append("chordcompl: '");
            String chordcompl = this.getChordsComplete();
            chordcompl = ServiceItem.escapeJson(chordcompl);
            chordcompl = chordcompl.replaceAll("<br>=", "<br> ");
            if (chordcompl.charAt(0) == '='){
                chordcompl = chordcompl.replace("=", "");
            }
            sb.append(chordcompl);
            sb.append("'\n");
        }
        
        return sb.toString();
    }
    
}
