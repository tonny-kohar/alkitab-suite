/* This work has been placed into the public domain. */

package kiyut.swing.text.xml;

import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


/**
 * This is the set of things needed by a text component to be a reasonably
 * functioning editor for xml type document.
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class XMLEditorKit extends DefaultEditorKit {
    public static final String XML_MIME_TYPE = "text/xml";
    
    protected XMLContext context;
    protected ViewFactory factory = null;
    
    /** Creates a new instance of XMLEditorKit */
    public XMLEditorKit() {
        this(null);
    }
    
    
    public XMLEditorKit(XMLContext context) {
        super();
        factory = new XMLViewFactory();
        if (context == null) {
            this.context = new XMLContext();
        } else {
            this.context = context;
        }
    }
    
    /** 
     * @return XMLContext
     */
    public XMLContext getStylePreferences() {
        return context;
    }
    
    /** Overriden to set the JEditorPane font to match with the XMLContext
     * {@inheritDoc}
     */
    @Override
    public void install(JEditorPane c) {
        super.install(c);
        
        Font font = context.getSyntaxFont(XMLContext.DEFAULT_STYLE);
        if (font != null) {
            c.setFont(font);
        }
    }
    
    /**
     * Get the MIME type of the data that this
     * kit represents support for.  This kit supports
     * the type {@code text/xml}.
     */
    @Override
    public String getContentType() {
        return XML_MIME_TYPE;
    }
    
    @Override
    public Object clone() {
        XMLEditorKit kit = new XMLEditorKit();
        kit.context = context;
        return kit;
    }
    
    @Override
    public Document createDefaultDocument() {
        XMLDocument doc = new XMLDocument(context);
        return doc;
    }
    
    @Override
    public ViewFactory getViewFactory() {
        return factory;
    }
    
    /**
     * A simple view factory implementation.
     */
    protected class XMLViewFactory implements ViewFactory {
        // Creates the XML View.
        public View create(Element elem) {
            XMLView view = new XMLView(context,elem);
            view.setTabSize(4);;
            return view;
        }
    }
}
