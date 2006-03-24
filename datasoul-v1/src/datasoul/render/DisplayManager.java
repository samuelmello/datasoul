/*
 * DisplayManager.java
 *
 * Created on March 20, 2006, 9:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

/**
 *
 * @author root
 */
public class DisplayManager {
    
    static private SDLDisplay mainDisplay;
    static private DisplayItf monitorDisplay;
    
    static public SDLDisplay getMainDisplay(){
        
        if (mainDisplay == null){
            mainDisplay = new SDLDisplay();
            mainDisplay.initDisplay(640,480);
        }
        return mainDisplay;
    }
    
    static public DisplayItf getMonitorDisplay(){
        
        if (monitorDisplay == null){
            monitorDisplay = new SwingDisplay();
            monitorDisplay.getContentRender().setMonitor(true);
            monitorDisplay.getContentRender().setTemplate("monitor");
            monitorDisplay.initDisplay(640, 480);
        }
        return monitorDisplay;
    }
    
}

