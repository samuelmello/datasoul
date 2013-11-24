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

import datasoul.DatasoulMainForm;
import datasoul.datashow.LastAlertShown;
import datasoul.serviceitems.ServiceItem;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.servicelist.ServiceListTable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import nanohttpd.NanoHTTPD;

/**
 *
 * @author samuel
 */
public class PublicHTTPD extends NanoHTTPD {
    
    public PublicHTTPD () throws IOException {
        super(8012);
    }
    
    private void getHeader(StringBuilder sb){
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

        sb.append(".objcontent { display: none; background: #fff; }");
        sb.append(".alertcontent { background: #fff; }");
        sb.append(".objhdr { font-weight: bold; background: #ddd; padding: 5px; }");
        sb.append(".objtable { background: #333; width: 100%; }");
        sb.append(".sectionhdr { font-weight: bold; background: #bbb; padding: 5px; }");
        sb.append("td { padding: 5px; }");

        sb.append("-->");
        sb.append("</style>");

        sb.append("<script language='javascript'>\n");
        sb.append("function showHide(id){\n");
        sb.append("	if (id.style.display == 'table-row'){\n");
        sb.append("		id.style.display = 'none';\n");
        sb.append("	}else{\n");
        sb.append("		id.style.display = 'table-row';\n");
        sb.append("	}\n");
        sb.append("}\n");
        sb.append("function goAlerts(){\n");
        sb.append("	window.location.href='/alerts';\n");
        sb.append("}\n");
        sb.append("function goHome(){\n");
        sb.append("	window.location.href='/';\n");
        sb.append("}\n");
        sb.append("</script>");

        sb.append("</head>");
        sb.append("<img src='datasoul-web-logo.png' /Alerts>");

        sb.append("<div class='content'>");
    }
    
    private String getService(){
        
        ResourceBundle i18n = java.util.ResourceBundle.getBundle("datasoul/internationalize");
        
        StringBuilder sb = new StringBuilder();

        getHeader(sb);
        
        sb.append("<h2>");
        ServiceListTable slt = ServiceListTable.getActiveInstance();
        
        sb.append(slt.getTitle());
        sb.append("</h2>");
        
        sb.append("<table class='objtable'>");
        
        for (int i=0; i<slt.getRowCount(); i++){
            ServiceItem si = slt.getServiceItem(i);
            if (si instanceof TextServiceItem){
                sb.append("<tr class='objhdr' onclick='showHide(data"+i+");'><td>");
                sb.append(si.getTitle());
                sb.append("</td></tr>");
                sb.append("<tr id='data"+i+"' class='objcontent'><td>");
                sb.append( ((TextServiceItem)si).getText().replaceAll("===", "<br>").replaceAll("==", "").replaceAll("\n", "<br>") );
                sb.append("</td></tr>");
            }
        }
        
        sb.append("</table>");

        sb.append("<br>");
        
        sb.append("<table class='objtable'>");
        sb.append("<tr><td class='sectionhdr' onclick='goAlerts();'>");
        sb.append(i18n.getString("ALERT"));
        sb.append("</tr></td>");
        sb.append("</table>");
        
        sb.append("</div>");
        sb.append("</html>");
        return sb.toString();
    }
    
    private String getAlerts(){
        
        ResourceBundle i18n = java.util.ResourceBundle.getBundle("datasoul/internationalize");
        
        StringBuilder sb = new StringBuilder();

        getHeader(sb);

        sb.append("<h2>");
        sb.append(i18n.getString("ALERT"));
        sb.append("</h2>");
        
        sb.append("<table class='objtable'>");
        sb.append("<tr class='objhdr'><td colspan='2'>");
        sb.append(i18n.getString("ALERT"));
        sb.append("</td></tr>");
        sb.append(LastAlertShown.getInstance().getHtmlTable());
        sb.append("</table>");
        
        sb.append("<br>");
        
        sb.append("<table class='objtable'>");
        sb.append("<tr><td class='sectionhdr' onclick='goHome();'>");
        sb.append(i18n.getString("SERVICE LIST"));
        sb.append("</tr></td>");
        sb.append("</table>");

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
            
            if (uri.equals("/alerts"))
                output = this.getAlerts();
            else
                output = this.getService();
            
            return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, output);
    }
    
}
