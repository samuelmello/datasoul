
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
    private JFrame window;

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

    public void setWindowFullScreen(JFrame frame){
        if (device.isFullScreenSupported()){
            device.setFullScreenWindow(frame);
        }else{
            frame.setBounds(gconfig.getBounds());
            frame.setVisible(true);
        }
        this.window = frame;
    }

    void closeFullScreen() {
        if (window != null){
            if (device.isFullScreenSupported()){
                device.setFullScreenWindow(null);
            }
            window.setVisible(false);
        }
        window = null;
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


}
