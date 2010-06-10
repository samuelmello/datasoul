/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import datasoul.config.UsageStatsConfig;
import datasoul.render.OutputDevice;
import datasoul.serviceitems.song.AllSongsListTable;
import datasoul.templates.TemplateManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;


/**
 *
 * @author samuel
 */
public class OnlineUsageStats extends Thread {

    private static final long UPD_INTERVAL = 3 * 24 * 60 * 60 * 1000; // 3 days
    //private static final long UPD_INTERVAL = 0; // uncomment for debug

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

        sb.append(OnlineUpdateCheck.ONLINE_BASE_URL+"datasoulweb?");

        sb.append("sysid=");
        sb.append(UsageStatsConfig.getInstance().getID());
        sb.append("&");

        sb.append("osname=");
        try {
            sb.append(URLEncoder.encode(System.getProperty("os.name"), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            sb.append("unknown");
        }
        sb.append("&");

        sb.append("osversion=");
        try {
            sb.append(URLEncoder.encode(System.getProperty("os.version"), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            sb.append("unknown");
        }
        sb.append("&");

        sb.append("javaversion=");
        try {
            sb.append(URLEncoder.encode(System.getProperty("java.version"), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            sb.append("unknown");
        }
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
        sb.append("&");

        sb.append("numdisplays=");
        sb.append(OutputDevice.getNumDisplays());
        sb.append("&");
        
        sb.append("geometry1=");
        sb.append(ConfigObj.getActiveInstance().getMainOutputDeviceObj().getDiplayGeometry());
        sb.append("&");

        sb.append("geometry2=");
        if (ConfigObj.getActiveInstance().getMonitorOutputDeviceObj() != null){
            sb.append(ConfigObj.getActiveInstance().getMonitorOutputDeviceObj().getDiplayGeometry());
        }else{
            sb.append("disabled");
        }

        HttpClient client = new HttpClient();
        
        HttpMethod method = new GetMethod(sb.toString());
        
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

