/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasoul.datashow;

import java.util.Date;
import java.util.LinkedList;


/**
 *
 * @author samuel
 */
public class LastAlertShown {
    
    public class LastAlertShownData {
        private Date date;
        private String text;
        public LastAlertShownData(String s){ 
            date = new Date();
            text = s;
        }
        public String getHtmlLine(){
            return "<tr class='alertcontent'><td width='10%'>" + 
                    TimerManager.getInstance().formatDate(date) + 
                    "</td><td width='90%'>" +
                    text + 
                    "</td></tr>";
        }
    }
    
    private LinkedList<LastAlertShownData> list;
    
    private static LastAlertShown instance = null;
    private LastAlertShown() {
        list = new LinkedList<>();
    }
    public static LastAlertShown getInstance(){
        if (instance == null)
            instance = new LastAlertShown();
        return instance;
    }
    
    public void addAlert(String s) {
        LastAlertShownData obj = new LastAlertShownData(s);
        if (list.size() >= 15){
            list.removeLast();
        }
        list.addFirst(obj);
    }
    
    public String getHtmlTable(){
        StringBuffer sb = new StringBuffer();
        for (LastAlertShownData s : list){
            sb.append(s.getHtmlLine());
        }
        return sb.toString();
    }
}
