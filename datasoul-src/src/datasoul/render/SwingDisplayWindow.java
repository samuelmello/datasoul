/*
 * Copyright 2005-2012 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 */


package datasoul.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JWindow;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;

/**
 *
 * @author samuel
 */
public class SwingDisplayWindow extends JWindow {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8080565325793000741L;
	
	private ContentDisplayRenderer contentDisplay;
    private ContentRender render;
    
    public SwingDisplayWindow(){
        DatasoulMainForm.setDatasoulIcon(this);
        if (!ConfigObj.getActiveInstance().getDisableOverlayAlphaBool())
            setBackground(new Color(0,0,0,0));
        contentDisplay = new ContentDisplayRenderer() {
            @Override
            public void repaint() {
                    SwingDisplayWindow.this.repaint();
            }
        };        
    }
    
    public void registerAsMain(){
        int w = ConfigObj.getActiveInstance().getMainOutputDeviceObj().getWidth();
        int h = ConfigObj.getActiveInstance().getMainOutputDeviceObj().getProportionalHeight(w);
        contentDisplay.initDisplay(w, h);
        render = new ContentRender(w, h, contentDisplay);
        ContentManager.getInstance().registerMainRender(render);
        this.setSize(w, h);
    }

    public void registerAsMonitor(){
        int w = ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getWidth();
        int h = ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getProportionalHeight(w);
        contentDisplay.initDisplay(w, h);
        render = new ContentRender(w, h, contentDisplay);
        ContentManager.getInstance().registerMonitorRender(render);
        this.setSize(w, h);
    }
    
    
    @Override
    public void paint (Graphics g){
        BufferedImage img = contentDisplay.getActiveImage();
        if (img != null){
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.Src);
            synchronized(img){
                g.drawImage(img, 0,0, img.getWidth(), img.getHeight(), null);
            }
        }  
    }
    
    public ContentRender getContentRender(){
        return render;
    }
}
