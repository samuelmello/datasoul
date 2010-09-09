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

/**
 *
 * @author samuel
 */
public class GstNotificationVideoItemEnd extends GstNotification {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2829574508805973164L;

	@Override
    public void run(){
        if (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC){
            ContentManager.getInstance().setMainShowBackground(true);
        }
        ContentManager.getInstance().slideChange(-1);
        ObjectManager.getInstance().getLivePanel().notifyVideoEnd();
    }

}

