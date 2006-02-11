/*
 * TextServiceItemRenderer.java
 *
 * Created on February 10, 2006, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTextArea;

/**
 *
 * @author samuelm
 */
public class TextServiceItemRenderer implements ServiceItemRenderer {
    
    JTextArea area;
    
    /** Creates a new instance of TextServiceItemRenderer */
    public TextServiceItemRenderer() {
        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }
    
    public void setWidth(int width) {
        area.setSize(width, 10000);
    }
    
    public int getHeight() {
        return area.getPreferredSize().height;
    }
    
    public Component getComponent(boolean selected) {

        if (selected){
            area.setBackground( Color.YELLOW );
        }else{
            area.setBackground( Color.WHITE );
        }
        
        return area;
    }
    
    public void setText(String t){
        area.setText(t);
    }
    
}
