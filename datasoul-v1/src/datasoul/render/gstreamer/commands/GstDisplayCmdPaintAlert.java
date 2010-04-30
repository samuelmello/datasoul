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
public class GstDisplayCmdPaintAlert extends GstDisplayCmd {

    private ImageIcon img;

    public GstDisplayCmdPaintAlert (Image img){
        this.img = new ImageIcon(img);
    }

    public void run(){
        GstManagerClient.getInstance().getMainContentDisplay().paintAlert(img.getImage());
    }

}

