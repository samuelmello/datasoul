/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
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
 * ChordShapePanel.java
 *
 * Created on 21 de Janeiro de 2006, 19:10
 */

package datasoul.serviceitems.song;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

/**
 *
 * @author  Administrador
 */
public class ChordShapePanel extends javax.swing.JPanel {

    private String shape;
    private String chordName; 
    private int startFret;
    private boolean editable;
    private int chordSize;
    private int sbc;
    private int sbf;
    private int snb;

    /**
     * Creates new form ChordShapePanel
     */
    public ChordShapePanel() {
        this.chordSize = 1;
        //size between chords
        sbc = 10 - (chordSize - 1)*2;
        //size between frets
        sbf = 20 - (chordSize - 1)*4;
        //note ball size
        snb = 8 - (chordSize - 1);
        
        initComponents();

        startFret = 1;
        editable = true;
        
        this.setShape("X X X X X X");
        this.setChordName("");
        
        startFret = 1;
    }

    public boolean isEmpty(){
        if(this.shape.equals("X X X X X X")){
            return true;
        }
        return false;
    }
    
    public ChordShapePanel(int chordSize, String chordName, String shape) {
        this.chordSize = chordSize;
        //size between chords
        sbc = 10 - (chordSize - 1)*2;
        //size between frets
        sbf = 20 - (chordSize - 1)*4;
        //note ball size
        snb = 8 - (chordSize - 1);
        
        initComponents();

        startFret = 1;
        editable = true;
        
        this.setShape(shape);
        this.setChordName(chordName);
    }
    
    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
        
