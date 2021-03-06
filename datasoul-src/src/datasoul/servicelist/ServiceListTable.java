/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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

/*
 * ServiceListTable.java
 *
 * Created on 9 de Janeiro de 2006, 22:28
 *
 */
package datasoul.servicelist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import datasoul.DatasoulMainForm;
import datasoul.config.ConfigObj;
import datasoul.serviceitems.AttachmentServiceItem;
import datasoul.serviceitems.ContentlessServiceItem;
import datasoul.serviceitems.ServiceItem;
import datasoul.serviceitems.VideoServiceItem;
import datasoul.serviceitems.imagelist.ImageListServiceItem;
import datasoul.serviceitems.song.Song;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.util.ListTable;
import datasoul.util.ObjectManager;
import datasoul.util.SerializableItf;
import datasoul.util.ShowDialog;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;

/**
 *
 * @author Administrador
 */
public class ServiceListTable extends ListTable {

    static ServiceListTable instance;
    private int startHour;
    private int startMinute;
    private String title;
    private String notes;
    private String fileName = "";

    public static ServiceListTable getActiveInstance() {
        if (instance == null) {
            instance = new ServiceListTable();
        }
        return instance;
    }

    public ServiceItem getServiceItem(int index){
        if (index >= 0 && index < objectList.size()){
            Object o = objectList.get(index);
            if (o instanceof ServiceItem)
                return (ServiceItem) o;
            else
                return null;
        }else{
            return null;
        }
    }

    public String getJSon(){
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("var service = {");
        sb.append("\n");
        
        sb.append("notes : '");
        sb.append(ServiceItem.escapeJson(this.getNotes()));
        sb.append("', ");
        sb.append("\n");
        
        sb.append("usetime : ");
        sb.append(ConfigObj.getActiveInstance().getTrackDurationBool());
        sb.append(", ");
        sb.append("\n");

        sb.append("title : '");
        sb.append(ServiceItem.escapeJson(this.getTitle()));
        sb.append("', ");
        sb.append("\n");

        sb.append("items : [");
        sb.append("\n");
        boolean first = true;
        for( Object o : objectList){
            if (o instanceof ServiceItem){
                
                if (first) {
                    first = false;
                }else{
                sb.append(",");
                }
                
                ServiceItem si = (ServiceItem) o;
                sb.append("{");
                sb.append("\n");
                sb.append(si.getJSon());
                sb.append("\n");
                sb.append("}");
                sb.append("\n");
            }
        }
        sb.append("\n");
        sb.append("]"); //items
        
        sb.append("\n");
        sb.append("};"); // service
        
        return sb.toString();
    }
    
    @Override
    public Node writeObject(ZipWriter zip) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.newDocument();

        Node nodeOut = doc.createElement("ServiceListTable");
        Node nTitle = doc.createElement("title");
        nTitle.setTextContent(title);
        nodeOut.appendChild(doc.importNode(nTitle, true));
        Node nNotes = doc.createElement("notes");
        nNotes.setTextContent(notes);
        nodeOut.appendChild(doc.importNode(nNotes, true));
        Node nHour = doc.createElement("hour");
        nHour.setTextContent(Integer.toString(startHour));
        nodeOut.appendChild(doc.importNode(nHour, true));
        Node nMinute = doc.createElement("minute");
        nMinute.setTextContent(Integer.toString(startMinute));
        nodeOut.appendChild(doc.importNode(nMinute, true));
        Node nVersion = doc.createElement("DatasoulFileVersion");
        nVersion.setTextContent(DatasoulMainForm.getFileFormatVersion());
        nodeOut.appendChild(doc.importNode(nVersion, true));

        Node node;
        for (int i = 0; i < objectList.size(); i++) {
            node = ((SerializableItf) objectList.get(i)).writeObject(zip);
            nodeOut.appendChild(doc.importNode(node, true));
        }

