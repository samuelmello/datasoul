/*
 * TextServiceItemTextArea.java
 *
 * Created on 24 November 2006, 00:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JTextField;

/**
 *
 * @author samuelm
 */
public class ServiceItemTextField extends JTextField {
    
        public static final int SPACE_HEIGHT = 2;
        
        private boolean showMark;
        
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
