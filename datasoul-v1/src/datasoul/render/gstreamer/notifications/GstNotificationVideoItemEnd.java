/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;

/**
 *
 * @author samuel
 */
public class GstNotificationVideoItemEnd extends GstNotification {

    @Override
    public void run(){
        ContentManager.getInstance().setMainShowBackground(true);
        ContentManager.getInstance().slideChange(-1);
        ObjectManager.getInstance().getLivePanel().notifyVideoEnd();
    }

}

