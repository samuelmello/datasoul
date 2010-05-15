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
 * ContentRender.java
 *
 * Created on March 20, 2006, 8:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.config.BackgroundConfig;
import datasoul.templates.DisplayTemplate;
import datasoul.templates.ImageTemplateItem;
import datasoul.templates.TemplateItem;
import datasoul.templates.TemplateManager;
import datasoul.templates.TextTemplateItem;
import datasoul.templates.TimerProgressbarTemplateItem;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

/**
 *
 * @author root
 */
public class ContentRender implements ContentRenderItf {
    
    protected DisplayTemplate template;
    private boolean templateChanged;
    private String title;
    private boolean titleChanged;
    private String slide;
    private boolean slideChanged;
    private String nextSlide;
    private boolean nextSlideChanged;
    private String clock;
    private boolean clockChanged;
    private String timer;
    private boolean timerChanged;
    private String alert;
    private boolean alertChanged;
    private String songAuthor;
    private boolean songAuthorChanged;
    private String copyright;
    private boolean copyrightChanged;
    private String songSource;
    private boolean songSourceChanged;
    private DisplayTemplate alertTemplate;
    private boolean alertActive;
    private boolean showTimer;
    private float timerProgress;
    private BufferedImage activeImage;
    private boolean activeImageChanged;
    private BufferedImage nextImage;
    private boolean nextImageChanged;
    private boolean showHideNeedUpdate;

    private int width;
    private int height;
    
    private Semaphore updSemaphore;
    private UpdateThread updThread;
    
    public static final int TRANSITION_HIDE = 0;
    public static final int TRANSITION_SHOW = 1;
    public static final int TRANSITION_CHANGE = 2;
    
    private int slideTransTimer;
    private int slideTransTimerTotal;
    private int slideTransition;
    
    private int alertTransTimer;
    private int alertTransTimerTotal;
    private int alertTransition;
    
    final protected BufferedImage transitionImage;
    final protected BufferedImage templateImage;
    final protected BufferedImage alertImage;
    final protected BufferedImage backgroundImage;

    private ContentDisplayRenderer display;

    private boolean run;

    private boolean isBlack;
    private boolean templateNeedsTimer;
    private boolean alertNeedsTimer;

    private boolean showTemplate;
    private boolean showBackground;

    /** Creates a new instance of ContentRender */
    public ContentRender(int width, int height, ContentDisplayRenderer display) {
        updSemaphore = new Semaphore(0);
        slideTransition = TRANSITION_HIDE;
        slideTransTimerTotal = 1;
        slideTransTimer = 0;
        showTemplate = true;
        showBackground = true;
        this.display = display;
        this.width = width;
        this.height = height;

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        transitionImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        templateImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        alertImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        backgroundImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        run = true;
        updThread = new UpdateThread();
        updThread.start();
    }

    protected void shutdown(){
        run = false;
        updSemaphore.release();
        updThread.interrupt();
    }

    public void setActiveImage(BufferedImage img){
        this.activeImage = img;
        this.activeImageChanged = true;
    }

    public void setNextImage(BufferedImage img){
        this.nextImage = img;
        this.nextImageChanged = true;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleChanged = true;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
        this.songAuthorChanged = true;
    }
    
    public void setCopyright(String copyright) {
        this.copyright = copyright;
        this.copyrightChanged = true;
    }
    
    public void setSongSource(String songSource) {
        this.songSource = songSource;
        this.songSourceChanged = true;
    }
    
    public void setSlide(String slide) {
        this.slide = slide;
        this.slideChanged = true;
    }
    
    public void setNextSlide(String nextSlide) {
        this.nextSlide = nextSlide;
        this.nextSlideChanged = true;
    }
    
    public void setClock(String clock) {
        this.clock = clock;
        this.clockChanged = true;
    }
    
    public void setTimer(String timer) {
        this.timer = timer;
        this.timerChanged = true;
    }
    
    public void setShowTimer(boolean show){
        this.showTimer = show;
        this.timerChanged = true;
    }
    
    public void setTimerProgress(float f){
        this.timerProgress = f;
        this.timerChanged  = true;
    }
    
    public void setAlert(String alert){
        this.alert = alert;
        this.alertChanged = true;
    }
    
