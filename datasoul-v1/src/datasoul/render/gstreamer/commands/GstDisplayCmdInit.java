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
public class GstDisplayCmdInit extends GstDisplayCmd {

    private boolean monitorEnabled;
    private String mainDevice;
    private String monitorDevice;

    public GstDisplayCmdInit(boolean monitorEnabled, String mainDevice, String monitorDevice){
        this.monitorDevice = monitorDevice;
        this.monitorEnabled = monitorEnabled;
        this.mainDevice = mainDevice;
    }

    public void run(){
        GstManagerClient.getInstance().init(mainDevice, monitorDevice, monitorEnabled);
    }

}
