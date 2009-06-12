/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.config;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author samuel
 */
public class WindowPropConfig extends AbstractConfig {


    private static WindowPropConfig instance = null;

    public static synchronized WindowPropConfig getInstance(){
        if (instance == null){
            instance = new WindowPropConfig();
        }
        return instance;
    }

    /** Creates a new instance of ConfigObj */
    private WindowPropConfig() {
        load("windowProp.config");
    }

    public void save(){
        save("windowProp.config");
    }

    private String hMainForm;
    private String wMainForm;
    private String hTextEditor;
    private String wTextEditor;
    private String hSongEditor;
    private String wSongEditor;
    private String hSongSearch;
    private String wSongSearch;
    private String datashowSplit1;
    private String datashowSplit2;
    private String datashowSplit3;
    private String serviceSplit1;
    private String serviceSplit2;
    private String serviceSplit3;
    private String songSplit1;
    private String songSplit2;
    private String templateSplit1;
    private String templateSplit2;

    protected void registerProperties() {
        super.registerProperties();
        properties.add("HMainForm");
        properties.add("WMainForm");
        properties.add("HTextEditor");
        properties.add("WTextEditor");
        properties.add("HSongEditor");
        properties.add("WSongEditor");
        properties.add("HSongSearch");
        properties.add("WSongSearch");
        properties.add("DatashowSplit1");
        properties.add("DatashowSplit2");
        properties.add("DatashowSplit3");
        properties.add("ServiceSplit1");
        properties.add("ServiceSplit2");
        properties.add("ServiceSplit3");
        properties.add("SongSplit1");
        properties.add("SongSplit2");
        properties.add("TemplateSplit1");
        properties.add("TemplateSplit2");
   }

    private boolean checkStr(String str){
        return !(str == null || str.length() == 0);
    }

    public String getHMainForm() {
        return hMainForm;
    }

    public void setHMainForm(String hMainForm) {
        this.hMainForm = hMainForm;
    }

    public String getWMainForm() {
        return wMainForm;
    }

    public void setWMainForm(String wMainForm) {
        this.wMainForm = wMainForm;
    }

    public void setMainForm(JFrame m){
        setWMainForm(Integer.toString(m.getWidth()));
        setHMainForm(Integer.toString(m.getHeight()));
        save();
    }

    public void getMainForm(JFrame m){
        if (checkStr(wMainForm) && checkStr(hMainForm)){
            try{
                m.setSize(Integer.parseInt(wMainForm), Integer.parseInt(hMainForm));
            }catch(Exception e){
                //ignore
            }
        }
    }

    public String getHTextEditor() {
        return hTextEditor;
    }

    public void setHTextEditor(String hTextEditor) {
        this.hTextEditor = hTextEditor;
    }

    public String getWTextEditor() {
        return wTextEditor;
    }

    public void setWTextEditor(String wTextEditor) {
        this.wTextEditor = wTextEditor;
    }


    public void setTextEditor(JFrame m){
        setWTextEditor(Integer.toString(m.getWidth()));
        setHTextEditor(Integer.toString(m.getHeight()));
        save();
    }

    public void getTextEditor(JFrame m){
        if (checkStr(wTextEditor) && checkStr(hTextEditor)){
            try{
                m.setSize(Integer.parseInt(wTextEditor), Integer.parseInt(hTextEditor));
            }catch(Exception e){
                //ignore
            }
        }
    }


    public String getHSongEditor() {
        return hSongEditor;
    }

    public void setHSongEditor(String hSongEditor) {
        this.hSongEditor = hSongEditor;
    }

    public String getWSongEditor() {
        return wSongEditor;
    }

    public void setWSongEditor(String wSongEditor) {
        this.wSongEditor = wSongEditor;
    }


    public void setSongEditor(JFrame m){
        setWSongEditor(Integer.toString(m.getWidth()));
        setHSongEditor(Integer.toString(m.getHeight()));
        save();
    }

    public void getSongEditor(JFrame m){
        if (checkStr(wSongEditor) && checkStr(hSongEditor)){
            try{
                m.setSize(Integer.parseInt(wSongEditor), Integer.parseInt(hSongEditor));
            }catch(Exception e){
                //ignore
            }
        }
    }

    public String getHSongSearch() {
        return hSongSearch;
    }

    public void setHSongSearch(String hSongSearch) {
        this.hSongSearch = hSongSearch;
    }

    public String getWSongSearch() {
        return wSongSearch;
    }

    public void setWSongSearch(String wSongSearch) {
        this.wSongSearch = wSongSearch;
    }


