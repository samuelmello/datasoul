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
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JTextArea;

/**
 *
 * @author samuelm
 */
public class TextServiceItemRenderer implements ServiceItemRenderer {
    
    private TextServiceItemTextArea area;
    private float defaultFontSize;
    
    private boolean showMark;
    
    /** Creates a new instance of TextServiceItemRenderer */
    public TextServiceItemRenderer() {
        area = new TextServiceItemTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(true);
        area.setDoubleBuffered(false);
        defaultFontSize = area.getFont().getSize2D();
    }
    
    public void setWidth(int width) {
        area.setSize(width, 10000);
    }
    
    public int getHeight() {
        return area.getPreferredSize().height;
    }
    
    public Component getComponent(boolean selected, boolean hasFocus) {

        if (selected){
            area.setBackground( area.getSelectionColor() );
        }else{
            area.setBackground( Color.WHITE );
        }
        
        return area;
    }
    
    public void setText(String t){
        area.setText(t);
    }

    public String getText(){
        return area.getText();
    }
    
    public void setZoom(int f) {

        switch (f){
            case ServiceItemRenderer.ZOOM_TINY:
                area.setFont( area.getFont().deriveFont( (float) (defaultFontSize * 0.5) ) );
                break;
                
            case ServiceItemRenderer.ZOOM_SMALL:
                area.setFont( area.getFont().deriveFont( (float) (defaultFontSize * 0.75 )) );
                break;
                
            case ServiceItemRenderer.ZOOM_NORMAL:
                area.setFont( area.getFont().deriveFont( (float) (defaultFontSize * 1 )) );
                break;
                
            case ServiceItemRenderer.ZOOM_LARGE:
                area.setFont( area.getFont().deriveFont( (float) (defaultFontSize * 1.25 )) );
                break;

            case ServiceItemRenderer.ZOOM_HUGE:
                area.setFont( area.getFont().deriveFont( (float) (defaultFontSize * 1.5 )) );
                break;
                
        }
        
    }

    public boolean getShowMark() {
        return this.showMark;
    }
    
    public void setMark(boolean showMark){
        this.showMark = showMark;
        area.setShowMark(showMark);
    }

    public boolean getMark() {
        return showMark;
    }
    
}
