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
 * TextServiceItemTextArea.java
 *
 * Created on 24 November 2006, 00:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.serviceitems.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JTextArea;

/**
 *
 * @author samuelm
 */
public class TextServiceItemTextArea extends JTextArea {
    
        public static final int SPACE_HEIGHT = 2;
        
        private boolean showMark;

        public TextServiceItemTextArea(){
            super();
            setFont(new Font(Font.SANS_SERIF, Font.PLAIN, getFont().getSize() ));
        }
        
        public void setShowMark(boolean showMark){
            this.showMark = showMark;
        }
        
        public Dimension getPreferredSize() {
            Dimension retValue;
            
            retValue = super.getPreferredSize();
            if (showMark){
                retValue.setSize(retValue.getWidth(), retValue.getHeight()+SPACE_HEIGHT );
            }
            return retValue;
        }

        public void paint(Graphics g) {
            if (showMark){
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, (int)g.getClipBounds().getWidth(), SPACE_HEIGHT);
                g.translate(0, SPACE_HEIGHT);
            }
            super.paint(g);
        }
    
}
