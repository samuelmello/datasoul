/*
 * BackgroundConfig.java
 *
 * Created on 6 November 2006, 22:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

import datasoul.render.ContentManager;
import datasoul.util.SerializableObject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author samuelm
 */
public class BackgroundConfig extends SerializableObject {
    
    private static BackgroundConfig instance = null; 
    
    public static synchronized BackgroundConfig getInstance(){
        if (instance == null){
            instance = new BackgroundConfig();
        }
        return instance;
    }
    
    /** Creates a new instance of ConfigObj */
    private BackgroundConfig() {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
        + "config" + System.getProperty("file.separator") + "background.config";

        File configFile = new File(path);

        Document dom=null;
        Node node = null;
        try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse(configFile);

                //node = dom.getDocumentElement().getChildNodes().item(0);
                node = dom.getElementsByTagName("BackgroundConfig").item(0);

        }catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error parsing config file\n"+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
            return;
        }        

        try {
            this.readObject(node);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error parsing config file\nError: "+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
        }
    }
    
    public void save(){
        try{
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") 
            + "config" + System.getProperty("file.separator") + "background.config";

            Node node = this.writeObject();
            Document doc = node.getOwnerDocument();
            doc.appendChild( node);                        // Add Root to Document
            FileOutputStream fos = new FileOutputStream(path);
            org.apache.xml.serialize.XMLSerializer xs = new org.apache.xml.serialize.XMLSerializer();
            OutputFormat outFormat = new OutputFormat();
            outFormat.setIndenting(true);
            outFormat.setEncoding("ISO-8859-1");
            xs.setOutputFormat(outFormat);
            xs.setOutputByteStream(fos);
            xs.serialize(doc);

        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error writing file.\nError:"+e.getMessage(),"DataSoul Error",0);    
            e.printStackTrace();
        }
    }
    
    

    private BufferedImage mainBackgroundImg;
    private BufferedImage monitorBackgroundImg;
    

    public String getMainBackgroundImgStr(){
        return getImgStr(this.mainBackgroundImg);
    }    
    
    public String getMonitorBackgroundImgStr(){
        return getImgStr(this.monitorBackgroundImg);
    }    

    private String getImgStr(BufferedImage img){
        
        if (img == null)
            return "";
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( img, "png", baos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        byte[] ba = baos.toByteArray();

        int len=ba.length,j;
        StringBuffer sb=new StringBuffer(len*2);
        for (j=0;j<len;j++) {
            String sByte=Integer.toHexString((int)(ba[j] & 0xFF));
            sb.append(stringAlign2chars(sByte));
        }
        return sb.toString();
    }

    private String stringAlign2chars(String str){
        if (str.length()!=2)
            return '0'+str;
        else
            return str;
    }
    
    public void setMainBackgroundImgStr(String strImage){
        setImgStr( 0, strImage );
    }
    
    public void setMonitorBackgroundImgStr(String strImage){
        setImgStr( 1, strImage );
    }

    /**
     * @param idx possible values: 0 for Main, 1 for monitor
     */
    private void setImgStr(int idx, String strImage){

        if (strImage.equals("")){
            if (idx == 0){
                this.mainBackgroundImg = null;
            }else if (idx == 1){
                this.monitorBackgroundImg = null;
            }
            return;
        }
        
        String str="";
        int intAux=0;
        byte[] bytes = new byte[strImage.length()/2];
        for(int i=0; i< strImage.length()-1;i=i+2){
            str = strImage.substring(i,i+2);
            intAux = Integer.parseInt(str,16);
            bytes[i/2]=(byte)intAux;
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            if (idx == 0){
                setMainBackgroundImg( ImageIO.read(bais) );
            }else if (idx == 1){
                setMonitorBackgroundImg( ImageIO.read(bais) );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public BufferedImage getMainBackgroundImg(){
        return mainBackgroundImg;
    }
    
    public void setMainBackgroundImg(BufferedImage img){
        this.mainBackgroundImg = img;
        if ( ConfigObj.getInstance().getMainOutput() ){
            ContentManager.getMainDisplay().paintBackground(img);
        }
        save();
    }

    public BufferedImage getMonitorBackgroundImg(){
        return monitorBackgroundImg;
    }
    
    public void setMonitorBackgroundImg(BufferedImage img){
        this.monitorBackgroundImg = img;
        if ( ConfigObj.getInstance().getMonitorOutput() ){
            ContentManager.getMonitorDisplay().paintBackground(img);
        }
        save();
    }
    
    protected void registerProperties() {
        properties.add("MainBackgroundImgStr");
        properties.add("MonitorBackgroundImgStr");
    }
    
    
}
