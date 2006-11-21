/*
 * Song.java
 *
 * Created on 3 de Janeiro de 2006, 20:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.song;

import datasoul.*;
import datasoul.util.*;
import datasoul.datashow.*;
import datasoul.song.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.ImageIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public class Song extends TextServiceItem{
  
    private String songAuthor="";
    private String chordsComplete="";
    private String chordsSimplified="";
    private String obs="";

    private String filePath="";        
    private String view="Title";
    
    /** Creates a new instance of Song */
    public Song() {
        super();
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("SongAuthor");
        properties.add("ChordsComplete");
        properties.add("ChordsSimplified");
        properties.add("Obs");

    }

    public void setSongAuthor(String songAuthor){
        this.songAuthor = songAuthor;
    }

    public ImageIcon getSongIcon(){
            Image icon1 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp1.gif")).getImage();
            Image icon2 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp2.gif")).getImage();
            Image icon3 = new ImageIcon(getClass().getResource("/datasoul/icons/songProp3.gif")).getImage();
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

    public String getFileName(){
        return this.getTitle();
    }
    public void setFilePath(String file){
        this.filePath = file;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public boolean containsStringInField(String field, String strSearch) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        
        return (boolean)(((String)this.getClass().getMethod("get"+field).invoke(this)).toLowerCase().contains(strSearch.toLowerCase()));     
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
    

}
