/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.bookviewer.event.BookChangeEvent;
import kiyut.alkitab.history.BookViewerHistory;
import kiyut.alkitab.history.BookViewerHistoryManager;
import kiyut.alkitab.history.History;
import kiyut.alkitab.history.HistoryManager;
import kiyut.alkitab.options.ViewerHintsOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.passage.Key;

/**
 * Implementation of {@link kiyut.alkitab.api.BookViewer BookViewer} which display single book
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class SingleBookViewerPane extends AbstractBookViewerPane {

    protected ResourceBundle bundle = ResourceBundle.getBundle(SingleBookViewerPane.class.getName());
    //protected transient TextPaneRenderer bookRenderer;
    protected transient WebViewRenderer bookRenderer;
    protected HistoryManager historyManager;
    
    /** Creates new SingleBookViewerPane */
    public SingleBookViewerPane() {
        initComponents();
        initCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    protected void initCustom() {
        ViewerHints<ViewerHints.Key,Object> viewerHints = new ViewerHints<>(ViewerHintsOptions.getInstance().getViewerHints());
        //JScrollPane bookScrollPane = new JScrollPane();
        //bookRenderer = new TextPaneRenderer(viewerHints);
        //bookScrollPane.setViewportView(bookRenderer);
        //add(this.bookScrollPane, BorderLayout.CENTER);
        bookRenderer = new WebViewRenderer(viewerHints);
        add(bookRenderer, BorderLayout.CENTER);
        historyManager = new BookViewerHistoryManager();

        //getActionMap().setParent(bookRenderer.getActionMap());
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        
        if (bookRenderer == null) {
            return "";
        }

        List<Book> books = bookRenderer.getBooks();

        if (books.isEmpty()) {
            return "";
        }

        Book book = books.get(0);
        if (book == null) {
            return "";
        }

        return book.getInitials();
    }
    
    @Override
    public BookRenderer getBookRenderer() {
        return bookRenderer;
    }

    @Override
    public ViewerHints<ViewerHints.Key,Object> getViewerHints() {
        return bookRenderer.getViewerHints();
    }

    @Override
    public void addHyperlinkListener(HyperlinkListener listener) {
        bookRenderer.addHyperlinkListener(listener);
    }

    @Override
    public void removeHyperlinkListener(HyperlinkListener listener) {
        bookRenderer.removeHyperlinkListener(listener);
    }

    @Override
    public List<Book> getBooks() {
        List<Book> srcBooks = bookRenderer.getBooks();
        return Collections.unmodifiableList(srcBooks);
    }

    @Override
    public int getBookCount() {
        return bookRenderer.getBooks().size();
    }
    
    @Override
    public void viewSource() {
        SwingUtilities.invokeLater(() -> {
            try {
                SourceViewerPane sourcePane = new SourceViewerPane();
                sourcePane.initSource(bookRenderer);
                //sourcePane.initSource(bookRenderer.getBooks(), bookRenderer.getKey(), bookRenderer.getConverter(), bookRenderer.getViewerHints(), bookRenderer.isCompareView());
                sourcePane.showDialog(this, true);
            } catch (Exception ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, ex.getMessage(), ex);
            }
        });
    }

    @Override
    public void openURI(SwordURI uri) {
        openURI(uri, null);
    }

    @Override
    public void openURI(SwordURI uri, String info) {
        Book book = Books.installed().getBook(uri.getPath());
        setBook(book);
        reload();
    }

    public void setBook(String bookName) {
        Book book = Books.installed().getBook(bookName);
        setBook(book);
    }
    
    public void setBook(Book book) {
        List<Book> books = bookRenderer.getBooks();

        if (books.isEmpty()) {
            books.add(book);
            firePropertyChange(BookViewer.VIEWER_NAME, null, getName());
            fireBookChange(new BookChangeEvent(this));
        } else {
            books.set(0, book);
        }
        
        if (book == null) {
            return;
        }
        
        Key tKey = book.getGlobalKeyList();

        if (tKey == null) {
            return;
        }
        if (tKey.getCardinality() > 0) {
            History hist = new BookViewerHistory(tKey,null);
            historyManager.add(hist);
            bookRenderer.setKey(hist.current());
        }
    }
    
    @Override
    public void setKey(Key key) {
        // find the key in history
        History hist = historyManager.current();
        if (hist == null) { return; }
        
        Key keyHist = hist.first();
        if (keyHist == null) {
            return;
        }
        
        while (!keyHist.equals(key)) {
            keyHist = hist.next();
            if (keyHist == null) {
                break;
            }
        }
        
        if (keyHist == null) {
            return;
        }
        
        bookRenderer.setKey(keyHist);
    }

    @Override
    public Key getKey() {
        return bookRenderer.getKey();
    }

    @Override
    public void reload() {
        bookRenderer.reload(true);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void goPrevious() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasPrevious()) {
            return;
        }
        
        bookRenderer.setKey(hist.previous());
        reload();
    }

    public void goNext() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasNext()) {
            return;
        }
        
        bookRenderer.setKey(hist.next());
        reload();
    }
    
    /** 
     * For single book viewer, this method do nothing 
     * {@inheritDoc}
     */
    @Override
    public void compareView(boolean compare) {
        // do nothing
    }
}
