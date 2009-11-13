/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.datashow;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuel
 */
public class ImageListServiceItem extends ServiceItem {

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


}
