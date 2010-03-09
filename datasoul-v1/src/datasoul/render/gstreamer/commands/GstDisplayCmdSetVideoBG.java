/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.commands;

import datasoul.render.gstreamer.GstManagerBgVideoPipeline;
import datasoul.render.gstreamer.GstManagerClient;

/**
 *
 * @author samuel
 */
public class GstDisplayCmdSetVideoBG extends GstDisplayCmd {

    private String filename;

    public GstDisplayCmdSetVideoBG(String filename){
        this.filename = filename;
    }

    public void run(){
        if (filename != null)
            GstManagerClient.getInstance().setBgPipeline(new GstManagerBgVideoPipeline(filename));
    }

}
