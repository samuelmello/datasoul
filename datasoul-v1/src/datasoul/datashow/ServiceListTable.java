/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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

/*
 * ServiceListTable.java
 *
 * Created on 9 de Janeiro de 2006, 22:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package datasoul.datashow;

import datasoul.DatasoulMainForm;
import datasoul.servicelist.ContentlessServiceItem;
import datasoul.song.Song;
import datasoul.util.ListTable;
import datasoul.util.SerializableItf;
import datasoul.util.ShowDialog;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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

    public Object getServiceItem(int index){
        if (index >= 0 && index < objectList.size()){
            return objectList.get(index);
        }else{
            return null;
        }
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

        public static final int COLUMN_TIME = 0;
        public static final int COLUMN_DURATION = 1;
        public static final int COLUMN_TITLE = 2;
        public static final int COLUMN_NOTES = 3;
        public static final int COLUMN_TEMPLATE = 4;
        public static final int COLUMN_COUNT = 5;

        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case COLUMN_TIME:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Time");
                case COLUMN_DURATION:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Duration");
                case COLUMN_TITLE:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Title");
                case COLUMN_NOTES:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Notes");
                case COLUMN_TEMPLATE:
                    return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Template");
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
                    if ( ! (getServiceItem(rowIndex) instanceof ContentlessServiceItem)){
                        return true;
                    }else{
                        return false;
                    }
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
                            si.setDuration(aValue.toString());
                            updateStartTimes();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Invalid_value"));
                        }
                        break;
                    case COLUMN_NOTES:
                        si.setNotes(aValue.toString());
                        break;
                    case COLUMN_TEMPLATE:
                    si.setTemplate(aValue.toString());
                }
            }

        }

    public void fileNew(){

        File f = new File(fileName);
        String name;
        if (f.exists()){
            name = f.getName();
        }else{
            name = "";
        }
        int resp = JOptionPane.showConfirmDialog(null, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Do_you_want_to_save_")+name+ "?" );

        switch(resp){
            case JOptionPane.CANCEL_OPTION:
                return;
            case JOptionPane.YES_OPTION:
                saveServiceList();
                break;
        }

        cleanup();
        fileName = "";
        tableModelChanged();
    }
    
    private void saveFile() {
        try {

            ZipWriter zip = new ZipWriter(fileName);

            Node node = this.writeObject(zip);
            Document doc = node.getOwnerDocument();
            doc.appendChild(node);                        // Add Root to Document

            Source source = new DOMSource(doc);

            // Prepare the output file
            Result result = new StreamResult(zip.getOutputStream());

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);

            zip.close();

        } catch (Exception e) {
            ShowDialog.showWriteFileError(fileName, e);
        }
    }

    public void openServiceList() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName();
                if (name.endsWith(".servicelistz")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return ".servicelistz";
            }
        });
        File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
        fc.setCurrentDirectory(dir);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();

            Document dom = null;
            Node node = null;
            try {
                ZipReader zip = new ZipReader(fileName);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(zip.getMainInputStream());

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("ServiceListTable").item(0);

                this.readObject(node, zip);

                zip.close();
            } catch (Exception e) {
                ShowDialog.showReadFileError(fileName, e);
            }

            tableModelChanged();
        }
    }

    public void saveServiceListAs() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName();
                if (name.endsWith(".servicelistz")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return ".servicelistz";
            }
        });
        File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
        fc.setCurrentDirectory(dir);
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Select_the_file_to_save."));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();
            if (!fileName.contains(".servicelistz")) {
                fileName = fileName + ".servicelistz";
            }
            saveFile();
        }

    }

    public void saveServiceList() {
        
        if (fileName.equals("")) {
            saveServiceListAs();
            return;
        }

        if (!fileName.contains(".servicelistz")) {
            fileName = fileName + ".servicelistz";
        }


        saveFile();
    }
}
