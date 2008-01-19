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
