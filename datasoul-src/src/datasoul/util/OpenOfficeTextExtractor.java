/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.serviceitems.text.TextServiceItem;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author samuel
 */
public class OpenOfficeTextExtractor {

    public void findText(Node n, StringBuffer sb){
        if (n.getNodeName().equals("text:span")){
            sb.append(n.getTextContent());
            sb.append("\n");

        }else{
            NodeList childs = n.getChildNodes();
            for (int i=0; i<childs.getLength(); i++){
                if (childs.item(i).getNodeType() == Node.ELEMENT_NODE)
                findText(childs.item(i), sb);
            }
        }
    }

    public void convertPresentationToText (File f, StringBuffer sb) throws IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex);
        }

        ZipFile zip;
        Document dom;

        // Newer format, zip
        zip = new ZipFile(f);
        try{
            ZipEntry ze = zip.getEntry("content.xml");

            if (ze == null){
                zip.close();
                throw new IOException("Invalid OpenOffice Presentation file");
            }

            dom = db.parse(zip.getInputStream(ze));

            NodeList pagelist = dom.getElementsByTagName("draw:page");

            for (int i=0; i<pagelist.getLength(); i++){
                findText(pagelist.item(i), sb);
                System.out.println(sb.toString().trim());

                if (i+1 < pagelist.getLength()){
                    System.out.println(TextServiceItem.CHORUS_MARK);
                }
            }
        }catch(SAXException e){
            throw new IOException(e);
        }finally{
            // Close the input stream
            zip.close();
        }
    }

}
