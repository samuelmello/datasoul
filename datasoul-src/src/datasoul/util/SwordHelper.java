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

package datasoul.util;

import java.util.ArrayList;
import java.util.List;

import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookException;
import org.crosswire.jsword.book.OSISUtil;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.NoSuchKeyException;

import datasoul.serviceitems.text.TextServiceItem;

/**
 *
 * @author jrobicha
 */
public class SwordHelper {

    static String endl = System.getProperty("line.separator");

    public enum ReferenceTxtType {

        Full(0),
        ChapterAndVerse(1),
        VerseOnly(2),
        NoReference(3);
        int idx = 0;

        private ReferenceTxtType(int idx) {
            this.idx = idx;
        }

       public  static ReferenceTxtType getRefTypeById(int idx) {
            ReferenceTxtType arr[] = ReferenceTxtType.values();
            if (idx >= arr.length) {
                throw new IndexOutOfBoundsException("No ReferenceTxtType has the idx [" + idx + "], the maximum value is [" + (arr.length - 1) + "]");
            }
            return arr[idx];
        }
    }

    public enum TxtSplitType {

        Sections(0,TextServiceItem.CHORUS_MARK + endl),
        Slides(1, TextServiceItem.SLIDE_BREAK + endl),
        Continuous(2, "");
        
        int idx = 0;
        String txt = "";

        TxtSplitType(int idx, String txt) {

            this.idx = idx;
            this.txt = txt;
        }

      public  static TxtSplitType getTxtSplitTypeById(int idx) {
            TxtSplitType arr[] = TxtSplitType.values();
            if (idx >= arr.length) {
                throw new IndexOutOfBoundsException("No TxtSplitType has the idx [" + idx + "], the maximum value is [" + (arr.length - 1) + "]");
            }
            return arr[idx];
        }

      public String getTxt(){
          return txt;
      }
    }

    public static boolean isReferenceValid(Book swordBook, String reference) {
        if (swordBook == null) {
            return false;
        }
        if (reference == null) {
            return true;
        }
        reference = reference.trim();
        if (reference.length() == 0) {
            return true;
        }
        Key key = null;
        try {
            key = swordBook.getKey(reference);
            if (key.getCardinality() > 0) {
                return true;
            }
        } catch (NoSuchKeyException ex) {
            return false;
        }

        return false;
    }

    public static List<String> getTextForReference(Book swordBook,
            String reference) throws NoSuchKeyException, BookException {
        return getTextForReference(swordBook, reference, ReferenceTxtType.ChapterAndVerse);

    }

    public static List<String> getTextForReference(Book swordBook,
            String reference,
            ReferenceTxtType refType) throws NoSuchKeyException, BookException {

        List<String> txt = new ArrayList<String>();
        if (swordBook == null || reference == null) {
            return txt;
        }

        reference = reference.trim();
        if (reference.length() == 0) {
            return txt;
        }


        System.out.println("REF: " + reference);
        Key key = null;

        //If the reference is not valid, the next line will throw a NoSuchKeyEx
        key = swordBook.getKey(reference);

        String fullTxt = null;
        // Somewhow, the following lines always throw a BookException...
        // It seems that there is a bug in the jsword library we're using
        /*
        BookData data = new BookData(swordBook, key);
        fullTxt = OSISUtil.getCanonicalText(data.getOsis());
         */


        int cardinality = key.getCardinality();
        for (int i = 0; i < cardinality; i++) {
            BookData data = new BookData(swordBook, key);
            //The following line can throw a BookException
            fullTxt = OSISUtil.getCanonicalText(data.getOsis());
            txt.add(fullTxt);
        }




        return txt;

    }
}
