/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.userguide;

import com.sun.java.help.impl.ViewAwareComponent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import org.openide.awt.HtmlBrowser;

/**
 * Simple lightweight component to be included in HTML content within JavaHelp 
 * to open link externally using default browser.
 * @author Tonny
 */
public class ExternalLink extends JLabel implements ViewAwareComponent {
    private String content = "";
    
    private View view;
    private SimpleAttributeSet textAttribs;
    private HTMLDocument doc;
    
    private Cursor origCursor;
    private final static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    
    public ExternalLink() {
        super();
        setForeground(Color.blue);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setToolTipText(null);
                openLink();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(handCursor);
                setToolTipText(getContent());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(origCursor);
                setToolTipText(null);
            }
        });
    }
    
    /**
     * Sets data obtained from the View
     * @param v the View
     */
    @Override
    public void setViewData(View v) {
	view = v;
	doc = (HTMLDocument) view.getDocument();
	
	Font font = getFont();
	textAttribs = new SimpleAttributeSet();
        textAttribs.removeAttribute(StyleConstants.FontFamily);
	textAttribs.removeAttribute(StyleConstants.FontSize);
	textAttribs.removeAttribute(StyleConstants.Bold);
	textAttribs.removeAttribute(StyleConstants.Italic);
	textAttribs.addAttribute(StyleConstants.FontFamily, font.getName());
	textAttribs.addAttribute(StyleConstants.FontSize, font.getSize());
	textAttribs.addAttribute(StyleConstants.Bold, font.isBold());
	textAttribs.addAttribute(StyleConstants.Italic, font.isItalic());
    }
    
    public void openLink() {
        URL url;
        try {
            url = new URL(content);
        } catch (MalformedURLException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.FINE ,ex.getMessage(),ex);
            return;
        }
        HtmlBrowser.URLDisplayer.getDefault().showURLExternal(url);
    }
    
    /**
     * Gets the font from an attribute set.  This is
     * implemented to try and fetch a cached font
     * for the given AttributeSet, and if that fails 
     * the font features are resolved and the
     * font is fetched from the low-level font cache.
     * Font's are cached in the StyleSheet of a document
     *
     * @param attr the attribute set
     * @return the font
     */
    protected Font getAttributeSetFont(AttributeSet attr) {
        int style = Font.PLAIN;
        if (StyleConstants.isBold(attr)) {
            style |= Font.BOLD;
        }
        if (StyleConstants.isItalic(attr)) {
            style |= Font.ITALIC;
        }
        String family = StyleConstants.getFontFamily(attr);
        int size = StyleConstants.getFontSize(attr);

	// fonts are cached in the StyleSheet so use that
        return doc.getStyleSheet().getFont(family, style, size);
    }
    
    
    /**
     * Set the content URL
     * @param content a valid URL
     */
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * Returns the content URL
     * @return String
     */
    public String getContent() {
	return content;
    }
    
    public void setTextFontSize(String size) {
	int newsize;
	StyleSheet css = doc.getStyleSheet();
	try {
	    if (size.equals("xx-small")) {
		newsize = (int)css.getPointSize(0);
	    } else if (size.equals("x-small")) {
		newsize = (int)css.getPointSize(1);
	    } else if (size.equals("small")) {
		newsize = (int)css.getPointSize(2);
	    } else if (size.equals("medium")) {
		newsize = (int)css.getPointSize(3);
	    } else if (size.equals("large")) {
		newsize = (int)css.getPointSize(4);
	    } else if (size.equals("x-large")) {
		newsize = (int)css.getPointSize(5);
	    } else if (size.equals("xx-large")) {
		newsize = (int)css.getPointSize(6);
	    } else if (size.equals("bigger")) {
		newsize = (int)css.getPointSize("+1");
	    } else if (size.equals("smaller")) {
		newsize = (int)css.getPointSize("-1");
	    } else if (size.endsWith("pt")) {
		String sz = size.substring(0, size.length() - 2);
		newsize = Integer.parseInt(sz);
	    } else {
		newsize = (int) css.getPointSize(size);
	    }
	} catch (NumberFormatException nfe) {
	    return;
	}
	if (newsize == 0) {
	    return;
	}
	textAttribs.removeAttribute(StyleConstants.FontSize);
	textAttribs.addAttribute(StyleConstants.FontSize, newsize);
	setFont(getAttributeSetFont(textAttribs));
    }
    
    /**
     * Returns the text Font size
     * @return font size
     */
    public String getTextFontSize() {
	return Integer.toString(StyleConstants.getFontSize(textAttribs));
    }
    
}
