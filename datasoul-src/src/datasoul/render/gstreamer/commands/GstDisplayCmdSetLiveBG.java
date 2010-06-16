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

package datasoul.render.gstreamer.commands;

import datasoul.render.gstreamer.GstManagerClient;
import datasoul.render.gstreamer.GstManagerVideoBackgroundPipeline;
import datasoul.render.gstreamer.GstManagerVideoLiveBgPipeline;

/**
 *
 * @author samuel
 */
public class GstDisplayCmdSetLiveBG extends GstDisplayCmd {

    public GstDisplayCmdSetLiveBG(){
    }

    @Override
    public void run(){
        GstManagerClient.getInstance().setBgPipeline(new GstManagerVideoLiveBgPipeline());
    }

}

