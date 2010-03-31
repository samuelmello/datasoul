/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul;

import datasoul.config.BackgroundConfig;
import datasoul.config.ConfigObj;
import datasoul.config.DisplayControlConfig;
import datasoul.datashow.TimerManager;
import datasoul.render.ContentManager;
import datasoul.render.gstreamer.GstManagerServer;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.commands.GstDisplayCmdInit;
import datasoul.serviceitems.song.AllSongsListTable;
import datasoul.serviceitems.song.ChordsDB;
import datasoul.util.DatasoulKeyListener;
import datasoul.util.ObjectManager;
import datasoul.util.OnlineUpdateCheck;
import datasoul.util.Splash;
import java.awt.AWTEvent;
import java.awt.GraphicsEnvironment;
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
        if (stgloc == null || stgloc.trim().equals("")){
            stgloc = System.getProperty("user.home")+System.getProperty("file.separator")+".datasoul"+System.getProperty("file.separator")+"data";
            ConfigObj.getActiveInstance().setStorageLoc(stgloc);
            ConfigObj.getActiveInstance().save();
        }
        System.setProperty("datasoul.stgloc", stgloc);

        File stglocdir  = new File(stgloc);
        if (!stglocdir.exists()){
            stglocdir.mkdirs();
        }

        File templates  = new File(stgloc+System.getProperty("file.separator")+"templates");
        if (!templates.exists()){
            templates.mkdirs();
            copySampleTemplates(templates.getAbsolutePath());
        }

        File songs  = new File(stgloc+System.getProperty("file.separator")+"songs");
        if (!songs.exists()){
            songs.mkdirs();
            copySampleSongs(songs.getAbsolutePath());
        }

        File serviceslist  = new File(stgloc+System.getProperty("file.separator")+"servicelists");
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
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
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
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }


    protected static void copySampleServices(String dir){
        String files[] = {"SampleService.servicelist"};

        for (String f: files){
            try{
                copyFile("samples/"+f, dir+System.getProperty("file.separator")+f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }


    void run() {

        // Use IPv4, needed by http-commons
        System.getProperties().setProperty("java.net.preferIPv4Stack", "true");

        // Enable anti-aliasing
        System.setProperty("swing.aatext","true");

        // JVM is broken for windows Vista and 7
        // Disable Direct3D to avoid problems with Swing
        if (System.getProperty("os.name").contains("Windows")){
            System.setProperty("sun.java2d.d3d","false");
        }



        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            //ignore and fall back to java look and feel
        }

        // Put the AvailableFontFamilyName list
        Thread t = new Thread(){
            @Override
          public void run(){
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
          }
        };
        t.start();

        //start splashscreen
        final Splash splash = new Splash();
        splash.setVisible(true);

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_configuration..."));
        ConfigObj.getActiveInstance();

        ContentManager.getInstance();
        checkStorageLocation();

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

        BackgroundConfig.getInstance();
        DisplayControlConfig.getInstance();

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_songs..."));
        AllSongsListTable.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Loading_chords_database..."));
        ChordsDB.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Starting_content_manager..."));
        TimerManager.getInstance();
        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Creating_user_interface..."));

        long l1 = System.currentTimeMillis();
        final DatasoulMainForm mainForm = new DatasoulMainForm();
        System.out.println(System.currentTimeMillis() - l1);

        splash.setStatusText(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Starting_application..."));

        Toolkit.getDefaultToolkit().addAWTEventListener( DatasoulKeyListener.getInstance(), AWTEvent.KEY_EVENT_MASK);

        // Join the FontFamily cache thread
        try{
            t.join();
        }catch(Exception e){
            // Do nothing
        }

        ContentManager.getInstance().initMainDisplay();
        ContentManager.getInstance().initMonitorDisplay();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainForm.setVisible(true);
                splash.setVisible(false);
                splash.dispose();
                ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().shortcutShowMain();
            }
        });

        // Check for online updates
        if (ConfigObj.getActiveInstance().getOnlineCheckUpdateBool()){
            OnlineUpdateCheck ouc = new OnlineUpdateCheck();
            ouc.start();
        }

    }

}
