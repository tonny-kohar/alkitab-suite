/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.actions.GoNextAction;
import kiyut.alkitab.actions.GoPreviousAction;
import kiyut.alkitab.actions.ReloadAction;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.BookViewer;
import kiyut.alkitab.api.BookViewerNode;
import kiyut.alkitab.api.History;
import kiyut.alkitab.api.HistoryManager;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.bookviewer.SingleBookViewerPane;
import kiyut.alkitab.util.ComponentOrientationSupport;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;

/**
 * TopComponent which displays {@link kiyut.alkitab.bookviewer.SingleBookViewerPane SingleBookViewerPane}.
 */
public class SingleBookTopComponent extends BookViewerTopComponent {
    
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "SingleBookTopComponent";
    
    private SingleBookViewerPane bookViewer;

    private Action goPreviousDelegateAction;
    private Action goNextDelegateAction;
    
    public SingleBookTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SingleBookTopComponent.class, "CTL_SingleBookTopComponent"));
        setToolTipText(NbBundle.getMessage(SingleBookTopComponent.class, "HINT_SingleBookTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        
        initCustom();

        ComponentOrientationSupport.applyComponentOrientation(this);
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

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ONLY_OPENED;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    /** replaces this in object stream */
    @Override
    public Object writeReplace() throws ObjectStreamException {
        return new ResolvableHelper(this);
    }
    
    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;
        
        private List<String> bookNames;
        private Key key;
        private boolean focused;
                
        public ResolvableHelper(SingleBookTopComponent tc) {
            SingleBookViewerPane bookViewer = tc.bookViewer;
            
            if (bookViewer == null) { return; }
        
            List<Book> books = bookViewer.getBooks();

            bookNames = new ArrayList<String>(books.size());
            for (int i = 0; i < books.size(); i++) {
                bookNames.add(books.get(i).getInitials());
            }

            key = bookViewer.getKey();
            this.focused = tc.isFocusOwner();
        }

        public Object readResolve() {
            final SingleBookTopComponent result = new SingleBookTopComponent();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!BookViewerOptions.getInstance().isSessionPersistence()) {
                        // just a fallback mechanism in case ModuleInstall fail to handle
                        result.close();
                        return;
                    }

                    try {
                        restoreSession(result);
                    } catch (Exception ex) {
                        Logger logger = Logger.getLogger(ParallelBookTopComponent.class.getName());
                        logger.log(Level.FINER, "Unable to restore session.\n{0}", ex.getMessage());
                        result.close();
                    }
                }
            });
            
            return result;
        }
        
        private void restoreSession(SingleBookTopComponent tc) {
            if (bookNames == null || key == null) {
                throw new IllegalStateException("bookNames or key is null");
            }

            SingleBookViewerPane bookViewer = tc.bookViewer;

            for (int i = 0; i < bookNames.size(); i++) {
                bookViewer.setBook(bookNames.get(i));
            }

            if (bookViewer.getBookCount() == 0) {
                throw new IllegalStateException("bookViewer.getBookCount() == 0");
            }

            if (key != null) {
                bookViewer.setKey(key);
            }

            bookViewer.reload();

            if (focused) {
                tc.requestActive();
            }
        }
    }
    
    /** If you override this, please make sure to call super.initCustom() */
    protected void initCustom() {
        bookViewer = new SingleBookViewerPane() {
            @Override
            public void reload() {
                super.reload();
                updateHistoryAction();
            }
        };
        add(BorderLayout.CENTER,(JComponent)bookViewer);
        
        bookViewerNode = new BookViewerNode(bookViewer);
        
        bookViewer.addPropertyChangeListener(BookViewer.VIEWER_NAME, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = (String)evt.getNewValue();
                setName(name);
                setToolTipText(name);
            }
        });
        
        bookViewer.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                SingleBookTopComponent.this.hyperlinkUpdate(evt);
                
            }
        });
        
        /*// Init action map: cut,copy,delete,paste actions.
        javax.swing.ActionMap tcActionMap = getActionMap();
        javax.swing.ActionMap bookViewerActionMap = ((JComponent)bookViewer).getActionMap();
        tcActionMap.setParent(bookViewerActionMap);
        */

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(BorderLayout.NORTH, toolBar);

        ActionMap actionMap = getActionMap();

        CallbackSystemAction goPreviousAction = SystemAction.get(GoPreviousAction.class);
        CallbackSystemAction goNextAction = SystemAction.get(GoNextAction.class);
        CallbackSystemAction reloadAction = SystemAction.get(ReloadAction.class);

        goPreviousDelegateAction = new GoPreviousDelegateAction();
        goNextDelegateAction = new GoNextDelegateAction();

        actionMap.put(goPreviousAction.getActionMapKey(), goPreviousDelegateAction);
        actionMap.put(goNextAction.getActionMapKey(), goNextDelegateAction);
        actionMap.put(reloadAction.getActionMapKey(), new ReloadDelegateAction());;

        toolBar.add(goPreviousAction.getToolbarPresenter());
        toolBar.add(goNextAction.getToolbarPresenter());
        toolBar.add(reloadAction.getToolbarPresenter());
    }
    
    @Override
    public void openURI(SwordURI uri, String info) {
        bookViewer.openURI(uri);
    }
    
    @Override
    public BookViewer getBookViewer() {
        return bookViewer;
    }
    
    private void hyperlinkUpdate(HyperlinkEvent evt) {
        EventType eventType = evt.getEventType();
        String uri = evt.getDescription();
        SwordURI swordURI = SwordURI.createURI(uri);
        
        if (swordURI == null) {
            Logger logger = Logger.getLogger(DefinitionsTopComponent.class.getName());
            logger.log(Level.WARNING, "invalid SwordURI: {0}", uri);
            
        }
        
        if (eventType.equals(HyperlinkEvent.EventType.ACTIVATED)) {
            String fragment = swordURI.getFragment();
            if (fragment.length() > 0) {
                if (fragment.charAt(0) == '#') {
                    return;
                }
            }
            
            BookViewManager.getInstance().openURI(swordURI);
        } else if (eventType.equals(HyperlinkEvent.EventType.ENTERED)) {
            //StatusDisplayer.getDefault().setStatusText(uri);
            StatusDisplayer.getDefault().setStatusText(swordURI.toString());
        }
    }

    /**
     * Update history related UI action state eg: setEnabled(true/false)
     * This methods is called after {@link kiyut.alkitab.api.BookViewer#reload()} called
     */
    protected void updateHistoryAction() {
        HistoryManager historyManager = bookViewer.getHistoryManager();

        History hist = historyManager.current();
        if (hist == null) {
            goPreviousDelegateAction.setEnabled(false);
            goNextDelegateAction.setEnabled(false);
        } else {
            goPreviousDelegateAction.setEnabled(hist.hasPrevious());
            goNextDelegateAction.setEnabled(hist.hasNext());
        }
    }

    public class GoNextDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.goNext();
        }
    }

    public class GoPreviousDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.goPrevious();
        }
    }

    public class ReloadDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.reload();
        }
    }

}
