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
 * SwingPanelContentRender.java
 *
 * Created on 11 May 2006, 19:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.AlphaComposite;
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

    @Override
    public void paint (BufferedImage img, AlphaComposite rule){
        p.paint(img, rule);
    }
    
    public void clear() {
        p.clear();
    }

    public void flip() {
        p.flip();
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
        super.initDisplay(width, height, top, left);
        p.initDisplay(width, height, top, left);
    }
    
    public void shutdown(){
        p.shutdown();
    }

    public void setWindowTitle(String title) {
    }
    
}
