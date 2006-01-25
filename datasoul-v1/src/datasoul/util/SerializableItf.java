/*
 * SerializableItf.java
 *
 * Created on 21 de Janeiro de 2006, 18:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import org.w3c.dom.Node;

/**
 *
 * @author Administrador
 */
public interface SerializableItf {
     public Node writeObject() throws Exception;
     
     public void readObject(Node nodeIn) throws Exception;
    
}
