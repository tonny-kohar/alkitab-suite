/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.SwingHTMLConverter;
import kiyut.alkitab.api.TransformerHints;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.FormatType;
import org.crosswire.common.xml.PrettySerializingContentHandler;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.common.xml.XMLUtil;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookMetaData;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.Verse;
import org.xml.sax.ContentHandler;

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
    
    protected TransformerHints<TransformerHints.Key,Object> transformerHints;
    
    protected String rawText;
    protected String osisText;

    /** Creates new BookTextPane. 
     * By default it is using empty TransfomerHints
     * 
     */
    public BookTextPane() {
        this(new TransformerHints<TransformerHints.Key,Object>());
    }
    
    public BookTextPane(TransformerHints<TransformerHints.Key,Object> transformerHints) {
        books = new ArrayList<Book>();
        setEditable(false);
        setEditorKit(new HTMLEditorKit());
        addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                BookTextPane.this.hyperlinkUpdate(evt);
            }
        });

        setConverter(new SwingHTMLConverter());
        setTransformerHints(transformerHints);
        this.compareView = false;

        //XXX workaround for Linux GTK lnf JEditorPane.setEditable(false) background color
        Color color = UIManager.getColor("EditorPane.background");
        if (color != null) {
            if (!color.equals(getBackground())) {
                setBackground(color);
            }
        }
    }

    public void setTransformerHints(TransformerHints<TransformerHints.Key,Object> transformerHints) {
        if (transformerHints == null) {
            throw new IllegalArgumentException("transformerHints could not be null");
        }
        this.transformerHints = transformerHints;
    }
    
    public TransformerHints<TransformerHints.Key,Object> getTransformerHints() {
        return transformerHints;
    }
    
    /** Default it is false */
    public void setCompareView(boolean compareView) {
        this.compareView = compareView;
    }

    public boolean isCompareView() {
        return this.compareView;
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

    protected void refreshImpl() {
        if (books.isEmpty() || key == null) {
            clear();
            return;
        }

        if (key == null) {
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
            
            // Raw Text
            StringBuilder sb = new StringBuilder();
            Iterator<Key> iter = key.iterator();
            while (iter.hasNext()) {
                Key curKey = iter.next();
                
                // XXX JSword Bug? Non bible key getOsisID end up in endless loop
                String osisID = null;
                if (key instanceof Verse) {
                    osisID = key.getOsisID();
                }
                //System.out.println("BookTextPane.refreshImpl osisID: " + osisID);
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(book.getInitials());
                    if (osisID != null) {
                        sb.append(':' + osisID);
                    }
                    sb.append(" - " + book.getRawText(curKey));
                }
            }
            
            rawText = sb.toString();
            
            SAXEventProvider osissep = bookData.getSAXEventProvider();
            
            // OSIS Text
            ContentHandler osis = new PrettySerializingContentHandler(FormatType.CLASSIC_INDENT);
            osissep.provideSAXEvents(osis);
            osisText = osis.toString();
            
            TransformingSAXEventProvider htmlsep = (TransformingSAXEventProvider) converter.convert(osissep);
            htmlsep.setParameter("direction", ltr ? "ltr" : "rtl");
            
            URI uri = bmd.getLocation();
            //String uriString = uri == null ? "" : NetUtil.getAsFile(uri).getCanonicalPath();
            String uriString = uri == null ? "" : uri.toURL().toString();
            transformerHints.put(TransformerHints.BASE_URL, uriString);
            
            transformerHints.updateProvider(htmlsep);
            
            // HTML Text
            text = XMLUtil.writeToString(htmlsep);

        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }

        setText(text);
        select(0, 0);
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
    public String getRawText() {
        return rawText;
    }
    
    public String getOSISText() {
        return osisText;
    }
    
    public String getHTMLText() {
        return getText();
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
        rawText = null;
        osisText = null;
        setText(null);
    }
}
