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

package datasoul.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import datasoul.serviceitems.song.Song;
import datasoul.serviceitems.text.TextServiceItem;

/**
 *
 * @author samuel
 */
public class HighlightTextArea extends JTextArea {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1670409895687878135L;
	private boolean chords;
    private Pattern slidePattern;
    private Pattern chorusPattern;
    private Pattern chordsPattern;



    public HighlightTextArea() {
        super();
        slidePattern = Pattern.compile("^"+TextServiceItem.SLIDE_BREAK+"$");
        chorusPattern = Pattern.compile("^"+TextServiceItem.CHORUS_MARK+"$");
        chordsPattern = Song.CHORDS_REGEX_PATTERN;
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                updateHighlight();
            }
        });
    }

    public void setChords(boolean b){
        chords = b;
    }

    @Override
    public void setText(String text){
        super.setText(text);
        updateHighlight();
    }

    protected void updateHighlight(){
        SwingUtilities.invokeLater( new Runnable(){
            @Override
            public void run(){
                removeHighlights();
                highlight(slidePattern, Color.ORANGE);
                highlight(chorusPattern, Color.PINK);
                if (chords){
                    highlight(chordsPattern, Color.LIGHT_GRAY);
                }
            }
        });
    }

    // Creates highlights around all occurrences of pattern in textComp
    protected void highlight(Pattern pattern, Color color) {
        Highlighter.HighlightPainter highlightPainter = new MyHighlightPainter(color);

        Highlighter hilite = getHighlighter();

        try {

            int start = 0;

            for (String line : getText().split("\n")){
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()){
                    hilite.addHighlight(start + matcher.start(), start + matcher.end(), highlightPainter);
                }
                start += line.length() + 1;
            }

        } catch (BadLocationException e) {
            e.printStackTrace();

        }

    }

    // Removes only our private highlights
    protected void removeHighlights() {
        Highlighter hilite = getHighlighter();
        for ( Highlighter.Highlight h : hilite.getHighlights()){
            if (h.getPainter() instanceof MyHighlightPainter){
                hilite.removeHighlight(h);
            }
        }
    }
    
    // A private subclass of the default highlight painter
    protected class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }





}

