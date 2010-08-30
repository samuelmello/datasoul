/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.serviceitems.imagelist.ImageListServiceItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author samuel
 */
public class OpenofficeHelper {

    private File helper;
    private boolean abort;

    public OpenofficeHelper() throws IOException {

        abort = false;
        helper = File.createTempFile("DatasoulOpenofficeHelper", ".odt");
        helper.deleteOnExit();
        InputStream is = OpenofficeHelper.class.getResourceAsStream("OpenofficeHelper.odt");
        FileOutputStream fos = new FileOutputStream(helper);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();

    }

    public void dispose() throws IOException {
        helper.delete();
    }

    public String convertToODP(File f) throws IOException, InterruptedException {

        String ret = null;

        // Create a copy of the file
        File tmp = File.createTempFile("dsconv", f.getName());
        tmp.deleteOnExit();
        FileInputStream is = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(tmp);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();

        // Run openoffice to convert
        String[] cmd = { "soffice",
            helper.getAbsolutePath(),
            "macro://./Standard.Module1.ConvertToODP(\""+tmp.getAbsolutePath()+"\")"
        };
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        if (proc.waitFor() == 0){
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

        // Create a copy of the file
        File tmp = File.createTempFile("dsconv", f.getName());
        tmp.deleteOnExit();
        FileInputStream is = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(tmp);
        int x;
        while ((x=is.read()) != -1){
            fos.write((byte)x);
        }
        is.close();
        fos.close();

        // Run openoffice to convert
        String[] cmd = { "soffice",
            helper.getAbsolutePath(),
            "macro://./Standard.Module1.ConvertToImage(\""+tmp.getAbsolutePath()+"\")"
        };
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        if (proc.waitFor() == 0){
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
