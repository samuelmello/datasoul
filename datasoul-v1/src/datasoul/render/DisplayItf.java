/*
 * DisplayItf.java
 *
 * Created on March 20, 2006, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.image.BufferedImage;

/**
 *
 * @author root
 */
public interface DisplayItf {
    
    public void initDisplay (int width, int height, int top, int left);
    public void setInputSrc(int src);
    public void setInputMode(int mode);
    public void setDeintrelaceMode(int mode);
    
    final public int DEBUG_MODE_OFF = 0;
    final public int DEBUG_MODE_ON = 1;
    public void setDebugMode(int mode);
    
    final public int BG_MODE_STATIC = 0;
    final public int BG_MODE_LIVE = 1;
    public void setBackgroundMode(int mode);
    
    final public int CLEAR_MODE_OFF = 0;
    final public int CLEAR_MODE_ON = 1;
    public void clear(int mode);
    
    final public int BLACK_MODE_OFF = 0;
    final public int BLACK_MODE_ON = 1;
    public void black(int mode);
    
    public void paintBackground(BufferedImage img);
    
    public ContentRender getContentRender();
}
