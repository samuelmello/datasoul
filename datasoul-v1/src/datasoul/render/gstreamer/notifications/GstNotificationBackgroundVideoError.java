/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.config.BackgroundConfig;
import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import javax.swing.JOptionPane;

/**
 *
 * @author samuel
 */
public class GstNotificationBackgroundVideoError extends GstNotification {

    private String msg;
    public GstNotificationBackgroundVideoError(String msg){
        this.msg = msg;
    }

    @Override
    public void run(){
        BackgroundConfig.getInstance().setMode(BackgroundConfig.MODE_STATIC);
        ContentManager.getInstance().slideChange(-1);
        JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                "Error playing background video:"+"\n"+msg);

    }

}
