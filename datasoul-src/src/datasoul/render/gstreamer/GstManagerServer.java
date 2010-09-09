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

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import datasoul.config.ConfigObj;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.notifications.GstNotification;

/**
 *
 * @author samuellucas
 */
public class GstManagerServer {

    private static GstManagerServer instance;

    private GstManagerServer(){
        workerers = new ArrayList<WorkerThread>();
        server = new ServerThread();
        server.start();
    }

    public static GstManagerServer getInstance(){
            if (instance == null){
                instance = new GstManagerServer();
            }
            return instance;
    }

    private Process proc;
    protected ServerThread server;
    protected Semaphore connectSemaphore;
    protected StdoutDumpThread stdoutDumpThread;
    protected ArrayList<WorkerThread> workerers;

    public class ServerThread extends Thread {
        private ServerSocket ss;
        
        @Override
        public void run(){
            try{
                ss = new ServerSocket(34912);
                while(true){
                    Socket s = ss.accept();
                    WorkerThread t = new WorkerThread(s);
                    workerers.add(t);
                    t.start();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean start(){
        try {
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

            for (WorkerThread w : workerers){
                if (w.getOutput() == null){
                    System.err.println("Rejecting command "+obj);
                    return;
                }
                w.getOutput().writeObject(obj);
                w.getOutput().reset();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isRunning(){
        return (workerers.size() > 0);
    }

    public void clientConnected(){
        connectSemaphore.release();
    }

    public class WorkerThread extends Thread {

        protected ObjectOutputStream output;
        protected ObjectInputStream input;
        protected Socket s;

        public WorkerThread(Socket s){
            this.s = s;
        }

        @Override
        public void run(){
            try {
                s.setTcpNoDelay(true);
                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());

                while (true){
                    Object o = input.readObject();
                    if (o instanceof GstNotification){
                        ((GstNotification)o).run();
                    }
                }
            } catch (SocketException eof){
                // Silently ignore disconnection
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            workerers.remove(WorkerThread.this);
        }

        public ObjectOutputStream getOutput(){
            return output;
        }

        public ObjectInputStream getInput(){
            return input;
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

