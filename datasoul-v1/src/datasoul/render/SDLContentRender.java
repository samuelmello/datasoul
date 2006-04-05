/*
 * SDLContentRender.java
 *
 * Created on March 20, 2006, 9:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;

/**
 *
 * @author root
 */
public class SDLContentRender extends ContentRender {
    
    private SDLDisplay display;
    
    /** Creates a new instance of SDLContentRender */
    public SDLContentRender(SDLDisplay display) {
        this.display = display;
    }

    public void paint(DisplayTemplate d) {
        display.paintOverlay(d);
    }

    public void clear() {
        display.clear();
    }

    public void flip() {
        display.flip();
    }
    
}
