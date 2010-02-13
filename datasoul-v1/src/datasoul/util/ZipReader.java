/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author samuel
 */
public class ZipReader {

    private ZipFile zf;

    public ZipReader(String filename) throws IOException{
        zf = new ZipFile(filename);
    }

    public InputStream getInputStream(String name) throws IOException{
        ZipEntry ze = zf.getEntry(name);

        if (ze == null){
            throw new IOException("Unable to find entry "+name+" in archive "+zf.getName());
        }

        return zf.getInputStream(ze);
    }

    public InputStream getMainInputStream() throws IOException{
        return getInputStream("maindata.xml");
    }

    public void close() throws IOException {
        zf.close();
    }

}
