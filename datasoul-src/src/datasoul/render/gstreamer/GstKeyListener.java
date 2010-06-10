/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.notifications.GstNotificationKeyboard;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author samuel
 */
public class GstKeyListener implements KeyListener, AWTEventListener {

    private static GstKeyListener instance;

    private GstKeyListener(){

    }

    public static GstKeyListener getInstance(){
        if (instance == null)
            instance = new GstKeyListener();
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
       GstNotificationKeyboard evt = new GstNotificationKeyboard(e);
       GstManagerClient.getInstance().sendNotification(evt);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        switch (event.getID()) {
            case KeyEvent.KEY_PRESSED:
               keyPressed((KeyEvent) event);
            break;
            case KeyEvent.KEY_RELEASED:
               keyReleased((KeyEvent) event);
            break;
            case KeyEvent.KEY_TYPED:
               keyTyped((KeyEvent) event);
            break;
        }
    }

}

