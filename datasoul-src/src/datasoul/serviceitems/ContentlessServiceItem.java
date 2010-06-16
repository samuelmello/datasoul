/* 
 * Copyright 2005-2010 Samuel Mello & Eduardo Schnell
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

package datasoul.serviceitems;

import datasoul.config.DisplayControlConfig;
import datasoul.util.ObjectManager;
import datasoul.util.ZipWriter;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;

/**
 *
 * @author samuel
 */
public class ContentlessServiceItem extends ServiceItem {

    /**
     * Used when saving a ServiceItem in a format that does not support it,
     * for example, a ImageListServiceItem in Datasoul 1.x
     */
    public static Node writeNotSupportedObject(ServiceItem item, ZipWriter zip) throws Exception{

        ContentlessServiceItem dummy = new ContentlessServiceItem();
        item.assignTo(dummy);
        return dummy.writeObject(zip);

    }

    @Override
    public boolean isTemplateEditEnabled(){
        return false;
    }

    @Override
    public String getDefaultMonitorTemplate(){
        return DisplayControlConfig.getInstance().getMonitorTemplateContentless();
    }

    @Override
    public Color getBackgroundColor(){
        return Color.decode("0xddffff");
    }

    @Override
    public void edit(){
        String s = JOptionPane.showInputDialog(ObjectManager.getInstance().getDatasoulMainForm(), java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SERVICE ITEM TITLE:"), getTitle());
        if (s != null && !s.trim().equals("")){
            this.setTitle(s);
        }
        ObjectManager.getInstance().getDatasoulMainForm().repaint();        
    }

    private static final Icon icon = new ImageIcon(ContentlessServiceItem.class .getResource("/datasoul/icons/v2/stock_insert-note.png"));
    @Override
    public Icon getIcon(){
        return icon;
    }
}

