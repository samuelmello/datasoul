/*
 * KeyListner.java
 *
 * Created on 25 de Junho de 2006, 23:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Administrador
 */
public class KeyListner implements KeyListener, AWTEventListener{
    
    private boolean ctrlPressed = false;
    
    /** Creates a new instance of KeyListner */
    public KeyListner() {
    }

    public void eventDispatched(AWTEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
               keyPressed((KeyEvent) e);
            break;
            case KeyEvent.KEY_RELEASED:
               keyReleased((KeyEvent) e);
            break;
            case KeyEvent.KEY_TYPED:
               keyTyped((KeyEvent) e);
            break;        
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_CONTROL)
            ctrlPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        if(ctrlPressed){
            switch(e.getKeyCode()){ 
                case KeyEvent.VK_CONTROL:
                    ctrlPressed = false;
                    break;
                case KeyEvent.VK_UP: 
                    ObjectManager.getInstance().getLivePanel().goLastMarkedSlide();
                    break;
                case KeyEvent.VK_DOWN:     
                    ObjectManager.getInstance().getLivePanel().goNextMarkedSlide();
                    break;
            }
        }
        switch(e.getKeyCode()){ 
            case KeyEvent.VK_F1: 
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F3: 
            case KeyEvent.VK_F4: 
            case KeyEvent.VK_F5: 
            case KeyEvent.VK_F6: 
            case KeyEvent.VK_F7: 
            case KeyEvent.VK_F8: 
            case KeyEvent.VK_F9: 
            case KeyEvent.VK_F10: 
            case KeyEvent.VK_F11: 
            case KeyEvent.VK_F12:     
        }
    }
}
