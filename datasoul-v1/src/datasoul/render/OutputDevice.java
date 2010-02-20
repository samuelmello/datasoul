
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

    /**
     * Creates a new OutputDevice
     * Try to find a device with IDstring equal to hintID
     * @param hintID
     */
    public OutputDevice(String hintID){
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()){
            if (gd.getIDstring().equals(hintID)){
                this.device= gd;
                break;
            }
        }
        // if not found, fallback to default
        if (this.device == null){
            this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
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
