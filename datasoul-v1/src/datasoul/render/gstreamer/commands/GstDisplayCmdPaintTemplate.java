/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.commands;

import datasoul.render.gstreamer.GstManagerClient;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author samuel
 */
public class GstDisplayCmdPaintTemplate extends GstDisplayCmd {

    private ImageIcon img;

    public GstDisplayCmdPaintTemplate (Image img){
        this.img = new ImageIcon(img);
    }

    public void run(){
        GstManagerClient.getInstance().getMainContentDisplay().paintTemplate(img.getImage());
    }

}

