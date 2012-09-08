/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

import datasoul.config.ConfigObj;
import datasoul.config.UsageStatsConfig;
import datasoul.datashow.TimerManager;
import datasoul.render.ContentManager;
import datasoul.render.remote.RemoteContentServer;
import datasoul.serviceitems.song.AllSongsListTable;
import datasoul.templates.DisplayTemplate;
import datasoul.templates.TemplateManager;
import datasoul.util.AlertHTTPD;
import datasoul.util.DatasoulKeyListener;
import datasoul.util.ObjectManager;
import datasoul.util.OnlineUpdateCheck;
import datasoul.util.OnlineUsageStats;

/**
 *
 * @author samuel
 */
public class StartupManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // If there is a file, first try to send a FileOpen notification to another instance
        if (args.length > 0){
            sendFileOpenNotification(args[0]);
        }

        StartupManager sm = new StartupManager();
        sm.run(args.length > 0 ? args[0] : null);
    }

    public static void sendFileOpenNotification(String filename){
        try{
            File f = new File(filename);
            if (!f.exists()) return;

            Socket s = new Socket("localhost", 34912);
            s.setTcpNoDelay(true);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            //output.writeObject(new GstNotificationFileOpen(f.getAbsolutePath()));
            output.flush();
            output.reset();
            output.close();
            // If we were able to send the notification, just quit
            System.exit(0);
        }catch(Exception e){
            // ignore and start a new session
        }
    }

    public void checkStorageLocation(){
        String stgloc = ConfigObj.getActiveInstance().getStorageLoc();

        File stglocdir  = new File(stgloc);
        if (!stglocdir.exists()){
            stglocdir.mkdirs();
        }

        File templates  = new File(ConfigObj.getActiveInstance().getStoragePathTemplates());
        if (!templates.exists()){
            templates.mkdirs();
            copySampleTemplates(templates.getAbsolutePath());
            checkLegacyTemplates();
        }

        File songs  = new File(ConfigObj.getActiveInstance().getStoragePathSongs());
        if (!songs.exists()){
            songs.mkdirs();
            copySampleSongs(songs.getAbsolutePath());
        }

        File serviceslist  = new File(ConfigObj.getActiveInstance().getStoragePathServiceLists());
        if (!serviceslist.exists()){
            serviceslist.mkdirs();
            copySampleServices(serviceslist.getAbsolutePath());
        }

    }

    public static void copyFile(String resource, String targetName) throws IOException{
        InputStream is = DatasoulMainForm.class.getResourceAsStream(resource);
        FileOutputStream fos = new FileOutputStream(targetName);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();
    }

    protected void checkLegacyTemplates(){
        File legacyDir = new File( ConfigObj.getActiveInstance().getStoragePathLegacyTemplates() );
        if (legacyDir.exists()){
            File[] files = legacyDir.listFiles();

            if (files != null){

                updateSplash(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UPDATING TEMPLATES..."));

                // Ensure current templates are read.
                // If we let this be called when the first template is being conveted,
                // the new template may be read by the manager constructor (refreshAvailableTemplates)
                // what may raise an exception
                TemplateManager.getInstance();

                for (File f : files){
                    if(f.getName().endsWith(".template")){
                        // Check if already converted
                        File newf = new File(f.getAbsolutePath()+"z");
                        if (!newf.exists()){
                            DisplayTemplate.importTemplate(f.getAbsolutePath());
                        }else{
                            System.out.println("exists."+newf.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    protected void copySampleTemplates(String dir){
        String files[] = {
            "ds-alert-sick.templatez",
            "ds-alert-smile.templatez",
            "ds-alert-surprise.templatez",
            "ds-alert-uncertain.templatez",
            "ds-alert-warning.templatez",
            "ds-alert-wink.templatez",
            "ds-alert-worried.templatez",
            "ds-images.templatez",
            "ds-song.templatez",
            "ds-stage-contentless.templatez",
            "ds-stage-images.templatez",
            "ds-stage-song.templatez",
            "ds-stage-text.templatez",
            "ds-subtitle.templatez",
            "ds-text.templatez"
        };

        for (String f: files){
            try{
                copyFile("samples/"+f, dir + File.separator + f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    protected void copySampleSongs(String dir){
        String files[] = {"Amazing Grace.song",
            "How Great Thou Art.song",
            "It Is Well With My Soul.song",
            "Joyful Joyful We Adore Thee.song",
            };

        for (String f: files){
            try{
                copyFile("samples/"+f, dir + File.separator + f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }


    protected void copySampleServices(String dir){
        String files[] = {"SampleService.servicelistz"};

        for (String f: files){
            try{
                copyFile("samples/"+f, dir + File.separator + f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    public void updateSplash(String s){
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null){

            Graphics g = splash.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(110, 105, 500, 500);
            g.setColor(Color.BLACK);
            g.drawString(s, 120, 120);
            g.dispose();

            try{
                splash.update();
            }catch(IllegalStateException e){
                // ignore
            }

        }
    }
    
    private String getJarPath(){
        File f;
        try {
            f = new File(StartupManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarpath = f.getAbsolutePath();
            String path = jarpath.substring(0, jarpath.lastIndexOf(File.separator));
            return path;
        
        } catch (URISyntaxException ex) {
            Logger.getLogger(StartupManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    void run(String initialFile) {

        // Use IPv4, needed by http-commons
        System.getProperties().setProperty("java.net.preferIPv4Stack", "true");

        // Enable anti-aliasing
        System.setProperty("swing.aatext","true");

        // MacOS menu
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // JVM is broken for windows Vista and 7
        // Disable Direct3D to avoid problems with Swing
        if (Platform.isWindows()){
            System.setProperty("sun.java2d.d3d","false");
        }

        updateSplash(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("INITIALIZING..."));

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            //ignore and fall back to java look and feel
        }

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
        
        ConfigObj.getActiveInstance();

        ContentManager.getInstance();
        checkStorageLocation();

        updateSplash(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("LOADING SONGS..."));
        AllSongsListTable.getInstance();

        updateSplash(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CREATING USER INTERFACE..."));

        final DatasoulMainForm mainForm = new DatasoulMainForm();
        mainForm.setVisible(true);

        Toolkit.getDefaultToolkit().addAWTEventListener( DatasoulKeyListener.getInstance(), AWTEvent.KEY_EVENT_MASK);

        // Init displays
        ObjectManager.getInstance().initMainDisplay();
        ObjectManager.getInstance().initMonitorDisplay();

        // Start remote server
        if (ConfigObj.getActiveInstance().getAcceptRemoteDisplaysBool()){
            RemoteContentServer.getInstance().startServer();
        }

        // Ensure timer properly initiated
        TimerManager.getInstance().setTimerOff();

        // Check for first time run
        if (UsageStatsConfig.getInstance().getID() == null || UsageStatsConfig.getInstance().getID().length() == 0){
            UsageStatsGrantDialog dialog = new UsageStatsGrantDialog(new javax.swing.JFrame(), true);
            dialog.setLocationRelativeTo(mainForm);
            dialog.setVisible(true);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ObjectManager.getInstance().getDatasoulMainForm().setOutputEnabled(true);
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().shortcutShowMain();
                ContentManager.getInstance().updateBackgroundMode();
                ObjectManager.getInstance().getMainVideoFrame().playBackground();
            }
        });

        // If user provided an initial file, open it
        if (initialFile != null){
            if (initialFile.endsWith(".servicelistz") || initialFile.endsWith(".servicelist")){
                final File f = new File(initialFile);
                if (f.exists()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            ObjectManager.getInstance().getDatasoulMainForm().openServiceList(f.getAbsolutePath());
                        }
                    });
                }
            }
        }
        
        // Check for online updates
        if (ConfigObj.getActiveInstance().getOnlineCheckUpdateBool()){
            OnlineUpdateCheck ouc = new OnlineUpdateCheck();
            ouc.start();
        }

        // Send anonymous usage data
        if (ConfigObj.getActiveInstance().getOnlineUsageStatsBool()){
            OnlineUsageStats ous = new OnlineUsageStats();
            ous.start();
        }

        // Start AlertHTTPD
        if (ConfigObj.getActiveInstance().getAcceptRemoteAlertsBool()){
            try{
                new AlertHTTPD();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

}

