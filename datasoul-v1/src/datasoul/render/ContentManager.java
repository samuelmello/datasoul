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
import datasoul.templates.DisplayTemplate;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class ContentManager {
    
    private static ContentManager instance;
    
    private ArrayList<ContentRender> previewRenderList;
    private ArrayList<ContentRender> liveRenderList;
    private ArrayList<ContentRender> mainRenderList;
    private ArrayList<ContentRender> monitorRenderList;
    
    
    /** Creates a new instance of ContentManager */
    private ContentManager() {
        previewRenderList = new ArrayList<ContentRender>();
        liveRenderList = new ArrayList<ContentRender>();
        mainRenderList = new ArrayList<ContentRender>();
        monitorRenderList = new ArrayList<ContentRender>();

        if ( ConfigObj.getInstance().getMonitorOutput() ){
            liveRenderList.add( ContentManager.getMonitorDisplay() );
            monitorRenderList.add( ContentManager.getMonitorDisplay() );
        }
        
        if ( ConfigObj.getInstance().getMainOutput() ){
            liveRenderList.add( ContentManager.getMainDisplay() );
            mainRenderList.add( ContentManager.getMainDisplay() );
        }
    }
    
    static public ContentManager getInstance(){
        if (instance == null ){
            instance = new ContentManager();
        }
        return instance;
    }
    
    public void setTitleLive(String title){
        for (ContentRender r : liveRenderList){
            r.setTitle(title);
        }
    }
    
    public void saveTransitionImage(){
        for (ContentRender r : liveRenderList){
            r.saveTransitionImage();
        }
    }
    
    public void setSlideLive(String slide){
        for (ContentRender r : liveRenderList){
            r.setSlide(slide);
        }
    }
    
    public void setNextSlideLive(String slide){
        for (ContentRender r : liveRenderList){
            r.setNextSlide(slide);
        }
    }
    
    public void setClockLive(String text){
        for (ContentRender r : liveRenderList){
            r.setClock(text);
        }
    }
    
    public void setTimerLive(String timer){
        for (ContentRender r : liveRenderList){
            r.setTimer(timer);
        }
    }

    public void setSongAuthorLive(String songAuthor){
        for (ContentRender r : liveRenderList){
            r.setSongAuthor(songAuthor);
        }
    }

    public void setSongSourceLive(String songSource){
        for (ContentRender r : liveRenderList){
            r.setSongSource(songSource);
        }
    }
    
    public void setCopyrightLive(String copyright){
        for (ContentRender r : liveRenderList){
            r.setCopyright(copyright);
        }
    }
    
    public void setTemplateLive(String template){
        for (ContentRender r : mainRenderList){
            r.setTemplate(template);
        }
    }

    public void setTemplateMonitorLive(String template){
        for (ContentRender r : monitorRenderList){
            r.setTemplate(template);
        }
    }
    
    public synchronized void slideChange (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.slideChange(transictionTime);
        }
    }
    
    public void alertShow (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.alertShow(transictionTime);
        }
    }
    
    public void alertHide (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.alertHide(transictionTime);
        }
    }

        

    public void setTitlePreview(String title){
        for (ContentRender r : previewRenderList){
            r.setTitle(title);
        }
    }
    
    public void setSlidePreview(String t){
        for (ContentRender r : previewRenderList){
            r.setSlide(t);
        }
    }
    
    public void setNextSlidePreview(String t){
        for (ContentRender r : previewRenderList){
            r.setNextSlide(t);
        }
    }
    
    public void setClockPreview(String t){
        for (ContentRender r : previewRenderList){
            r.setClock(t);
        }
    }
    
    public void setTimerPreview(String t){
        for (ContentRender r : previewRenderList){
            r.setTimer(t);
        }
    }

    public void setSongAuthorPreview(String songAuthor){
        for (ContentRender r : previewRenderList){
            r.setSongAuthor(songAuthor);
        }
    }
    
    public void setSongSourcePreview(String songSource){
        for (ContentRender r : previewRenderList){
            r.setSongSource(songSource);
        }
    }
    
    public void setCopyrightPreview(String copyright){
        for (ContentRender r : previewRenderList){
            r.setCopyright(copyright);
        }
    }
    
    public void setTemplatePreview(String t){
        for (ContentRender r : previewRenderList){
            r.setTemplate(t);
        }
    }
    
    public void updatePreview(){
        for (ContentRender r : previewRenderList){
            r.slideShow(0);
        }
    }

    public void registerMainLiveRender(ContentRender r){
        liveRenderList.add(r);
        mainRenderList.add(r);
    }
    
    public void registerMonitorLiveRender(ContentRender r){
        liveRenderList.add(r);
        monitorRenderList.add(r);
    }
   
    public void setAlertText(String t){
        for (ContentRender r : liveRenderList){
            r.setAlert(t);
        }
    }
    
    public void registerPreviewPanel(ContentRender r){
        if (!previewRenderList.contains(r)){
            r.slideShow(1);
            previewRenderList.add(r);
        }
    }

    public void setTimerProgress(float f){
        for (ContentRender r : liveRenderList){
            r.setTimerProgress(f);
        }
    }
    
    public void setShowTimer(boolean b){
        for (ContentRender r : liveRenderList){
            r.setShowTimer(b);
        }
    }

    public void setMainBlack(int i){
        for (ContentRender r : mainRenderList){
            r.setBlack(i);
        }
    }
    
    public void slideShowMain(int i){
        for (ContentRender r : mainRenderList){
            r.slideShow(i);
        }
    }
    
    public void slideHideMain(int i){
        for (ContentRender r : mainRenderList){
            r.slideHide(i);
        }
    }

    public void paintBackgroundMain(BufferedImage img){
        for (ContentRender r : mainRenderList){
            r.paintBackground(img);
        }
    }
    
    public void setAlertTemplateMain(String template){
        for (ContentRender r : mainRenderList){
            r.setAlertTemplate(template);
        }
    }
    
    public void setAlertActiveMain(Boolean b){
        for (ContentRender r : mainRenderList){
            r.setAlertActive(b);
        }
    }

    public void setMonitorBlack(int i){
        for (ContentRender r : monitorRenderList){
            r.setBlack(i);
        }
    }
    
    public void slideShowMonitor(int i){
        for (ContentRender r : monitorRenderList){
            r.slideShow(i);
        }
    }
    
    public void slideHideMonitor(int i){
        for (ContentRender r : monitorRenderList){
            r.slideHide(i);
        }
    }

    public void paintBackgroundMonitor(BufferedImage img){
        for (ContentRender r : monitorRenderList){
            r.paintBackground(img);
        }
    }
    
    public void setAlertTemplateMonitor(String template){
        for (ContentRender r : monitorRenderList){
            r.setAlertTemplate(template);
        }
    }
    
    public void setAlertActiveMonitor(Boolean b){
        for (ContentRender r : monitorRenderList){
            r.setAlertActive(b);
        }
    }
    
    
    static private ContentRender mainDisplay;
    static private ContentRender monitorDisplay;
    static private String mainDisplayEngine = "SwingDisplay";
    static private String monitorDisplayEngine = "SwingDisplay";
    
    static public boolean isMainDisplayActive(){
        return mainDisplay != null;
    }
    
    static public ContentRender getMainDisplay(){

        if (mainDisplay == null ){
            /*
            try{
                mainDisplay = (ContentRender) Class.forName( "datasoul.render." + mainDisplayEngine ).newInstance();
            }catch(Exception e){
                e.printStackTrace();
                mainDisplay = new SwingContentRender();
            }
             */
            mainDisplay = new SwingContentRender();
            
            int width, height, top, left;
            try{
                width = Integer.parseInt(ConfigObj.getInstance().getMainOutputSizeWidth());
            }catch(Exception e){
                width = DisplayTemplate.TEMPLATE_WIDTH;
            }
            try{
                height = Integer.parseInt(ConfigObj.getInstance().getMainOutputSizeHeight());
            }catch(Exception e){
                height = DisplayTemplate.TEMPLATE_HEIGHT;
            }
            try{
                top = Integer.parseInt(ConfigObj.getInstance().getMainOutputPositionTop());
            }catch(Exception e){
                top = 0;
            }
            try{
                left = Integer.parseInt(ConfigObj.getInstance().getMainOutputPositionLeft());
            }catch(Exception e){
                left = 0;
            }
            
            mainDisplay.initDisplay(width, height, top, left);
            mainDisplay.setWindowTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Main_Display"));
        }
        return mainDisplay;
    }
    
    static public boolean isMonitorDisplayActive(){
        return monitorDisplay != null;
    }
    
    
    static public ContentRender getMonitorDisplay(){
        
        if (monitorDisplay == null  ){

            /*
            try{
                monitorDisplay = (ContentRender) Class.forName( "datasoul.render." + monitorDisplayEngine ).newInstance();
            }catch(Exception e){
                e.printStackTrace();
                monitorDisplay = new SwingContentRender();
            }
            */
            
            monitorDisplay = new SwingContentRender();
            
            int width, height, top, left;
            try{
                width = Integer.parseInt(ConfigObj.getInstance().getMonitorOutputSizeWidth());
            }catch(Exception e){
                width = DisplayTemplate.TEMPLATE_WIDTH;
            }
            try{
                height = Integer.parseInt(ConfigObj.getInstance().getMonitorOutputSizeHeight());
            }catch(Exception e){
                height = DisplayTemplate.TEMPLATE_HEIGHT;
            }
            try{
                top = Integer.parseInt(ConfigObj.getInstance().getMonitorOutputPositionTop());
            }catch(Exception e){
                top = 0;
            }
            try{
                left = Integer.parseInt(ConfigObj.getInstance().getMonitorOutputPositionLeft());
            }catch(Exception e){
                left = 0;
            }

            monitorDisplay.initDisplay(width, height, top, left);
            monitorDisplay.setWindowTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Monitor_Display"));
        }
        return monitorDisplay;
    }

    static public void setMainDisplayEngine(String engine){
        mainDisplayEngine = engine;
    }
    
    static public void setMonitorDisplayEngine(String engine) {
        monitorDisplayEngine = engine;
    }
    
}
