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

import datasoul.render.gstreamer.notifications.GstNotificationBackgroundVideoError;
import org.gstreamer.State;

/**
 *
 * @author samuel
 */
public class GstManagerVideoBackgroundPipeline extends GstManagerVideoGenericPipeline {

    public GstManagerVideoBackgroundPipeline(String filename){
        super(filename);
    }

    @Override
    public void error(int code, String msg){
        GstManagerClient.getInstance().sendNotification(new GstNotificationBackgroundVideoError("("+code+") "+msg));
        pipe.setState(State.NULL);
    }

    
    @Override
    public void eos(){
        super.eos();
        /* play in loop */
        pipe.setState(State.PLAYING);
    }
}

