/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer.notifications;

import datasoul.servicelist.ServiceListTable;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 *
 * @author samuel
 */
public class GstNotificationFileOpen extends GstNotification {

    private String filename;

    public GstNotificationFileOpen(String f){
        this.filename = f;
    }

    @Override
    public void run(){

        // If user provided an initial file, open it
        if (filename != null && (filename.endsWith(".servicelistz") || filename.endsWith(".servicelist"))){
            final File f = new File(filename);
            if (f.exists()){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        ServiceListTable.getActiveInstance().openFile(f.getAbsolutePath());
                    }
                });
            }
        }
    }
}
