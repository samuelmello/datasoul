/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.commands.GstDisplayCmd;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuellucas
 */
public class GstManagerServer {

    private static GstManagerServer instance;

    private GstManagerServer(){

    }

    public static GstManagerServer getInstance(){
            if (instance == null){
                instance = new GstManagerServer();
            }
            return instance;
    }

    private Process proc;
    private ServerSocket ss;
    protected Socket s;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    public void start(){
        try {
            ss = new ServerSocket(34912);
            String[] cmd = { System.getProperty("java.home")+File.separator+"bin"+File.separator+"java",
                        "-cp",
                        System.getProperty("java.class.path"),
                        "datasoul.render.gstreamer.GstManagerClient"
            };
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream();
            proc = pb.start();

            new StdoutDumpThread().start();

            s = ss.accept();
            s.setTcpNoDelay(true);
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

            new WorkerThread().start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    public synchronized void sendCommand(GstDisplayCmd obj) {
        try {
            if (output == null){
                System.err.println("Rejecting command "+obj);
                return;
            }
            output.writeObject(obj);
            output.reset();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public class WorkerThread extends Thread {
        public void run(){
            try {
                while (true){
                    Object o = input.readObject();
                    if (o instanceof GstEventNotification){
                        ((GstEventNotification)o).run();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public class StdoutDumpThread extends Thread {
        public void run(){
            try {
                int x;
                while ((x = proc.getInputStream().read()) > 0) {
                    if (x < 0) break;
                    System.out.print((char) x);
                }
            } catch (IOException ex) {
                Logger.getLogger(GstManagerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