        //sets the start fret
        int minFret = 33;
        String[] notes = shape.split(" ");
        for(int i=0;i<notes.length;i++){
            if(!notes[i].equals("X")){
                int num = Integer.parseInt(notes[i]);
                if(num<minFret)
                    minFret = num;
            }
        }
        if(minFret==0)
            startFret = 1;
        else
            startFret = minFret;
    }

    @Override
    public void paint(java.awt.Graphics g) {

        this.setBackground(Color.WHITE);
        
        super.paint(g);
        
        drawShape(g);
        
    }

    public void drawShape(Graphics g){
        Graphics2D g2 = (Graphics2D) g;        

        //draw white background
        g2.setPaint(Color.white);
        g2.fill(new Rectangle2D.Double(0,0,30+5*sbc,50+5*sbf));        

        //draw chord name
        g2.setPaint(Color.black);        
        g2.setFont(new Font("Arial", Font.BOLD, 13 - chordSize));
        g2.drawString(this.getChordName(), 20, 23);
        g2.setFont(new Font("Arial", Font.PLAIN, 11 - chordSize));
        
        g2.setPaint(Color.black);        
        g2.drawString(String.valueOf(this.startFret), 2, 43);
        if(this.startFret<10){
            g2.draw(new java.awt.geom.Ellipse2D.Double(8,34,3,3));                                
            g2.draw(new Rectangle2D.Double(8,39,3,0));        
        }else{
            g2.draw(new java.awt.geom.Ellipse2D.Double(13,34,3,3));
            g2.draw(new Rectangle2D.Double(13,39,3,0));                
        }
        
        //draw horizontal lines
        g2.draw(new Rectangle2D.Double(20,31,5*sbc,sbf*5));        
        g2.draw(new Rectangle2D.Double(20,30,sbc,sbf*5));
        g2.draw(new Rectangle2D.Double(20+sbc,30,sbc,sbf*5));
        g2.draw(new Rectangle2D.Double(20+2*sbc,30,sbc,sbf*5));        
        g2.draw(new Rectangle2D.Double(20+3*sbc,30,sbc,sbf*5));
        g2.draw(new Rectangle2D.Double(20+4*sbc,30,sbc,sbf*5));        

        //draw vertical lines
        g2.draw(new Rectangle2D.Double(20,30,5*sbc,1*sbf));        
        g2.draw(new Rectangle2D.Double(20,30,5*sbc,2*sbf));                
        g2.draw(new Rectangle2D.Double(20,30,5*sbc,3*sbf));        
        g2.draw(new Rectangle2D.Double(20,30,5*sbc,4*sbf));        
        g2.draw(new Rectangle2D.Double(20,30,5*sbc,5*sbf));        

        String[] notes = shape.split(" ");
        int numX = 0;
        int min = 33;
        for(int i=0;i<notes.length;i++){
            if(notes[i].equals("X")){
                numX ++;
            }else{
                int num = Integer.parseInt(notes[i]);
                if(num<min)
                    min = num;
            }
        }
        
        //print pestana
        if(min>0){
            g2.setPaint(Color.decode("0xff8888"));
            if(numX==0){
                int fret = (min-startFret+1);
                g2.fill(new RoundRectangle2D.Double(20-sbc/2,(30-sbf/2-snb/2)+sbf*fret,6*sbc,snb,snb,snb));
            }else if(numX==1){
                if(notes[0].equals("X")){
                    int fret = (min-startFret+1);
                    g2.fill(new RoundRectangle2D.Double(20+sbc/2,(30-sbf/2-snb/2)+sbf*fret,5*sbc,snb,snb,snb));
                }
                if(notes[5].equals("X")){
                    int fret = (min-startFret+1);
                    g2.fill(new RoundRectangle2D.Double(20-sbc/2,(30-sbf/2-snb/2)+sbf*fret,5*sbc,snb,snb,snb));
                }
            }
        }

        int sx = 12 - chordSize*2;
        g2.setFont(new Font("Arial", Font.PLAIN, sx));
        //print the notes
        boolean bassNote = true;
        if(notes.length==6){
            for(int i=0;i<notes.length;i++){
                if(notes[i].equals("X")){
                    //string without note
                    g2.setPaint(Color.black);        
                    g2.drawString("X", 17+sbc*i, 30+5*sbf+sx/2+7);
                }else{
                    g2.setPaint(Color.red);        
                    if(Integer.parseInt(notes[i])>0){
                        //finger note
                        int fret = (Integer.parseInt(notes[i])-startFret+1);
                        g2.fill(new Ellipse2D.Double((20-snb/2)+sbc*i,(30-snb/2-sbf/2)+sbf*fret,snb,snb));
                    }else{
                        //free note
                        g2.setPaint(Color.blue);        
                        g2.fill(new Ellipse2D.Double((20-snb/2)+sbc*i,30-snb/2+1,snb,snb));
                    }
                    g2.setPaint(Color.black);        
                    //if is the first note print a filled circle
                    if(bassNote){
                        g2.fill(new Ellipse2D.Double(17+sbc*i,30+5*sbf+5,7-chordSize,7-chordSize));                    
                        bassNote = false;
                    }else{
                        g2.draw(new Ellipse2D.Double(17+sbc*i,30+5*sbf+5,7-chordSize,7-chordSize));
                    }
                }
            }
        }

    }
    
    public BufferedImage createImage(){

        BufferedImage image = new BufferedImage(20+10+5*sbc,30+20+5*sbf,java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        
        drawShape(g);
        
//        FileOutputStream out = new FileOutputStream(file);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        encoder.encode(image);
        return image;
    }
    
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getChordName() {
        return chordName;
    }

    public void setChordName(String chordName) {
        this.chordName = chordName;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChordShapePanel.this.mouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 105, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        //just change if is editable
        if(editable){
            int x = evt.getX();
            int y = evt.getY();

            //it's a blank area
            if(x<15&&y>45)
                return;

            //change the start fret
            if(x<15&&y<50&&y>25){
                String str = JOptionPane.showInputDialog(this,java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DIGIT THE START FRET DESIRED."),java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("START FRET"),JOptionPane.QUESTION_MESSAGE);
                try{
                    startFret = Integer.parseInt(str);
                }catch (Exception e){

                }
                this.repaint();

                return;
            }

            int chordSelected = (int)Math.ceil((x-15)/sbc);
            String[] notes = shape.split(" ");
            String strAux = "";
            for(int i=0;i<notes.length;i++){
                if(i==chordSelected)    
                    if(y<30+5*sbf)
                        strAux = strAux + ((int)Math.ceil((y-sbc)/sbf)+startFret-1)+" ";
                    else
                        strAux = strAux + "X ";
                else
                    if(!notes[i].equals("X"))
                        strAux = strAux + (Integer.parseInt(notes[i]))+" ";
                    else
                        strAux = strAux + notes[i]+" ";                    
            }
            shape = strAux;
            this.repaint();
        }
    }//GEN-LAST:event_mouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}


