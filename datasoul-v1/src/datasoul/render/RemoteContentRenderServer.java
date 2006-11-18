/*
 * RemoteContentRenderServer.java
 *
 * Created on 21 June 2006, 22:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

/**
 *
 * @author samuelm
 */
public class RemoteContentRenderServer {
    
    private static Socket sock;
    private static ContentRender cr;
    private static ObjectInputStream input;
    
    /**
     * Creates a new instance of RemoteContentRenderServer
     */
    public RemoteContentRenderServer() {

        
    }
    
    public static void main(String[] args){
        
        cr = new SDLContentRender();
        
        //int port = Integer.parseInt(args[1]);
        int port = 12345;
        ServerSocket serv;
        try {
            serv = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        
        while (true){
            try {
                
                System.out.println("Server listening...");
                sock = serv.accept();
                System.out.println("Connected!");
                sock.setTcpNoDelay(true);
                input = new ObjectInputStream(sock.getInputStream());

                receiveMessages();

                cr.shutdown();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        
    }

    private static void receiveMessages() throws IOException{

        int cmd = 0;
        
        while (true){
            
            try {
                
                cmd = input.readInt();
                
            } catch (EOFException e){
                System.out.println("Disconnected!");
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            switch(cmd){
                
                case RemoteContentRenderConstants.CMD_PAINT:
                    paint();
                    break;
                    
                case RemoteContentRenderConstants.CMD_CLEAR:
                    clear();
                    break;
                    
                case RemoteContentRenderConstants.CMD_FLIP:
                    flip();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SETWINDOWTITLE:
                    setWindowTitle();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SETDEBUGMODE:
                    setDebugMode();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SETBGMODE:
                    setBackgroundMode();
                    break;
                    
                case RemoteContentRenderConstants.CMD_PAINTBG:
                    paintBackground();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SETCLEAR:
                    setClear();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SETBLACK:
                    setBlack();
                    break;
                    
                case RemoteContentRenderConstants.CMD_INIT:
                    initDisplay();
                    break;
                    
                case RemoteContentRenderConstants.CMD_SHUTDOWN:
                    shutdown();
                    break;
            
            }
            
        }

    }
    
    private static void shutdown() {
        cr.shutdown();
    }
    
    private static void initDisplay() throws IOException {
        int width, height, top, left;
        boolean isMonitor;
        width = input.readInt();
        height = input.readInt();
        top = input.readInt();
        left = input.readInt();
        isMonitor = input.readBoolean();
        cr.initDisplay(width, height, top, left, isMonitor);
    }

    private static void paint() throws IOException {
        AlphaComposite rule = null;
        int ruleCode;
        float ruleAlpha;
        
        try {
            ruleCode = input.readInt();
            ruleAlpha = input.readFloat();
            rule = AlphaComposite.getInstance(ruleCode, ruleAlpha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        long t1, t2;
        
        t1 = System.currentTimeMillis();
        BufferedImage img = ImageIO.read(input);
        t2 = System.currentTimeMillis();
        
        System.out.println("Read: "+(t2-t1));
        
        
        cr.paint(img, rule);
        
    }

    private static void clear() {
        cr.clear();
    }

    private static void flip() {
        cr.flip();
    }

    private static void setWindowTitle() throws IOException {
        String str;
        try {
            str = input.readObject().toString();
            cr.setWindowTitle("[REMOTE] " + str);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void setDebugMode() throws IOException {
        int i = input.readInt();
        cr.setDebugMode(i);
    }

    
    private static void setBackgroundMode() throws IOException {
        int i = input.readInt();
    //    cr.setBackgroundMode(i);
    }

    private static void paintBackground() throws IOException {
        BufferedImage img = ImageIO.read(input);
        cr.paintBackground(img);
    }

    private static void setClear() throws IOException {
        int i = input.readInt();
        cr.setClear(i);
    }

    private static void setBlack() throws IOException {
        int i = input.readInt();
        cr.setBlack(i);
    }
 
    
    
}
