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
public class GstDisplayCmdShowHide extends GstDisplayCmd {

    private boolean param;

    public GstDisplayCmdShowHide(boolean param){
        this.param = param;
    }

    public void run(){
        GstManagerClient.getInstance().setOutputVisible(param);
    }

}
