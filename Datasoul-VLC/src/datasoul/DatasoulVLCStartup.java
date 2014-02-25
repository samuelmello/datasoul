package datasoul;


import datasoul.StartupManager;
import datasoul.render.DatasoulVideoFactory;
import datasoul.render.VlcjDatasoulVideoFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author samuel
 */
public class DatasoulVLCStartup {
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        DatasoulVideoFactory.setInstance(new VlcjDatasoulVideoFactory());
        StartupManager.datasoulBaseMain(args);
    }
    
}
