/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import kiyut.alkitab.util.FontUtilities;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.common.xml.XMLUtil;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookMetaData;
import org.crosswire.jsword.passage.Key;

/**
 * BookRenderer implementation that use {@link javax.swing.JTextPane JTextPane} HTML mode or HTMLEditorKit. 
 * This support parallel book.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class TextPaneRenderer extends JTextPane implements BookRenderer {

    protected List<Book> books;
    protected Key key;
    protected Converter converter;
    /**
     * Default it is false
     */
    protected boolean compareView;
    protected ViewerHints<ViewerHints.Key, Object> viewerHints;

    /**
     * Creates new TextPaneRenderer. By default it is using empty TransfomerHints
     *
     */
    public TextPaneRenderer() {
        this(new ViewerHints<ViewerHints.Key, Object>());
    }

    public TextPaneRenderer(ViewerHints<ViewerHints.Key, Object> viewerHints) {
        books = new ArrayList<>();
        setEditable(false);
        setEditorKit(new HTMLEditorKit());
        addHyperlinkListener((HyperlinkEvent evt) -> {
            TextPaneRenderer.this.hyperlinkUpdate(evt);
        });

        setConverter(new HTMLConverter());
        setViewerHints(viewerHints);
        this.compareView = false;

        // force setBackground for linux GTK bug workaround.
        // please see this class setBackground override
        setBackground(null);
    }

    @Override
    public void setBackground(Color bg) {
        if (bg != null) {
            super.setBackground(bg);
            return;
        }

        Color color = UIManager.getColor("TextPane.background");
        if (color != null) {
            if (!color.equals(getBackground())) {
                super.setBackground(color);
            }
        }
    }

    public void setViewerHints(ViewerHints<ViewerHints.Key, Object> viewerHints) {
        if (viewerHints == null) {
            throw new IllegalArgumentException("transformerHints could not be null");
        }
        this.viewerHints = viewerHints;
    }

    public ViewerHints<ViewerHints.Key, Object> getViewerHints() {
        return viewerHints;
    }

    /**
     * Default it is false
     * @param compareView true or false
     */
    public void setCompareView(boolean compareView) {
        this.compareView = compareView;
    }

    @Override
    public boolean isCompareView() {
        return this.compareView;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Converter getConverter() {
        return this.converter;
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    /**
     * Redisplay or refresh content, equivalent with calling {@code refresh(false)}
     *
     * @see #reload(boolean)
     */
    @Override
    public void reload() {
        reload(false);
    }

    /**
     * Redisplay or refresh content
     *
     * @param invokeLater using Event Dispatch Thread
     */
    @Override
    public void reload(boolean invokeLater) {
        if (invokeLater) {
            SwingUtilities.invokeLater(() -> {
                reloadImpl();
            });
        } else {
            reloadImpl();
        }
    }

    @SuppressWarnings("unchecked")
    protected void reloadImpl() {
        //System.out.println("TextPaneRenderer refreshImpl()");

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

        // Set the correct locale
        this.setLocale(new Locale(bmd.getLanguage().getCode()));

        String text = null;
        try {
            SAXEventProvider osissep = bookData.getSAXEventProvider();

            TransformingSAXEventProvider htmlSEP = (TransformingSAXEventProvider) converter.convert(osissep);
            htmlSEP.setParameter(HTMLConverter.DIRECTION, ltr ? "ltr" : "rtl");

            URI uri = bmd.getLocation();
            //String uriString = uri == null ? "" : NetUtil.getAsFile(uri).getCanonicalPath();
            String uriString = uri == null ? "" : uri.toURL().toExternalForm();
            htmlSEP.setParameter(HTMLConverter.BASE_URL, uriString);
            htmlSEP.setParameter(HTMLConverter.CSS, uriString);

            // set the font, overrides default if needed
            String fontSpec = FontUtilities.font2String(BookFontStore.getInstance().getFont(bookData.getFirstBook()));
            htmlSEP.setParameter(HTMLConverter.FONT, fontSpec);

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
    
    @Override
    public String getContentSource() {
        return getText();
    }
    
    @Override
    public JComponent getComponent() {
        return this;
    }
}
