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
import java.util.concurrent.Semaphore;

/**
 *
 * @author root
 */
public class SwingContentRender extends ContentRender {
    
    private SwingDisplayPanel display;
    private DisplayTemplate updateTemplate;
    
    /** Creates a new instance of SwingContentRender */
    public SwingContentRender(SwingDisplayPanel display) {
        super();
        this.display = display;
    }

    public void paint(DisplayTemplate d) {
        display.updateImg(d);
    }

    public void clear() {
        display.clear();
    }

    public void flip() {
        display.flip();
    }
    

    
}
