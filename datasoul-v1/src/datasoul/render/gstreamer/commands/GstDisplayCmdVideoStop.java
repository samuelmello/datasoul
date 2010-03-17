/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.commands;

import datasoul.render.gstreamer.GstManagerClient;

/**
 *
 * @author samuel
 */
public class GstDisplayCmdVideoStop extends GstDisplayCmd {

    @Override
    public void run(){
        GstManagerClient.getInstance().stopVideoItem();
    }
}
