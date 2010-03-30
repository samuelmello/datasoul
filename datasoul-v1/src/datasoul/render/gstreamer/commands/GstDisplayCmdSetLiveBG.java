/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
