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

/*
 * BibleTextPanel.java
 *
 * Created on Apr 20, 2009, 10:10:17 AM
 */
package datasoul.bible;

import datasoul.config.WindowPropConfig;
import datasoul.serviceitems.text.TextServiceItem;
import org.crosswire.jsword.versification.BibleInfo;
import javax.swing.JTextArea;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.BookFilters;
import org.crosswire.jsword.book.BooksEvent;
import org.crosswire.jsword.book.BooksListener;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.OSISUtil;
import org.crosswire.jsword.passage.NoSuchVerseException;
import org.crosswire.jsword.passage.Verse;

/**
 *
 * @author samuel
 */
public class BibleTextPanel extends javax.swing.JPanel {

    private MyBooksListener listener;
    private JTextArea txtArea;
    private JTextField titleField;
    private int chapter;
    private int versefrom;
    private int verseto;

    /** Creates new form BibleTextPanel */
    public BibleTextPanel() {
        initComponents();

        listener = new MyBooksListener(this);
        Books.installed().addBooksListener(listener);
        updateBibles();


        // cbHowToSplit options
        // Keep the current idem ordem. They are handled by their index in Load.
        cbHowToSplit.removeAllItems();
        cbHowToSplit.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SECTIONS"));
        cbHowToSplit.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("SLIDES"));
        cbHowToSplit.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CONTINUOUS"));

