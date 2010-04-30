/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.serviceitems;

import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author samuel
 */
public class AttachmentServiceItem extends GenericAttachmentServiceItem {


    public AttachmentServiceItem(){
        super();
    }

    public AttachmentServiceItem(String filename, InputStream is) throws IOException{
        super(filename, is);
    }

    @Override
    public void showItem(){
        super.showItem();
        ContentManager.getInstance().setMainShowTemplate(false);
        if (ContentManager.getInstance().isOutputVisible()){
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ERROR LAUNCHING FILE:")+" "+filename+"\n"+ex.getLocalizedMessage());
            }
        }
    }
}

