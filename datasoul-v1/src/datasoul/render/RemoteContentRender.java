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

    public void paint(DisplayTemplate d, float time) {

        // paint the template to a temporary bitmap and send the
        // bitmap to the server. This simplify the process in two ways:
        //   1) The display template does not need to be serializable
        //   2) If the server is running live video, this helps to 
        //      keep server's load predictable, as the template complexity 
        //      is abstracted here
        // This can not be the more optimized solution in terms of performance, 
        // but is better due to predictability.
        
        if (templateImg != null){
        
            Graphics2D g = templateImg.createGraphics();
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
            g.fillRect(0, 0, transitionImage.getWidth(), transitionImage.getHeight());

            if (d != null){
                d.paint(g, time);
            }   
            
            paint (templateImg, 1.0f);
            
        }
    }

    public void paint(BufferedImage img, float alpha) {
        
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_PAINT);
            output.writeFloat(alpha);
            ImageIO.write(img, "png", output);
            output.flush();
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

    public void setDebugMode(int i) {
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_SETDEBUGMODE);
            output.writeInt(i);
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
    
    public void initDisplay(int width, int height, int top, int left) {
        super.initDisplay(width, height, top, left);
        templateImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        try{
            output.writeInt(RemoteContentRenderConstants.CMD_INIT);
            output.writeInt(width);
            output.writeInt(height);
            output.writeInt(top);
            output.writeInt(left);
            output.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
}
