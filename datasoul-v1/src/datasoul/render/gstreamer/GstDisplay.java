/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.ContentDisplay;
import datasoul.render.gstreamer.commands.GstDisplayCmdPaintAlert;
import datasoul.render.gstreamer.commands.GstDisplayCmdPaintBackground;
import datasoul.render.gstreamer.commands.GstDisplayCmdPaintTemplate;
import datasoul.render.gstreamer.commands.GstDisplayCmdPaintTransition;
import datasoul.render.gstreamer.commands.GstDisplayCmdUpdateScreen;
import java.awt.Image;

/**
 *
 * @author samuellucas
 */
public class GstDisplay implements ContentDisplay {

    @Override
    public synchronized void paintBackground(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmdPaintBackground(im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintTransition(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmdPaintTransition(im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintTemplate(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmdPaintTemplate(im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void paintAlert(Image im) {
        GstDisplayCmd cmd = new GstDisplayCmdPaintAlert(im);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    @Override
    public synchronized void updateScreen(boolean isBlack, boolean keepbg, float background, float transition, float template, float alert) {
        GstDisplayCmd cmd = new GstDisplayCmdUpdateScreen(isBlack, keepbg, background, transition, template, alert);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

}

