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

import datasoul.config.ConfigObj;
import datasoul.templates.DisplayTemplate;
import datasoul.templates.ImageTemplateItem;
import datasoul.templates.TemplateItem;
import datasoul.templates.TemplateManager;
import datasoul.templates.TextTemplateItem;
import datasoul.templates.TimerProgressbarTemplateItem;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author root
 */
public class ContentRender {
    
    private DisplayTemplate template;
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
    
    protected BufferedImage transitionImage;
    private BufferedImage templateImage;
    private BufferedImage alertImage;
    private BufferedImage backgroundImage;
    final private BufferedImage outputImage;

    private LinkedList<ContentDisplay> displays;

    private boolean run;

    private boolean isBlack;
    private boolean templateNeedsTimer;
    private boolean alertNeedsTimer;

    /** Creates a new instance of ContentRender */
    public ContentRender() {
        updSemaphore = new Semaphore(0);
        slideTransition = TRANSITION_HIDE;
        slideTransTimerTotal = 1;
        slideTransTimer = 0;
        displays = new LinkedList<ContentDisplay>();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        transitionImage = gc.createCompatibleImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, Transparency.TRANSLUCENT);
        templateImage = gc.createCompatibleImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, Transparency.TRANSLUCENT);
        alertImage = gc.createCompatibleImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, Transparency.TRANSLUCENT);
        backgroundImage = gc.createCompatibleImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, Transparency.TRANSLUCENT);
        outputImage = gc.createCompatibleImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, Transparency.TRANSLUCENT);

        run = true;
        updThread = new UpdateThread();
        updThread.start();
    }
    
    public void shutdown(){
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

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.titleChanged = true;
    }

    public String getSongAuthor() {
        return songAuthor;
    }
    
    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
        this.songAuthorChanged = true;
    }
    
    public String getCopyright() {
        return copyright;
    }
    
    public void setCopyright(String copyright) {
        this.copyright = copyright;
        this.copyrightChanged = true;
    }
    
    public String getSongSource() {
        return songSource;
    }
    
    public void setSongSource(String songSource) {
        this.songSource = songSource;
        this.songSourceChanged = true;
    }
    
    public String getSlide() {
        return slide;
    }
    
    public void setSlide(String slide) {
        this.slide = slide;
        this.slideChanged = true;
    }
    
    public String getNextSlide() {
        return nextSlide;
    }
    
    public void setNextSlide(String nextSlide) {
        this.nextSlide = nextSlide;
        this.nextSlideChanged = true;
    }
    
    public String getClock() {
        return clock;
    }
    
    public void setClock(String clock) {
        this.clock = clock;
        this.clockChanged = true;
    }
    
    public String getTimer() {
        return timer;
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
    
    public String getAlert(){
        return this.timer;
    }
    
    public void setAlert(String alert){
        this.alert = alert;
        this.alertChanged = true;
    }
    
    public void setTemplate(String template){
        try{
            try{
                this.template = TemplateManager.getInstance().newDisplayTemplate(template);
            }catch(FileNotFoundException f){
                // inexistent template, fallback to default
                this.template = TemplateManager.getInstance().newDisplayTemplate( ConfigObj.getInstance().getTemplateText() );
            }
            this.templateChanged = true;

            this.templateNeedsTimer = this.template.useTimer();

        }catch(Exception e){
            e.printStackTrace();
            this.template = null;
        }
    }
    
    public String getTemplate(){
        if (template != null){
            return this.template.getName();
        }else{
            return null;
        }
    }
    
    public void setAlertTemplate(String template){
        try{
            this.alertTemplate = TemplateManager.getInstance().newDisplayTemplate(template);
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
        }else if (transictionTime < 0){
            updateDisplayValues();
            saveImage(templateImage, template, 1.0f);   
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

    public void registerDisplay(ContentDisplay d){
        synchronized(this){
            displays.add(d);
            d.registerOutputImage(outputImage);
        }
        updSemaphore.release();
    }
    
    /**
     * Update the screen only if needed.
     * That is, if the changes were only in content that isn't
     * being shown, no update is done.
     *
     * returns if a screen refresh is needed
     */
    public boolean updateDisplayValues(){
        
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
    
    
    private void updateScreen(){
        
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
            paintOutputBlack();
        }else{

            synchronized(outputImage){
                //paint it
                clearOutput();
                paintOutput(backgroundImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

                if (slideTransition == TRANSITION_CHANGE){
                    if (template.getTransitionKeepBGIdx() == DisplayTemplate.KEEP_BG_YES && paintSlideLevel < 1.0f){
                        paintOutput(transitionImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                        paintOutput(templateImage, AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, paintSlideLevel));
                    }else{
                        paintOutput(transitionImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - paintSlideLevel));
                        paintOutput(templateImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintSlideLevel));
                    }
                }else{
                    paintOutput(templateImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintSlideLevel));
                }

                if (paintAlertLevel > 0){
                    paintOutput(alertImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, paintAlertLevel));
                }
            }
        }

        synchronized(this){
            for (ContentDisplay d : displays){
                d.flip();
            }
        }

    }
    
    

    private void saveImage(BufferedImage dst, DisplayTemplate tpl, float alpha){
        synchronized(dst){
            Graphics2D g = dst.createGraphics();

            // Clear it first
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
            g.fillRect(0, 0, dst.getWidth(), dst.getHeight());

            if (tpl != null){
                tpl.paint(g, alpha);
            }   
        }
    }
    
    public void saveTransitionImage(){
        saveImage(transitionImage, template, 1.0f);
    }

    void setBlack(boolean b) {
        this.isBlack = b;
        updSemaphore.release();
    }

    public void paintBackground(BufferedImage img) {
        if (img != null && backgroundImage != null){
            Graphics2D g = backgroundImage.createGraphics();
            g.drawImage(img, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), null);
            updSemaphore.release();
        }
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

    public boolean getNeedTimer(){
        return (templateNeedsTimer || (alertActive && alertNeedsTimer));
    }

    private void paintOutput(BufferedImage img, AlphaComposite rule){
        Graphics2D g = outputImage.createGraphics();
        g.setComposite( rule );
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    private void paintOutputBlack(){
        Graphics2D g = outputImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
    }

    private void clearOutput(){
        Graphics2D g = outputImage.createGraphics();

        // Clear it first
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
            g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
        }finally{
            g.setComposite(oldComp);
        }
    }

}

