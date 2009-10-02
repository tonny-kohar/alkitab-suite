/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import kiyut.alkitab.api.BookFontStore;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.SwingHTMLConverter;
import kiyut.alkitab.api.ViewerHints;
import org.crosswire.common.swing.GuiConvert;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.common.xml.XMLUtil;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookMetaData;
import org.crosswire.jsword.passage.Key;

/**
 * BookTextPane that use {@link javax.swing.JTextPane JTextPane} HTML mode or HTMLEditorKit.
 * This support parallel book.
 *
 */
public class BookTextPane extends JTextPane {

    protected List<Book> books;
    protected Key key;
    protected Converter converter;
    
    /** Default it is false */
    protected boolean compareView;
    
    protected ViewerHints<ViewerHints.Key,Object> viewerHints;
    
    /** Creates new BookTextPane. 
     * By default it is using empty TransfomerHints
     * 
     */
    public BookTextPane() {
        this(new ViewerHints<ViewerHints.Key,Object>());
    }
    
    public BookTextPane(ViewerHints<ViewerHints.Key,Object> viewerHints) {
        books = new ArrayList<Book>();
        setEditable(false);
        setEditorKit(new HTMLEditorKit());
        addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                BookTextPane.this.hyperlinkUpdate(evt);
            }
        });

        setConverter(new SwingHTMLConverter());
        setViewerHints(viewerHints);
        this.compareView = false;

        //XXX workaround for Linux GTK lnf JEditorPane.setEditable(false) background color
        try {
            if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                Color color = UIManager.getColor("TextPane.background");
                if (color != null) {
                    if (!color.equals(getBackground())) {
                        setBackground(color);
                    }
                }
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.CONFIG,ex.getMessage(),ex);
        }
    }

    public void setViewerHints(ViewerHints<ViewerHints.Key,Object> viewerHints) {
        if (viewerHints == null) {
            throw new IllegalArgumentException("transformerHints could not be null");
        }
        this.viewerHints = viewerHints;
    }
    
    public ViewerHints<ViewerHints.Key,Object> getViewerHints() {
        return viewerHints;
    }
    
    /** Default it is false */
    public void setCompareView(boolean compareView) {
        this.compareView = compareView;
    }

    public boolean isCompareView() {
        return this.compareView;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Converter getConverter() {
        return this.converter;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return this.key;
    }

    /** Redisplay or refresh content, equivalent with calling {@code refresh(false)}
     * @see #refresh(boolean)
     */
    public void refresh() {
        refresh(false);
    }

    /** Redisplay or refresh content 
     * @param invokeLater using Event Dispatch Thread
     */
    public void refresh(boolean invokeLater) {
        if (invokeLater) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    refreshImpl();
                }
            });
        } else {
            refreshImpl();
        }
    }

    @SuppressWarnings("unchecked")
    protected void refreshImpl() {
        //System.out.println("BookTextPane refreshImpl()");
        
        if (books.isEmpty() || key == null) {
            clear();
            return;
        }
        
        BookData bookData = new BookData(books.toArray(new Book[books.size()]), key, compareView);

        BookMetaData bmd = bookData.getFirstBook().getBookMetaData();
        if (bmd == null) {
            clear();
            return;
        }

        boolean ltr = bmd.isLeftToRight();
        applyComponentOrientation(ltr ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT);

        String text = null;
        try {
            SAXEventProvider osissep = bookData.getSAXEventProvider();
            
            TransformingSAXEventProvider htmlSEP = (TransformingSAXEventProvider) converter.convert(osissep);
            htmlSEP.setParameter(SwingHTMLConverter.DIRECTION, ltr ? "ltr" : "rtl");
            
            URI uri = bmd.getLocation();
            //String uriString = uri == null ? "" : NetUtil.getAsFile(uri).getCanonicalPath();
            String uriString = uri == null ? "" : uri.toURL().toString();
            htmlSEP.setParameter(SwingHTMLConverter.BASE_URL, uriString);
            
            // set the font, overrides default if needed
            String fontSpec = GuiConvert.font2String(BookFontStore.getInstance().getFont(bookData.getFirstBook()));
            htmlSEP.setParameter(SwingHTMLConverter.FONT, fontSpec);

            viewerHints.updateProvider(htmlSEP);
            
            // HTML Text
            text = XMLUtil.writeToString(htmlSEP);
            
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }

        setText(text);
        select(0, 0);
    }

    /** 
     * If it is in the same document, then scroll to the referenced URI
     * 
     * @param evt the Event
     */
    protected void hyperlinkUpdate(HyperlinkEvent evt) {
        if (evt.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            String uri = evt.getDescription();
            try {
                String[] parts = SwordURI.parseParts(uri);
                if (parts[2].length() > 0) {
                    if (parts[2].charAt(0) == '#') {
                        scrollToReference(uri);
                    }
                }
            } catch (URISyntaxException ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, "uri: " + uri, ex);
            }
        }
    }
    
    protected void clear() {
        setText(null);
    }
}
