/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.util.DatasoulKeyListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import javax.swing.SwingUtilities;

/**
 *
 * @author samuel
 */
public class GstNotificationKeyboard extends GstNotification {

    private KeyEvent key;
    
    public GstNotificationKeyboard(KeyEvent key){
        this.key = key;
    }

    @Override
    public void run(){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                DatasoulKeyListener.getInstance().keyPressed(key);
            }
        });

    }

}
