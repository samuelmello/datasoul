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

/**
 *
 * @author Administrador
 */
public class KeyListner implements AWTEventListener{
    
    /** Creates a new instance of KeyListner */
    public KeyListner() {
    }

    public void eventDispatched(AWTEvent event) {
        System.out.println("opa"+System.currentTimeMillis());
    }
    
}
