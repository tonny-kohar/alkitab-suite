/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import kiyut.alkitab.navigator.BookNavigatorPane;
import kiyut.alkitab.api.BookViewer;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * TopComponent which displays {@link kiyut.alkitab.navigator.BookNavigatorPane BookNavigatorPane}.
 */
public final class BookNavigatorTopComponent extends TopComponent {

    private static BookNavigatorTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "BookNavigatorTopComponent";
    
    private PropertyChangeListener tcPropertyChangeListener;
    
    private Lookup.Result result = null;
    private LookupListener bookViewerLookupListener = null;
    
    private Map<BookViewer,BookNavigatorPane> navigatorMap;
    
    private BookViewer bookViewer;

    /** For bible reuse this component */
    private BookNavigatorPane bibleNavPane;
    private boolean displayUpdated = false;


    private BookNavigatorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(BookNavigatorTopComponent.class, "CTL_BookNavigatorTopComponent"));
        setToolTipText(NbBundle.getMessage(BookNavigatorTopComponent.class, "HINT_BookNavigatorTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        
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
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized BookNavigatorTopComponent getDefault() {
        if (instance == null) {
            instance = new BookNavigatorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the BookNavigatorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized BookNavigatorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(BookNavigatorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof BookNavigatorTopComponent) {
            return (BookNavigatorTopComponent) win;
        }
        Logger.getLogger(BookNavigatorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        TopComponent.getRegistry().addPropertyChangeListener(tcPropertyChangeListener);
    }

    @Override
    public void componentActivated() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!displayUpdated) {
                    if (bookViewer == null) {
                        
                    }
                    updateDisplay();
                }
            }
        });
        
    }

    @Override
    public void componentClosed() {
        TopComponent.getRegistry().removePropertyChangeListener(tcPropertyChangeListener);
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return BookNavigatorTopComponent.getDefault();
        }
    }
    
    private void initCustom() {
        navigatorMap = new HashMap<BookViewer,BookNavigatorPane>();
        
        tcPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Object obj = evt.getNewValue();
                if (!(obj instanceof BookViewerTopComponent)) {
                    return;
                }

                String propName = evt.getPropertyName();
                if (propName.equals(TopComponent.Registry.PROP_TC_CLOSED)) {
                    BookViewerTopComponent tc = (BookViewerTopComponent)obj;
                    BookViewer bookViewer = tc.getBookViewer();
                    unregisterBookViewer(bookViewer);
                } 
            }

        };
        
        bookViewerLookupListener = new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                bookViewerLookupListenerResultChanged(lookupEvent);
            }
        };

        result = Utilities.actionsGlobalContext().lookupResult(BookViewer.class);
        result.addLookupListener(bookViewerLookupListener);
        //result.allInstances(); // needed to tell Nb that it is processed
        bookViewerLookupListenerResultChanged(new LookupEvent(result));
    }
    
    private void bookViewerLookupListenerResultChanged(LookupEvent lookupEvent) {
        Lookup.Result r = (Lookup.Result)lookupEvent.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            bookViewer = (BookViewer)c.iterator().next();
            BookNavigatorPane navPane = navigatorMap.get(bookViewer);
            if (navPane == null) {
                registerBookViewer(bookViewer);
            } else {
                updateDisplay();
            }
        } 
    }
    
    private synchronized void registerBookViewer(final BookViewer bookViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (bookViewer == null) { return; }
                registerBookViewerImpl(bookViewer);
                updateDisplay();
            }
        });
    }
    
    private synchronized void unregisterBookViewer(BookViewer bookViewer) {
        navigatorMap.remove(bookViewer);
        //System.out.println("BookNavTC.unregisterBookViewer()");
    }

    private void registerBookViewerImpl(BookViewer bookViewer) {
        if (navigatorMap.get(bookViewer) != null) {
            return;
        }

        if (bookViewer.getBooks().isEmpty()) {
            return;
        }

        Book book = bookViewer.getBooks().get(0);
        BookCategory bookCategory = book.getBookCategory();

        BookNavigatorPane navPane = null;

        if (bookCategory.equals(BookCategory.BIBLE) || bookCategory.equals(BookCategory.COMMENTARY)) {
            if (bibleNavPane == null) {
                bibleNavPane = new BookNavigatorPane();
            }
            navPane = bibleNavPane;
        } else {
            navPane = new BookNavigatorPane();
        }

        if (navigatorMap.get(bookViewer) == null) {
            navigatorMap.put(bookViewer, navPane);
            navPane.setDisplayMode(book); // build nav structure
        }
    }
    
    private void updateDisplay() {
        displayUpdated = false;

        BookNavigatorPane navPane = navigatorMap.get(bookViewer);
        if (navPane == null) {
            return;
        }

        displayUpdated = true;
        navPane.setBookViewer(bookViewer);

        this.removeAll();
        this.add(BorderLayout.CENTER,navPane);
        this.revalidate();
        this.repaint();
        
        //System.out.println("BookNavTC.updateDisplay()");
    }
}
