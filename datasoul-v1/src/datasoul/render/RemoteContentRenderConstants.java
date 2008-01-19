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
 * RemoteContentRenderConstants.java
 *
 * Created on 22 June 2006, 21:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

/**
 *
 * @author samuelm
 */
public interface RemoteContentRenderConstants {
    
    static public final int CMD_PAINT           = 0x01;
    static public final int CMD_CLEAR           = 0x02;
    static public final int CMD_FLIP            = 0x03;
    static public final int CMD_SETWINDOWTITLE  = 0x04;
    static public final int CMD_SETDEBUGMODE    = 0x05;
    static public final int CMD_SETBGMODE       = 0x06;
    static public final int CMD_PAINTBG         = 0x07;
    static public final int CMD_SETCLEAR        = 0x08;
    static public final int CMD_SETBLACK        = 0x09;
    static public final int CMD_INIT            = 0x0A;
    static public final int CMD_SHUTDOWN        = 0x0B;
            
}
