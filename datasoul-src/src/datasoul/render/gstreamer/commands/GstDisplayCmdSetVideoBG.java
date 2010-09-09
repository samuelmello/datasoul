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

/**
 *
 * @author samuel
 */
public class GstDisplayCmdSetVideoBG extends GstDisplayCmd {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3311433090825497006L;
	private String filename;

    public GstDisplayCmdSetVideoBG(String filename){
        this.filename = filename;
    }

    public void run(){
        if (filename != null)
            GstManagerClient.getInstance().setBgPipeline(new GstManagerVideoBackgroundPipeline(filename));
    }

}

