/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import kiyut.alkitab.api.BookViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.api.History;
import kiyut.alkitab.api.HistoryManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.TransformerHints;
import kiyut.alkitab.api.event.BookChangeEvent;
import kiyut.alkitab.options.TransformerHintsOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.passage.Key;

/**
 * Implementation of {@link kiyut.alkitab.api.BookViewer BookViewer} which display single book
 *
 */
public class SingleBookViewerPane extends AbstractBookViewerPane {

    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    protected BookTextPane bookTextPane;
    protected HistoryManager historyManager;
    
    
    /** Creates new SingleBookViewerPane */
    public SingleBookViewerPane() {
        this.transformerHints = new TransformerHints<TransformerHints.Key, Object>(TransformerHintsOptions.getInstance().getTransformerHints());
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

        jToolBar1 = new javax.swing.JToolBar();
        goPreviousButton = new javax.swing.JButton();
        goNextButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        bookScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        goPreviousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/previous.png"))); // NOI18N
        goPreviousButton.setToolTipText(bundle.getString("HINT_GoPrevious.Text")); // NOI18N
        goPreviousButton.setFocusable(false);
        goPreviousButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goPreviousButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(goPreviousButton);

        goNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/next.png"))); // NOI18N
        goNextButton.setToolTipText(bundle.getString("HINT_GoNext.Text")); // NOI18N
        goNextButton.setFocusable(false);
        goNextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goNextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(goNextButton);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/refresh.png"))); // NOI18N
        refreshButton.setToolTipText(bundle.getString("HINT_Refresh.Text")); // NOI18N
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(refreshButton);

        add(jToolBar1, java.awt.BorderLayout.NORTH);
        add(bookScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane bookScrollPane;
    private javax.swing.JButton goNextButton;
    private javax.swing.JButton goPreviousButton;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

    protected void initCustom() {
        bookTextPane = new BookTextPane(transformerHints);
        bookScrollPane.setViewportView(bookTextPane);
        historyManager = new BookViewerHistoryManager();

        //getActionMap().setParent(bookTextPane.getActionMap());

        goPreviousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                goPrevious();
            }
        });

        goNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                goNext();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bookTextPane.refresh(true);
            }
        });
    }

    @Override
    public String getName() {
        if (bookTextPane == null) {
            return "";
        }

        List<Book> books = bookTextPane.getBooks();

        if (books.isEmpty()) {
            return "";
        }

        Book book = books.get(0);
        if (book == null) {
            return "";
        }

        return book.getInitials();
    }
    
    public JComponent getViewerComponent() {
        return bookTextPane;
    }

    public void addHyperlinkListener(HyperlinkListener listener) {
        bookTextPane.addHyperlinkListener(listener);
    }

    public void removeHyperlinkListener(HyperlinkListener listener) {
        bookTextPane.removeHyperlinkListener(listener);
    }

    public List<Book> getBooks() {
        List<Book> srcBooks = bookTextPane.getBooks();
        return Collections.unmodifiableList(srcBooks);
    }

    public int getBookCount() {
        return bookTextPane.getBooks().size();
    }
    
    public void viewSource() {
        SourceCodePane sourcePane = new SourceCodePane();
        sourcePane.setText(bookTextPane.getRawText(), bookTextPane.getOSISText(), bookTextPane.getHTMLText());
        sourcePane.showDialog(this,true);
    }

    public void openURI(SwordURI uri) {
        List<Book> books = bookTextPane.getBooks();
        Book book = Books.installed().getBook(uri.getPath());

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
            History hist = new BookViewerHistory(tKey);
            historyManager.add(hist);
            bookTextPane.setKey(hist.current());
            refresh();
        }
    }

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
        
        bookTextPane.setKey(keyHist);
    }

    public Key getKey() {
        return bookTextPane.getKey();
    }

    public void refresh() {
        bookTextPane.refresh(true);
        updateHistoryUI();
    }

    public void goPrevious() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasPrevious()) {
            return;
        }
        
        bookTextPane.setKey(hist.previous());
        refresh();
    }

    public void goNext() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasNext()) {
            return;
        }
        
        bookTextPane.setKey(hist.next());
        refresh();
    }

    protected void updateHistoryUI() {
        History hist = historyManager.current();
        if (hist == null) {
            goPreviousButton.setEnabled(false);
            goNextButton.setEnabled(false);
        } else {
            goPreviousButton.setEnabled(hist.hasPrevious());
            goNextButton.setEnabled(hist.hasNext());
        }
    }
    
    /** For single book viewer, this method do nothing 
     * {@inheritDoc}
     */
    public void compareView(boolean compare) {
        // do nothing
    }
}
