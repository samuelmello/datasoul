/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.ContentDisplay;
import java.awt.Image;

/**
 *
 * @author samuellucas
 */
public class GstDisplay implements ContentDisplay {

    @Override
    public synchronized void paintBackground(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmd(GstDisplayCmd.CMD_PAINT_BACKGROUND, im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintTransition(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmd(GstDisplayCmd.CMD_PAINT_TRANSITION, im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintTemplate(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmd(GstDisplayCmd.CMD_PAINT_TEMPLATE, im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintAlert(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmd(GstDisplayCmd.CMD_PAINT_ALERT, im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void updateScreen(boolean isBlack, boolean keepbg, float background, float transition, float template, float alert) {
        GstDisplayCmd cmd = new GstDisplayCmd(GstDisplayCmd.CMD_UPDATE_SCREEN, isBlack, keepbg, background, transition, template, alert);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

}
