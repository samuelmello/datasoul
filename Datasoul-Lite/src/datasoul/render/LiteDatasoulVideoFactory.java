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
public class LiteDatasoulVideoFactory implements DatasoulVideoFactoryItf {

    @Override
    public void init() {
        // Nothing to do
    }

    @Override
    public DatasoulVideoFrameItf createNew() {
        return new LiteDatasoulVideoFrame();
    }

    @Override
    public boolean hasVideoEnabled() {
        return false;
    }
    
}
