/*
 * SDLContentRender.java
 *
 * Created on March 20, 2006, 9:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import datasoul.config.SDLRenderConfig;
import datasoul.templates.DisplayTemplate;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

/**
 *
 * @author root
 */
public class SDLContentRender extends ContentRender {
    
    static {
        
        if (System.getProperty("os.name").contains("Windows")){
            String path = System.getProperty("user.dir") + System.getProperty("file.separator");
            System.load(path+"libsdlrender.dll");
        }else{
            String path = System.getProperty("user.dir") + System.getProperty("file.separator");
            System.load(path+"libsdlrender.so");            
        }
        
    }
    
    private BufferedImage overlay;
    private BufferedImage overlayTemplateSize;
    private ByteBuffer overlayBuf;
    private BufferedImage background;
    private ByteBuffer backgroundBuf;
    
    private SDLContentRender contentRender;
    
    public SDLContentRender getContentRender(){
        return contentRender;
    }
    
    public void initDisplay (final int width, final int height, final int top, final int left, final boolean isMonitor){

        super.initDisplay(width, height, top, left, isMonitor);
        
        overlayTemplateSize = new BufferedImage(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        overlay = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        overlayBuf = ByteBuffer.allocateDirect(width * height * 4);
        background = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        backgroundBuf = ByteBuffer.allocateDirect(width * height * 4);
        
        SDLRenderConfig config = SDLRenderConfig.getInstance(isMonitor);
        String x11display = "";
        if (config.getSetX11Display()){
            setX11Display(config.getX11Display());
        }
        init(width, height, top, left);        

        
        paintBackgroundColor(Color.ORANGE);
        
    }
    
    @Override
    protected void finalize() throws Throwable {
        cleanup();
    }
            
    private native void init(int width, int height, int top, int left);
    private native void setX11Display(String x11display);
    private native void cleanup();
    private native void displayOverlay(ByteBuffer bb);
    private native void nativeSetBackground(ByteBuffer bb);
    public native void setBlack(int active);
    public native void setClear(int active);
    /*
    public native void setBackgroundMode(int mode);
    public native void setInputSrc(int src);
    public native void setInputMode(int mode);
    public native void setDeintrelaceMode(int mode);
     */
    public native void setDebugMode (int mode);
    public native void shutdown();
    
    public void clear(){

        Graphics2D g = overlay.createGraphics();

        Composite oldComp = g.getComposite();
        g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
        g.fillRect(0, 0, overlay.getWidth(), overlay.getHeight());
        g.setComposite(oldComp);
        
    }

    
    @Override
    public void paint(BufferedImage img, AlphaComposite rule){
        Graphics2D g = overlay.createGraphics();
        g.setComposite( rule );
        g.drawImage(img, 0, 0, overlay.getWidth(), overlay.getHeight(), null);
    }
    
    public void flip(){

        // send it to the native display
        overlayBuf.put( ((DataBufferByte)overlay.getRaster().getDataBuffer()).getData() ) ;
        overlayBuf.flip();
        this.displayOverlay(overlayBuf);
        overlayBuf.clear();
        
    }
    
    
    public void paintBackground(BufferedImage img){
        
        if (img == null || background == null) return;

        // we need to convert it to ensure that its on the correct format and dimensions
        Graphics2D g = background.createGraphics();
        g.drawImage(img, 0, 0, background.getWidth(), background.getHeight(), null);
        
        backgroundBuf.put(((DataBufferByte)background.getRaster().getDataBuffer()).getData() ) ;
        backgroundBuf.flip();
        this.nativeSetBackground(backgroundBuf);
        backgroundBuf.clear();
        
    }

    public void paintBackgroundColor(Color color){

        Graphics2D g = background.createGraphics();
        g.setColor(color);
        g.fillRect(0,0,background.getWidth(), background.getHeight());
        
        backgroundBuf.put(((DataBufferByte)background.getRaster().getDataBuffer()).getData() ) ;
        backgroundBuf.flip();
        this.nativeSetBackground(backgroundBuf);
        backgroundBuf.clear();
        
    }

    @Override
    public native void setWindowTitle(String title);

    
}
