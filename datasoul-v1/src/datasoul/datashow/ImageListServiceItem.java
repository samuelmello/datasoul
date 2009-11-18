/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.util.ImageSerializer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author samuel
 */
public class ImageListServiceItem extends ServiceItem {

    public void addImage(File f) throws IOException{
        BufferedImage img = ImageIO.read(f);
        addImage(img);
    }

    public void addImage(BufferedImage img){
        ImageListServiceRenderer r1 = new ImageListServiceRenderer();
        r1.setImage(img);
        slides.add(r1);
        this.fireTableChanged();
    }

    public void delImage(int index){
        slides.remove(index);
        this.fireTableChanged();
    }

    public ImageListServiceItem getEditableCopy(){
        ImageListServiceItem ret = new ImageListServiceItem();
        for (ServiceItemRenderer r : slides){
            ret.slides.add(r);
        }
        return ret;
    }

    public void assignImages(ImageListServiceItem item){
        slides.clear();
        slides.addAll(item.slides);
        this.fireTableChanged();
    }

    @Override
    public Node writeObject() throws Exception {
        Node n = super.writeObject();
        Document doc = n.getOwnerDocument();
        Node nodeList = doc.createElement("ImageList");

        for (ServiceItemRenderer r : slides){
            ImageListServiceRenderer imgrend = (ImageListServiceRenderer) r;
            BufferedImage img = imgrend.getImage();
            Node nodeItem = doc.createElement("ImageItem");
            nodeItem.setTextContent(ImageSerializer.imageToStr(img));
            nodeList.appendChild(doc.importNode(nodeItem, true));
        }

        n.appendChild(doc.importNode(nodeList, true));

        return n;

    }

    @Override
    public void readObject(Node nodeIn)  {
        super.readObject(nodeIn);


        NodeList nodeList= nodeIn.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE &&
                nodeList.item(i).getNodeName().equals("ImageList")){

                NodeList nodeImgList= nodeList.item(i).getChildNodes();
                for(int j=0;j<nodeImgList.getLength();j++){
                    if (nodeImgList.item(j).getNodeType() == Node.ELEMENT_NODE &&
                         nodeImgList.item(j).getNodeName().equals("ImageItem")){
                        try{
                            ImageListServiceRenderer r = new ImageListServiceRenderer();
                            String s = nodeImgList.item(j).getTextContent();
                            BufferedImage img = ImageSerializer.strToImage(s);
                            r.setImage(img);
                            slides.add(r);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void setImageList(String s){
        // ignore, will read it afterwards

    }

}
