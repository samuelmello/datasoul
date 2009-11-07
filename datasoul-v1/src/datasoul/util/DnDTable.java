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
 * DnDTable.java
 *
 * Created on 9 de Janeiro de 2006, 23:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.song.Song;
import datasoul.song.SongListTable;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;
import java.util.TooManyListenersException;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.Container;
import javax.swing.JViewport;

/**
 *
 * @author Administrador
 */
public class DnDTable extends JTable implements java.awt.dnd.DropTargetListener, java.awt.dnd.DragSourceListener, java.awt.dnd.DragGestureListener{
    
    private boolean droppable=true;
    
    private boolean draggable=true;
    
    private boolean deleteAfterDragAndDrop=false;

    public boolean getDeleteAfterDragAndDrop(){
        return this.deleteAfterDragAndDrop;
    }
    
    public void setDeleteAfterDragAndDrop(boolean bool){
        this.deleteAfterDragAndDrop = bool;
    }
    
    public boolean getDroppable(){
        return this.droppable;
    }
    
    public void setDroppable(boolean bool){
        this.droppable = bool;
    }
    
    public void setDraggable(boolean b){
        this.draggable = b;
    }
    
    /**
     * Creates a new instance of DnDTable
     */
    public DnDTable() {

        this.setShowHorizontalLines(false);
        java.awt.dnd.DropTarget drop = new java.awt.dnd.DropTarget();
        try {
            drop.addDropTargetListener(this);
        } catch (TooManyListenersException ex) {
            ex.printStackTrace();
        }
        this.setDropTarget(drop);

        DragSource dragSource = DragSource.getDefaultDragSource();
        // creating the recognizer is all that's necessary - it
        // does not need to be manipulated after creation
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this); // drag gesture listener        
    }

    
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    public void dragOver(DropTargetDragEvent dtde) {

    }

    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    public void dragExit(DropTargetEvent dte) {
        if(deleteAfterDragAndDrop){
            this.removeItem();
        }
    }

    
    public void drop(DropTargetDropEvent dtde) {

        if(this.droppable){        
            try {
                Object obj = dtde.getTransferable().getTransferData(SerializableObject.serializableObjectFlavor);
                if (obj instanceof Song){
                    Song objclone = ((Song)obj).getClone();
                    ((ListTable)this.getModel()).addItem(objclone);
                }else{
                    ((ListTable)this.getModel()).addItem(obj);
                }
            } catch (UnsupportedFlavorException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
        }
    }

    public void dragEnter(DragSourceDragEvent dsde) {
        DragSourceContext context = dsde.getDragSourceContext();

        context.setCursor(DragSource.DefaultCopyDrop);   
    }

    public void dragOver(DragSourceDragEvent dsde) {
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    public void dragExit(DragSourceEvent dse) {
        DragSourceContext context = dse.getDragSourceContext();        
        context.setCursor(DragSource.DefaultCopyNoDrop);           
        
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        
        if (!draggable) return;
        
        int column=0;
        
        TableModel tm = this.getModel();
        if(tm instanceof SongListTable){
            column = ((SongListTable)tm).getSongColumn();
        }
        
        SerializableObject obj = (SerializableObject)((ListTable)this.getModel()).getValueAt(this.getSelectedRows()[0],column);        

        dge.startDrag(DragSource.DefaultCopyNoDrop, (Transferable)obj, this);
    }

    public void upItem(){
        ListTable model = (ListTable)this.getModel();
        int row = this.getSelectedRow();
        if (row >= 0){
            model.upItem(row);
            if(row>0)
                this.setRowSelectionInterval(row-1,row-1);
        }
    }

    public void downItem(){
        ListTable model = (ListTable)this.getModel();
        int row = this.getSelectedRow();        
        if (row >= 0){
            model.downItem(row);
            if(row<this.getRowCount()-1)
                this.setRowSelectionInterval(row+1,row+1);
        }
    }

    public void removeItem(){
        ListTable model = (ListTable)this.getModel();
        int row = this.getSelectedRow();
        
        if (row >= 0){
            model.removeItem(row);
        }
    }

    public boolean getScrollableTracksViewportHeight() {
        // fetch the table's parent
        Container viewport = getParent();

        // if the parent is not a viewport, calling this isn't useful
        if (!(viewport instanceof JViewport)) {
            return false;
        }

        // return true if the table's preferred height is smaller
        // than the viewport height, else false
        return getPreferredSize().height < viewport.getHeight();
    }



}
