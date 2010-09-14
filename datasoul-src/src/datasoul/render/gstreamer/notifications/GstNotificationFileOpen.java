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

package datasoul.render.gstreamer.notifications;

import java.io.File;

import javax.swing.SwingUtilities;

import datasoul.util.ObjectManager;

/**
 *
 * @author samuel
 */
public class GstNotificationFileOpen extends GstNotification {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7143882768264011882L;
	private String filename;

    public GstNotificationFileOpen(String f){
        this.filename = f;
    }

    @Override
    public void run(){

        // If user provided an initial file, open it
        if (filename != null && (filename.endsWith(".servicelistz") || filename.endsWith(".servicelist"))){
            final File f = new File(filename);
            if (f.exists()){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        ObjectManager.getInstance().getDatasoulMainForm().openServiceList(f.getAbsolutePath());
                    }
                });
            }
        }
    }

    @Override
    public boolean isUnconnectedAllowed(){
        return true;
    }

}