    public void setSongSearch(JPanel m){
        setWSongSearch(Integer.toString(m.getWidth()));
        setHSongSearch(Integer.toString(m.getHeight()));
        save();
    }

    public void getSongSearch(JPanel m){
        if (checkStr(wSongSearch) && checkStr(hSongSearch)){
            try{
                m.setSize(Integer.parseInt(wSongSearch), Integer.parseInt(hSongSearch));
            }catch(Exception e){
                //ignore
            }
        }
    }

    public String getDatashowSplit1() {
        return datashowSplit1;
    }

    public void getDatashowSplit1(JSplitPane j){
        if (!checkStr(datashowSplit1)) return;
        try{
            j.setDividerLocation(Integer.parseInt(datashowSplit1));
        }catch(Exception e){
        }
    }

    public void setDatashowSplit1(String datashowSplit1) {
        this.datashowSplit1 = datashowSplit1;
        save();
    }

    public String getDatashowSplit2() {
        return datashowSplit2;
    }

    public void getDatashowSplit2(JSplitPane j){
        if (!checkStr(datashowSplit2)) return;
        try{
            j.setDividerLocation(Integer.parseInt(datashowSplit2));
        }catch(Exception e){
        }
    }
    public void setDatashowSplit2(String datashowSplit2) {
        this.datashowSplit2 = datashowSplit2;
        save();
    }

    public String getDatashowSplit3() {
        return datashowSplit3;
    }

    public void getDatashowSplit3(JSplitPane j){
        if (!checkStr(datashowSplit3)) return;
        try{
            j.setDividerLocation(Integer.parseInt(datashowSplit3));
        }catch(Exception e){
        }
    }

    public void setDatashowSplit3(String datashowSplit3) {
        this.datashowSplit3 = datashowSplit3;
        save();
    }

    public String getServiceSplit1() {
        return serviceSplit1;
    }

    public void getServiceSplit1(JSplitPane j){
        if (!checkStr(serviceSplit1)) return;
        try{
            j.setDividerLocation(Integer.parseInt(serviceSplit1));
        }catch(Exception e){
        }
    }

    public void setServiceSplit1(String serviceSplit1) {
        this.serviceSplit1 = serviceSplit1;
        save();
    }

    public String getServiceSplit2() {
        return serviceSplit2;
    }

    public void getServiceSplit2(JSplitPane j){
        if (!checkStr(serviceSplit2)) return;
        try{
            j.setDividerLocation(Integer.parseInt(serviceSplit2));
        }catch(Exception e){
        }
    }
    public void setServiceSplit2(String serviceSplit2) {
        this.serviceSplit2 = serviceSplit2;
        save();
    }

    public String getServiceSplit3() {
        return serviceSplit3;
    }

    public void getServiceSplit3(JSplitPane j){
        if (!checkStr(serviceSplit3)) return;
        try{
            j.setDividerLocation(Integer.parseInt(serviceSplit3));
        }catch(Exception e){
        }
    }
    public void setServiceSplit3(String serviceSplit3) {
        this.serviceSplit3 = serviceSplit3;
        save();
    }

    public String getSongSplit1() {
        return songSplit1;
    }

    public void getSongSplit1(JSplitPane j){
        if (!checkStr(songSplit1)) return;
        try{
            j.setDividerLocation(Integer.parseInt(songSplit1));
        }catch(Exception e){
        }
    }

    public void setSongSplit1(String songSplit1) {
        this.songSplit1 = songSplit1;
        save();
    }

    public String getSongSplit2() {
        return songSplit2;
    }

    public void getSongSplit2(JSplitPane j){
        if (!checkStr(songSplit2)) return;
        try{
            j.setDividerLocation(Integer.parseInt(songSplit2));
        }catch(Exception e){
        }
    }
    public void setSongSplit2(String songSplit2) {
        this.songSplit2 = songSplit2;
        save();
    }


    public String getTemplateSplit1() {
        return templateSplit1;
    }

    public void getTemplateSplit1(JSplitPane j){
        if (!checkStr(templateSplit1)) return;
        try{
            j.setDividerLocation(Integer.parseInt(templateSplit1));
        }catch(Exception e){
        }
    }

    public void setTemplateSplit1(String templateSplit1) {
        this.templateSplit1 = templateSplit1;
        save();
    }

    public String getTemplateSplit2() {
        return templateSplit2;
    }

    public void getTemplateSplit2(JSplitPane j){
        if (!checkStr(templateSplit2)) return;
        try{
            j.setDividerLocation(Integer.parseInt(templateSplit2));
        }catch(Exception e){
        }
    }
    public void setTemplateSplit2(String templateSplit2) {
        this.templateSplit2 = templateSplit2;
        save();
    }


}
