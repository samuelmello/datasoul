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

package datasoul.serviceitems;

import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmdVideoItem;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author samuellucas
 */
public class VideoServiceItem extends GenericAttachmentServiceItem {

    public VideoServiceItem(){
        super();
    }

    public VideoServiceItem(File f, boolean isLink) throws IOException{
        super(f, isLink);
    }

    @Override
    public boolean getShowMediaControls(){
        return true;
    }

    @Override
    public void showItem(){
        super.showItem();
        ContentManager.getInstance().setMainShowBackground(false);
        ContentManager.getInstance().setMainShowTemplate(false);
        GstManagerServer.getInstance().sendCommand(new GstDisplayCmdVideoItem(file.getAbsolutePath()));
    }

    private static final Icon icon = new ImageIcon(VideoServiceItem.class .getResource("/datasoul/icons/v2/video-x-generic.png"));
    @Override
    public Icon getIcon(){
        return icon;
    }
}

