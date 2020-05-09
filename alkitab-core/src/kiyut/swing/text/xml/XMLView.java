/* This work has been placed into the public domain. */

package kiyut.swing.text.xml;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.ViewFactory;

/**
 * View that uses the lexical information to determine the
 * style characteristics of the text that it renders.  This
 * simply colorizes the various tokens and assumes a constant
 * font family and size.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class XMLView extends PlainView {
    protected XMLContext context = null;
    protected XMLScanner lexer = new XMLScanner();
    
    /** default is 8 */
    protected int tabSize = 8;
    
    /**
     * Construct a simple colorized view of XML text.
     * @param context
     * @param elem
     */
    public XMLView(XMLContext context, Element elem) {
        super(elem);
        this.context = context;
    }
    
    @Override
    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }
    
    @Override
    protected float drawUnselectedText(Graphics2D g, float x, float y, int p0, int p1) throws BadLocationException {
        XMLDocument doc = (XMLDocument)getDocument();
        XMLToken token = doc.getScannerStart(p0);
        
        String str = doc.getText(token.getStartOffset(),(p1-token.getStartOffset()) + 1);
        
        lexer.setString(str);
        lexer.reset();
        
        // read until p0
        int pos = token.getStartOffset();
        int ctx = token.getContext();
        int lastCtx = ctx;
        while (pos < p0) {
            pos = lexer.scan(ctx) + token.getStartOffset();
            lastCtx = ctx;
            ctx = lexer.getScanValue();
        }
        int mark = p0;
        
        while (pos < p1) {
            if (lastCtx != ctx) {
                //syntax = context.getSyntaxName(lastCtx);
                g.setColor(context.getSyntaxForeground(lastCtx));
                g.setFont(context.getSyntaxFont(lastCtx));
                Segment text = getLineBuffer();
                doc.getText(mark, pos - mark, text);
                x = Utilities.drawTabbedText(text, x, y, g, this, mark);
                mark = pos;
            }
            
            pos = lexer.scan(ctx) + token.getStartOffset();
            lastCtx = ctx;
            ctx = lexer.getScanValue();
            
        }
        
        // flush remaining
        //syntax = context.getSyntaxName(lastCtx);
        g.setColor(context.getSyntaxForeground(lastCtx));
        g.setFont(context.getSyntaxFont(lastCtx));
        Segment text = getLineBuffer();
        doc.getText(mark, p1 - mark, text);
        x = Utilities.drawTabbedText(text, x, y, g, this, mark);
        
        return x;
    }
    
    /** 
     * override to handle multi line node
     * {@inheritDoc}
     */
    @Override
    protected void updateDamage(javax.swing.event.DocumentEvent changes, Shape a, ViewFactory f) {
        super.updateDamage(changes,a, f);
        java.awt.Component host = getContainer();
        host.repaint();
    }
}

