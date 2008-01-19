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
 * ServiceItemRenderer.java
 *
 * Created on February 10, 2006, 10:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.Component;

/**
 *
 * @author samuelm
 */
public interface ServiceItemRenderer {
    
    public void setWidth(int width);
    
    public int getHeight();
    
    public Component getComponent(boolean selected, boolean hasFocus);
    
    public static final int ZOOM_TINY = 1;
    public static final int ZOOM_SMALL = 2;
    public static final int ZOOM_NORMAL = 3;
    public static final int ZOOM_LARGE = 4;
    public static final int ZOOM_HUGE = 5;
    
    public void setZoom(int f);
    
    public boolean getShowMark();
    
    /**
     * A mark to logically divide the slides.
     * For example can be use to divide the lyrics 
     * in chorus
     */ 
    public boolean getMark();
    
}
