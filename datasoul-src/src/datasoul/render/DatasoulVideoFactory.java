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
public class DatasoulVideoFactory {
    
    private static DatasoulVideoFactoryItf instance = null;
    
    public static void setInstance(DatasoulVideoFactoryItf inst){
        instance = inst;
    }
    
    public static DatasoulVideoFactoryItf getInstance() {
        return instance;
    }
    
}
