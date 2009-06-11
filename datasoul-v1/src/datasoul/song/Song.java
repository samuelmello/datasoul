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
 * Song.java
 *
 * Created on 3 de Janeiro de 2006, 20:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.datashow.TextServiceItem;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrador
 */
public class Song extends TextServiceItem implements Cloneable {
  
    private String songAuthor="";
    private String chordsComplete="";
    private String chordsSimplified="";
    private String obs="";
    private String copyright = "";
    private String songSource = "";

    private String filePath="";        
    //private String view=java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Title");
    private String view="Title";

    private boolean isClone;
    
    /** Creates a new instance of Song */
    public Song() {
        super();
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
            System.out.println("Error in getValueAt!\nError:"+ex.getMessage());
        }
        
        return ret;
    }
    
    public ArrayList<String> getChordsUsedSimple(){
        return getChordsUsed(chordsSimplified);
    }
    
    public ArrayList<String> getChordsUsedComplete(){
        return getChordsUsed(chordsComplete);
    }

    private ArrayList<String> getChordsUsed(String source){
            ArrayList<String> result = new ArrayList<String>();
        
        String lines[] = source.split("\n");
        for (String l : lines){
            
            if (l.trim().equals(CHORUS_MARK) || l.trim().equals(SLIDE_BREAK))
                continue;
            
            if (l.startsWith("=")){
                String tokens[] = l.substring(1).trim().split("[ \\t]+");
                for (String t : tokens){
                    if (!result.contains(t) && !t.equals("=")){
                        result.add(t);
                    }
                }
            }
        }
        
        return result;

    }
}