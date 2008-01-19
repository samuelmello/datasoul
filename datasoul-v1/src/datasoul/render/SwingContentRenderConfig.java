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
 * SwingContentRenderConfig.java
 *
 * Created on 18 November 2006, 12:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import javax.swing.JOptionPane;

/**
 *
 * @author samuelm
 */
public class SwingContentRenderConfig implements RenderConfigItf {
    
    /** Creates a new instance of SwingContentRenderConfig */
    public SwingContentRenderConfig() {
    }
    
    public void showConfiguration(boolean isMonitor){
        JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("There_is_nothing_to_configure"), "SwingContentRender", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
