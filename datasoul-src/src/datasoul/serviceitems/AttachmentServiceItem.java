/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.serviceitems;

import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author samuel
 */
public class AttachmentServiceItem extends GenericAttachmentServiceItem {


    public AttachmentServiceItem(){
        super();
    }

    public AttachmentServiceItem(File f, boolean isLink) throws IOException{
        super(f, isLink);
    }

    @Override
    public void showItem(){
        super.showItem();
        ContentManager.getInstance().setMainShowTemplate(false);
        if (ObjectManager.getInstance().isOutputVisible()){
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ObjectManager.getInstance().getDatasoulMainForm(),
                        java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("ERROR LAUNCHING FILE:")+" "+filename+"\n"+ex.getLocalizedMessage());
            }
        }
    }
}

