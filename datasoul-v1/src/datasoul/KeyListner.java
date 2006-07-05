/*
 * KeyListner.java
 *
 * Created on 25 de Junho de 2006, 23:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul;

import datasoul.datashow.AuxiliarPanel;
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
                    //ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);                    
                    ObjectManager.getInstance().getLivePanel().goLastMarkedSlide();
                    break;
                case KeyEvent.VK_DOWN:     
                    //ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);                    
                    ObjectManager.getInstance().getLivePanel().goNextMarkedSlide();
                    break;
                case KeyEvent.VK_1:     
                    ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);
                    break;
                case KeyEvent.VK_2:     
                    ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_SONGS);
                    break;
                case KeyEvent.VK_3:     
                    ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_TEMPLATES);
                    break;
                case KeyEvent.VK_4:     
                    ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_CONFIG);
                    break;
            }
        }

        switch(e.getKeyCode()){ 
            case KeyEvent.VK_F1: 
                break;
            case KeyEvent.VK_F2:
                break;
            case KeyEvent.VK_F3: 
                break;
            case KeyEvent.VK_F4: 
                break;
            case KeyEvent.VK_F5: 
                ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);
                ObjectManager.getInstance().getAuxiliarPanel().setVisibleTab(AuxiliarPanel.TAB_DISPLAY);
                break;
            case KeyEvent.VK_F6: 
                ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);
                ObjectManager.getInstance().getAuxiliarPanel().setVisibleTab(AuxiliarPanel.TAB_ALARM);
                break;
            case KeyEvent.VK_F7: 
                ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);
                ObjectManager.getInstance().getAuxiliarPanel().setVisibleTab(AuxiliarPanel.TAB_CLOCK);
                break;                
            case KeyEvent.VK_F8: 
                ObjectManager.getInstance().getDatasoulMainForm().showPanel(ObjectManager.VIEW_PROJECTOR);
                ObjectManager.getInstance().getAuxiliarPanel().setVisibleTab(AuxiliarPanel.TAB_BACKGROUND);
                break;                
            case KeyEvent.VK_F9: 
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().mainDisplayShow();
                break;                
            case KeyEvent.VK_F10: 
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().mainDisplayHide();
                break;                
            case KeyEvent.VK_F11: 
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().mainDisplayClean();
                break;                
            case KeyEvent.VK_F12:     
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().mainDisplayBlack();
                break;                
        }
    }
}
