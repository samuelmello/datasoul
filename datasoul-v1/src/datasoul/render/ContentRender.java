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
    
    private boolean monitor;
    
    
    
    /** Creates a new instance of ContentRender */
    public ContentRender() {
    }

    public abstract void paint (DisplayTemplate d);
    
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
    
    public String getTemplate(){
        return template;
    }
    
    public void setTemplate(String template){
        this.template = template;
        this.templateChanged = true;
    }

    public boolean isMonitor(){
        return this.monitor;
    }
    
    public void setMonitor(boolean b){
        this.monitor = b;
    }
    
    /**
     * Update the screen only if needed. 
     * That is, if the changes were only in content that isn't 
     * being shown, no update is done.
     */
    public synchronized void update(){
        
        try {
            DisplayTemplate templ = TemplateManager.getDisplayTemplate(template);
            
            if (templ == null)
                return;
            
            boolean needUpdate = false;
            String content;
            
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
                }// if is TextTemplateItem
                
            }// for need update
            
            
            if (needUpdate){
                
                // Set the text
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
                        
                    }// is texttempalteitem
                    
                }// for templateItem
                
                // here we have the template ready for painting
                paint(templ);
                

                // clean up
                for (TemplateItem t : templ.getItems() ){
                    if (t instanceof TextTemplateItem) {
                        ((TextTemplateItem)t).setText(TextTemplateItem.DEFAULT_TEXT);
                    }                
                }

                
                // everything has been updated
                templateChanged = false;
                titleChanged = false;
                slideChanged = false;
                nextSlideChanged = false;
                clockChanged = false;
                timerChanged = false;
                
                
            }// if need update
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}

