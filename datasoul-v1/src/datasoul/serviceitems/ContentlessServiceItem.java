/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.serviceitems;

import datasoul.config.DisplayControlConfig;
import datasoul.datashow.ServiceItem;
import datasoul.util.ObjectManager;
import datasoul.util.ZipWriter;
import java.awt.Color;
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
        String s = JOptionPane.showInputDialog(ObjectManager.getInstance().getDatasoulMainForm(), "Service Item Title:", getTitle());
        if (s != null && !s.trim().equals("")){
            this.setTitle(s);
        }
        ObjectManager.getInstance().getDatasoulMainForm().repaint();        
    }
}
