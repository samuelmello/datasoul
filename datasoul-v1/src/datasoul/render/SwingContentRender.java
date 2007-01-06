/*
 * SwingContentRender.java
 *
 * Created on March 20, 2006, 9:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

/**
 *
 * @author root
 */
public class SwingContentRender extends ContentRender {
    
    private SwingDisplay display;
    private DisplayTemplate updateTemplate;
    
    /** Creates a new instance of SwingContentRender */
    public SwingContentRender() {
        super();
        display = new SwingDisplay();
    }

    public void paint(BufferedImage img, AlphaComposite rule){
        display.paint(img, rule);
    }
    
    public void clear() {
        display.clear();
    }

    public void flip() {
        display.flip();
    }

    public void setBackgroundMode(int i) {
        display.setBackgroundMode(i);
    }

    public void paintBackground(BufferedImage bufferedImage) {
        display.paintBackground(bufferedImage);
    }

    public void setClear(int i) {
        display.setClear(i);
    }

    public void setBlack(int i) {
        display.setBlack(i);
    }

    public void initDisplay(int width, int height, int top, int left, boolean isMonitor) {
        super.initDisplay(width, height, top, left, isMonitor);
        display.initDisplay(width, height, top, left, isMonitor);
    }

    public void shutdown(){
        display.shutdown();
    }
    
    public void setWindowTitle(String title) {
        display.setWindowTitle(title);
    }

    

    
}
