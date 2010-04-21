
package datasoul.render;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

/**
 *
 * @author samuel
 */
public class OutputDevice {

    private GraphicsDevice device;
    private GraphicsConfiguration gconfig;

    public static final int USAGE_MAIN = 0;
    public static final int USAGE_MONITOR = 1;

    /**
     * Creates a new OutputDevice
     * Try to find a device with IDstring equal to hintID
     * @param hintID
     */
    public OutputDevice(String hintID, int usage){
        
        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (GraphicsDevice gd : local.getScreenDevices()){
            if (gd.getIDstring().equals(hintID)){
                this.device= gd;
                break;
            }
        }

        /*
         * If not found, determine default monitor
         */
        try{
            if (this.device == null){
                if (usage == USAGE_MAIN){
                    /* main, get first monitor that is not the default */
                    for (GraphicsDevice gd : local.getScreenDevices()){
                        if (!gd.getIDstring().equals( local.getDefaultScreenDevice().getIDstring() )){
                            this.device= gd;
                            break;
                        }
                    }
                }else{
                    /* monitor, use default device if system has up to 2 monitor, otherwise try the 3rd */
                    if ( local.getScreenDevices().length <= 2 ){
                        this.device = local.getDefaultScreenDevice();
                    }else{
                        this.device = local.getScreenDevices()[2];
                    }
                }
            }
            // just to double check...
            if (this.device == null){
                this.device = local.getDefaultScreenDevice();
            }
        }catch(Exception e){
            this.device = local.getDefaultScreenDevice();
        }

        this.gconfig = device.getDefaultConfiguration();
    }


    public String getName(){
        return device.getIDstring();
    }

    /**
     * Monitor is allowed only in systems with at least 2 monitors
     * @return
     */
    public static boolean isMonitorAllowed(){
        return (GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1);
    }

    public void setWindowFullScreen(JFrame frame){
        if (device.getFullScreenWindow() == null){
            frame.setResizable(true);
            device.setFullScreenWindow(frame);
        }
        frame.setVisible(true);
    }

    public void closeFullScreen(JFrame frame) {
        frame.setVisible(false);
        device.setFullScreenWindow(null);
    }

    public int getProportionalHeight(int width){

        float fwidth = (float) width;
        float sizewidth = (float) gconfig.getBounds().getWidth();
        float sizeheight = (float) gconfig.getBounds().getHeight();

        return (int) (fwidth * (sizeheight / sizewidth));

    }

    public int getWidth(){
        return (int) gconfig.getBounds().getWidth();
    }

    public int getHeight(){
        return (int) gconfig.getBounds().getHeight();
    }


}
