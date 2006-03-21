/*
 * ContentManager.java
 *
 * Created on March 20, 2006, 11:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

/**
 *
 * @author root
 */
public class ContentManager {
    
    private static ContentManager instance;
    
    private ContentRender monitorRender;
    private ContentRender mainRender;
    
    /** Creates a new instance of ContentManager */
    private ContentManager() {
        monitorRender = DisplayManager.getMonitorDisplay().getContentRender();
        mainRender = DisplayManager.getMainDisplay().getContentRender();
    }
    
    static public ContentManager getInstance(){
        if (instance == null){
            instance = new ContentManager();
        }
        return instance;
    }
    
    public void setTitleLive(String title){
        monitorRender.setTitle(title);
        mainRender.setTitle(title);
    }
    
    public void setSlideLive(String slide){
        monitorRender.setSlide(slide);
        mainRender.setSlide(slide);
    }
    
    public void setNextSlideLive(String slide){
        monitorRender.setNextSlide(slide);
        mainRender.setNextSlide(slide);
    }
    
    public void setClockLive(String text){
        monitorRender.setClock(text);
        mainRender.setClock(text);
    }
    
    public void setTimerLive(String timer){
        monitorRender.setTimer(timer);
        mainRender.setTimer(timer);
    }
    
    public void setTemplateLive(String template){
        monitorRender.setTemplate(template);
        mainRender.setTemplate(template);
    }

    public void updateLive(){
        mainRender.update();
        monitorRender.update();
    }
    
}
