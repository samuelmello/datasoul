/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
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
    private LinkedList<ZipSerializerFile> files;
    private ZipOutputStream zos;
    private int version;

    public ZipWriter(int version){
        // Dummy constructor for versions that doesn't support zip yet
        this.version = version;
    }

    public ZipWriter(String filename, int version) throws IOException {
        images = new LinkedList<ZipSerializerImage>();
        files = new LinkedList<ZipSerializerFile>();
        zos = new ZipOutputStream(new FileOutputStream(filename));
        ZipEntry mainEntry = new ZipEntry("maindata.xml");
        zos.putNextEntry(mainEntry);
        this.version = version;
    }

    public void startMetadata() throws IOException{
        ZipEntry metaEntry = new ZipEntry("metadata.xml");
        zos.putNextEntry(metaEntry);
    }

    public void appendImage(BufferedImage im, String name){
        if (version < 2){
            return;
        }
        ZipSerializerImage zsi = new ZipSerializerImage(im, name);
        images.add(zsi);
    }

    public void appendFile(String internalname, String filename){
        if (version < 2){
            return;
        }
        ZipSerializerFile zsf = new ZipSerializerFile(internalname, filename);
        files.add(zsf);
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

        for (ZipSerializerFile zsf : files){
            ZipEntry ze = new ZipEntry(zsf.internalname);
            zos.putNextEntry(ze);
            byte[] buf = new byte[1024];
            int i = 0;
            FileInputStream is = new FileInputStream(zsf.filename);
            while ((i = is.read(buf)) != -1) {
                zos.write(buf, 0, i);
            }
            is.close();
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

    private class ZipSerializerFile {
        String internalname;
        String filename;
        public ZipSerializerFile (String internalname, String filename){
            this.internalname = internalname;
            this.filename = filename;
        }
    }


}
