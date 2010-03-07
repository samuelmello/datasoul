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
public class GstDisplayCmdUpdateScreen extends GstDisplayCmd {

    private boolean isBlack;
    private boolean keepBG;
    private float background;
    private float transition;
    private float template;
    private float alert;

    public GstDisplayCmdUpdateScreen(boolean isBlack, boolean keepBG, float background, float transition, float template, float alert){
        this.isBlack = isBlack;
        this.keepBG = keepBG;
        this.background = background;
        this.transition = transition;
        this.template = template;
        this.alert = alert;
    }

    public void run(){
        GstManagerClient.getInstance().getMainContentDisplay().updateScreen(isBlack, keepBG, background, transition, template, alert);
    }

}
