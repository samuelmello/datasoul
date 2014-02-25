/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import datasoul.StartupManager;
import datasoul.render.vlcj.VlcjBackgroundFrame;
import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author samuel
 */
public class VlcjDatasoulVideoFactory implements DatasoulVideoFactoryItf {

    
    private String getJarPath(){
        File f;
        try {
            f = new File(VlcjDatasoulVideoFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarpath = f.getAbsolutePath();
            String path = jarpath.substring(0, jarpath.lastIndexOf(File.separator));
            return path;
        
        } catch (URISyntaxException ex) {
            Logger.getLogger(StartupManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    @Override
    public void init() {
        // Initialize VLC
        if (Platform.isLinux()){
           System.loadLibrary("jawt");
        }
       
        if (Platform.isMac()){
            /* In Mac, the directory structure is something like this:
             * 
             * Resources/
             *    Java/
             *      datasoul.jar
             *      lib/*.jar
             *    vlc-intel32/
             *      lib/ <-- libvlc
             *      plugins/
             *    vlc-intel64/
             *      lib/ <-- libvlc
             *      plugins/
             * 
             */
            String platform = "vlc-intel32";
            if (Platform.is64Bit()){
                platform = "vlc-intel64";
            }
            String path = getJarPath().substring(0, getJarPath().lastIndexOf(File.separator)) 
                    + File.separator + platform + File.separator + "lib";
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
        }
        
        if (Platform.isWindows()){
            /* In Windows, the directory structure is something like this:
             * 
             * Datasoul/
             *   datasoul.jar
             *   lib/*.jar
             *   vlc-x32/ <-- libvlc
             *      plugins/
             *   vlc-x64/ <-- libvlc
             *      plugins/
             */
            String path = getJarPath();
            if (Platform.is64Bit()){
                path += File.separator + "vlc-x64";
            }else{
                path += File.separator + "vlc-x32";
            }
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
        }

        // Now load VLC
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        
    }

    @Override
    public DatasoulVideoFrameItf createNew() {
        VlcjBackgroundFrame obj = new VlcjBackgroundFrame();
        
        SwingDisplayWindow overlay = new SwingDisplayWindow();
        obj.setOverlay(overlay);
        
        return obj;
    }
    
}
