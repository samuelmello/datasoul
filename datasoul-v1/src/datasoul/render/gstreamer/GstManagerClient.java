/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.ContentDisplay;
import datasoul.render.OutputDevice;
import datasoul.render.gstreamer.notifications.GstNotification;
import datasoul.render.gstreamer.notifications.GstNotificationCmdDone;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import org.gstreamer.Element;
import org.gstreamer.Gst;

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
                        ((GstDisplayCmd)o).run();
                    }catch(Exception e){
                        System.out.println(">>>>> ERROR:"+e.getLocalizedMessage());
                        e.printStackTrace( new PrintStream(System.out));
                    }
                    sendNotification(new GstNotificationCmdDone());
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
    private GstManagerPipeline bgpipeline;
    private boolean isOutputVisible;
    
    public void init ( String mainDevice, String monitorDevice, boolean monitorEnabled ) {

        this.isMonitorEnabled = monitorEnabled;
        this.mainDevice = new OutputDevice(mainDevice, OutputDevice.USAGE_MAIN);
        mainFrame = new GstDisplayFrame();
        mainFrame.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Datasoul_-_Main_Display"));
        mainFrame.init(this.mainDevice.getWidth(), this.mainDevice.getHeight());
        mainFrame.getVideoSink().setName("MainVideoSink");
        if (isMonitorEnabled){
            this.monitorDevice = new OutputDevice(monitorDevice, OutputDevice.USAGE_MONITOR);
            monitorFrame = new GstDisplayFrame();
            monitorFrame.init(this.monitorDevice.getWidth(), this.monitorDevice.getHeight());
            monitorFrame.getVideoSink().setName("MonitorVideoSink");
        }

    }

    public boolean isMonitorEnabled(){
        return this.isMonitorEnabled;
    }

    public ContentDisplay getMainContentDisplay(){
        return mainFrame.getContentDisplay();
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        args = Gst.init("Datasoul", args);
        Toolkit.getDefaultToolkit().addAWTEventListener( GstKeyListener.getInstance(), AWTEvent.KEY_EVENT_MASK);
        GstManagerClient.getInstance().run();
        System.exit(0);
    }

    protected Element getMainVideoSink(){
        return mainFrame.getVideoSink();
    }

    protected Element getMonitorVideoSink(){
        return monitorFrame.getVideoSink();
    }

    public void setOutputVisible(boolean b){

        this.isOutputVisible = b;

        if (b){
            resumeBgPipeline();
            
            mainDevice.setWindowFullScreen(mainFrame);
            /*
            if (isMonitorEnabled){
                monitorDevice.setWindowFullScreen(monitorFrame);
            }*/
        }else{
            pauseBgPipeline();
            mainDevice.closeFullScreen(mainFrame);
            /*
            if (isMonitorEnabled){
                monitorDevice.closeFullScreen();
            }*/
        }
    }

    void sendNotification(GstNotification evt) {
        try{
            if (output != null){
                output.writeObject(evt);
            }
        }catch (IOException e){
            e.printStackTrace(new PrintStream(System.out));
        }
    }

    public void setBgPipeline(GstManagerPipeline pipe){
        if (this.bgpipeline != null){
            this.bgpipeline.stop();
            this.bgpipeline.dispose();
        }
        this.bgpipeline = pipe;
        if (isOutputVisible && this.bgpipeline != null){
            this.bgpipeline.start();
        }
    }

    public void pauseBgPipeline(){
        if (this.bgpipeline != null){
            this.bgpipeline.stop();
        }
    }

    public void resumeBgPipeline(){
        if (this.bgpipeline != null){
            this.bgpipeline.start();
        }
    }

    public void setVideoItem(String filename) {
        GstManagerVideoItemPipeline item = new GstManagerVideoItemPipeline(filename, bgpipeline);
        setBgPipeline(item);
    }

    public void stopVideoItem() {
        if (bgpipeline instanceof GstManagerVideoItemPipeline){
            ((GstManagerVideoItemPipeline)bgpipeline).eos();
        }
    }

}
