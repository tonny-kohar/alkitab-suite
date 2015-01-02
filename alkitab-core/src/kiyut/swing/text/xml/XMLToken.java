/* This work has been placed into the public domain. */

package kiyut.swing.text.xml;

/**
 * XML Token
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class XMLToken {
    private int context;
    private int startOffset;
    private int endOffset;
    
    /** Creates a new instance of XMLToken */
    public XMLToken(int context, int startOffset, int endOffset) {
        this.context = context;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }
    
    public int getContext() {
        return context;
    }
    
    public int getStartOffset() {
        return startOffset;
    }
    
    public int getEndOffset() {
        return endOffset;
    }
    
}
