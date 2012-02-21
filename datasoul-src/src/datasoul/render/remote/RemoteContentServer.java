/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasoul.render.remote;

import datasoul.config.BackgroundConfig;
import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuel
 */
public class RemoteContentServer {
    
    static private RemoteContentServer instance = null;
    
    protected ServerThread server;
    protected Semaphore connectSemaphore;
    protected ArrayList<WorkerThread> workerers;
    protected RemoteContentRender mainRender;
    protected RemoteContentRender monitorRender;
    
    private RemoteContentServer(){
        workerers = new ArrayList<WorkerThread>();
        server = new ServerThread();
    }
    
    static public RemoteContentServer getInstance(){
        if (instance == null){
            instance = new RemoteContentServer();
        }
        return instance;
    }
    
    public void startServer(){
        server.start();
        mainRender = new RemoteContentRender(RemoteContentCommand.Target.TARGET_MAIN);
        ContentManager.getInstance().registerMainRender(mainRender);
        monitorRender = new RemoteContentRender(RemoteContentCommand.Target.TARGET_MONITOR);
        ContentManager.getInstance().registerMonitorRender(mainRender);
    }

    public void sendCommand(RemoteContentCommand cmd) {
        for (WorkerThread t : workerers){
            t.sendObject(cmd);
        }
    }
    
    public class ServerThread extends Thread {
        private ServerSocket ss;
        
        @Override
        public void run(){
            try{
                ss = new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(34913));
                while(true){
                    Socket s = ss.accept();
                    WorkerThread t = new WorkerThread(s);
                    workerers.add(t);
                    t.start();
                    initRemoteConnection();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void initRemoteConnection(){
        ContentManager.getInstance().refreshLive();
        ObjectManager.getInstance().getAuxiliarPanel().getDisplayControlPanel().refreshStatus();
        // Update output visible
        RemoteContentCommand cmd = new RemoteContentCommand(RemoteContentCommand.Target.TARGET_MAIN, ObjectManager.getInstance().isOutputVisible());
        RemoteContentServer.getInstance().sendCommand(cmd);

    }
    

    public class WorkerThread extends Thread {

        protected ObjectOutputStream output;
        protected ObjectInputStream input;
        protected Socket s;
        protected boolean quit;

        public WorkerThread(Socket s) throws IOException{
            this.s = s;
            this.quit = false;
            this.setName("RemoteWorkerThread"+s.getRemoteSocketAddress().toString());
            s.setTcpNoDelay(true);
            output = new ObjectOutputStream(s.getOutputStream());
            
            mainRender = new RemoteContentRender(RemoteContentCommand.Target.TARGET_MAIN);
            
        }

        public void disconnect(){
            this.quit = true;
        }

        @Override
        public void run(){
            try {
                input = new ObjectInputStream(s.getInputStream());
                while (quit == false){
                    Object o = input.readObject();
                    System.out.println(o);
                }
            } catch (EOFException eof){
                // Silently ignore disconnection
            } catch (SocketException eof){
                // Silently ignore disconnection
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            workerers.remove(WorkerThread.this);
        }

        public void sendObject(Serializable obj){
            try {
                output.writeObject(obj);
            } catch (IOException ex) {
                Logger.getLogger(RemoteContentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}
