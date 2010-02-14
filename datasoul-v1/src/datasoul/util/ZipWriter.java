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
    private int version;

    public ZipWriter(int version){
        // Dummy constructor for versions that doesn't support zip yet
        this.version = version;
    }

    public ZipWriter(String filename, int version) throws IOException {
        images = new LinkedList<ZipSerializerImage>();
        zos = new ZipOutputStream(new FileOutputStream(filename));
        ZipEntry mainEntry = new ZipEntry("maindata.xml");
        zos.putNextEntry(mainEntry);
        this.version = version;
    }

    public void appendImage(BufferedImage im, String name){
        if (version < 2){
            return;
        }
        ZipSerializerImage zsi = new ZipSerializerImage(im, name);
        images.add(zsi);
    }

    public OutputStream getOutputStream(){
        if (version < 2)
            return null;
        else
            return zos;
    }

    public int getVersion(){
        return version;
    }

    public void close() throws IOException {

        if (version < 2)
            return;

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
