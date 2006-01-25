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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

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
    
    protected TemplateItem(){
        
        super();
        
        this.rect = new Rectangle();
                
        properties.add("Name");
        this.setName("");

        properties.add("Top");
        this.setTop(0);
        properties.add("Left");
        this.setLeft(0);
        properties.add("Width");
        this.setWidth(0);
        properties.add("Height");
        this.setHeight(0);
        properties.add("Alpha");
        this.setAlpha(1f);
        
    }
    
    public abstract void draw(Graphics2D g);

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


}
