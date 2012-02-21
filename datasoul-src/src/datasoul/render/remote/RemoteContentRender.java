/* 
 * Copyright 2012 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.remote;

import datasoul.render.ContentRenderItf;
import datasoul.serviceitems.imagelist.ImageListServiceRenderer;

/**
 *
 * @author samuel
 */
public class RemoteContentRender implements ContentRenderItf {

    private RemoteContentCommand.Target target;

    public RemoteContentRender(RemoteContentCommand.Target target){
        this.target = target;
    }
    public void alertHide(int transictionTime) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "alertHide", transictionTime);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void alertShow(int transictionTime) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "alertShow", transictionTime);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void paintBackground(ImageListServiceRenderer img) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "paintBackground", img);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void saveTransitionImage() {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "saveTransitionImage");
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setActiveImage(ImageListServiceRenderer img) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setActiveImage", img);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }


    public void setAlert(String alert) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setAlert", alert);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setAlertActive(boolean active) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setAlertActive", active);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setAlertTemplate(String template) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setAlertTemplate", template);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setBlack(boolean b) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setBlack", b);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setClock(String clock) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setClock", clock);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setCopyright(String copyright) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setCopyright", copyright);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setNextImage(ImageListServiceRenderer img) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setNextImage", img);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setNextSlide(String nextSlide) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setNextSlide", nextSlide);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setShowBackground(boolean b) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setShowBackground", b);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setShowTemplate(boolean b) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setShowTemplate", b);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setShowTimer(boolean show) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setShowTimer", show);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setSlide(String slide) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setSlide", slide);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setSongAuthor(String songAuthor) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setSongAuthor", songAuthor);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setSongSource(String songSource) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setSongSource", songSource);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setTemplate(String template) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setTemplate", template);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setTimer(String timer) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setTimer", timer);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setTimerProgress(float f) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setTimerProgress", f);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void setTitle(String title) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "setTitle", title);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void slideChange(int transictionTime) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "slideChange", transictionTime);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void slideChangeFromTimerManager() {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "slideChangeFromTimerManager");
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void slideHide(int transictionTime) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "slideHide", transictionTime);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

    public void slideShow(int transictionTime) {
        RemoteContentCommand cmd = new RemoteContentCommand(target, "slideShow", transictionTime);
        RemoteContentServer.getInstance().sendCommand(cmd);
    }

}
