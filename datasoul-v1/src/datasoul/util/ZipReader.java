/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.DatasoulMainForm;
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
    private int version;

    public ZipReader(String filename, int version) throws IOException{

        if (filename != null)
            zf = new ZipFile(filename);

        this.version = version;
    }

    public InputStream getInputStream(String name) throws IOException{

        if (version < 2)
            return null;

        ZipEntry ze = zf.getEntry(name);

        if (ze == null){
            throw new IOException("Unable to find entry "+name+" in archive "+zf.getName());
        }

        return zf.getInputStream(ze);
    }

    public InputStream getMainInputStream() throws IOException{
        return getInputStream("maindata.xml");
    }

    public InputStream getMetadataInputStream() throws IOException{
        return getInputStream("metadata.xml");
    }

    public void close() throws IOException {
        if (zf != null)
            zf.close();
    }

    public int getVersion(){
        return version;
    }


}

