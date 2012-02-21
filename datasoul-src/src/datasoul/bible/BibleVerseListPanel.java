/*
 * Copyright 2005-2010 Jean Philippe Robichaud, Samuel Mello
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
 * BibleVerseListPanel.java
 *
 * Created on Jul 11, 2010, 10:03:10 PM
 */
package datasoul.bible;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookException;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.OSISUtil;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.KeyUtil;
import org.crosswire.jsword.passage.NoSuchKeyException;
import org.crosswire.jsword.passage.NoSuchVerseException;
import org.crosswire.jsword.passage.Verse;
import org.crosswire.jsword.versification.BibleInfo;
import org.jdom.Element;

import datasoul.config.DisplayControlConfig;
import datasoul.serviceitems.text.TextServiceItem;
import datasoul.servicelist.ServiceListTable;
import datasoul.util.SwordHelper;
import datasoul.util.SwordHelper.ReferenceTxtType;
import datasoul.util.SwordHelper.TxtSplitType;
import datasoul.util.TextSplitPanel;

/**
 *
 * @author jrobicha
 */
public class BibleVerseListPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7320663763971411520L;
	Book swordBook = null;
    String swordBookName = null;
    String endl = System.getProperty("line.separator");
    BibleTextPanel btp;
    StyledDocument doc;
    BvlDocListener docListener;
    Style errorStyle;
    Style clearStyle;
    Map<String, Boolean> validityCache = new HashMap<String, Boolean>();

    /** Creates new form BibleVerseListPanel */
    public BibleVerseListPanel() {
        initComponents();
        doc = jTextPane.getStyledDocument();
        docListener = new BvlDocListener();
        doc.addDocumentListener(docListener);
        errorStyle = doc.addStyle("errorLine", null);
        StyleConstants.setBackground(errorStyle, Color.red);
        clearStyle = doc.addStyle("normalLine", null);
        StyleConstants.setBackground(clearStyle, Color.white);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel2.setText("<html>"+bundle.getString("BIBLE_VERSE_LIST_SHORT_HELP")+"</html>");
    }

    public void registerBibleTextPanel(BibleTextPanel btp) {
        this.btp = btp;
    }

    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

        public MyHighlightPainter(Color color) {
            super(color);
        }
    }

    private void clearAndClose() {
        jTextPane.setText("");
        this.setVisible(false);
    }

    private boolean checkSelectedBook() {
        if (swordBook == null) {
            return false;
        }
        if (!btp.getSelectedBookName().equals(swordBookName)) {
            Book book = Books.installed().getBook(btp.getSelectedBookName());
            setBook(book);
        }
        return true;
    }

    private void updateMarkup() {
        if (!checkSelectedBook()) {
            return;
        }

        String listTxt = jTextPane.getText();
        if (listTxt.length() == 0) {
            clearAndClose();
            return;
        }
        String refs[] = listTxt.split(endl);

        int endlsz = endl.length();
        int pos = 0;
        int end = refs.length;
        for (int i = 0; i < end; i++) {
            String reference = refs[i];
            if (reference == null) {
                continue;
            }

            if (reference.trim().length() == 0) {
                doc.setCharacterAttributes(pos, reference.length() + endlsz, clearStyle, true);
                pos = pos + reference.length() + endlsz;
                continue;
            }

            boolean valid = SwordHelper.isReferenceValid(swordBook, reference);
            if (valid) {
                doc.setCharacterAttributes(pos, reference.length() + endlsz, clearStyle, true);
                pos = pos + reference.length() + endlsz;
            } else {
                doc.setCharacterAttributes(pos, reference.length() + endlsz, errorStyle, true);
                pos = pos + reference.length() + endlsz;
            }
        }

    }

    public void createTextServiceItems(TextSplitPanel split) {

        if (!checkSelectedBook()) {
            clearAndClose();
            return;
        }

        String listTxt = jTextPane.getText().trim();
        if (listTxt.length() == 0) {
            clearAndClose();
            return;
        }
        String refs[] = listTxt.split(endl);

        ReferenceTxtType refStype = ReferenceTxtType.getRefTypeById(btp.getSelectedRefType());
        TxtSplitType splitType = TxtSplitType.getTxtSplitTypeById(btp.getSelectedHowToSplit());

        for (String reference : refs) {
            if (reference == null) {
                continue;
            }
            reference = reference.trim();
            if (reference.length() == 0) {
                return;
            }

            Key key = null;
            try {
                key = swordBook.getKey(reference);
            } catch (NoSuchKeyException ex) {
                Logger.getLogger(BibleVerseListPanel.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            String fullTxt = null;

            StringBuffer sb = new StringBuffer();

            int cardinality = key.getCardinality();
            String localBookName = null;
            for (int i = 0; i < cardinality; i++) {
                Key k = key.get(i);
                Verse v = KeyUtil.getVerse(k);
                if (localBookName == null) {
                    int bookNum = v.getBook();
                    try {
                        localBookName = BibleInfo.getPreferredBookName(bookNum);
                    } catch (NoSuchVerseException ex) {
                        Logger.getLogger(BibleVerseListPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                BookData data = new BookData(swordBook, k);

                try {
                    //The following line can throw a BookException
                    Element xmlData = data.getOsis();
                    fullTxt = OSISUtil.getCanonicalText(xmlData);

                    if (sb.length() > 0) {
                        sb.append(splitType.getTxt());
                    }
                    switch (refStype) {
                        case Full:
                            sb.append(localBookName).append(" ").append(v.getChapter()).append(":").append(v.getVerse()).append(" ");
                            break;
                        case ChapterAndVerse:
                            sb.append(v.getChapter()).append(":").append(v.getVerse()).append(" ");
                            break;
                        case VerseOnly:
                            sb.append(v.getVerse()).append(" ");
                            break;
                    }


                    sb.append(fullTxt).append(endl);

                } catch (BookException ex) {
                    Logger.getLogger(BibleVerseListPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            TextServiceItem textServiceItem = new TextServiceItem();

            textServiceItem.setTitle(reference);
            String text = split.splitText(sb.toString());
            textServiceItem.setText(text);

            textServiceItem.setTemplate(DisplayControlConfig.getInstance().getDefaultTemplateText());
            ServiceListTable.getActiveInstance().addItem(textServiceItem);
        }


    }

    public void setBook(Book swordBook) {
        this.swordBook = swordBook;
        this.swordBookName = swordBook.getName();
    }

    private class BvlDocListener implements DocumentListener, Runnable {

        long last = 0;
        long minDelta = 500; // 0.5 sec minimum between two updates
        AtomicBoolean inQueue = new AtomicBoolean(false);

        public BvlDocListener() {
            last = System.currentTimeMillis();
        }

        public void run() {
            updateMarkup();
            last = System.currentTimeMillis();
            inQueue.set(false);
        }

        public void doUpdateMarkup(boolean force) {
            if (force || (inQueue.get() == false && (System.currentTimeMillis() - last) >= minDelta)) {
                inQueue.set(true);
                SwingUtilities.invokeLater(this);
            }
        }

        public void insertUpdate(DocumentEvent e) {
            doUpdateMarkup(false);
        }

        public void removeUpdate(DocumentEvent e) {
            doUpdateMarkup(false);
        }

        public void changedUpdate(DocumentEvent e) {
            doUpdateMarkup(false);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jScrollPane2.setViewportView(jTextPane);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel2.setText(bundle.getString("BIBLE_VERSE_LIST_SHORT_HELP")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane;
    // End of variables declaration//GEN-END:variables
}
