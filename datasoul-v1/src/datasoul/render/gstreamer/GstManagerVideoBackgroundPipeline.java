/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import org.gstreamer.State;

/**
 *
 * @author samuel
 */
public class GstManagerVideoBackgroundPipeline extends GstManagerVideoGenericPipeline {

    public GstManagerVideoBackgroundPipeline(String filename){
        super(filename);
    }
    
    @Override
    public void eos(){
        super.eos();
        /* play in loop */
        pipe.setState(State.PLAYING);
    }
}
