/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author samuel
 */
public class OnlineUpdateCheck extends Thread {

    public static final int VERSION = 0;

    @Override
    public void run(){
        
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod("http://datasoul.sourceforge.net/onlineupdatecheck.txt");
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                return;
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            String resp = new String(responseBody);

            // Expected format is: <NUMBER>;<NEW VERSION>;<URL>
            String toks[] = resp.split(";");

            if (toks.length != 3){
                return;
            }

            int latestversion = Integer.parseInt(toks[0]);

            if (latestversion > VERSION){
                String msg = "New Datasoul version"+
                        " "+toks[1]+" "+
                        "is available at"+
                        " "+toks[2];

                System.out.println(msg);
                ObjectManager.getInstance().getDatasoulMainForm().setInfoText(msg);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

    }


}