    public void setTemplate(String template){
        try{
            if (template != null && !template.equals("")){
                this.template = TemplateManager.getInstance().newDisplayTemplate(template, width, height);
                this.templateChanged = true;

                this.templateNeedsTimer = this.template.useTimer();
            }else{
                this.template = null;
                this.templateChanged = true;
                this.templateNeedsTimer = false;
            }

        }catch(Exception e){
            e.printStackTrace();
            this.template = null;
        }
    }
    
    public void setAlertTemplate(String template){
        try{
            this.alertTemplate = TemplateManager.getInstance().newDisplayTemplate(template, width, height);
            this.alertNeedsTimer = this.alertTemplate.useTimer();
        }catch(Exception e){
            e.printStackTrace();
            this.alertTemplate = null;
        }
    
    }
    
    public void setAlertActive(boolean active){
        this.alertActive = active;
        this.showHideNeedUpdate = true;
    }
    
    public void slideShow(int transictionTime){
        slideTransition = TRANSITION_SHOW;
        if (transictionTime >= 0){
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
            showHideNeedUpdate = true;
            updateDisplayValues();
            saveImage(templateImage, template, 1.0f);
            display.paintTemplate(templateImage);
        }
        update();
    }
    
    public void slideChange(int transictionTime){
        if (transictionTime >= 0 && slideTransition != TRANSITION_HIDE){
            slideTransition = TRANSITION_CHANGE;
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
            updateDisplayValues();
            saveImage(templateImage, template, 1.0f);   
            display.paintTemplate(templateImage);
        }else if (transictionTime < 0){
            updateDisplayValues();
            saveImage(templateImage, template, 1.0f);   
            display.paintTemplate(templateImage);
        }
        update();
    }
    
    public void slideHide(int transictionTime){
        slideTransition = TRANSITION_HIDE;
        if (transictionTime >= 0){
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
            showHideNeedUpdate = true;
        }
        update();
    }
    
    public void alertShow(int transictionTime){
        alertTransition = TRANSITION_SHOW;
        if (transictionTime >= 0){
            alertTransTimer = transictionTime;
            alertTransTimerTotal = transictionTime;
            updateDisplayValues();
            saveImage(alertImage, alertTemplate, 1.0f);
            display.paintAlert(alertImage);
        }
        update();
    }
    
    public void alertHide(int transictionTime){
        alertTransition = TRANSITION_HIDE;
        if (transictionTime >= 0){
            alertTransTimer = transictionTime;
            alertTransTimerTotal = transictionTime;
        }
        update();
    }
    
    
    private void update(){
        updSemaphore.release();
    }

