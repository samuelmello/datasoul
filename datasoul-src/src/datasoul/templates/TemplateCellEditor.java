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

package datasoul.templates;

import datasoul.serviceitems.imagelist.ImageListServiceItem;
import datasoul.serviceitems.ServiceItem;
import datasoul.servicelist.ServiceListTable;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.serviceitems.song.Song;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;

public class TemplateCellEditor extends DefaultCellEditor implements ActionListener {

    private TemplateComboBox cbSong;
    private TemplateComboBox cbText;
    private TemplateComboBox cbImage;
    private TemplateComboBox cbAll;

    public TemplateCellEditor() {
        super(new JTextField());
        cbSong = new TemplateComboBox();
        cbSong.setTargetContent(DisplayTemplate.TARGET_CONTENT_SONG);
        cbSong.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        cbSong.addActionListener(this);
        cbText = new TemplateComboBox();
        cbText.setTargetContent(DisplayTemplate.TARGET_CONTENT_TEXT);
        cbText.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        cbText.addActionListener(this);
        cbImage = new TemplateComboBox();
        cbImage.setTargetContent(DisplayTemplate.TARGET_CONTENT_IMAGES);
        cbImage.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        cbImage.addActionListener(this);
        cbAll = new TemplateComboBox();
        cbAll.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        cbAll.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JComboBox) {
            this.setValue(((JComboBox) source).getSelectedItem());
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        TemplateComboBox cbRet;
        ServiceItem item = (ServiceItem) ServiceListTable.getActiveInstance().getServiceItem(row);
        if (item instanceof ImageListServiceItem) {
            cbRet = cbImage;
        } else if (item instanceof Song) {
            cbRet = cbSong;
        } else if (item instanceof TextServiceItem) {
            cbRet = cbText;
        } else {
            cbRet = cbAll;
        }
        cbRet.setSelectedItem(value);
        return cbRet;
    }

    protected void setValue(Object o) {
        this.delegate.setValue(o);
        stopCellEditing();
    }
}

