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

package datasoul.bible;

import datasoul.datashow.*;

/**
 *
 * @author Administrador
 */
public class BibleText extends TextServiceItem{
  
    private String bookname="";
    private String chapter="";
    private String verses="";

    final static public String[] bookNames = {
            "Genesis", 
            "Exodus", 
            "Leviticus", 
            "Numbers", 
            "Deuteronomy", 
            "Joshua", 
            "Judges", 
            "Ruth", 
            "1 Samuel", 
            "2 Samuel", 
            "1 Kings", 
            "2 Kings", 
            "1 Chronicles", 
            "2 Chronicles", 
            "Ezra", 
            "Nehemiah", 
            "Esther", 
            "Job", 
            "Psalm", 
            "Proverbs", 
            "Ecclesiastes", 
            "Song of Solomon", 
            "Isaiah", 
            "Jeremiah", 
            "Lamentations", 
            "Ezekiel", 
            "Daniel", 
            "Hosea", 
            "Joel", 
            "Amos", 
            "Obadiah", 
            "Jonah", 
            "Micah", 
            "Nahum", 
            "Habakkuk", 
            "Zephaniah", 
            "Haggai", 
            "Zechariah", 
            "Malachi", 
            "Matthew", 
            "Mark", 
            "Luke", 
            "John", 
            "Acts", 
            "Romans", 
            "1 Corinthians", 
            "2 Corinthians", 
            "Galatians", 
            "Ephesians", 
            "Philippians", 
            "Colossians", 
            "1 Thessalonians", 
            "2 Thessalonians", 
            "1 Timothy", 
            "2 Timothy", 
            "Titus", 
            "Philemon", 
            "Hebrews", 
            "James", 
            "1 Peter", 
            "2 Peter", 
            "1 John", 
            "2 John", 
            "3 John", 
            "Jude", 
            "Revelation" 
    };
    
    
    /** Creates a new instance of Song */
    public BibleText() {
        super();
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        properties.add("Bookname");
        properties.add("Chapter");
        properties.add("Verses");
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getVerses(){
        return verses;
    }
    
    public void setVerses(String verses){
        this.verses = verses;
    }
}
