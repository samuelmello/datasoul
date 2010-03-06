/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.util.DatasoulKeyListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 *
 * @author samuel
 */
public class GstEventNotification implements Serializable {

    private int evt;
    private KeyEvent key;
    
    public static final int EVT_KEY = 0;

    public GstEventNotification(int evt, KeyEvent key){
        this.evt = evt;
        this.key = key;
    }

    public void run(){
        switch (evt){
            case EVT_KEY:
                DatasoulKeyListener.getInstance().keyPressed(key);
                break;
        }
    }

}
