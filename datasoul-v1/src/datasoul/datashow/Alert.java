/*
 * Alert.java
 *
 * Created on March 29, 2006, 10:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.ConfigObj;
import datasoul.render.ContentManager;
import datasoul.render.DisplayManager;

/**
 *
 * @author samuelm
 */
public class Alert extends Thread {
    
    private int time;
    private String text;
    private boolean showOnMain;
    private boolean showOnMonitor;
    private String mainTemplate;
    private String monitorTemplate;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isShowOnMain() {
        return showOnMain;
    }

    public void setShowOnMain(boolean showOnMain) {
        this.showOnMain = showOnMain;
    }

    public boolean isShowOnMonitor() {
        return showOnMonitor;
    }

    public void setShowOnMonitor(boolean showOnMonitor) {
        this.showOnMonitor = showOnMonitor;
    }
    
    public void run(){
        
        if ( showOnMain == false && showOnMonitor == false) {
            return;
        }
        
        
        // show
        ContentManager cm = ContentManager.getInstance();
        if (showOnMain && ConfigObj.getInstance().getMainOutput().equals("TRUE") ){
            DisplayManager.getMainDisplay().getContentRender().setAlertTemplate(getMainTemplate());
            DisplayManager.getMainDisplay().getContentRender().setAlertActive(true);
        }
        if (showOnMonitor && ConfigObj.getInstance().getMonitorOutput().equals("TRUE") ){
            DisplayManager.getMonitorDisplay().getContentRender().setAlertTemplate(getMonitorTemplate());
            DisplayManager.getMonitorDisplay().getContentRender().setAlertActive(true);
        }
        cm.setAlertText(text);
        cm.updateLive();
        
        
        // wait
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        // hide
        if (showOnMain && ConfigObj.getInstance().getMainOutput().equals("TRUE") ){
            DisplayManager.getMainDisplay().getContentRender().setAlertActive(false);
        }
        if (showOnMonitor && ConfigObj.getInstance().getMonitorOutput().equals("TRUE") ){
            DisplayManager.getMonitorDisplay().getContentRender().setAlertActive(false);
        }
        cm.updateLive();
        
        
    }

    public String getMainTemplate() {
        return mainTemplate;
    }

    public void setMainTemplate(String mainTemplate) {
        this.mainTemplate = mainTemplate;
    }

    public String getMonitorTemplate() {
        return monitorTemplate;
    }

    public void setMonitorTemplate(String monitorTemplate) {
        this.monitorTemplate = monitorTemplate;
    }
           
    
    
}
