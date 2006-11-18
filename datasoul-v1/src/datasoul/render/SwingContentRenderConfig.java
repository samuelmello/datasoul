/*
 * SwingContentRenderConfig.java
 *
 * Created on 18 November 2006, 12:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import javax.swing.JOptionPane;

/**
 *
 * @author samuelm
 */
public class SwingContentRenderConfig implements RenderConfigItf {
    
    /** Creates a new instance of SwingContentRenderConfig */
    public SwingContentRenderConfig() {
    }
    
    public void showConfiguration(boolean isMonitor){
        JOptionPane.showMessageDialog(null, "There is nothing to configure", "SwingContentRender", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
