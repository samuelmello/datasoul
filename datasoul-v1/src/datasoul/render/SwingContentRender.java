/*
 * SwingContentRender.java
 *
 * Created on March 20, 2006, 9:29 PM
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
public class SwingContentRender extends ContentRender {
    
    private SwingDisplay display;
    
    /** Creates a new instance of SwingContentRender */
    public SwingContentRender(SwingDisplay display) {
        this.display = display;
    }

    public void paint(DisplayTemplate d) {
        display.updateImg(d);
    }
    
}
