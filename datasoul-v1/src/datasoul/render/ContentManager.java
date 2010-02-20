/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.config.ConfigObj;
import datasoul.datashow.PreviewPanel;
import datasoul.templates.DisplayTemplate;
import java.awt.image.BufferedImage;

/**
 *
 * @author root
 */
public class ContentManager {
    
    private static ContentManager instance;
    
    private ContentRender previewRender;
    private ContentRender mainRender;
    private ContentRender monitorRender;

    private SwingDisplayFrame mainDisplay;
    private SwingDisplayFrame monitorDisplay;

    public static final int PREVIEW_WIDTH = 160;


    /** Creates a new instance of ContentManager */
    private ContentManager() {
        mainRender = new ContentRender( ConfigObj.getInstance().getMainRenderWidth(), ConfigObj.getInstance().getMainRenderHeight());

        if ( ConfigObj.getInstance().getMonitorOutput() ){
            monitorRender = new ContentRender( ConfigObj.getInstance().getMonitorRenderWidth(), ConfigObj.getInstance().getMonitorRenderHeight());
        }

        previewRender = new ContentRender(PREVIEW_WIDTH, getPreviewHeight());
        
    }
    
    static public ContentManager getInstance(){
        if (instance == null ){
            instance = new ContentManager();
        }
        return instance;
    }

    public int getPreviewHeight(){
        return ConfigObj.getInstance().getMainOutputDeviceObj().getProportionalHeight(PREVIEW_WIDTH);
    }

    public int getPreviewMonitorHeight(){

        if (ConfigObj.getInstance().getMonitorOutput()){
            return ConfigObj.getInstance().getMonitorOutputDeviceObj().getProportionalHeight(PREVIEW_WIDTH);
        }else{
            return getPreviewHeight();
        }
    }

    public void setTitleLive(String title){
        mainRender.setTitle(title);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setTitle(title);
    }
    
    public void saveTransitionImage(){
        mainRender.saveTransitionImage();
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.saveTransitionImage();
    }
    
    public void setSlideLive(String slide){
        mainRender.setSlide(slide);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setSlide(slide);
    }
    
    public void setNextSlideLive(String slide){
        mainRender.setNextSlide(slide);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setNextSlide(slide);
    }
    
    public void setClockLive(String text){
        mainRender.setClock(text);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setClock(text);
    }
    
    public void setTimerLive(String timer){
        mainRender.setTimer(timer);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setTimer(timer);
    }

    public void setSongAuthorLive(String songAuthor){
        mainRender.setSongAuthor(songAuthor);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setSongAuthor(songAuthor);
    }

    public void setSongSourceLive(String songSource){
        mainRender.setSongSource(songSource);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setSongSource(songSource);
    }
    
    public void setCopyrightLive(String copyright){
        mainRender.setCopyright(copyright);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setCopyright(copyright);
    }
    
    public void setTemplateLive(String template){
        mainRender.setTemplate(template);
    }

    public void setTemplateMonitorLive(String template){
        if ( ConfigObj.getInstance().getMonitorOutput() ){
            monitorRender.setTemplate(template);
        }

    }
    
    public synchronized void slideChange (int transictionTime){
        mainRender.slideChange(transictionTime);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.slideChange(transictionTime);
    }
    
    public void alertShow (int transictionTime){
        mainRender.alertShow(transictionTime);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.alertShow(transictionTime);
    }
    
    public void alertHide (int transictionTime){
        mainRender.alertHide(transictionTime);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.alertHide(transictionTime);
    }

    public void registerMainDisplay(ContentDisplay d){
        mainRender.registerDisplay(d);
    }

    public void registerMonitorDisplay(ContentDisplay d){
        if ( ConfigObj.getInstance().getMonitorOutput() ){
            monitorRender.registerDisplay(d);
        }
    }

