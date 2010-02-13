/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author samuel
 */
public class ZipWriter {

    private LinkedList<ZipSerializerImage> images;
    private ZipOutputStream zos;

    public ZipWriter(String filename) throws IOException {
        images = new LinkedList<ZipSerializerImage>();
        zos = new ZipOutputStream(new FileOutputStream(filename));
        ZipEntry mainEntry = new ZipEntry("maindata.xml");
        zos.putNextEntry(mainEntry);
    }

    public void appendImage(BufferedImage im, String name){
        ZipSerializerImage zsi = new ZipSerializerImage(im, name);
        images.add(zsi);
    }

    public OutputStream getOutputStream(){
        return zos;
    }

    public void close() throws IOException {

        for (ZipSerializerImage zsi : images){
            ZipEntry ze = new ZipEntry(zsi.name);
            zos.putNextEntry(ze);
            ImageIO.write( zsi.im , "png", zos);
        }

        zos.close();

    }

    private class ZipSerializerImage {
        BufferedImage im;
        String name;
        public ZipSerializerImage (BufferedImage im, String name){
            this.im = im;
            this.name = name;
        }
    }


}
