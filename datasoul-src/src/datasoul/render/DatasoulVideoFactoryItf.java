/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render;

/**
 *
 * @author samuel
 */
public interface DatasoulVideoFactoryItf {
    
    /**
     * Initialize video subsystem
     */
    public void init();
    
    /**
     * Creates a new DatasoulVideoFrame
     * @return new object
     */
    public DatasoulVideoFrameItf createNew();
}
