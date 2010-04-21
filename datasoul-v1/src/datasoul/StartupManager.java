/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul;

import datasoul.config.BackgroundConfig;
import datasoul.config.ConfigObj;
import datasoul.config.UsageStatsConfig;
import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.commands.GstDisplayCmdInit;
import datasoul.serviceitems.song.AllSongsListTable;
import datasoul.util.DatasoulKeyListener;
import datasoul.util.ObjectManager;
import datasoul.util.OnlineUpdateCheck;
import datasoul.util.OnlineUsageStats;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.UIManager;

/**
 *
 * @author samuel
 */
public class StartupManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        StartupManager sm = new StartupManager();
        sm.run();
    }

    public static void checkStorageLocation(){
        String stgloc = ConfigObj.getActiveInstance().getStorageLoc();

        File stglocdir  = new File(stgloc);
        if (!stglocdir.exists()){
            stglocdir.mkdirs();
        }

        File templates  = new File(ConfigObj.getActiveInstance().getStoragePathTemplates());
        if (!templates.exists()){
            templates.mkdirs();
            copySampleTemplates(templates.getAbsolutePath());
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

    protected static void copyFile(String resource, String targetName) throws IOException{
        InputStream is = DatasoulMainForm.class.getResourceAsStream(resource);
        FileOutputStream fos = new FileOutputStream(targetName);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();
    }

    protected static void copySampleTemplates(String dir){
        String files[] = {"alert-general.templatez",
            "alert-nursery.templatez",
            "alert-parking.templatez",
            "bible.templatez",
            "monitor-preaching.templatez",
            "monitor-song.templatez",
            "default.templatez",
            "song.templatez",
            "subtitle.templatez"
        };

        for (String f: files){
            try{
                copyFile("samples/"+f, dir + File.separator + f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    protected static void copySampleSongs(String dir){
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


    protected static void copySampleServices(String dir){
        String files[] = {"SampleService.servicelist"};

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

    void run() {

        long l1 = System.currentTimeMillis();

        // Use IPv4, needed by http-commons
        System.getProperties().setProperty("java.net.preferIPv4Stack", "true");

        // Enable anti-aliasing
        System.setProperty("swing.aatext","true");

        // MacOS menu
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // JVM is broken for windows Vista and 7
        // Disable Direct3D to avoid problems with Swing
        if (System.getProperty("os.name").contains("Windows")){
            System.setProperty("sun.java2d.d3d","false");
        }

        updateSplash("Initializing...");

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            //ignore and fall back to java look and feel
        }

        ConfigObj.getActiveInstance();

        ContentManager.getInstance();
        checkStorageLocation();

        updateSplash("Loading Songs...");
        AllSongsListTable.getInstance();

        updateSplash("Creating User Interface...");

        final DatasoulMainForm mainForm = new DatasoulMainForm();
        mainForm.setVisible(true);

        System.out.println(System.currentTimeMillis() - l1);


        try{
            if (ConfigObj.getActiveInstance().isGstreamerActive()){
                boolean gst = GstManagerServer.getInstance().start();
                if (gst){
                    GstDisplayCmd cmd = new GstDisplayCmdInit(
                        ConfigObj.getActiveInstance().getMonitorOutput(),
                        ConfigObj.getActiveInstance().getMainOutputDevice(),
                        ConfigObj.getActiveInstance().getMonitorOutputDevice());
                    GstManagerServer.getInstance().sendCommand(cmd);
                }else{
                    ConfigObj.getActiveInstance().setGstreamerActive(gst);
                }
            }
        }catch(Exception e){
            ConfigObj.getActiveInstance().setGstreamerActive(false);
        }


        Toolkit.getDefaultToolkit().addAWTEventListener( DatasoulKeyListener.getInstance(), AWTEvent.KEY_EVENT_MASK);

        // Init displays
        ContentManager.getInstance().initMainDisplay();
        ContentManager.getInstance().initMonitorDisplay();

        // Ensure background properly loaded
        BackgroundConfig.getInstance().refreshMode();

        // Check for first time run
        if (UsageStatsConfig.getInstance().getID() == null || UsageStatsConfig.getInstance().getID().length() == 0){
            UsageStatsGrantDialog dialog = new UsageStatsGrantDialog(new javax.swing.JFrame(), true);
            dialog.setLocationRelativeTo(mainForm);
            dialog.setVisible(true);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().shortcutShowMain();
            }
        });

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


    }

}
