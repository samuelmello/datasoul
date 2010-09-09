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

import java.io.Serializable;

/**
 *
 * @author samuellucas
 */
public abstract class GstDisplayCmd implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5512569505017124430L;

	public abstract void run();
    
}

