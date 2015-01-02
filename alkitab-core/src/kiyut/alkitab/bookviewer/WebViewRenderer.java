/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.options.OptionsUtilities;
import kiyut.alkitab.util.FontUtilities;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.FormatType;
import org.crosswire.common.xml.PrettySerializingContentHandler;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.book.BookData;
import org.crosswire.jsword.book.BookMetaData;
import org.crosswire.jsword.passage.Key;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.xml.sax.ContentHandler;


/**
 * BookRenderer implementation that use JavaFX WebView
 */
public class WebViewRenderer extends JFXPanel implements BookRenderer {
    
    public static final String EVENT_TYPE_CLICK = "click";
    public static final String EVENT_TYPE_MOUSEOVER = "mouseover";
    public static final String EVENT_TYPE_MOUSEOUT = "mouseclick";
    
    /** It is used to convert <br></br> to <br />*/
    public static Pattern FixBRTagPattern = Pattern.compile("<br>\\s*</br>");
    
    //protected JFXPanel fxPanel;
    protected WebView webView;
    
    protected List<Book> books;
    protected Key key;
    protected Converter converter;
    /**
     * Default it is false
     */
    protected boolean compareView;
    protected ViewerHints<ViewerHints.Key, Object> viewerHints;
    
    //protected EventListenerList listenerList;
    
    protected String contentSource;
    
    
    
    public WebViewRenderer() {
        this(new ViewerHints<ViewerHints.Key, Object>());
    }
    
