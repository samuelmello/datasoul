/*
 * liveTableKeyListner.java
 *
 * Created on 18 de Julho de 2006, 22:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Administrador
 */
public class liveTableKeyListner implements KeyListener{
    
    private long time=0;
    private StringBuffer numStrBuffer;
    private static final int MAX_TIME_BETWEEN_KEY=1000;
    private ServiceItemTable serviceItemTable;

    /** Creates a new instance of liveTableKeyListner */
    public liveTableKeyListner(ServiceItemTable serviceItemTable) {
        this.serviceItemTable = serviceItemTable;
        numStrBuffer = new StringBuffer();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if( ((e.getKeyCode()>=KeyEvent.VK_0)&&(e.getKeyCode()<=KeyEvent.VK_9) )||
            ((e.getKeyCode()>=KeyEvent.VK_NUMPAD0)&&(e.getKeyCode()<=KeyEvent.VK_NUMPAD9) )||
            (e.getKeyCode()== KeyEvent.VK_ENTER) ){
            if(time-System.currentTimeMillis()<MAX_TIME_BETWEEN_KEY){
                time=System.currentTimeMillis();
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    serviceItemTable.setSlideIndex(Integer.parseInt(numStrBuffer.toString())-1);
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
    }
    
}
