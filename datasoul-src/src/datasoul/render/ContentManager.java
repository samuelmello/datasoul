/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
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

/*
 * ContentManager.java
 *
 * Created on March 20, 2006, 11:55 PM
 *
 */

package datasoul.render;

import datasoul.config.ConfigObj;
import datasoul.render.gstreamer.GstContentRender;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmdShowHide;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * @author root
 */
public class ContentManager {
    
    private static ContentManager instance;
    
    private ContentRenderItf previewRender;
    private LinkedList<ContentRenderItf> mainRenderList;
    private LinkedList<ContentRenderItf> monitorRenderList;

    public static final int PREVIEW_WIDTH = 160;

    /** Creates a new instance of ContentManager */
    private ContentManager() {
        mainRenderList = new LinkedList<ContentRenderItf>();
        monitorRenderList = new LinkedList<ContentRenderItf>();
    }
    
    static public ContentManager getInstance(){
        if (instance == null ){
            instance = new ContentManager();
        }
        return instance;
    }

    public int getPreviewHeight(){
        return ConfigObj.getActiveInstance().getMainOutputDeviceObj().getProportionalHeight(PREVIEW_WIDTH);
    }

    public int getPreviewMonitorHeight(){

        if (ConfigObj.getActiveInstance().getMonitorOutput()){
            return ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getProportionalHeight(PREVIEW_WIDTH);
        }else{
            return getPreviewHeight();
        }
    }

    public void setTitleLive(String title){
        for (ContentRenderItf r : mainRenderList)
            r.setTitle(title);
        for (ContentRenderItf r : monitorRenderList)
            r.setTitle(title);
    }
    
    public void saveTransitionImage(){
        for (ContentRenderItf r : mainRenderList)
            r.saveTransitionImage();
        for (ContentRenderItf r : monitorRenderList)
            r.saveTransitionImage();
    }
    
    public void setSlideLive(String slide){
        for (ContentRenderItf r : mainRenderList)
            r.setSlide(slide);
        for (ContentRenderItf r : monitorRenderList)
            r.setSlide(slide);
    }
    
    public void setNextSlideLive(String slide){
        for (ContentRenderItf r : mainRenderList)
            r.setNextSlide(slide);
        for (ContentRenderItf r : monitorRenderList)
            r.setNextSlide(slide);
    }
    
    public void setClockLive(String text){
        for (ContentRenderItf r : mainRenderList)
            r.setClock(text);
        for (ContentRenderItf r : monitorRenderList)
            r.setClock(text);
    }
    
    public void setTimerLive(String timer){
        for (ContentRenderItf r : mainRenderList)
            r.setTimer(timer);
        for (ContentRenderItf r : monitorRenderList)
            r.setTimer(timer);
    }

    public void setSongAuthorLive(String songAuthor){
        for (ContentRenderItf r : mainRenderList)
            r.setSongAuthor(songAuthor);
        for (ContentRenderItf r : monitorRenderList)
            r.setSongAuthor(songAuthor);
    }

    public void setSongSourceLive(String songSource){
        for (ContentRenderItf r : mainRenderList)
            r.setSongSource(songSource);
        for (ContentRenderItf r : monitorRenderList)
            r.setSongSource(songSource);
    }
    
    public void setCopyrightLive(String copyright){
        for (ContentRenderItf r : mainRenderList)
            r.setCopyright(copyright);
        for (ContentRenderItf r : monitorRenderList)
            r.setCopyright(copyright);
    }
    
    public void setTemplateLive(String template){
        for (ContentRenderItf r : mainRenderList)
            r.setTemplate(template);
    }

    public void setTemplateMonitorLive(String template){
        for (ContentRenderItf r : monitorRenderList)
            r.setTemplate(template);
    }
    
    public synchronized void slideChange (int transictionTime){
        for (ContentRenderItf r : mainRenderList)
            r.slideChange(transictionTime);
        for (ContentRenderItf r : monitorRenderList)
            r.slideChange(transictionTime);
    }
    
    public void alertShow (int transictionTime){
        for (ContentRenderItf r : mainRenderList)
            r.alertShow(transictionTime);
        for (ContentRenderItf r : monitorRenderList)
            r.alertShow(transictionTime);
    }
    
    public void alertHide (int transictionTime){
        for (ContentRenderItf r : mainRenderList)
            r.alertHide(transictionTime);
        for (ContentRenderItf r : monitorRenderList)
            r.alertHide(transictionTime);
    }

    public void registerPreviewDisplay(ContentDisplayRenderer d){
        previewRender = new ContentRender(PREVIEW_WIDTH, getPreviewHeight(), d);
    }

    public void setTitlePreview(String title){
        previewRender.setTitle(title);
    }
    
    public void setSlidePreview(String t){
		previewRender.setSlide(t);
    }
    
