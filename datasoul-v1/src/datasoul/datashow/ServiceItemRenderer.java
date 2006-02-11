/*
 * ServiceItemRenderer.java
 *
 * Created on February 10, 2006, 10:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.Component;

/**
 *
 * @author samuelm
 */
public interface ServiceItemRenderer {
    
    public void setWidth(int width);
    
    public int getHeight();
    
    public Component getComponent(boolean selected);
    
}
