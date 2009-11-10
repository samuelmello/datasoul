/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.config.BackgroundConfig;

/**
 *
 * @author samuel
 */
public class ImageListServiceItem extends ServiceItem {

    public ImageListServiceItem(){

        ImageListServiceRenderer r1 = new ImageListServiceRenderer();
        r1.setImage(BackgroundConfig.getInstance().getMainBackgroundImg());
        slides.add(r1);
        ImageListServiceRenderer r2 = new ImageListServiceRenderer();
        r2.setImage(BackgroundConfig.getInstance().getMonitorBackgroundImg());
        slides.add(r2);
    }


}
