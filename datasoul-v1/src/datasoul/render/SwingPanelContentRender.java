/*
 * SwingPanelContentRender.java
 *
 * Created on 11 May 2006, 19:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;
import java.awt.image.BufferedImage;

/**
 *
 * @author samuelm
 */
public class SwingPanelContentRender extends ContentRender {
    
    private SwingDisplayPanel p;
    
    /** Creates a new instance of SwingPanelContentRender */
    public SwingPanelContentRender(SwingDisplayPanel p) {
        this.p = p;
    }

    public void paint(DisplayTemplate d, float time) {
        p.paint(d, time);
    }

    public void paint (BufferedImage img, float alpha){
        p.paint(img, alpha);
    }
    
    public void clear() {
        p.clear();
    }

    public void flip() {
        p.flip();
    }

    public void setDebugMode(int i) {
        p.setDebugMode(i);
    }

    public void setBackgroundMode(int i) {
        p.setBackgroundMode(i);
    }

    public void paintBackground(BufferedImage bufferedImage) {
        p.paintBackground(bufferedImage);
    }

    public void setClear(int i) {
        p.setClear(i);
    }

    public void setBlack(int i) {
        p.setBlack(i);
    }

    public void initDisplay(int width, int height, int top, int left) {
        p.initDisplay(width, height, top, left);
    }
    
}
