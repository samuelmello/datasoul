/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.gstreamer;

import java.awt.image.BufferedImage;

import datasoul.render.ContentRenderItf;
import datasoul.render.gstreamer.commands.GstDisplayCmdUpdateContent;
import datasoul.serviceitems.imagelist.ImageListServiceRenderer;

/**
 *
 * @author samuel
 */
public class GstContentRender implements ContentRenderItf {

    public enum Target {
        TARGET_MAIN,
        TARGET_MONITOR
    }

    private Target target;

    public GstContentRender(Target target){
        this.target = target;
    }

    public void alertHide(int transictionTime) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "alertHide", transictionTime);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void alertShow(int transictionTime) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "alertShow", transictionTime);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void paintBackground(ImageListServiceRenderer img) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "paintBackground", img);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void saveTransitionImage() {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "saveTransitionImage");
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setActiveImage(ImageListServiceRenderer img) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setActiveImage", img);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setAlert(String alert) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setAlert", alert);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setAlertActive(boolean active) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setAlertActive", active);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setAlertTemplate(String template) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setAlertTemplate", template);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setBlack(boolean b) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setBlack", b);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setClock(String clock) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setClock", clock);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setCopyright(String copyright) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setCopyright", copyright);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setNextImage(ImageListServiceRenderer img) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setNextImage", img);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setNextSlide(String nextSlide) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setNextSlide", nextSlide);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setShowBackground(boolean b) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setShowBackground", b);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setShowTemplate(boolean b) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setShowTemplate", b);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setShowTimer(boolean show) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setShowTimer", show);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setSlide(String slide) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setSlide", slide);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setSongAuthor(String songAuthor) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setSongAuthor", songAuthor);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setSongSource(String songSource) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setSongSource", songSource);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setTemplate(String template) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setTemplate", template);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setTimer(String timer) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setTimer", timer);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setTimerProgress(float f) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setTimerProgress", f);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void setTitle(String title) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "setTitle", title);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void slideChange(int transictionTime) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "slideChange", transictionTime);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void slideChangeFromTimerManager() {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "slideChangeFromTimerManager");
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void slideHide(int transictionTime) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "slideHide", transictionTime);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

    public void slideShow(int transictionTime) {
        GstDisplayCmdUpdateContent cmd = new GstDisplayCmdUpdateContent(target, "slideShow", transictionTime);
        GstManagerServer.getInstance().sendCommand(cmd);
    }

}