    public WebViewRenderer(ViewerHints<ViewerHints.Key, Object> viewerHints) {
        super();
        
        listenerList = new EventListenerList();
        books = new ArrayList<Book>();
        setConverter(new HTMLConverter());
        setViewerHints(viewerHints);
        this.compareView = false;
        
        //fxPanel = new JFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX();
            }
        });
    }
    
    protected void initFX() {
        this.setScene(createScene());
        
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    EventListener listener = new EventListener() {
                        @Override
                        public void handleEvent(Event ev) {
                            String domEventType = ev.getType();
                            //System.err.println("EventType: " + domEventType);
                            final EventType eventType;
                            if (domEventType.equals(EVENT_TYPE_CLICK)) {
                                eventType = HyperlinkEvent.EventType.ACTIVATED;
                            } else if (domEventType.equals(EVENT_TYPE_MOUSEOVER)) {
                                eventType = HyperlinkEvent.EventType.ENTERED;
                            } else if (domEventType.equals(EVENT_TYPE_MOUSEOUT)) {
                                eventType = HyperlinkEvent.EventType.EXITED;
                            } else {
                                return;
                            }
                            
                            final String href = ((Element)ev.getTarget()).getAttribute("href");
                            if (href == null) { return; }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    fireHyperlinkUpdate(eventType, href);
                                }
                            });
                            
                            //System.err.println(e.getAttribute("href"));
                        }
                    };

                    Document doc = webView.getEngine().getDocument();
                    NodeList nodeList = doc.getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        ((EventTarget) nodeList.item(i)).addEventListener(EVENT_TYPE_CLICK, listener, false);
                        ((EventTarget) nodeList.item(i)).addEventListener(EVENT_TYPE_MOUSEOVER, listener, false);
                        ((EventTarget) nodeList.item(i)).addEventListener(EVENT_TYPE_MOUSEOVER, listener, false);
                    }
                }
            }
        });
    }
    
    protected Scene createScene() {
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane);
        webView = new WebView();
        pane.setCenter(webView);
        //scene.setFill(javafx.scene.paint.Color.WHITE);
        return scene;
        
        //return new Scene(ButtonBuilder.create().minHeight(40.0).minWidth(40.0).build());
    }
    
    public void setViewerHints(ViewerHints<ViewerHints.Key, Object> viewerHints) {
        if (viewerHints == null) {
            throw new IllegalArgumentException("viewerHints could not be null");
        }
        this.viewerHints = viewerHints;
    }

    public ViewerHints<ViewerHints.Key, Object> getViewerHints() {
        return viewerHints;
    }
    
    /**
     * Default it is false
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
     * Simply calling reload(true)
     * 
     * @see #reload(boolean)
     */
    @Override
    public void reload() {
        reload(true);
    }

    /**
     * Since it is using JavaFX WebView, this method ignore parameter invokeLater 
     * and always use Platfrom.runLater() thread
     */
    @Override
    public void reload(boolean invokeLater) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                reloadImpl();
            }
        });
    }
    
    protected void reloadImpl() {
        //webView.getEngine().load("http://www.kiyut.com");
        //System.err.println("reloadImpl() start");
        
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
        
        /*boolean ltr = bmd.isLeftToRight();
        applyComponentOrientation(ltr ? ComponentOrientation.LEFT_TO_RIGHT : ComponentOrientation.RIGHT_TO_LEFT);
        
        System.err.println("reloadImpl() after applyComponentOrientation");

        // Set the correct locale
        this.setLocale(new Locale(bmd.getLanguage().getCode()));
        */

        try {
            SAXEventProvider osissep = bookData.getSAXEventProvider();
            TransformingSAXEventProvider htmlSEP = (TransformingSAXEventProvider) converter.convert(osissep);
            
            if (bmd.getBookCategory() != BookCategory.BIBLE) {
                boolean ltr = bmd.isLeftToRight();
                htmlSEP.setParameter(HTMLConverter.DIRECTION, ltr ? "ltr" : "rtl");

                URI uri = bmd.getLocation();
                //String uriString = uri == null ? "" : NetUtil.getAsFile(uri).getCanonicalPath();
                String uriString = uri == null ? "" : uri.toURL().toExternalForm();
                htmlSEP.setParameter(HTMLConverter.BASE_URL, uriString);
                htmlSEP.setParameter(HTMLConverter.CSS, uriString);
            }
            
            // set the font, overrides default if needed
            String fontSpec = FontUtilities.font2String(BookFontStore.getInstance().getFont(bookData.getFirstBook()));
            htmlSEP.setParameter(HTMLConverter.FONT, fontSpec);
            
            Color bgColor = BookViewerOptions.getInstance().getBackground();
            if (bgColor != null) {
                htmlSEP.setParameter(HTMLConverter.BACKGROUND_COLOR, OptionsUtilities.colorToString(bgColor));
            }

            viewerHints.updateProvider(htmlSEP);
            
            // HTML Text
            ContentHandler html = new PrettySerializingContentHandler(FormatType.CLASSIC);
            htmlSEP.provideSAXEvents(html);
            contentSource = html.toString();
            //contentSource = XMLUtil.writeToString(htmlSEP);
            
            contentSource = FixBRTagPattern.matcher(contentSource).replaceAll("<br />");
            
        } catch (Exception ex) {
            clear();
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }

        //webView.getEngine().loadContent("<html><body>Test JavaFX WebView Component</body></html>");
        webView.getEngine().loadContent(contentSource);
    }
    
    @Override
    public void addHyperlinkListener(HyperlinkListener listener) {
        listenerList.add(HyperlinkListener.class, listener);
    }
    
    @Override
    public void removeHyperlinkListener(HyperlinkListener listener) {
        listenerList.remove(HyperlinkListener.class, listener);
    }
    
    protected void fireHyperlinkUpdate(EventType eventType, String desc) {
        HyperlinkEvent event = new HyperlinkEvent(this, eventType, null, desc);
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == HyperlinkListener.class) {
                ((HyperlinkListener) listeners[i + 1]).hyperlinkUpdate(event);
            }
        }
    }
    
    protected void clear() {
        webView.getEngine().loadContent("");
    }
    
    @Override
    public String getContentSource() {
        return contentSource;
    }
    
    @Override
    public JComponent getComponent() {
        return this;
    }
}
