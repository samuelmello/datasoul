/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul;

import datasoul.render.DatasoulVideoFactory;
import datasoul.render.LiteDatasoulVideoFactory;

/**
 *
 * @author samuel
 */
public class DatasoulLiteStartup {
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        DatasoulVideoFactory.setInstance(new LiteDatasoulVideoFactory());
        StartupManager.datasoulBaseMain(args);
    }
    
}