    public void registerPreviewDisplay(ContentDisplay d){
        previewRender.registerDisplay(d);
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
    
    public void setTemplatePreview(String t){
		previewRender.setTemplate(t);
    }
    
    public void updatePreview(){
		previewRender.slideShow(0);
    }

    public void setAlertText(String t){
        mainRender.setAlert(t);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setAlert(t);
    }
    
    public void setTimerProgress(float f){
        mainRender.setTimerProgress(f);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setTimerProgress(f);
    }
    
    public void setShowTimer(boolean b){
        mainRender.setShowTimer(b);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setShowTimer(b);
    }

    public void setMainBlack(boolean i){
        mainRender.setBlack(i);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setBlack(i);
    }
    
    public void slideShowMain(int i){
        mainRender.slideShow(i);
    }
    
    public void slideHideMain(int i){
        mainRender.slideHide(i);
    }

    public void paintBackgroundMain(BufferedImage img){
        mainRender.paintBackground(img);
    }
    
    public void setAlertTemplateMain(String template){
        mainRender.setAlertTemplate(template);
    }
    
    public void setAlertActiveMain(Boolean b){
        mainRender.setAlertActive(b);
    }

    public void setMonitorBlack(boolean i){
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setBlack(i);
    }
    
    public void slideShowMonitor(int i){
		if ( ConfigObj.getInstance().getMonitorOutput() )
                    monitorRender.slideShow(i);
    }
    
    public void slideHideMonitor(int i){
		if ( ConfigObj.getInstance().getMonitorOutput() )
                    monitorRender.slideHide(i);
    }

    public void paintBackgroundMonitor(BufferedImage img){
		if ( ConfigObj.getInstance().getMonitorOutput() )
                    monitorRender.paintBackground(img);
    }
    
    public void setAlertTemplateMonitor(String template){
		if ( ConfigObj.getInstance().getMonitorOutput() )
                    monitorRender.setAlertTemplate(template);
    }
    
    public void setAlertActiveMonitor(Boolean b){
		if ( ConfigObj.getInstance().getMonitorOutput() )
                    monitorRender.setAlertActive(b);
    }

    public void setActiveImageLive(BufferedImage img){
        mainRender.setActiveImage(img);
        if ( ConfigObj.getInstance().getMonitorOutput() )
            monitorRender.setActiveImage(img);
    }
    
    public void setActiveImagePreview(BufferedImage img){
		previewRender.setActiveImage(img);
    }

    public void setNextImageLive(BufferedImage img){
        mainRender.setNextImage(img);
        if ( ConfigObj.getInstance().getMonitorOutput() )
                monitorRender.setNextImage(img);
    }

    public void setNextImagePreview(BufferedImage img){
		previewRender.setNextImage(img);
    }

    public boolean isMainDisplayActive(){
        return mainDisplay != null;
    }
    
    public void initMainDisplay(){
        mainDisplay = new SwingDisplayFrame();
        mainDisplay.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Main_Display"));
        mainDisplay.registerAsMain();
    }
    
    public boolean isMonitorDisplayActive(){
        return monitorDisplay != null;
    }
    
    
    public void initMonitorDisplay(){
        
        if (ConfigObj.getInstance().getMonitorOutput() ){
            monitorDisplay = new SwingDisplayFrame();
            monitorDisplay.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Monitor_Display"));
            monitorDisplay.registerAsMonitor();
        }
    }

    public void slideChangeFromTimerManager() {

        if (mainRender.getNeedTimer()){
            mainRender.slideChange(-1);
        }

        if ( ConfigObj.getInstance().getMonitorOutput() && monitorRender.getNeedTimer() ){
            monitorRender.slideChange(-1);
        }
    }

    public void setOutputVisible(boolean b){


        if (b){
            ConfigObj.getInstance().getMainOutputDeviceObj().setWindowFullScreen(mainDisplay);
            if (ConfigObj.getInstance().getMonitorOutput())
                ConfigObj.getInstance().getMonitorOutputDeviceObj().setWindowFullScreen(monitorDisplay);
        }else{
            ConfigObj.getInstance().getMainOutputDeviceObj().closeFullScreen();
            if (ConfigObj.getInstance().getMonitorOutput())
                ConfigObj.getInstance().getMonitorOutputDeviceObj().closeFullScreen();
        }

    }

    public boolean getOutputHasFocus(){
        if (mainDisplay.hasFocus())
            return true;

        if (ConfigObj.getInstance().getMonitorOutput())
            return monitorDisplay.hasFocus();

        return false;
    }

}
