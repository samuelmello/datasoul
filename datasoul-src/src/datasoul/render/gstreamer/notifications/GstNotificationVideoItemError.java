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

package datasoul.render.gstreamer.notifications;

import datasoul.config.BackgroundConfig;
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
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC){
            ContentManager.getInstance().setMainShowBackground(true);
        }
        ContentManager.getInstance().slideChange(-1);
        ObjectManager.getInstance().getLivePanel().notifyVideoEnd();
        JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ERROR PLAYING VIDEO:")+"\n"+msg);

    }

}

