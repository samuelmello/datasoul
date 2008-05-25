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

import datasoul.servicelist.ContentlessServiceItem;
import datasoul.servicelist.ExtServiceListPanel;
import datasoul.util.*;
import datasoul.song.*;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrador
 */
public class ServiceListTable extends ListTable {

    static ServiceListTable instance;
    private ExtendedServiceListTable instanceExt;
    private int startHour;
    private int startMinute;
    private String title;
    private String notes;
    private String fileName = "";
    private ExtServiceListPanel extPanel;

    /** Creates a new instance of ServiceListTable */
    public ServiceListTable() {
        instanceExt = new ExtendedServiceListTable();
    }

    public static ServiceListTable getActiveInstance() {
        if (instance == null) {
            instance = new ServiceListTable();
        }
        return instance;
    }

    public static void registerExtendedInstance(ExtServiceListPanel extPanel) {
        ServiceListTable.getActiveInstance().setExtPanel(extPanel);
        extPanel.getTableServiceList().setModel(ServiceListTable.getActiveInstance().getExtendedModel());
    }

    public void setExtPanel(ExtServiceListPanel panel) {
        this.extPanel = panel;
    }

    public ExtendedServiceListTable getExtendedModel() {
        return instanceExt;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if ( ! (getServiceItem(rowIndex) instanceof ContentlessServiceItem)){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Service_Item");
        } else if (columnIndex == 1) {
            return java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Template");
        } else {
            return "Time";
        }
    }

    public Object getServiceItem(int index){
        if (index >= 0 && index < objectList.size()){
            return objectList.get(index);
        }else{
            return null;
        }
    }
    
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            Object object = objectList.get(rowIndex);
            return object;
        } else if (columnIndex == 1) {
            Object object = objectList.get(rowIndex);
            return ((ServiceItem) object).getTemplate();
        } else {
            Object object = objectList.get(rowIndex);
            return ((ServiceItem) object).getDuration();
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            objectList.set(rowIndex, (String) aValue.toString());
            tableModelChanged();
        } else if (columnIndex == 1) {
            ((ServiceItem) objectList.get(rowIndex)).setTemplate((String) aValue.toString());
            tableModelChanged();
        } 
    }

    public int getColumnCount() {
        return 3;
    }

    public Node writeObject() throws Exception {

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

        Node node;
        for (int i = 0; i < objectList.size(); i++) {
            node = ((SerializableItf) objectList.get(i)).writeObject();
            nodeOut.appendChild(doc.importNode(node, true));
        }

        return nodeOut.cloneNode(true);
    }

    protected void cleanup(){
        // clean up
        this.objectList.clear();
        startHour = 0;
        startMinute = 0;
        title = "";
        notes = "";
    }
    
    public void readObject(Node nodeIn) {
        cleanup();
        
        NodeList nodeList = nodeIn.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if (nodeList.item(i).getNodeName().equals("Song")) {
                    Song song = new Song();
                    song.readObject(nodeList.item(i));
                    this.addItem(song);
                } else if (nodeList.item(i).getNodeName().equals("TextServiceItem")) {
                    TextServiceItem tsi = new TextServiceItem();
                    tsi.readObject(nodeList.item(i));
                    this.addItem(tsi);
                } else if (nodeList.item(i).getNodeName().equals("ContentlessServiceItem")) {
                    ContentlessServiceItem csi = new ContentlessServiceItem();
                    csi.readObject(nodeList.item(i));
                    this.addItem(csi);
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
        updateExtPanel();
        
    }

    public void updateExtPanel(){
        if (extPanel != null) {
            extPanel.setTitle(title);
            extPanel.setStartHourMinute(Integer.toString(startHour), Integer.toString(startMinute));
            extPanel.setNotes(notes);
        }
        updateStartTimes();
        
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

    public class ExtendedServiceListTable extends ListTable {

        public static final int COLUMN_TIME = 0;
        public static final int COLUMN_DURATION = 1;
        public static final int COLUMN_TITLE = 2;
        public static final int COLUMN_NOTES = 3;
        public static final int COLUMN_TEMPLATE = 4;
        public static final int COLUMN_COUNT = 5;

        public int getRowCount() {
            return ServiceListTable.this.getRowCount();
        }

        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case COLUMN_TIME:
                    return "Time";
                case COLUMN_DURATION:
                    return "Duration";
                case COLUMN_TITLE:
                    return "Title";
                case COLUMN_NOTES:
                    return "Notes";
                case COLUMN_TEMPLATE:
                    return "Template";
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
                            JOptionPane.showMessageDialog(null, "Invalid Value");
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

        public void addTableModelListener(TableModelListener l) {
            ServiceListTable.this.addTableModelListener(l);
        }

        public void removeTableModelListener(TableModelListener l) {
            ServiceListTable.this.removeTableModelListener(l);
        }

        public void addItem(Object item) {
            ServiceListTable.this.addItem(item);
        }

        public void removeItem(int index) {
            ServiceListTable.this.removeItem(index);
        }

        public void sortByName() {
            ServiceListTable.this.sortByName();
        }

        public void upItem(int row) {
            ServiceListTable.this.upItem(row);
        }

        public void downItem(int row) {
            ServiceListTable.this.downItem(row);
        }
        
        public Object getServiceItem(int row){
            return ServiceListTable.this.getServiceItem(row);
        }
        
    }

    public void fileNew(){
        cleanup();
        fileName = "";
        tableModelChanged();
        updateExtPanel();
    }
    
    private void saveFile() {
        try {
            Node node = this.writeObject();
            Document doc = node.getOwnerDocument();
            doc.appendChild(node);                        // Add Root to Document
            FileOutputStream fos = new FileOutputStream(fileName);
            org.apache.xml.serialize.XMLSerializer xs = new org.apache.xml.serialize.XMLSerializer();
            OutputFormat outFormat = new OutputFormat();
            outFormat.setIndenting(true);
            outFormat.setEncoding("ISO-8859-1");
            xs.setOutputFormat(outFormat);
            xs.setOutputByteStream(fos);
            xs.serialize(doc);

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
                if (name.endsWith(".servicelist")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return ".servicelist";
            }
        });
        File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
        fc.setCurrentDirectory(dir);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();

            File file = new File(fileName);

            Document dom = null;
            Node node = null;
            ServiceListTable slt;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(file);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("ServiceListTable").item(0);

            } catch (Exception e) {
                ShowDialog.showReadFileError(file, e);
            }

            try {
                this.readObject(node);
            } catch (Exception e) {
                ShowDialog.showReadFileError(file, e);
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
                if (name.endsWith(".servicelist")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return ".servicelist";
            }
        });
        File dir = new File(System.getProperty("datasoul.stgloc") + System.getProperty("file.separator") + "servicelists");
        fc.setCurrentDirectory(dir);
        fc.setDialogTitle(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Select_the_file_to_save."));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getPath();
            if (!fileName.contains(".servicelist")) {
                fileName = fileName + ".servicelist";
            }
            saveFile();
        }

    }

    public void saveServiceList() {
        
        if (fileName.equals("")) {
            saveServiceListAs();
            return;
        }

        if (!fileName.contains(".servicelist")) {
            fileName = fileName + ".servicelist";
        }


        saveFile();
    }
}
