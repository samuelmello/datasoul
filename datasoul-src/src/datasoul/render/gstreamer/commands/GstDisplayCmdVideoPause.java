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
public class GstDisplayCmdVideoPause extends GstDisplayCmd {

    private boolean pause;

    public GstDisplayCmdVideoPause (boolean b){
        pause = b;
    }

    @Override
    public void run() {
        if (pause){
            GstManagerClient.getInstance().pauseBgPipeline();
        }else{
            GstManagerClient.getInstance().resumeBgPipeline();
        }
    }



}

