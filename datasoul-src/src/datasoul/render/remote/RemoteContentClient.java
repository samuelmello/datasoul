/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasoul.render.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
    
    public void run(){
        try {
            s = new Socket("localhost", 34913);
            ois = new ObjectInputStream(s.getInputStream());
            
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
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(RemoteContentClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemoteContentClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
