/* 
 * Copyright 2012 Samuel Mello
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
package datasoul.render.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
            dialog.setStatus(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO CONNECT. UNKNOWN HOST: ") + ex.getMessage());
            return;
        } catch (IOException ex) {
            dialog.setStatus(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DISCONNECTED"));
            return;
        }
        
        dialog.setStatus(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CONNECTED"));

        try {
            while(true){
                Object o;
                try {
                    o = ois.readObject();
                    if (o instanceof RemoteContentCommand){
                        RemoteContentCommand cmd = (RemoteContentCommand) o;
                        cmd.run();
                    }
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }catch(IOException ex){
            dialog.setStatus(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DISCONNECTED"));
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
