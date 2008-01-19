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

    @Override
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

    public void updatePosition(int width, int height, int top, int left){
        display.setBounds(left, top, width, height);
    }

    
}