    /**
     * Update the screen only if needed.
     * That is, if the changes were only in content that isn't
     * being shown, no update is done.
     *
     * returns if a screen refresh is needed
     */
    protected boolean updateDisplayValues(){
        
            boolean needUpdate = false;
            
            needUpdate = showHideNeedUpdate || templateChanged;
            
            int content;
            if (template != null){
                synchronized(template){
                    for (TemplateItem t : template.getItems() ){
                        if (t instanceof TextTemplateItem) {
                            content = ((TextTemplateItem)t).getContentIdx();
                            if ( (templateChanged || titleChanged) && content == TextTemplateItem.CONTENT_TITLE) {
                                ((TextTemplateItem)t).setText(title);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || slideChanged) && content == TextTemplateItem.CONTENT_SLIDE) {
                                ((TextTemplateItem)t).setText(slide);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || nextSlideChanged) && content == TextTemplateItem.CONTENT_NEXTSLIDE) {
                                ((TextTemplateItem)t).setText(nextSlide);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || clockChanged) && content == TextTemplateItem.CONTENT_CLOCK) {
                                ((TextTemplateItem)t).setText(clock);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || timerChanged) && content == TextTemplateItem.CONTENT_TIMER) {
                                ((TextTemplateItem)t).setText(timer);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || alertChanged) && content == TextTemplateItem.CONTENT_ALERT) {
                                ((TextTemplateItem)t).setText(alert);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || songAuthorChanged) && content == TextTemplateItem.CONTENT_SONGAUTHOR) {
                                ((TextTemplateItem)t).setText(songAuthor);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || copyrightChanged) && content == TextTemplateItem.CONTENT_COPYRIGHT) {
                                ((TextTemplateItem)t).setText(copyright);
                                needUpdate = true;
                                continue;
                            }
                            if ( (templateChanged || songSourceChanged) && content == TextTemplateItem.CONTENT_SONGSOURCE) {
                                ((TextTemplateItem)t).setText(songSource);
                                needUpdate = true;
                                continue;
                            }
                        }else if (t instanceof TimerProgressbarTemplateItem && (templateChanged || timerChanged)){
                            ((TimerProgressbarTemplateItem)t).setPosition(timerProgress);
                            ((TimerProgressbarTemplateItem)t).setShowTimer( showTimer );
                            needUpdate = true;
                            continue;
                        }else if (t instanceof ImageTemplateItem){
                            content = ((ImageTemplateItem)t).getContentIdx();
                            if ((templateChanged || activeImageChanged) && content == ImageTemplateItem.IMAGE_CONTENT_SLIDE){
                                ((ImageTemplateItem)t).setImage(activeImage);
                                needUpdate = true;
                                continue;
                            }
                            if ((templateChanged || nextImageChanged) && content == ImageTemplateItem.IMAGE_CONTENT_NEXT_SLIDE){
                                ((ImageTemplateItem)t).setImage(nextImage);
                                needUpdate = true;
                                continue;
                            }
                        }// if is TextTemplateItem
                    }// for need update
                }//synchornized
            }// if template not null
            
            if (alertActive){
                for (TemplateItem t : alertTemplate.getItems() ){
                    if (t instanceof TextTemplateItem) {
                        content = ((TextTemplateItem)t).getContentIdx();
                        if ( titleChanged && content == TextTemplateItem.CONTENT_TITLE) {
                            ((TextTemplateItem)t).setText(title);
                            needUpdate = true;
                            continue;
                        }
                        if ( slideChanged && content == TextTemplateItem.CONTENT_SLIDE) {
                            ((TextTemplateItem)t).setText(slide);
                            needUpdate = true;
                            continue;
                        }
                        if ( nextSlideChanged && content == TextTemplateItem.CONTENT_NEXTSLIDE) {
                            ((TextTemplateItem)t).setText(nextSlide);
                            needUpdate = true;
                            continue;
                        }
                        if ( clockChanged && content == TextTemplateItem.CONTENT_CLOCK) {
                            ((TextTemplateItem)t).setText(clock);
                            needUpdate = true;
                            continue;
                        }
                        if ( timerChanged && content == TextTemplateItem.CONTENT_TIMER) {
                            ((TextTemplateItem)t).setText(timer);
                            needUpdate = true;
                            continue;
                        }
                        if ( alertChanged && content == TextTemplateItem.CONTENT_ALERT) {
                            ((TextTemplateItem)t).setText(alert);
                            needUpdate = true;
                            continue;
                        }
                        if ( songAuthorChanged && content == TextTemplateItem.CONTENT_SONGAUTHOR) {
                            ((TextTemplateItem)t).setText(songAuthor);
                            needUpdate = true;
                            continue;
                        }
                        if ( copyrightChanged && content == TextTemplateItem.CONTENT_COPYRIGHT) {
                            ((TextTemplateItem)t).setText(copyright);
                            needUpdate = true;
                            continue;
                        }
                        if ( songSourceChanged && content == TextTemplateItem.CONTENT_SONGSOURCE) {
                            ((TextTemplateItem)t).setText(songSource);
                            needUpdate = true;
                            continue;
                        }
                    }else if (t instanceof TimerProgressbarTemplateItem && timerChanged){
                        ((TimerProgressbarTemplateItem)t).setPosition(timerProgress);
                        ((TimerProgressbarTemplateItem)t).setShowTimer( showTimer );
                        continue;
                    }// if is TextTemplateItem
                }// for need update
            }

        // everything has been updated
        if (slideTransTimer == 0 && alertTransTimer == 0){
            templateChanged = false;
            titleChanged = false;
            slideChanged = false;
            nextSlideChanged = false;
            clockChanged = false;
            timerChanged = false;
            alertChanged = false;
            songAuthorChanged = false;
            copyrightChanged = false;
            songSourceChanged = false;
            showHideNeedUpdate = false;
            activeImageChanged = false;
        }
            
            
        return needUpdate;    
           
    }
    
    
    private synchronized void updateScreen(){
        
        float paintSlideLevel;
        float paintAlertLevel;
        
        // Determine the levels:
        if (alertTransTimerTotal > 0)
            paintAlertLevel = (float) alertTransTimer / (float) alertTransTimerTotal;
        else
            paintAlertLevel = 0;

        if (alertTransition != TRANSITION_HIDE){
            paintAlertLevel = 1 - paintAlertLevel;
        }
        
        if (slideTransTimerTotal > 0)
            paintSlideLevel = (float) slideTransTimer / (float) slideTransTimerTotal;
        else
            paintSlideLevel = 0;

        if (slideTransition != TRANSITION_HIDE){
            paintSlideLevel = 1 - paintSlideLevel;
        }



        if (isBlack){
            display.updateScreen(true, false, 0, 0, 0, 0);
        }else{
            float bglevel, slidelevel, transitionlevel;
            boolean keepbg = (template != null && template.getTransitionKeepBGIdx() == DisplayTemplate.KEEP_BG_YES && paintSlideLevel < 1.0f);

            if (showBackground){
                bglevel = (BackgroundConfig.getInstance().getModeAsInt() == BackgroundConfig.MODE_STATIC) ? 1.0f : 0.0f;
            }else{
                bglevel = 0.0f;
            }

            if (showTemplate){
                slidelevel = paintSlideLevel;
                if (slideTransition == TRANSITION_CHANGE){
                    transitionlevel = 1-paintSlideLevel;
                }else{
                    keepbg = false;
                    transitionlevel = 0.0f;
                }

            }else{
                slidelevel = 0.0f;
                transitionlevel = 0.0f;
            }

            display.updateScreen(false, keepbg, bglevel, transitionlevel, slidelevel, paintAlertLevel);

        }

    }
    
    protected void saveImage(BufferedImage dst, DisplayTemplate tpl, float alpha){
        synchronized(dst){
            Graphics2D g = dst.createGraphics();

            // Clear it first
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
            g.fillRect(0, 0, dst.getWidth(), dst.getHeight());

            if (tpl != null){
                tpl.paint(g, alpha);
            }
            g.dispose();
        }
    }
    
    public void saveTransitionImage(){
        updateDisplayValues();
        saveImage(transitionImage, template, 1.0f);
        display.paintTransition(transitionImage);
    }

    public void setBlack(boolean b) {
        this.isBlack = b;
        updSemaphore.release();
    }

    public void paintBackground(BufferedImage img) {
        if (img != null && backgroundImage != null){
            Graphics2D g = backgroundImage.createGraphics();
            g.drawImage(img, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), null);
            g.dispose();
            display.paintBackground(backgroundImage);

            updSemaphore.release();
        }
    }

    public void setShowTemplate(boolean b) {
        this.showTemplate = b;
    }

    public void setShowBackground(boolean b) {
        this.showBackground = b;
    }
    
    private class UpdateThread extends Thread {
        public void run(){
            long t1, t2, t3, sleepTime;
            while (run){
                try{
                    updSemaphore.acquire();
                    
                    while ( slideTransTimer > 0 || alertTransTimer > 0){
                        t1 = System.currentTimeMillis();
                        
                        if (updateDisplayValues()){
                            updateScreen();
                        }
                        
                        t2 = System.currentTimeMillis();
                        
                        sleepTime = 50 - (t2 - t1);
                        
                        if (sleepTime > 0){
                            Thread.sleep( sleepTime );
                        }
                        
                        t3 = System.currentTimeMillis();
                        
                        slideTransTimer -= (t3 - t1);
                        alertTransTimer -= (t3 - t1);
                        
                        if (slideTransTimer < 0)
                            slideTransTimer = 0;
                        
                        if (alertTransTimer < 0)
                            alertTransTimer = 0;
                        
                    }
                    
                    updateDisplayValues();
                    updateScreen();
                    
                }catch(Exception e){
                    // ignore
                    e.printStackTrace();
                }
            }
        }
    }

    public void slideChangeFromTimerManager(){
        if (templateNeedsTimer || (alertActive && alertNeedsTimer)){
            slideChange(-1);
        }
    }

}


