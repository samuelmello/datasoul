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

import datasoul.config.BackgroundConfig;
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
import datasoul.render.ContentManager;
import datasoul.render.gstreamer.commands.GstDisplayCmd;
import datasoul.render.gstreamer.notifications.GstNotification;
import datasoul.util.ObjectManager;

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

    protected ServerThread server;
    protected Semaphore connectSemaphore;
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

    public Process startRemote(String addr){
        try {
            String[] cmd = { System.getProperty("java.home")+File.separator+"bin"+File.separator+"java",
                        "-cp",
                        System.getProperty("java.class.path"),
                        "datasoul.render.gstreamer.GstManagerClient",
                        addr
            };
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process proc = pb.start();

            StdoutDumpThread dumpThread = new StdoutDumpThread();
            dumpThread.setProcess(proc);
            dumpThread.start();

            return proc;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
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
            Process proc = pb.start();

            StdoutDumpThread stdoutDumpThread = new StdoutDumpThread();
            stdoutDumpThread.setProcess(proc);
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

        for (WorkerThread w : workerers) {
            try {
                if (w.getOutput() == null) {
                    System.err.println("Rejecting command " + obj);
                    return;
                }
                w.getOutput().writeObject(obj);
                w.getOutput().reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isRunning(){
        return (workerers.size() > 0);
    }

    public void initRemoteConnection(){
        ContentManager.getInstance().refreshLive();
        ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().refreshStatus();
        ObjectManager.getInstance().refreshOutputVisible();
        BackgroundConfig.getInstance().refreshMode();
    }

    public void clientConnected(boolean isLocal){

        Thread t = Thread.currentThread();
        
        if (t instanceof WorkerThread){
            WorkerThread w = ((WorkerThread)t);
            w.setLocal(isLocal);
            w.setConnected(true);
            if (isLocal){
                connectSemaphore.release();
            }else{
                if (ConfigObj.getActiveInstance().getAcceptRemoteDisplaysBool()){
                    initRemoteConnection();
                }else{
                    w.disconnect();
                    System.out.println("Disconnected remote client");
                }
            }
        }
        
    }

    public class WorkerThread extends Thread {

        protected ObjectOutputStream output;
        protected ObjectInputStream input;
        protected Socket s;
        protected boolean isLocal;
        protected boolean isConnected;
        protected boolean quit;

        public WorkerThread(Socket s){
            this.s = s;
            this.quit = false;
            this.setName("GstWorkerThread"+s.getRemoteSocketAddress().toString());
        }

        public void setLocal(boolean isLocal){
            this.isLocal = isLocal;
        }

        public void setConnected(boolean isConnected){
            this.isConnected = isConnected;
        }

        public void disconnect(){
            this.quit = true;
        }

        @Override
        public void run(){
            try {
                s.setTcpNoDelay(true);
                output = new ObjectOutputStream(s.getOutputStream());
                input = new ObjectInputStream(s.getInputStream());

                while (quit == false){
                    Object o = input.readObject();
                    if (o instanceof GstNotification){
                        GstNotification notif = ((GstNotification)o);
                        if (isConnected && isLocal){
                            notif.run();
                        }else if (notif.isUnconnectedAllowed()) {
                            notif.run();
                        }
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

        private Process proc;

        public void setProcess(Process proc){
            this.proc = proc;
        }

        public void run(){
            this.setName("StdoutDumpThread "+proc);
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

