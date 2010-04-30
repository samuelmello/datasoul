/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.config.ConfigObj;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.notifications.GstNotification;
import datasoul.render.gstreamer.notifications.GstNotificationCmdDone;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
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
    protected Semaphore connectSemaphore;
    protected Semaphore cmdSemaphore;
    protected StdoutDumpThread stdoutDumpThread;
    protected WorkerThread workerThread;

    public boolean start(){
        try {
            ss = new ServerSocket(34912);
            String[] cmd = { System.getProperty("java.home")+File.separator+"bin"+File.separator+"java",
                        "-cp",
                        System.getProperty("java.class.path"),
                        "datasoul.render.gstreamer.GstManagerClient"
            };
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            proc = pb.start();

            stdoutDumpThread = new StdoutDumpThread();
            stdoutDumpThread.start();
            
            connectSemaphore = new Semaphore(0);
            cmdSemaphore = new Semaphore(0);
            
            workerThread = new WorkerThread();
            workerThread.start();

            for (int retries = 0; retries < 10; retries ++){
                if (connectSemaphore.tryAcquire(1, TimeUnit.SECONDS)){
                    return true;
                }else{
                    try{
                        int ret = proc.exitValue(); // should raise an exception if not terminated
                        System.out.println("Gstreamer process returned "+ret);
                        break;
                    }catch(IllegalThreadStateException e){
                        continue; // ignore and try again
                    }
                }
            }

            /* If reach here, the program didn't connected neither exited
             * Kill it and return false
             */

            proc.destroy();
            workerThread.interrupt();
            stdoutDumpThread.interrupt();
            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public synchronized void sendCommand(GstDisplayCmd obj) {
        /* Ignore for swing output */
        if (!ConfigObj.getActiveInstance().isGstreamerActive()){
            return;
        }

        try {

            if (output == null){
                System.err.println("Rejecting command "+obj);
                return;
            }
            output.writeObject(obj);
            output.reset();

            cmdSemaphore.acquire();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isRunning(){
        return (output != null);
    }

    public class WorkerThread extends Thread {
        @Override
        public void run(){
            try {
                s = ss.accept();
                s.setTcpNoDelay(true);
                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());

                connectSemaphore.release();

                while (true){
                    Object o = input.readObject();
                    if (o instanceof GstNotification){
                        if (o instanceof GstNotificationCmdDone){
                            cmdSemaphore.release();
                        }else{
                            ((GstNotification)o).run();
                        }
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

