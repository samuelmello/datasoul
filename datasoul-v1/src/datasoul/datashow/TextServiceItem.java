/*
 * TextServiceItem.java
 *
 * Created on February 10, 2006, 9:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import javax.swing.JTextArea;

/**
 *
 * @author samuelm
 */
public class TextServiceItem extends ServiceItem {
    
    private String text;
    
    /** Creates a new instance of TextServiceItem */
    public TextServiceItem() {
        super();
        this.text = "";
    }
    
    protected void registerProperties() {
        properties.add("Title");
        properties.add("Text");        
    }
             
    public void setText(String text){
        
        this.text = text.trim();
        
        String slidesStr[] = text.trim().split("\n\n");
        slides.clear();
        TextServiceItemRenderer j;
        for (int i=0; i<slidesStr.length; i++){
            j = new TextServiceItemRenderer();
            j.setText(slidesStr[i]);
            slides.add(j);
        }
    }
    
    public String getText(){
        return this.text;
    }
    
}
