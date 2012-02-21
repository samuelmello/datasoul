/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasoul.render.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuel
 */
public class RemoteContentClient {
    
    Socket s;
    ObjectInputStream ois;
    String hostname;
    RemoteLauncherDialog dialog;
    
    public RemoteContentClient(String hostname, RemoteLauncherDialog dialog){
        this.hostname = hostname;
        this.dialog = dialog;
        this.s = new Socket();
    }
    
    public void run(){
        try {
            s.connect(new InetSocketAddress(hostname, 34913), 10000);
            ois = new ObjectInputStream(s.getInputStream());
        } catch (UnknownHostException ex) {
            dialog.setStatus("Unknown host: " + ex.getMessage());
            return;
        } catch (IOException ex) {
            dialog.setStatus("Unable to connect");
            return;
        }
        
        dialog.setStatus("Connected");

        try {
            while(true){
                Object o;
                try {
                    o = ois.readObject();
                    if (o instanceof RemoteContentCommand){
                        RemoteContentCommand cmd = (RemoteContentCommand) o;
                        //System.out.println(cmd);
                        cmd.run();
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RemoteContentClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }catch(IOException ex){
            dialog.setStatus("Disconnected");
            ex.printStackTrace();
        }
            
    }

    public void disconnect(){
        try {
            if (s != null)
                s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
