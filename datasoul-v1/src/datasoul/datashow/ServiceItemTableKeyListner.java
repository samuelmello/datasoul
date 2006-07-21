/*
 * ServiceItemTableKeyListner.java
 *
 * Created on 18 de Julho de 2006, 22:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.ObjectManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Administrador
 */
public class ServiceItemTableKeyListner implements KeyListener{
    
    private long time=0;
    private StringBuffer numStrBuffer;
    private static final int MAX_TIME_BETWEEN_KEY=1000;
    private ServiceItemTable serviceItemTable;
    private boolean ctrlPressed;

    /**
     * Creates a new instance of ServiceItemTableKeyListner
     */
    public ServiceItemTableKeyListner(ServiceItemTable serviceItemTable) {
        this.serviceItemTable = serviceItemTable;
        numStrBuffer = new StringBuffer();
    }

    public void keyTyped(KeyEvent e) {
    }

    
    
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlPressed = true;
            return;
        }
        
        if (ctrlPressed){
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP: 
                    serviceItemTable.goPreviousMarkedSlide();
                    e.consume();
                    break;
                case KeyEvent.VK_DOWN:     
                    serviceItemTable.goNextMarkedSlide();
                    e.consume();
                    break;
            }
        }
        
        // user is typing a number to jump to
        if( ((e.getKeyCode()>=KeyEvent.VK_0)&&(e.getKeyCode()<=KeyEvent.VK_9) )||
            ((e.getKeyCode()>=KeyEvent.VK_NUMPAD0)&&(e.getKeyCode()<=KeyEvent.VK_NUMPAD9) )||
            (e.getKeyCode()== KeyEvent.VK_ENTER) ){
            if(time-System.currentTimeMillis()<MAX_TIME_BETWEEN_KEY){
                time=System.currentTimeMillis();
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    int num = 0;
                    // ignore if there is any garbage on the buffer
                    try{
                        num = Integer.parseInt(numStrBuffer.toString());
                    }catch(NumberFormatException e1){
                        numStrBuffer.delete(0,numStrBuffer.length());
                        e.consume();
                        return;
                    }
                    
                    // ignore if trying to go to an inexistent slide
                    if (num-1 >= 0 && num-1 < serviceItemTable.getSlideCount()){
                        
                        // everything ok, we can jump to the slide
                        serviceItemTable.setSlideIndex(num-1);
                    }
                    
                    // clear the buffer
                    numStrBuffer.delete(0,numStrBuffer.length());
                }else{
                    numStrBuffer.append(e.getKeyChar());
                }
            }else{
                numStrBuffer.delete(0,numStrBuffer.length());
            }
            e.consume();    
        }
        
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL){
            ctrlPressed = false;
            return;
        }
    }
    
}
