/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.util;

import datasoul.serviceitems.text.TextServiceItem;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.BadLocationException;
import javax.swing.SwingUtilities;

/**
 *
 * @author samuel
 */
public class HighlightTextArea extends JTextArea {

    private boolean chords;

    public HighlightTextArea() {
        super();
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
            public void run(){
                removeHighlights();
                highlight("\n"+TextServiceItem.SLIDE_BREAK+"\n",Color.ORANGE);
                highlight("\n"+TextServiceItem.CHORUS_MARK+"\n",Color.PINK);
                if (chords){
                    highlight("\n=",Color.LIGHT_GRAY);
                }
            }
        });
    }

    // Creates highlights around all occurrences of pattern in textComp
    protected void highlight(String pattern, Color color) {
        Highlighter.HighlightPainter highlightPainter = new MyHighlightPainter(color);

        try {
            Highlighter hilite = getHighlighter();
            String text = getText();
            int pos = 0;

            // Check for the pattern in the beggining of the string
            if (text.startsWith( pattern.substring(1) )){
                hilite.addHighlight(0, pattern.length()-1, highlightPainter);
            }

            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos+1, pos+pattern.length(), highlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {

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
