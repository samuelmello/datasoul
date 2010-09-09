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

    /**
	 * 
	 */
	private static final long serialVersionUID = -9175305702584608196L;

	public GstNotificationHello(){
    }

    @Override
    public void run(){
        GstManagerServer.getInstance().clientConnected();
    }

}