        // cbRefType options
        // Keep the current idem ordem. They are handled by their index in Load.
        cbRefType.removeAllItems();
        cbRefType.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("FULL"));
        cbRefType.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("CHAPTER AND VERSE"));
        cbRefType.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("VERSE ONLY"));
        cbRefType.addItem(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("NONE"));
        cbRefType.setSelectedIndex(1);

        // Setup bible books
        cbBook.removeAllItems();
        for (int i = 0; i < BibleInfo.booksInBible(); i++) {
            try {
                cbBook.addItem(BibleInfo.getPreferredBookName(i + 1));
            } catch (NoSuchVerseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getSelectedBookName(){
        Object o = cbBibles.getSelectedItem();
        if(o == null){
            return null;
        }
        return o.toString();
    }
    
    public void configureForVerseListEditorForm(){
        //TODO: hide the bible book combobox, the chapter & verses buttons and 
        //      the "load" button
        jLabel1.setVisible(false);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        cbBook.setVisible(false);
        cbChapter.setVisible(false);
        cbVersesFrom.setVisible(false);
        cbVersesTo.setVisible(false);
        btnLoad.setVisible(false);
    }

    public int getSelectedRefType() {
        return cbRefType.getSelectedIndex();
    }

    public int getSelectedHowToSplit() {
        return cbHowToSplit.getSelectedIndex();
    }

    public void registerTitlefield(JTextField field) {
        this.titleField = field;
    }

    public void registerTextArea(JTextArea area) {
        this.txtArea = area;
    }

    public void onClose() {
        Books.installed().removeBooksListener(listener);
    }

    public void updateBibles() {

        cbBibles.removeAllItems();

        List installed = Books.installed().getBooks(BookFilters.getOnlyBibles());

        String prevSelectedBible = WindowPropConfig.getInstance().getSelectedBible();

        for (Object o : installed) {
            if (o instanceof Book) {
                String name = ((Book) o).getName();
                cbBibles.addItem(name);
                if (prevSelectedBible != null && prevSelectedBible.equals(name)) {
                    cbBibles.setSelectedItem(name);
                }
            }
        }

        boolean hasAny = installed.size() > 0;

        cbBibles.setEnabled(hasAny);
        cbBook.setEnabled(hasAny);
        cbChapter.setEnabled(hasAny);
        cbHowToSplit.setEnabled(hasAny);
        cbRefType.setEnabled(hasAny);
        cbVersesFrom.setEnabled(hasAny);
        cbVersesTo.setEnabled(hasAny);
        btnLoad.setEnabled(hasAny);


    }

    static class MyBooksListener implements BooksListener {

        private BibleTextPanel orig;

        public MyBooksListener(BibleTextPanel orig) {
            this.orig = orig;
        }

        public void bookAdded(BooksEvent ev) {
            orig.updateBibles();
        }

        public void bookRemoved(BooksEvent ev) {
            orig.updateBibles();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        cbBibles = new javax.swing.JComboBox();
        btnManageBible = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbHowToSplit = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbRefType = new javax.swing.JComboBox();
        btnLoad = new javax.swing.JButton();
        cbVersesTo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cbVersesFrom = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbChapter = new javax.swing.JComboBox();
        cbBook = new javax.swing.JComboBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel3.setText(bundle.getString("BIBLE:")); // NOI18N

        cbBibles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbBibles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBiblesActionPerformed(evt);
            }
        });

        btnManageBible.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_data-sources-new.png"))); // NOI18N
        btnManageBible.setText(bundle.getString("MANAGE INSTALLED BIBLES")); // NOI18N
        btnManageBible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageBibleActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("VERSES:")); // NOI18N

        jLabel2.setText(bundle.getString("PUT VERSES IN:")); // NOI18N

        cbHowToSplit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText(bundle.getString("ADD REFERENCE:")); // NOI18N

        cbRefType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/v2/stock_edit-bookmark.png"))); // NOI18N
        btnLoad.setText(bundle.getString("LOAD")); // NOI18N
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        cbVersesTo.setEditable(true);
        cbVersesTo.setPreferredSize(new java.awt.Dimension(50, 20));
        cbVersesTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVersesToActionPerformed(evt);
            }
        });

        jLabel5.setText("-");
        jLabel5.setPreferredSize(new java.awt.Dimension(4, 20));

        cbVersesFrom.setEditable(true);
        cbVersesFrom.setPreferredSize(new java.awt.Dimension(50, 20));
        cbVersesFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVersesFromActionPerformed(evt);
            }
        });
        cbVersesFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbVersesFromFocusGained(evt);
            }
        });

        jLabel4.setText(":");
        jLabel4.setPreferredSize(new java.awt.Dimension(4, 20));

        cbChapter.setEditable(true);
        cbChapter.setPreferredSize(new java.awt.Dimension(50, 20));
        cbChapter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbChapterActionPerformed(evt);
            }
        });
        cbChapter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbChapterFocusLost(evt);
            }
        });

        cbBook.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBookActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbBibles, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnManageBible))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbHowToSplit, 0, 132, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbRefType, 0, 139, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbBook, 0, 127, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbChapter, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(cbVersesFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(cbVersesTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLoad))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel1))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbBibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(btnManageBible, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbHowToSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbRefType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(btnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbVersesTo, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(cbVersesFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(cbChapter, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(cbBook, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnManageBibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageBibleActionPerformed

        if (BibleInstaller.checkDownloadAllowed(this)) {
            BibleInstaller bi = new BibleInstaller();
            bi.setLocationRelativeTo(this);
            bi.setVisible(true);
        }
}//GEN-LAST:event_btnManageBibleActionPerformed

    private void cbBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBookActionPerformed
        cbChapter.removeAllItems();
        if (cbBook.getSelectedIndex() >= 0) {

            try {
                int max = BibleInfo.chaptersInBook(cbBook.getSelectedIndex() + 1);
                for (int i = 0; i < max; i++) {
                    cbChapter.addItem(Integer.toString(i + 1));
                }
            } catch (NoSuchVerseException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_cbBookActionPerformed

    private void cbChapterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbChapterActionPerformed

        if (cbChapter.getItemCount() > chapter && cbChapter.getSelectedIndex() < 0) {
            cbChapter.setSelectedIndex(chapter);
            return;
        }

        cbVersesFrom.removeAllItems();
        cbVersesTo.removeAllItems();

        if (cbBook.getSelectedIndex() >= 0 && cbChapter.getSelectedIndex() >= 0) {

            try {
                int max = BibleInfo.versesInChapter(cbBook.getSelectedIndex() + 1, cbChapter.getSelectedIndex() + 1);
                for (int i = 0; i < max; i++) {
                    cbVersesFrom.addItem(Integer.toString(i + 1));
                    cbVersesTo.addItem(Integer.toString(i + 1));
                }
            } catch (NoSuchVerseException ex) {
                ex.printStackTrace();
            }
            chapter = cbChapter.getSelectedIndex();
        }
    }//GEN-LAST:event_cbChapterActionPerformed

    private void cbVersesToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVersesToActionPerformed
        int index = cbVersesTo.getSelectedIndex();
        if (cbVersesTo.getItemCount() > verseto && (index < 0 || index < cbVersesFrom.getSelectedIndex())) {
            cbVersesTo.setSelectedIndex(verseto);
        } else {
            verseto = index;
        }
}//GEN-LAST:event_cbVersesToActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

        int begin = cbVersesFrom.getSelectedIndex() + 1;
        int end = cbVersesTo.getSelectedIndex() + 1;

        if (begin > end) {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("INVALID VERSE RANGE"));
            return;
        }

        Book book = Books.installed().getBook(cbBibles.getSelectedItem().toString());
        if (book == null) {
            return;
        }

        StringBuffer sb = new StringBuffer();

        if (txtArea.getText().length() > 0) {
            sb.append(txtArea.getText() + "\n");
        }

        try {
            for (int i = begin; i <= end; i++) {
                Verse temp = new Verse(cbBook.getSelectedIndex() + 1, cbChapter.getSelectedIndex() + 1, i);
                BookData data = new BookData(book, temp);
                String versetext = OSISUtil.getCanonicalText(data.getOsisFragment());

                // Skip blank lines (this may happen if the selected bible does not
                // contain the choosed text, it may be a New Testment only, for example)
                if (versetext.trim().length() == 0) {
                    continue;
                }

                // Add break if needed
                if (sb.length() > 0) {
                    if (cbHowToSplit.getSelectedIndex() == 1) {
                        sb.append(TextServiceItem.SLIDE_BREAK + "\n");
                    } else if (cbHowToSplit.getSelectedIndex() == 0) {
                        sb.append(TextServiceItem.CHORUS_MARK + "\n");
                    }
                }

                // Add the reference, if needed
                switch (cbRefType.getSelectedIndex()) {
                    case 0: // Full
                        sb.append(cbBook.getSelectedItem().toString());
                        sb.append(" ");
                    // do not break
                    case 1: // chapter + verse
                        sb.append(cbChapter.getSelectedItem().toString());
                        sb.append(":");
                    // do not break
                    case 2: // verse only
                        sb.append(Integer.toString(i));
                        sb.append(" ");
                        break;
                }

                // Add the text
                sb.append(versetext.trim() + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        txtArea.setText(sb.toString());

        // Updating the Title field automatically (if it is empty)
        if (titleField != null && titleField.getText().trim().length() == 0) {
            String bookName = cbBook.getSelectedItem().toString();
            String chap = cbChapter.getSelectedItem().toString();
            String title;
            if (begin != end) {
                title = String.format("%s %s:%d-%d", bookName, chap, begin, end);
            } else {
                title = String.format("%s %s:%d", bookName, chap, begin);
            }
            titleField.setText(title);
        }

    }//GEN-LAST:event_btnLoadActionPerformed

    private void cbChapterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbChapterFocusLost
    }//GEN-LAST:event_cbChapterFocusLost

    private void cbVersesFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVersesFromActionPerformed
        if (cbVersesFrom.getItemCount() > versefrom && cbVersesFrom.getSelectedIndex() < 0) {
            cbVersesFrom.setSelectedIndex(versefrom);
        } else {
            versefrom = cbVersesFrom.getSelectedIndex();
            if (cbVersesTo.getItemCount() == cbVersesFrom.getItemCount()
                    && cbVersesTo.getSelectedIndex() < cbVersesFrom.getSelectedIndex()) {
                cbVersesTo.setSelectedIndex(cbVersesFrom.getSelectedIndex());
            }
        }
    }//GEN-LAST:event_cbVersesFromActionPerformed

    private void cbVersesFromFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbVersesFromFocusGained
    }//GEN-LAST:event_cbVersesFromFocusGained

    private void cbBiblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBiblesActionPerformed
        if (cbBibles.getSelectedItem() != null) {
            WindowPropConfig.getInstance().setSelectedBible(cbBibles.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cbBiblesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnManageBible;
    private javax.swing.JComboBox cbBibles;
    private javax.swing.JComboBox cbBook;
    private javax.swing.JComboBox cbChapter;
    private javax.swing.JComboBox cbHowToSplit;
    private javax.swing.JComboBox cbRefType;
    private javax.swing.JComboBox cbVersesFrom;
    private javax.swing.JComboBox cbVersesTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}


