/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.serviceitems;

import datasoul.datashow.ServiceItem;
import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmdVideoItem;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author samuellucas
 */
public class VideoServiceItem extends AttachmentServiceItem {

    public VideoServiceItem(){
        super();
    }

    public VideoServiceItem(String filename, InputStream is) throws IOException{
        super(filename, is);
    }

    @Override
    public boolean getShowMediaControls(){
        return true;
    }

    @Override
    public void showItem(){
        ContentManager.getInstance().setMainShowBackground(false);
        ContentManager.getInstance().setMainShowTemplate(false);
        GstManagerServer.getInstance().sendCommand(new GstDisplayCmdVideoItem(file.getAbsolutePath()));
    }
}
