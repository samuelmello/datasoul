/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import datasoul.config.UsageStatsConfig;
import datasoul.serviceitems.song.AllSongsListTable;
import datasoul.templates.TemplateManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;


/**
 *
 * @author samuel
 */
public class OnlineUsageStats extends Thread {

    private static final long UPD_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours

    @Override
    public void run(){

        // Check if enabled
        if (!ConfigObj.getActiveInstance().getOnlineUsageStatsBool()){
            return;
        }

        // Check time from last update
        long last = 0;
        try{
            last = Long.parseLong(UsageStatsConfig.getInstance().getLastSent());
        }catch(Exception e){
            // ignore
        }

        if (System.currentTimeMillis() < last + UPD_INTERVAL){
            return;
        }

        StringBuffer sb = new StringBuffer();

        sb.append("http://datasoul.sourceforge.net/usage.php?");

        sb.append("sysid=");
        sb.append(UsageStatsConfig.getInstance().getID());
        sb.append("&");

        sb.append("osname=");
        sb.append(System.getProperty("os.name"));
        sb.append("&");

        sb.append("osversion=");
        sb.append(System.getProperty("os.version"));
        sb.append("&");

        sb.append("javaversion=");
        sb.append(System.getProperty("java.version"));
        sb.append("&");

        sb.append("dsversion=");
        sb.append(DatasoulMainForm.getVersion());
        sb.append("&");

        sb.append("templates=");
        sb.append(TemplateManager.getInstance().getRowCount());
        sb.append("&");

        sb.append("songall=");
        sb.append(AllSongsListTable.getInstance().getSongCount());
        sb.append("&");

        sb.append("songchords=");
        sb.append(AllSongsListTable.getInstance().getSongsWithChords());

        HttpClient client = new HttpClient();
        
        HttpMethod method;
        try {
            method = new GetMethod(URLEncoder.encode(sb.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return;
        }
        
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                UsageStatsConfig.getInstance().setLastSent(Long.toString(System.currentTimeMillis()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

    }

}
