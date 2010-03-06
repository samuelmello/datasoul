/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author samuellucas
 */
public class GstDisplayCmd implements Serializable {

    private int cmd;
    private ImageIcon img;
    private boolean isBlack;
    private boolean keepBG;
    private float background;
    private float transition;
    private float template;
    private float alert;

    private boolean monitorEnabled;
    private String mainDevice;
    private String monitorDevice;

    private boolean param;

    public static final int CMD_INIT = 1;
    public static final int CMD_PAINT_BACKGROUND = 2;
    public static final int CMD_PAINT_TRANSITION = 3;
    public static final int CMD_PAINT_TEMPLATE = 4;
    public static final int CMD_PAINT_ALERT = 5;
    public static final int CMD_UPDATE_SCREEN = 6;
    public static final int CMD_SHOW_HIDE = 7;

    public GstDisplayCmd(int cmd, boolean monitorEnabled, String mainDevice, String monitorDevice){
        this.cmd = cmd;
        this.monitorDevice = monitorDevice;
        this.monitorEnabled = monitorEnabled;
        this.mainDevice = mainDevice;
    }

    public GstDisplayCmd(int cmd, boolean param){
        this.cmd = cmd;
        this.param = param;
    }

    public GstDisplayCmd (int cmd, Image img){
        this.cmd = cmd;
        this.img = new ImageIcon(img);
    }

    public GstDisplayCmd(int cmd, boolean isBlack, boolean keepBG, float background, float transition, float template, float alert){
        this.cmd = cmd;
        this.isBlack = isBlack;
        this.keepBG = keepBG;
        this.background = background;
        this.transition = transition;
        this.template = template;
        this.alert = alert;
    }

    public void runCmd(){
        switch (cmd){
            case CMD_INIT:
                GstManagerClient.getInstance().init(mainDevice, monitorDevice, monitorEnabled);
                break;
            case CMD_PAINT_BACKGROUND:
                GstManagerClient.getInstance().getMainContentDisplay().paintBackground(img.getImage());
                break;
            case CMD_PAINT_TRANSITION:
                GstManagerClient.getInstance().getMainContentDisplay().paintTransition(img.getImage());
                break;
            case CMD_PAINT_TEMPLATE:
                GstManagerClient.getInstance().getMainContentDisplay().paintTemplate(img.getImage());
                break;
            case CMD_PAINT_ALERT:
                GstManagerClient.getInstance().getMainContentDisplay().paintAlert(img.getImage());
                break;
            case CMD_UPDATE_SCREEN:
                GstManagerClient.getInstance().getMainContentDisplay().updateScreen(isBlack, keepBG, background, transition, template, alert);
                break;
            case CMD_SHOW_HIDE:
                GstManagerClient.getInstance().setOutputVisible(param);
                break;
        }
    }
    
}
