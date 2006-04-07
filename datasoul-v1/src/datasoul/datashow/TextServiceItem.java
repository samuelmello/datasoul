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
        super.registerProperties();
        properties.add("Text");        
    }
             
    public void setText(String text){
        
        this.text = text.trim();
        
        String slidesStr[];
        if(text.contains("\r\n")){
            slidesStr = text.trim().split("\r\n==\r\n");
        }else{
            slidesStr = text.trim().split("\n==\n");
        }
        slides.clear();
        TextServiceItemRenderer j;
        for (int i=0; i<slidesStr.length; i++){
            j = new TextServiceItemRenderer();
            String str = slidesStr[i].replace("\n\n","\n \n").replace("\r\n\r\n","\r\n \r\n");
            j.setText(str);
            slides.add(j);
        }
    }
    
    public String getText(){
        return this.text;
    }

    public String getSlideText (int rowIndex){
        
        try{
            TextServiceItemRenderer r = (TextServiceItemRenderer) slides.get(rowIndex);
            return r.getText();
        }catch(Exception e){
            return "";
        }
        
    }
    
}
