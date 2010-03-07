/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.commands;

import java.io.Serializable;

/**
 *
 * @author samuellucas
 */
public abstract class GstDisplayCmd implements Serializable {

    public abstract void run();
    
}
