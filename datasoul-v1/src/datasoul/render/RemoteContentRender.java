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
 * RemoteContentRender.java
 *
 * Created on 19 June 2006, 22:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.templates.DisplayTemplate;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;

/**
 *
 * @author samuelm
 */
public class RemoteContentRender extends ContentRender {
    
    private Socket sock;
    private ObjectOutputStream output;
    private int port = 12345;
    private String host = "127.0.0.1";
    private BufferedImage templateImg;
    
    /**
     * Creates a new instance of RemoteContentRender
     */
    public RemoteContentRender() throws IOException {

        sock = new Socket(host, port);
        sock.setTcpNoDelay(true);
        output = new ObjectOutputStream(sock.getOutputStream());
    }

    @Override
    public void paint(BufferedImage img, AlphaComposite rule) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_PAINT);
            output.writeInt(rule.getRule());
            output.writeFloat(rule.getAlpha());

            long t1, t2;
            t1 = System.currentTimeMillis();
            ImageIO.write(img, "png", output);
            output.flush();
            t2 = System.currentTimeMillis();
            
            System.out.println("Send: "+(t2-t1));
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }

    public void clear() {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_CLEAR);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void flip() {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_FLIP);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setWindowTitle(String title) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SETWINDOWTITLE);
            output.writeObject(title);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setBackgroundMode(int i) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SETBGMODE);
            output.writeInt(i);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void paintBackground(BufferedImage bufferedImage) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_PAINTBG);
            ImageIO.write(bufferedImage, "png", output);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setClear(int i) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SETCLEAR);
            output.writeInt(i);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setBlack(int i) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SETBLACK);
            output.writeInt(i);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void shutdown(){
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SHUTDOWN);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void initDisplay(int width, int height, int top, int left, boolean isMonitor) {
        super.initDisplay(width, height, top, left, isMonitor);
        templateImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_INIT);
            output.writeInt(width);
            output.writeInt(height);
            output.writeInt(top);
            output.writeInt(left);
            output.writeBoolean(isMonitor);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
}
