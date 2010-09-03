/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.gstreamer;

import datasoul.config.ConfigObj;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.ContentDisplayRenderer;
import datasoul.render.ContentRender;
import datasoul.render.OutputDevice;
import datasoul.render.gstreamer.notifications.GstNotification;
import datasoul.render.gstreamer.notifications.GstNotificationHello;
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

    protected ContentRender mainRender;
    protected ContentRender monitorRender;


    private GstDisplayFrame mainFrame;
    private GstDisplayFrame monitorFrame;
    private boolean isMonitorEnabled;
    private OutputDevice mainDevice;
    private OutputDevice monitorDevice;
    private GstManagerPipeline bgpipeline;
    private boolean isOutputVisible;


    private GstManagerClient (){
        ConfigObj cfg = ConfigObj.getActiveInstance();
        init(cfg.getMainOutputDevice(), cfg.getMonitorOutputDevice(), cfg.getMonitorOutput() );
    }
    
    public static GstManagerClient getInstance(){
        if (instance == null){
            instance = new GstManagerClient();
        }
        return instance;
    }

    public ContentRender getMainRender(){
        return mainRender;
    }

    public ContentRender getMonitorRender(){
        return monitorRender;
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
            sendNotification(new GstNotificationHello());
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
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        System.out.println("Exiting");
    }

    private void init ( String mainDevice, String monitorDevice, boolean monitorEnabled ) {

        this.isMonitorEnabled = monitorEnabled;
        this.mainDevice = new OutputDevice(mainDevice, OutputDevice.USAGE_MAIN);
        mainFrame = new GstDisplayFrame();
        mainFrame.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL - MAIN DISPLAY"));
        mainFrame.init(this.mainDevice.getWidth(), this.mainDevice.getHeight());
        mainFrame.getVideoSink().setName("MainVideoSink");
        mainRender = new ContentRender(this.mainDevice.getWidth(), this.mainDevice.getHeight(), mainFrame.getContentDisplay());
        if (isMonitorEnabled){
            this.monitorDevice = new OutputDevice(monitorDevice, OutputDevice.USAGE_MONITOR);
            monitorFrame = new GstDisplayFrame();
            monitorFrame.setTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL - MONITOR DISPLAY"));
            monitorFrame.init(this.monitorDevice.getWidth(), this.monitorDevice.getHeight());
            monitorFrame.getVideoSink().setName("MonitorVideoSink");
            monitorRender = new ContentRender(this.monitorDevice.getWidth(), this.monitorDevice.getHeight(), monitorFrame.getContentDisplay());
        }

    }

    public boolean isMonitorEnabled(){
        return this.isMonitorEnabled;
    }

    public ContentDisplayRenderer getMainContentDisplay(){
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
            if (isMonitorEnabled){
                monitorDevice.setWindowFullScreen(monitorFrame);
            }
        }else{
            pauseBgPipeline();
            mainDevice.closeFullScreen(mainFrame);
            if (isMonitorEnabled){
                monitorDevice.closeFullScreen(monitorFrame);
            }
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

    public synchronized void setBgPipeline(GstManagerPipeline pipe){
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