    public void setNextSlidePreview(String t){
		previewRender.setNextSlide(t);
    }
    
    public void setClockPreview(String t){
		previewRender.setClock(t);
    }
    
    public void setTimerPreview(String t){
		previewRender.setTimer(t);
    }

    public void setSongAuthorPreview(String songAuthor){
		previewRender.setSongAuthor(songAuthor);
    }
    
    public void setSongSourcePreview(String songSource){
		previewRender.setSongSource(songSource);
    }
    
    public void setCopyrightPreview(String copyright){
		previewRender.setCopyright(copyright);
    }
    
    public void setTemplatePreview(String template){
        //if (template != null && !template.equals(""))
		previewRender.setTemplate(template);
    }
    
    public void updatePreview(){
		previewRender.slideShow(0);
    }

    public void setAlertText(String t){
        for (ContentRenderItf r : mainRenderList)
            r.setAlert(t);
        for (ContentRenderItf r : monitorRenderList)
            r.setAlert(t);
    }
    
    public void setTimerProgress(float f){
        for (ContentRenderItf r : mainRenderList)
            r.setTimerProgress(f);
        for (ContentRenderItf r : monitorRenderList)
            r.setTimerProgress(f);
    }
    
    public void setShowTimer(boolean b){
        for (ContentRenderItf r : mainRenderList)
            r.setShowTimer(b);
        for (ContentRenderItf r : monitorRenderList)
            r.setShowTimer(b);
    }

    public void setMainBlack(boolean i){
        for (ContentRenderItf r : mainRenderList)
            r.setBlack(i);
        for (ContentRenderItf r : monitorRenderList)
            r.setBlack(i);
    }
    
    public void slideShowMain(int i){
        for (ContentRenderItf r : mainRenderList)
            r.slideShow(i);
    }
    
    public void slideHideMain(int i){
        for (ContentRenderItf r : mainRenderList)
            r.slideHide(i);
    }

    public void paintBackground(BufferedImage img){
        for (ContentRenderItf r : mainRenderList)
            r.paintBackground(img);
        for (ContentRenderItf r : monitorRenderList)
            r.paintBackground(img);
    }
    
    public void setAlertTemplateMain(String template){
        for (ContentRenderItf r : mainRenderList)
            r.setAlertTemplate(template);
    }
    
    public void setAlertActiveMain(Boolean b){
        for (ContentRenderItf r : mainRenderList)
            r.setAlertActive(b);
    }

    public void setMonitorBlack(boolean i){
        for (ContentRenderItf r : monitorRenderList)
            r.setBlack(i);
    }
    
    public void slideShowMonitor(int i){
        for (ContentRenderItf r : monitorRenderList)
            r.slideShow(i);
    }
    
    public void slideHideMonitor(int i){
        for (ContentRenderItf r : monitorRenderList)
            r.slideHide(i);
    }

    public void setAlertTemplateMonitor(String template){
        for (ContentRenderItf r : monitorRenderList)
            r.setAlertTemplate(template);
    }
    
    public void setAlertActiveMonitor(Boolean b){
        for (ContentRenderItf r : monitorRenderList)
            r.setAlertActive(b);
    }

    public void setActiveImageLive(BufferedImage img){
        for (ContentRenderItf r : mainRenderList)
            r.setActiveImage(img);
        for (ContentRenderItf r : monitorRenderList)
            r.setActiveImage(img);
    }
    
    public void setActiveImagePreview(BufferedImage img){
        previewRender.setActiveImage(img);
    }

    public void setNextImageLive(BufferedImage img){
        for (ContentRenderItf r : mainRenderList)
            r.setNextImage(img);
        for (ContentRenderItf r : monitorRenderList)
            r.setNextImage(img);
    }

    public void setNextImagePreview(BufferedImage img){
        previewRender.setNextImage(img);
    }

    public void setMainShowBackground(boolean b){
        for (ContentRenderItf r : mainRenderList)
            r.setShowBackground(b);
    }

    public void setMainShowTemplate(boolean b){
        for (ContentRenderItf r : mainRenderList)
            r.setShowTemplate(b);
    }

    public void slideChangeFromTimerManager() {

        for (ContentRenderItf r : mainRenderList)
            r.slideChangeFromTimerManager();

        for (ContentRenderItf r : monitorRenderList)
            r.slideChangeFromTimerManager();
    }

    public void registerMainRender(ContentRenderItf r){
        mainRenderList.add(r);
    }

    public void registerMonitorRender(ContentRenderItf r){
        monitorRenderList.add(r);
    }

    public void setShowBackground(boolean b){

        for (ContentRenderItf r : mainRenderList)
            r.setShowBackground(b);

        for (ContentRenderItf r : monitorRenderList)
            r.setShowBackground(b);
    }

}


