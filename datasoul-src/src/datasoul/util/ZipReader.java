/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
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
            throw new IOException(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("UNABLE TO FIND ENTRY ")+name+java.util.ResourceBundle.getBundle("datasoul/internationalize").getString(" IN ARCHIVE ")+zf.getName());
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

