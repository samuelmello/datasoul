/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.serviceitems;

import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmdVideoItem;
import java.io.File;
import java.io.IOException;

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
}

