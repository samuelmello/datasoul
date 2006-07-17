/*
 * TemplateItem.java
 *
 * Created on December 24, 2005, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.templates;

import datasoul.util.AttributedObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * This class represents a item that can be painted in the 
 * output display, such as a text, a image or a shape.
 * @author samuelm
 */
public abstract class TemplateItem extends AttributedObject {

    
    private int top;
    private int left;
    private int width;
    private int height;
    private float alpha;
    private Rectangle rect;
    private String name;
    
    @Override
    protected void registerProperties(){
        properties.add("Name");
        properties.add("Top");
        properties.add("Left");
        properties.add("Width");
        properties.add("Height");
        properties.add("Alpha");
    }

    
    protected TemplateItem(){
        
        super();
        
        this.rect = new Rectangle();
                
        this.setName("");
        this.setTop(0);
        this.setLeft(0);
        this.setWidth(0);
        this.setHeight(0);
        this.setAlpha(1f);
        
    }
    
    public abstract void draw(Graphics2D g, float time);

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
        rect.setBounds( left, top, width, height );
        firePropChanged("Top");
    }

    public void setTop(String top){
        this.setTop(Integer.parseInt(top));
    }
    
    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
        rect.setBounds( left, top, width, height );
        firePropChanged("Left");
    }
    
    public void setLeft(String left){
        this.setLeft(Integer.parseInt(left));
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        rect.setBounds( left, top, width, height );
        firePropChanged("Width");
    }

    public void setWidth(String width){
        this.setWidth(Integer.parseInt(width));
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        rect.setBounds( left, top, width, height );
        firePropChanged("Height");
    }

    public void setHeight(String height){
        this.setHeight(Integer.parseInt(height));
    }
    
    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        if (alpha < 0) alpha = 0;
        if (alpha > 1) alpha = 1;
        this.alpha = alpha;
        firePropChanged("Alpha");
    }
    
    public void setAlpha(String alpha){
        this.setAlpha(Float.parseFloat(alpha));
    }

    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
        firePropChanged("Name");
    }
    
    
    public boolean containsPoint(Point p){
        return rect.contains(p);
    }
    
    public Rectangle getBoundingRect(){
        return rect;
    }

    protected Color stringToColor(String strColor){
        /*
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
        }*/
        return Color.decode("0x"+strColor);
        
    }

}
