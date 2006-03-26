/*
 * DisplayItf.java
 *
 * Created on March 20, 2006, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.render;

/**
 *
 * @author root
 */
public interface DisplayItf {
    public void initDisplay (int width, int height, int top, int left);
    public ContentRender getContentRender();
}
