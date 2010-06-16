/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
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

