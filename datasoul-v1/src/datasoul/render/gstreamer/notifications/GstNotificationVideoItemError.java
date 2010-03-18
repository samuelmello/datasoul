/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import javax.swing.JOptionPane;

/**
 *
 * @author samuel
 */
public class GstNotificationVideoItemError extends GstNotification {

    private String msg;
    public GstNotificationVideoItemError(String msg){
        this.msg = msg;
    }

    @Override
    public void run(){
        ContentManager.getInstance().setMainShowBackground(true);
        ContentManager.getInstance().slideChange(-1);
        ObjectManager.getInstance().getLivePanel().notifyVideoEnd();
        JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                "Error playing video:"+"\n"+msg);

    }

}
