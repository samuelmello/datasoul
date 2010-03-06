/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.ContentDisplay;
import datasoul.render.OutputDevice;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

/**
 *
 * @author samuellucas
 */
public class GstManagerClient {

    private static GstManagerClient instance;

    protected Socket s;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    private GstManagerClient (){
        
    }
    
    public static GstManagerClient getInstance(){
        if (instance == null){
            instance = new GstManagerClient();
        }
        return instance;
    }

    public void run(){
        try {
            boolean b = true;
            while(b){
                try {
                    s = new Socket("localhost", 34912);
                    b = false;
                }catch(ConnectException ex){
                    Thread.sleep(2000);
                }
            }
            s.setTcpNoDelay(true);
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());
            while(true){
                Object o = input.readObject();
                if (o == null){
                    System.out.println(">>>>> Is null, exiting");
                    break;
                }
                if (o instanceof GstDisplayCmd){
                    try{
                        ((GstDisplayCmd)o).runCmd();
                    }catch(Exception e){
                        System.out.println(">>>>> ERROR:"+e.getLocalizedMessage());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private GstDisplayFrame mainFrame;
    private GstDisplayFrame monitorFrame;
    private boolean isMonitorEnabled;
    private OutputDevice mainDevice;
    private OutputDevice monitorDevice;

    
    public void init ( String mainDevice, String monitorDevice, boolean monitorEnabled ) {

        this.isMonitorEnabled = monitorEnabled;
        this.mainDevice = new OutputDevice(mainDevice, OutputDevice.USAGE_MAIN);
        mainFrame = new GstDisplayFrame();
        mainFrame.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Main_Display"));
        mainFrame.init(this.mainDevice.getWidth(), this.mainDevice.getHeight());
        if (isMonitorEnabled){
            this.monitorDevice = new OutputDevice(monitorDevice, OutputDevice.USAGE_MONITOR);
            monitorFrame = new GstDisplayFrame();
            monitorFrame.init(this.monitorDevice.getWidth(), this.monitorDevice.getHeight());
        }

    }

    public ContentDisplay getMainContentDisplay(){
        return mainFrame.getContentDisplay();
    }

    public static void main(String[] args) {
        args = Gst.init("Datasoul", args);
        Toolkit.getDefaultToolkit().addAWTEventListener( GstKeyListener.getInstance(), AWTEvent.KEY_EVENT_MASK);
        GstManagerClient.getInstance().run();
        System.exit(0);
    }

    protected Element getMainVideoSink(){
        return mainFrame.getVideoSink();
    }

    private Pipeline pipe;
    private Element videosrc;

    public void updatePipeline(){
        pipe = new Pipeline("Datasoul");
        videosrc = ElementFactory.make("videotestsrc", "source");
        pipe.addMany(videosrc, getMainVideoSink());
        Element.linkMany(videosrc, getMainVideoSink());
        pipe.setState(State.PLAYING);
        mainFrame.setPipelineActive(true);
    }

    public void setOutputVisible(boolean b){

        if (b){
            mainDevice.setWindowFullScreen(mainFrame);
            if (isMonitorEnabled){
                monitorDevice.setWindowFullScreen(monitorFrame);
            }
        }else{
            mainDevice.closeFullScreen();
            if (isMonitorEnabled){
                monitorDevice.closeFullScreen();
            }
        }
    }

    void sendNotification(GstEventNotification evt) {
        try{
            if (output != null){
                output.writeObject(evt);
            }
        }catch (IOException e){
            e.printStackTrace(new PrintStream(System.out));
        }
    }

}
