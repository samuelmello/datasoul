/*
 * Copyright 2005-2012 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 */


package datasoul.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import nanohttpd.NanoHTTPD;
import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import datasoul.datashow.Alert;
import datasoul.templates.DisplayTemplate;
import datasoul.templates.DisplayTemplateMetadata;
import datasoul.templates.TemplateManager;

/**
 *
 * @author samuel
 */
public class AlertHTTPD extends NanoHTTPD {
    
    public AlertHTTPD () throws IOException {
        super(8011);
    }
    
    private String getForm(String msg){
        
        ResourceBundle i18n = java.util.ResourceBundle.getBundle("datasoul/internationalize");
        
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Datasoul</title>");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />");
        sb.append("<meta name='viewport' content='width=device-width, initial-scale=1' />");
        sb.append("<style type='text/css'>");
        sb.append("<!--");

        sb.append("* {");
        sb.append("	margin: 0;");
        sb.append("	padding: 0;");
        sb.append("}");

        sb.append("img {");
        sb.append("	margin: 5px;");
        sb.append("}");

        sb.append("body{");
        sb.append("	font-family: sans-serif;");
        sb.append("	color: #333;");
        sb.append("	background: #bbb;");
        sb.append("	margin: 0;");
        sb.append("}");

        sb.append(".content {");
        sb.append("	background: #fff;");
        sb.append("	padding: 5px;");
        sb.append("}");

        sb.append("h2 {");
        sb.append("	margin-top: 0.5em;");
        sb.append("	margin-bottom: 0.5em;");
        sb.append("}");

        sb.append(".dsinput {");
        sb.append("	margin-bottom: 0.5em;");
        sb.append("}");

        sb.append(".msg {");
        sb.append("	background: #ff0;");
        sb.append("	padding: 5px;");
        sb.append("}");

        sb.append("-->");
        sb.append("</style>");


        sb.append("</head>");
        sb.append("<img src='datasoul-web-logo.png' />");

        sb.append("<div class='content'>");

        if (msg != null){
            sb.append("<div class='msg'>");
            sb.append(msg);
            sb.append("</div>");
        }

        sb.append("<h2>");
        sb.append(i18n.getString("SHOW ALERT"));
        sb.append("</h2>");
        sb.append("<form action='/' method='POST'>");

        sb.append("<div class='dsinput'>");
        sb.append(i18n.getString("TEXT"));
        sb.append(":<br>");
        sb.append("<textarea name='text' cols='30'></textarea>");
        sb.append("</div>");

        sb.append("<div class='dsinput'>");
        sb.append(i18n.getString("DURATION"));
        sb.append(": <select name='duration'>");
        for(int i=1; i<60; i++){
            if (i==5){
                sb.append("<option name='"+i+"' selected>"+i+"</option>");
            }else{
                sb.append("<option name='"+i+"'>"+i+"</option>");
            }
        }
        sb.append("</select> ");
        sb.append(i18n.getString("SECONDS"));
        sb.append("</div>");

        sb.append("<div class='dsinput'>");
        sb.append("<input type='checkbox' name='showonmain' checked/> ");
        sb.append(i18n.getString("SHOW ON MAIN OUTPUT"));
        sb.append("</div>");

        sb.append("<div class='dsinput'>");
        sb.append("&nbsp;&nbsp;&nbsp;");
        sb.append(i18n.getString("TEMPLATE"));
        sb.append(": <select name='maintemplate'>");
        for(int i=0; i<TemplateManager.getInstance().getRowCount(); i++){
            DisplayTemplateMetadata meta = TemplateManager.getInstance().getDisplayTemplateMetadata(i);
            if (meta.getTargetContentIdx() == DisplayTemplate.TARGET_CONTENT_ALERT){
                sb.append("<option name='"+meta.getName()+"'>"+meta.getName()+"</option>");
            }
        }
        sb.append("</select>");
        sb.append("</div>");


        if (ConfigObj.getActiveInstance().getMonitorOutput()){
            sb.append("<div class='dsinput'>");
            sb.append("<input type='checkbox' name='showonmonitor' /> ");
            sb.append(i18n.getString("SHOW ON STAGE OUTPUT"));
            sb.append("</div>");
            sb.append("<div class='dsinput'>");
            sb.append("&nbsp;&nbsp;&nbsp;");
            sb.append(i18n.getString("TEMPLATE"));
            sb.append(": <select name='monitortemplate'>");
            for(int i=0; i<TemplateManager.getInstance().getRowCount(); i++){
                DisplayTemplateMetadata meta = TemplateManager.getInstance().getDisplayTemplateMetadata(i);
                if (meta.getTargetContentIdx() == DisplayTemplate.TARGET_CONTENT_ALERT){
                    sb.append("<option name='"+meta.getName()+"'>"+meta.getName()+"</option>");
                }
            }
            sb.append("</select>");
            sb.append("</div>");
        }

        sb.append("<br>");
        sb.append("<div class='dsinput'>");
        sb.append("<input type='submit' value='   "+i18n.getString("SHOW ALERT")+"   ' />");
        sb.append("</div>");

        sb.append("</div>");

        sb.append("</form>");
        sb.append("</html>");
        return sb.toString();
    }
    
    
    @Override
    public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
    {
            String output = "";
            
            if (uri.equals("/datasoul-web-logo.png")){
                
                InputStream is = DatasoulMainForm.class.getResourceAsStream("/datasoul/icons/datasoul-web-logo.png");
                
                Response r = new Response( HTTP_OK, "image/png", is );

                return r;

            }
            
            if (method.equals("POST")){

                String text = parms.getProperty("text");
                String maintemplate = parms.getProperty("maintemplate");
                String showonmain = parms.getProperty("showonmain");
                String monitortemplate = parms.getProperty("monitortemplate");
                String showonmonitor = parms.getProperty("showonmonitor");
                int duration = Integer.parseInt(parms.getProperty("duration"));

                // Consistence check
                if (text.trim().length() == 0 || 
                        (showonmain == null && showonmonitor == null)){
                    output = this.getForm("Invalid Parameters");
                }else{
                    Alert obj = new Alert();
                    obj.setText(text);
                    obj.setTime(duration*1000);
                    obj.setShowOnMain(showonmain != null);
                    obj.setShowOnMonitor(showonmonitor != null);
                    obj.setMainTemplate(maintemplate);
                    obj.setMonitorTemplate(monitortemplate);
                    Alert.enqueue(obj);
                    
                    output = this.getForm("Alert sent");
                }
                
            }else{
                output = this.getForm(null);
            }
            
            return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, output);
    }
    
}
