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
public class GstDisplayCmdSetVideoBG {

    private String filename;

    public GstDisplayCmdSetVideoBG(String filename){
        this.filename = filename;
    }

    public void run(){
        GstManagerClient.getInstance().updatePipeline();
    }

}
