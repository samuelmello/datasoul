/*
 * ContentRender.java
 *
 * Created on March 20, 2006, 8:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;
import datasoul.templates.TemplateItem;
import datasoul.templates.TemplateManager;
import datasoul.templates.TextTemplateItem;
import java.util.concurrent.Semaphore;

/**
 *
 * @author root
 */
public abstract class ContentRender {
    
    private String template;
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
    private String alertTemplate;
    private boolean alertTemplateChanged;
    private boolean alertActive;
    private boolean alertActiveChanged;
    
    private boolean monitor;
    
    private Semaphore updSemaphore;
    private UpdateThread updThread;
    
    /** Creates a new instance of ContentRender */
    public ContentRender() {
        updSemaphore = new Semaphore(0);
        updThread = new UpdateThread();
        updThread.start();
    }

    public abstract void paint (DisplayTemplate d);
    public abstract void clear ();
    public abstract void flip ();
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleChanged = true;
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
    
    public String getAlert(){
        return this.timer;
    }
    
    public void setAlert(String alert){
        this.alert = alert;
        this.alertChanged = true;
    }
    
    public String getTemplate(){
        return template;
    }
    
    public void setTemplate(String template){
        this.template = template;
        this.templateChanged = true;
    }

    public String getAlertTemplate(){
        return alertTemplate;
    }
    
    public void setAlertTemplate(String template){
        this.alertTemplate = template;
        this.alertTemplateChanged = true;
    }

    public void setAlertActive(boolean active){
        this.alertActive = active;
        this.alertActiveChanged = true;
    }
    
    public boolean isMonitor(){
        return this.monitor;
    }
    
    public void setMonitor(boolean b){
        this.monitor = b;
    }
    
    public void update(){
        updSemaphore.release();
    }
    
    /**
     * Update the screen only if needed. 
     * That is, if the changes were only in content that isn't 
     * being shown, no update is done.
     */
    public void updateDisplay(){
        
        try {
            DisplayTemplate templ = TemplateManager.getDisplayTemplate(template);
            DisplayTemplate alertTempl = null;
            if (alertActive){
                alertTempl = TemplateManager.getDisplayTemplate(alertTemplate);
                if (alertTempl == null)
                    alertActive = false;
            }

            //if (templ == null && alertTempl == null)
            //    return;
            
            boolean needUpdate = false;
            
            needUpdate = alertActiveChanged || templateChanged;
            
            String content;
            if (needUpdate == false && templ != null){
                for (TemplateItem t : templ.getItems() ){
                    if (t instanceof TextTemplateItem) {
                        content = ((TextTemplateItem)t).getContent();
                        if ( titleChanged && content.equals(TextTemplateItem.CONTENT_TITLE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( slideChanged && content.equals(TextTemplateItem.CONTENT_SLIDE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( nextSlideChanged && content.equals(TextTemplateItem.CONTENT_NEXTSLIDE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( clockChanged && content.equals(TextTemplateItem.CONTENT_CLOCK)) {
                            needUpdate = true;
                            break;
                        }
                        if ( timerChanged && content.equals(TextTemplateItem.CONTENT_TIMER)) {
                            needUpdate = true;
                            break;
                        }
                        if ( alertChanged && content.equals(TextTemplateItem.CONTENT_ALERT)) {
                            needUpdate = true;
                            break;
                        }
                    }// if is TextTemplateItem
                }// for need update
            }// if template not null
            
            if (needUpdate == false && alertActive){
                for (TemplateItem t : alertTempl.getItems() ){
                    if (t instanceof TextTemplateItem) {
                        content = ((TextTemplateItem)t).getContent();
                        if ( titleChanged && content.equals(TextTemplateItem.CONTENT_TITLE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( slideChanged && content.equals(TextTemplateItem.CONTENT_SLIDE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( nextSlideChanged && content.equals(TextTemplateItem.CONTENT_NEXTSLIDE)) {
                            needUpdate = true;
                            break;
                        }
                        if ( clockChanged && content.equals(TextTemplateItem.CONTENT_CLOCK)) {
                            needUpdate = true;
                            break;
                        }
                        if ( timerChanged && content.equals(TextTemplateItem.CONTENT_TIMER)) {
                            needUpdate = true;
                            break;
                        }
                        if ( alertChanged && content.equals(TextTemplateItem.CONTENT_ALERT)) {
                            needUpdate = true;
                            break;
                        }
                    }// if is TextTemplateItem
                }// for need update
            }
            
            
            if (needUpdate){

                // Start cleaning it
                clear();
                
                // Set the text
                if (templ != null){
                    
                    synchronized(templ){

                        for (TemplateItem t : templ.getItems() ){
                            if (t instanceof TextTemplateItem) {
                                content = ((TextTemplateItem)t).getContent();
                                if ( content.equals(TextTemplateItem.CONTENT_TITLE)) {
                                    ((TextTemplateItem)t).setText(title);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_SLIDE)) {
                                    ((TextTemplateItem)t).setText(slide);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_NEXTSLIDE)) {
                                    ((TextTemplateItem)t).setText(nextSlide);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_CLOCK)) {
                                    ((TextTemplateItem)t).setText(clock);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_TIMER)) {
                                    ((TextTemplateItem)t).setText(timer);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_ALERT)) {
                                    ((TextTemplateItem)t).setText(alert);
                                    continue;
                                }
                            }// is texttempalteitem
                        }// for templateItem

                        // paint it!
                        paint(templ);
                        
                    }// sync
                    
                }
                
                if (alertActive){
                  
                    synchronized(alertTempl){
                        
                        for (TemplateItem t : alertTempl.getItems() ){
                            if (t instanceof TextTemplateItem) {
                                content = ((TextTemplateItem)t).getContent();
                                if ( content.equals(TextTemplateItem.CONTENT_TITLE)) {
                                    ((TextTemplateItem)t).setText(title);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_SLIDE)) {
                                    ((TextTemplateItem)t).setText(slide);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_NEXTSLIDE)) {
                                    ((TextTemplateItem)t).setText(nextSlide);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_CLOCK)) {
                                    ((TextTemplateItem)t).setText(clock);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_TIMER)) {
                                    ((TextTemplateItem)t).setText(timer);
                                    continue;
                                }
                                if ( content.equals(TextTemplateItem.CONTENT_ALERT)) {
                                    ((TextTemplateItem)t).setText(alert);
                                    continue;
                                }
                            }// is texttempalteitem
                        }// for
                        
                        // paint it
                        paint(alertTempl);
                        
                    }// sync
                }// if alert active
                
                // Ok, show it!
                flip();
                
                // everything has been updated
                templateChanged = false;
                titleChanged = false;
                slideChanged = false;
                nextSlideChanged = false;
                clockChanged = false;
                timerChanged = false;
                alertChanged = false;
                alertActiveChanged = false;
                
            }// if need update
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    
    private class UpdateThread extends Thread {
        public void run(){
            while (true){
                try{
                    updSemaphore.acquire();
                    updateDisplay();
                }catch(Exception e){
                    // ignore
                }
            }
        }
    }
}

