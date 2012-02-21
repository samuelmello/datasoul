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

package datasoul.serviceitems;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import datasoul.render.ContentManager;
import datasoul.util.ObjectManager;

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

