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
            liveRenderList.add( DisplayManager.getMonitorDisplay() );
        }
        
        if ( ConfigObj.getInstance().getMainOutput().equals("TRUE") ){
            liveRenderList.add( DisplayManager.getMainDisplay() );
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
    
    public void setTemplateLive(String template){
        for (ContentRender r : liveRenderList){
            if (r.isMonitor() == false){
                r.setTemplate(template);
            }
        }
    }

    public void setTemplateMonitorLive(String template){
        for (ContentRender r : liveRenderList){
            if (r.isMonitor() == true){
                r.setTemplate(template);
            }
        }
    }
    
    public void slideShow (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.slideShow(transictionTime);
        }
    }
    
    public void slideChange (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.slideChange(transictionTime);
        }
    }
    
    public void slideHide (int transictionTime){
        for (ContentRender r : liveRenderList){
            r.slideHide(transictionTime);
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

    public void setTemplatePreview(String t){
        for (ContentRender r : previewRenderList){
            r.setTemplate(t);
        }
    }
    
    public void updatePreview(){
        for (ContentRender r : previewRenderList){
            r.slideShow(-1);
        }
    }

    public void registerLiveRender(ContentRender r){
        liveRenderList.add(r);
    }
    
    public void unregisterLiveRender(ContentRender r){
        liveRenderList.remove(r);
    }
    
    public void setAlertText(String t){
        for (ContentRender r : liveRenderList){
            r.setAlert(t);
        }
    }
    
    public void registerPreviewPanel(ContentRender r){
        previewRenderList.add(r);
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
    
}
