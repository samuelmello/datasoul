/*
 * ShowDialog.java
 *
 * Created on February 2, 2007, 10:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author samuelm
 */
public class ShowDialog {

    public static void showReadFileError(String filename, Exception e){
            JOptionPane.showMessageDialog(null,"Error reading file\nFile: "+filename+"\nError: "+e.getMessage(),"Datasoul Error",0);
    }
    
    public static void showReadFileError(File file, Exception e){
            showReadFileError(file.getName(), e);
    }

    public static void showWriteFileError(String filename, Exception e){
            JOptionPane.showMessageDialog(null,"Error writing file\nFile: "+filename+"\nError: "+e.getMessage(),"Datasoul Error",0);
    }
    
    public static void showWriteFileError(File file, Exception e){
            showWriteFileError(file.getName(), e);
    }



}
