/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render;

import javax.swing.JWindow;

/**
 *
 * @author samuel
 */
public interface DatasoulVideoFrameItf {

    void handleErrors();

    /*
     * Pause a Video Item
     */
    void pauseVideoItem();

    /*
     * This method is responsible for setting the appropriate background when a item stop playing
     */
    void playBackground();

    /*
     * Called to play a Video Item
     */
    void playVideoItem(String path);

    void registerAsMain();

    void registerAsMonitor();

    void seekVideoItem(float position);

    /*
     * Force stop for a video item
     */
    void stopVideoItem();

    /**
     * Called to clean up after video item finished
     */
    void videoItemCompleted();

    public void setVisible(boolean b);

    public void setTitle(String string);
    
    public ContentRender getContentRender();
}
