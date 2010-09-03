/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.render.gstreamer.GstManagerServer;

/**
 *
 * @author samuel
 */
public class GstNotificationHello extends GstNotification {

    public GstNotificationHello(){
    }

    @Override
    public void run(){
        GstManagerServer.getInstance().clientConnected();
    }

}
