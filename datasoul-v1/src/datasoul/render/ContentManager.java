/*
 * ContentManager.java
 *
 * Created on March 20, 2006, 11:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.ConfigObj;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class ContentManager {
    
    private static ContentManager instance;
    
    private ArrayList<ContentRender> previewRenderList;
    private ArrayList<ContentRender> liveRenderList;
    
    
    /** Creates a new instance of ContentManager */
    private ContentManager() {
        previewRenderList = new ArrayList<ContentRender>();
        liveRenderList = new ArrayList<ContentRender>();

        if ( ConfigObj.getInstance().getMonitorOutput().equals("TRUE") ){
            liveRenderList.add( DisplayManager.getMonitorDisplay().getContentRender() );
        }
        
        if ( ConfigObj.getInstance().getMainOutput().equals("TRUE") ){
            liveRenderList.add( DisplayManager.getMainDisplay().getContentRender() );
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
    
    public void setTemplateLive(String template){
        for (ContentRender r : liveRenderList){
            if (r.isMonitor() == false){
                r.setTemplate(template);
            }
        }
    }

    public void updateLive(){
        for (ContentRender r : liveRenderList){
            r.update();
        }
    }

    public void registerPreviewPanel(ContentRender r){
        previewRenderList.add(r);
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

    public void setTemplatePreview(String t){
        for (ContentRender r : previewRenderList){
            r.setTemplate(t);
        }
    }
    
    public void updatePreview(){
        for (ContentRender r : previewRenderList){
            r.update();
        }
    }
    
}