        return nodeOut.cloneNode(true);
    }

    protected void cleanup(){
        // clean up
        for (Object o : objectList){
            if (o instanceof ServiceItem){
                ((ServiceItem)o).dispose();
            }
        }
        this.objectList.clear();
        startHour = 8;
        startMinute = 0;
        title = "";
        notes = "";
    }

    @Override
    public void readObject(Node nodeIn, ZipReader zip) {
        cleanup();
        
        NodeList nodeList = nodeIn.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if (nodeList.item(i).getNodeName().equals("Song")) {
                    Song song = new Song();
                    song.readObject(nodeList.item(i), zip);
                    this.addItem(song);
                } else if (nodeList.item(i).getNodeName().equals("TextServiceItem")) {
                    TextServiceItem tsi = new TextServiceItem();
                    tsi.readObject(nodeList.item(i), zip);
                    this.addItem(tsi);
                } else if (nodeList.item(i).getNodeName().equals("ContentlessServiceItem")) {
                    ContentlessServiceItem csi = new ContentlessServiceItem();
                    csi.readObject(nodeList.item(i), zip);
                    this.addItem(csi);
                } else if (nodeList.item(i).getNodeName().equals("AttachmentServiceItem")) {
                    AttachmentServiceItem asi = new AttachmentServiceItem();
                    asi.readObject(nodeList.item(i), zip);
                    this.addItem(asi);
                } else if (nodeList.item(i).getNodeName().equals("VideoServiceItem")) {
                    VideoServiceItem vsi = new VideoServiceItem();
                    vsi.readObject(nodeList.item(i), zip);
                    this.addItem(vsi);
                } else if (nodeList.item(i).getNodeName().equals("ImageListServiceItem")) {
                    ImageListServiceItem ilsi = new ImageListServiceItem();
                    ilsi.readObject(nodeList.item(i), zip);
                    this.addItem(ilsi);
                } else if (nodeList.item(i).getNodeName().equals("title")) {
                    this.title = nodeList.item(i).getTextContent();
                } else if (nodeList.item(i).getNodeName().equals("notes")) {
                    this.notes = nodeList.item(i).getTextContent();
                } else if (nodeList.item(i).getNodeName().equals("hour")) {
                    this.startHour = Integer.parseInt(nodeList.item(i).getTextContent());
                } else if (nodeList.item(i).getNodeName().equals("minute")) {
                    this.startMinute = Integer.parseInt(nodeList.item(i).getTextContent());
                }
            }
        }
        
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int x) {
        startHour = x;
        updateStartTimes();
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int x) {
        startMinute = x;
        updateStartTimes();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {
        title = s;
    }
    
    public String getNotes(){
        return notes;
    }
    
    public void setNotes(String s){
        notes = s;
    }

    public void updateStartTimes() {

        int offset = startHour * 60 + startMinute;
        for (Object o : objectList) {
            if (o instanceof ServiceItem) {
                ServiceItem si = (ServiceItem) o;
                si.setStartHour(offset / 60);
                si.setStartMinute(offset % 60);
                offset += si.getDuration();
            }
        }

        tableModelChanged();

    }

    public void addItem(Object item) {
        super.addItem(item);
        updateStartTimes();
    }

    public void removeItem(int index) {
        super.removeItem(index);
        updateStartTimes();
    }

    public void sortByName() {
        super.sortByName();
        updateStartTimes();
    }

    public void upItem(int row) {
        super.upItem(row);
        updateStartTimes();
    }

    public void downItem(int row) {
        super.downItem(row);
        updateStartTimes();
    }

        public static final int COLUMN_TITLE = 0;
        public static final int COLUMN_NOTES = 1;
        public static final int COLUMN_TEMPLATE = 2;
        public static final int COLUMN_TIME = 3;
        public static final int COLUMN_DURATION = 4;
        private static final int COLUMN_COUNT = 5;

        public int getColumnCount() {
            /* If time tracking is not active, hide the 2 first columns */
            if (ConfigObj.getActiveInstance().getTrackDurationBool()){
                return COLUMN_COUNT;
            }else{
                return COLUMN_COUNT-2;
            }
        }

        public String getColumnName(int columnIndex) {

            switch (columnIndex) {
                case COLUMN_TIME:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TIME");
                case COLUMN_DURATION:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DURATION");
                case COLUMN_TITLE:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TITLE");
                case COLUMN_NOTES:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NOTES");
                case COLUMN_TEMPLATE:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("TEMPLATE");
            }
            return null;
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case COLUMN_DURATION:
                case COLUMN_NOTES:
                    return true;
                case COLUMN_TEMPLATE:
                    return getServiceItem(rowIndex).isTemplateEditEnabled(); 
                default:
                    return false;
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Object object = ServiceListTable.this.objectList.get(rowIndex);
            if (object instanceof ServiceItem) {
                ServiceItem si = (ServiceItem) object;
                switch (columnIndex) {
                    case COLUMN_TIME:
                        return si.getStartTime();
                    case COLUMN_DURATION:
                        return si.getDuration();
                    case COLUMN_TITLE:
                        return si.getTitle();
                    case COLUMN_NOTES:
                        return si.getNotes();
                    case COLUMN_TEMPLATE:
                        return si.getTemplate();
                }
                return si;
            } else {
                return null;
            }
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            Object object = ServiceListTable.this.objectList.get(rowIndex);
            if (object instanceof ServiceItem) {
                ServiceItem si = (ServiceItem) object;
                switch (columnIndex) {
                    case COLUMN_DURATION:
                        try {
                            int i = Integer.parseInt(aValue.toString());
                            si.setDuration(Integer.toString(i));
                            updateStartTimes();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("INVALID VALUE"));
                        }
                        break;
                    case COLUMN_NOTES:
                        si.setNotes(aValue.toString());
                        break;
                    case COLUMN_TEMPLATE:
                        si.setTemplate(aValue.toString());
                        ObjectManager.getInstance().getDatasoulMainForm().previewItem();
                        break;
                }
            }

        }

    public boolean askForSaveOrCancel(){
        File f = new File(fileName);
        String name;
        if (f.exists()){
            name = f.getName();
        }else{
            name = "";
        }

        // For empty files, never save
        if (name.equals("") && getRowCount() == 0)
            return true;



        int resp = JOptionPane.showConfirmDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DO YOU WANT TO SAVE ")+name+ "?" );

        if (resp == JOptionPane.CANCEL_OPTION){
            return false;
        }else if (resp == JOptionPane.YES_OPTION){
            saveServiceList();
        }
        return true;
    }

    public void fileNew(){

        if (askForSaveOrCancel() == false)
            return;

        cleanup();
        fileName = "";
        tableModelChanged();
    }
    
    private void saveFile() {
        try {

            OutputStream os = null;
            ZipWriter zip = null;

            if (fileName.endsWith(".servicelist")){
                os = new FileOutputStream(fileName);
                zip = new ZipWriter(1);
            }else{
                zip = new ZipWriter(fileName, DatasoulMainForm.FILE_FORMAT_VERSION);
                os = zip.getOutputStream();
            }

            Node node = this.writeObject(zip);
            Document doc = node.getOwnerDocument();
            doc.appendChild(node);                        // Add Root to Document

            Source source = new DOMSource(doc);

            // Prepare the output file
            Result result = new StreamResult(os);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

            if (fileName.endsWith(".servicelist")){
                os.close();
            }else{
                zip.close();
            }

        } catch (Exception e) {
            ShowDialog.showWriteFileError(fileName, e);
        }
    }

    public File openServiceList() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter( new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL SERVICE LISTS"), "servicelistz", "servicelist") );
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    public void openFile(String filename) {

        try{
            InputStream is = null;
            ZipReader zip = null;

            if (filename.endsWith(".servicelist")){
                is = new FileInputStream(filename);
                zip = new ZipReader(null, 1);
            }else{
                zip = new ZipReader(filename, DatasoulMainForm.FILE_FORMAT_VERSION);
                is = zip.getMainInputStream();
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            Document dom = db.parse(is);

            //node = dom.getDocumentElement().getChildNodes().item(0);
            Node node = dom.getElementsByTagName("ServiceListTable").item(0);

            this.readObject(node, zip);

            if (filename.endsWith(".servicelist")){
                is.close();
            }else{
                zip.close();
            }
        }catch (Exception e){
            ShowDialog.showReadFileError(filename, e);
        }

        this.fileName = filename;

        tableModelChanged();

    }

    public void saveServiceListAs() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter( new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL SERVICE LISTS"), "servicelistz") );
        fc.addChoosableFileFilter( new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL 1.X SERVICE LISTS"), "servicelist") );
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SELECT THE FILE TO SAVE."));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();
            FileNameExtensionFilter currentfilter = (FileNameExtensionFilter) fc.getFileFilter();

            if (currentfilter.getExtensions()[0].equals("servicelist")){

                /* Ask the user if he really wants to use the older format */
                int resp = JOptionPane.showConfirmDialog(fc, 
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("YOU HAVE CHOSEN TO SAVE THE FILE IN THE FORMAT SUPPORTED BY OLDER VERSIONS OF DATASOUL.") + "\n" +
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("THE SERVICE LIST MAY CONTAIN ITEMS NOT SUPPORTED BY OLDER VERSIONS AND THESE ITEMS WILL NOT BE SAVED.")+ "\n" +
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DO YOU WANT TO CONTINUE?"),
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("DATASOUL: SAVE AS AN OLDER VERSION"), JOptionPane.OK_CANCEL_OPTION);
                if (resp == JOptionPane.CANCEL_OPTION){
                    /* If user wants to change the format, give him back the file selection dialog */
                    saveServiceListAs();
                    return;
                }

                if (!fileName.endsWith(".servicelist")){
                    fileName += ".servicelist";
                }
            }else{
                // Default, newer version
                if (!fileName.endsWith(".servicelistz")){
                    fileName += ".servicelistz";
                }
            }

            saveFile();

        }

    }

    public void saveServiceList() {
        
        if (fileName.equals("")) {
            saveServiceListAs();
            return;
        }

        saveFile();
    }
}


