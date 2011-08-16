/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasoul.render.gstreamer.notifications;

import datasoul.config.BackgroundConfig;
import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;

/**
 *
 * @author samuel
 */
public class GstNotificationVideoItemStart extends GstNotification {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2829574508805223164L;
    
    
    @Override
    public void run(){
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC){
            ContentManager.getInstance().setMainShowBackground(false);
        }
        ContentManager.getInstance().slideChange(-1);
        ObjectManager.getInstance().getLivePanel().notifyVideoEnd();
    }
    
}
