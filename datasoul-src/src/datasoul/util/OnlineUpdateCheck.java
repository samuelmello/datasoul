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

    public static final int VERSION = 4;

    public static final String ONLINE_BASE_URL = "http://datasoul-presentation.appspot.com/";

    @Override
    public void run(){
        
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(ONLINE_BASE_URL+"latest-version");
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
                String msg = java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NEW DATASOUL VERSION")+
                        " "+toks[1]+" "+
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("IS AVAILABLE AT")+
                        " "+toks[2];

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

