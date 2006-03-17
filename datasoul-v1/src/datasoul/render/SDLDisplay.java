/*
 * SDLDisplay.java
 *
 * Created on March 11, 2006, 12:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

/**
 *
 * @author samuelm
 */
public class SDLDisplay {

    static {
        // depois a gente arruma isso
        if (System.getProperty("os.name").equals("Linux")){
            System.load("/home/samuelm/cvs/datasoul-v1/src/native/libdatasoulsdl.so");
        }
        if (System.getProperty("os.name").contains("Windows")){
            System.load("D:/Meus Documentos/Utils/nativedll/Debug/nativedll.dll");
        }
        
    }
    
    public static final int MAIN_OUTPUT_WIDTH = 640;
    public static final int MAIN_OUTPUT_HEIGHT = 480;
    public static final int BACKGROUND_MODE_STATIC = 0;
    public static final int BACKGROUND_MODE_LIVE = 1;
    
    private BufferedImage overlay;
    private ByteBuffer overlayBuf;
    private BufferedImage background;
    private ByteBuffer backgroundBuf;
    
    static private SDLDisplay instance = null;
    
    /** Creates a new instance of SDLDisplay */
    private SDLDisplay() {
        overlay = new BufferedImage(MAIN_OUTPUT_WIDTH, MAIN_OUTPUT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        overlayBuf = ByteBuffer.allocateDirect(MAIN_OUTPUT_WIDTH * MAIN_OUTPUT_HEIGHT * 4);
        background = new BufferedImage(MAIN_OUTPUT_WIDTH, MAIN_OUTPUT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        backgroundBuf = ByteBuffer.allocateDirect(MAIN_OUTPUT_WIDTH * MAIN_OUTPUT_HEIGHT * 4);
        
        Thread t = new Thread(){
            public void run(){
                init(MAIN_OUTPUT_WIDTH, MAIN_OUTPUT_HEIGHT);
            }
        };
        
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public static synchronized SDLDisplay getInstance(){
        if (instance == null){
            instance = new SDLDisplay();
        }
        return instance;
    }
    
    @Override
    protected void finalize() throws Throwable {
        cleanup();
    }
            
    private native void init(int width, int height);
    private native void cleanup();
    private native void displayOverlay(ByteBuffer bb);
    private native void nativeSetBackground(ByteBuffer bb);
    public native void black(int active);
    public native void clear(int active);
    public native void setBackgroundMode(int mode);
    

    public void paintOverlay(Paintable p){
        
        long time = System.currentTimeMillis();
        if (p == null) return;

        Graphics2D g = overlay.createGraphics();

        // Clear it first
        Composite oldComp = g.getComposite();
        try{
            g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 0) );
            g.fillRect(0, 0, overlay.getWidth(), overlay.getHeight());
        }finally{
            g.setComposite(oldComp);
        }
        
        // paint it
        p.paint(g);
        
        // send it to the native display
        overlayBuf.put( ((DataBufferByte)overlay.getRaster().getDataBuffer()).getData() ) ;
        overlayBuf.flip();
        this.displayOverlay(overlayBuf);
        overlayBuf.clear();
        System.out.println("Time: "+(time-System.currentTimeMillis()));
        
    }


    public void paintBackground(BufferedImage img){
        
        if (img == null) return;

        // we need to convert it to ensure that its on the correct format and dimensions
        Graphics2D g = background.createGraphics();
        g.drawImage(img, 0, 0, background.getWidth(), background.getHeight(), null);
        
        backgroundBuf.put(((DataBufferByte)background.getRaster().getDataBuffer()).getData() ) ;
        backgroundBuf.flip();
        this.nativeSetBackground(backgroundBuf);
        backgroundBuf.clear();
        
    }
    
    
}