/*
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 */

package datasoul.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.jna.Platform;

import datasoul.config.ConfigObj;
import datasoul.serviceitems.imagelist.ImageListServiceItem;

/**
 *
 * @author samuel
 */
public class OpenofficeHelper {

    private File helper;
    private boolean abort;
    private File tmp;

    public static final String SOFFICE_DEFAULT_LINUX = "/usr/bin/soffice";
    public static final String SOFFICE_DEFAULT_MAC = "/Applications/OpenOffice.org.app/Contents/MacOS/soffice";
    public static final String SOFFICE_DEFAULT_WINDOWS = "c:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe";

    public OpenofficeHelper() throws IOException {

        tmp = null;
        abort = false;
        helper = File.createTempFile("DatasoulOpenofficeHelper", ".odt");
        helper.deleteOnExit();
        InputStream is = OpenofficeHelper.class.getResourceAsStream("OpenofficeHelper.odt");
        FileOutputStream fos = new FileOutputStream(helper);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1){
            fos.write(buffer, 0, bytesRead);
        }
        is.close();
        fos.close();

    }

    public void dispose() throws IOException {
        helper.delete();
    }

    private String getSofficePath(){

        /* Check value configured by user */
        String config = ConfigObj.getActiveInstance().getSofficePath();
        File f1 = new File(config);
        if (f1.exists())
            return config;

        /* If not found, try default values */
        if (Platform.isLinux()){
            File f2 = new File(SOFFICE_DEFAULT_LINUX);
            if (f2.exists())
                return SOFFICE_DEFAULT_LINUX;
        }

        if (Platform.isMac()){
            File f2 = new File(SOFFICE_DEFAULT_MAC);
            if (f2.exists())
                return SOFFICE_DEFAULT_MAC;
        }

        if (Platform.isWindows()){
            File f2 = new File(SOFFICE_DEFAULT_WINDOWS);
            if (f2.exists())
                return SOFFICE_DEFAULT_WINDOWS;
        }

        /* If there is no OpenOffice at default locations, return value configured anyway */
        return config;
    }

    private int init(File f, String macro) throws IOException, InterruptedException{

        // Create a copy of the file
        tmp = File.createTempFile("dsconv", f.getName().substring(f.getName().lastIndexOf(".")));
        tmp.deleteOnExit();
        FileInputStream is = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(tmp);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1){
            fos.write(buffer, 0, bytesRead);
        }
        is.close();
        fos.close();

        // Run openoffice to convert
        String[] cmd = { getSofficePath(),
            "-headless",
            helper.getAbsolutePath(),
            "macro://./Standard.Module1."+macro+"(\""+tmp.getAbsolutePath()+"\")"
        };
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process proc = pb.start();


        return proc.waitFor();

    }

    public String convertToODP(File f) throws IOException, InterruptedException {

        String ret = null;

        if (init(f, "ConvertToODP") == 0){
            File done = new File(tmp.getAbsolutePath()+".done");
            done.deleteOnExit();
            while (abort == false && !done.exists()){
                Thread.sleep(1000);
            }
            done.delete();
            ret = tmp.getAbsolutePath() + ".odp";
        }
        
        tmp.delete();

        return ret;

    }

    public void convertImages(File f, ImageListServiceItem item) throws IOException, InterruptedException{

        if (init(f, "ConvertToImage") == 0){
            int i = 0;
            File done = new File(tmp.getAbsolutePath()+".done");
            done.deleteOnExit();
            File next = new File(tmp.getAbsolutePath()+"."+i+".jpg");
            while (abort == false && (!done.exists() || next.exists())){
                if (next.exists()){
                    item.addImage(next);
                    next.delete();
                    i++;
                    next = new File(tmp.getAbsolutePath()+"."+i+".jpg");
                }else{
                    Thread.sleep(1000);
                }
            }

            done.delete();
        }
        tmp.delete();
    }

    public void abort(){
        abort = true;
    }

}
