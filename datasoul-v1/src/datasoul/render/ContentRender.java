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
import datasoul.templates.TimerProgressbarTemplateItem;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

/**
 *
 * @author root
 */
public abstract class ContentRender {
    
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
    private DisplayTemplate alertTemplate;
    private boolean alertTemplateChanged;
    private boolean alertActive;
    private boolean alertActiveChanged;
    private boolean showTimer;
    private float timerProgress;
    
    
    private boolean monitor;
    
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
    
    protected int width, height, left, top;
    protected BufferedImage transitionImage;
    
    /** Creates a new instance of ContentRender */
    public ContentRender() {
        updSemaphore = new Semaphore(0);
        updThread = new UpdateThread();
        updThread.start();
    }
    
    public abstract void paint(DisplayTemplate d, float time);
    public abstract void paint(BufferedImage img, float alpha);
    public abstract void clear();
    public abstract void flip();
    
    
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
            this.template = new DisplayTemplate(template);
            this.templateChanged = true;
        }catch(Exception e){
            e.printStackTrace();
            this.template = null;
        }
    }
    
    public void setAlertTemplate(String template){
        try{
            this.alertTemplate = new DisplayTemplate(template);
            this.alertTemplateChanged = true;
        }catch(Exception e){
            e.printStackTrace();
            this.alertTemplate = null;
        }
    
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
    
    
    public void slideShow(int transictionTime){
        slideTransition = TRANSITION_SHOW;
        if (transictionTime >= 0){
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
        }
        update();
    }
    
    public void slideChange(int transictionTime){
        slideTransition = TRANSITION_CHANGE;
        if (transictionTime >= 0){
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
        }
        update();
    }
    
    public void slideHide(int transictionTime){
        slideTransition = TRANSITION_HIDE;
        if (transictionTime >= 0){
            slideTransTimer = transictionTime;
            slideTransTimerTotal = transictionTime;
        }
        update();
    }
    
    public void alertShow(int transictionTime){
        alertTransition = TRANSITION_SHOW;
        if (transictionTime >= 0){
            alertTransTimer = transictionTime;
            alertTransTimerTotal = transictionTime;
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
     */
    public synchronized void updateDisplay(){
        
        try {
            
            boolean needUpdate = false;
            boolean paintSlide = false;
            boolean paintAlert = false;
            float paintSlideLevel = 0, paintAlertLevel = 0;
            
            needUpdate = alertActiveChanged || templateChanged;
            
            String content;
            if (needUpdate == false && template != null){
                for (TemplateItem t : template.getItems() ){
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
                    }else if (t instanceof TimerProgressbarTemplateItem && timerChanged){
                        needUpdate = true;
                        break;
                    }// if is TextTemplateItem
                }// for need update
            }// if template not null
            
            if (needUpdate == false && alertActive){
                for (TemplateItem t : alertTemplate.getItems() ){
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
                    }else if (t instanceof TimerProgressbarTemplateItem && timerChanged){
                        needUpdate = true;
                        break;
                    }// if is TextTemplateItem
                }// for need update
            }
            
            
            if (needUpdate){
                
                // Set the text
                if (template != null){
                    
                    for (TemplateItem t : template.getItems() ){
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
                        }else if (t instanceof TimerProgressbarTemplateItem && timerChanged){
                            ((TimerProgressbarTemplateItem)t).setPosition(timerProgress);
                            ((TimerProgressbarTemplateItem)t).setShowTimer( showTimer );
                            continue;
                        }// is texttempalteitem
                    }// for templateItem
                    
                    
                    // paint it!
                    if (slideTransTimerTotal > 0)
                        paintSlideLevel = (float) slideTransTimer / (float) slideTransTimerTotal;
                    else
                        paintSlideLevel = 1;
                    
                    if (slideTransition != TRANSITION_HIDE){
                        paintSlideLevel = 1 - paintSlideLevel;
                    }
                    
                    paintSlide = true;
                    
                    
                }
                
                if (alertActive || alertTransTimer > 0){
                    
                    for (TemplateItem t : alertTemplate.getItems() ){
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
                        }else if (t instanceof TimerProgressbarTemplateItem && timerChanged){
                            ((TimerProgressbarTemplateItem)t).setPosition(timerProgress);
                            ((TimerProgressbarTemplateItem)t).setShowTimer( showTimer );
                            continue;
                        }// is texttempalteitem
                    }// for
                    
                    if (alertTransTimerTotal > 0)
                        paintAlertLevel = (float) alertTransTimer / (float) alertTransTimerTotal;
                    else
                        paintAlertLevel = 1;
                    
                    if (alertTransition != TRANSITION_HIDE){
                        paintAlertLevel = 1 - paintAlertLevel;
                    }
                    
                    paintAlert = true;
                    
                }// if alert active
                
                
                // ok, show it
                synchronized(this){
                    clear();

                    if (paintSlide && slideTransition == TRANSITION_CHANGE){
                        synchronized(transitionImage){
                            paint(transitionImage, 1 - paintSlideLevel);
                        }
                    }
                    
                    if (paintSlide){
                        paint(template, paintSlideLevel);
                        //System.out.println("Slide Level = "+paintSlideLevel);
                    }
                    
                    if (paintAlert){
                        paint(alertTemplate, paintAlertLevel);
                    }
                    
                    flip();
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
                    alertActiveChanged = false;
                }
                
            }// if need update
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    public synchronized void saveTransitionImage(){

        synchronized(transitionImage){
            Graphics2D g = transitionImage.createGraphics();

            // Clear it first
            Composite oldComp = g.getComposite();
            try{
                g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
                g.fillRect(0, 0, transitionImage.getWidth(), transitionImage.getHeight());
            }finally{
                g.setComposite(oldComp);
            }

            template.paint(g, 1.0f);

        }
    }
    
    
    /**
     * this method SOULD be overriden by super class
     */
    public void initDisplay(int width, int height, int top, int left){
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
        transitionImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    
    
    public abstract void setDebugMode(int i);
    
    public abstract void setBackgroundMode(int i);
    
    public abstract void paintBackground(BufferedImage bufferedImage);
    
    public abstract void setClear(int i);
    
    public abstract void setBlack(int i);
    
    private class UpdateThread extends Thread {
        public void run(){
            long t1, t2, t3, sleepTime;
            while (true){
                try{
                    updSemaphore.acquire();
                    while ( slideTransTimer > 0 || alertTransTimer > 0){
                        t1 = System.currentTimeMillis();
                        
                        updateDisplay();
                        
                        t2 = System.currentTimeMillis();
                        
                        sleepTime = 50 - (t2 - t1);
                        
                        if (sleepTime > 0){
                            Thread.sleep( sleepTime );
                        }
                        //System.out.println("Slide Timer ="+slideTransTimer+", alert Timer="+alertTransTimer+" ("+ sleepTime +")");
                        
                        t3 = System.currentTimeMillis();
                        
                        slideTransTimer -= (t3 - t1);
                        alertTransTimer -= (t3 - t1);
                        
                        if (slideTransTimer < 0)
                            slideTransTimer = 0;
                        
                        if (alertTransTimer < 0)
                            alertTransTimer = 0;
                        
                        
                    }
                    
                    updateDisplay();
                    
                }catch(Exception e){
                    // ignore
                }
            }
        }
    }
    
    
}

