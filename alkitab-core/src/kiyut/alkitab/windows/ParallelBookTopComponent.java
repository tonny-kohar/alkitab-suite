/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import kiyut.alkitab.actions.Expand1Action;
import kiyut.alkitab.actions.Expand5Action;
import kiyut.alkitab.actions.FocusPassageComponentAction;
import kiyut.alkitab.actions.FocusSearchComponentAction;
import kiyut.alkitab.actions.GoBackAction;
import kiyut.alkitab.actions.GoForwardAction;
import kiyut.alkitab.actions.GoNextAction;
import kiyut.alkitab.actions.GoPreviousAction;
import kiyut.alkitab.actions.ReloadAction;
import kiyut.alkitab.actions.ViewerHintsAction;
import kiyut.alkitab.bookviewer.BookToolTipFactory;
import kiyut.alkitab.bookviewer.BookViewer;
import kiyut.alkitab.bookviewer.BookViewerManager;
import kiyut.alkitab.bookviewer.BookViewerNode;
import kiyut.alkitab.bookviewer.ParallelBookViewerPane;
import kiyut.alkitab.bookviewer.SwordURI;
import kiyut.alkitab.bookviewer.ViewerHints;
import kiyut.alkitab.bookviewer.ViewerHintsPane;
import kiyut.alkitab.history.History;
import kiyut.alkitab.history.HistoryManager;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.options.ViewerHintsOptions;
import kiyut.alkitab.util.ComponentOrientationSupport;
import kiyut.alkitab.util.Indexer;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookFilters;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.RestrictionType;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * TopComponent which displays {@link kiyut.alkitab.bookviewer.ParallelBookViewerPane ParallelBookViewerPane}.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@TopComponent.Description(preferredID = "ParallelBookTopComponent",
    //iconBase="SET/PATH/TO/ICON/HERE", 
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
public class ParallelBookTopComponent extends BookViewerTopComponent {

    /** path to the icon used by the component and its open action */
    //static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    //private static final String PREFERRED_ID = "ParallelBookTopComponent";
    private transient ParallelBookViewerPane bookViewer;
    private transient Point linkToolTipLocation;
    private transient Timer linkToolTipTimer;
    private transient SwordURI linkToolTipSwordURI;
    private transient boolean linkToolTipForceVisible;

    private transient Action goBackDelegateAction;
    private transient Action goForwardDelegateAction;
    private transient Action goPreviousDelegateAction;
    private transient Action goNextDelegateAction;

    public ParallelBookTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ParallelBookTopComponent.class, "CTL_ParallelBookTopComponent"));
        setToolTipText(NbBundle.getMessage(ParallelBookTopComponent.class, "HINT_ParallelBookTopComponent"));
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

    /*@Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ONLY_OPENED;
    }*/

    /*@Override
    protected String preferredID() {
        return PREFERRED_ID;
    }*/
       
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        
        List<Book> books = bookViewer.getBooks();
        List<String> bookNames = new ArrayList<>(books.size());
        for (int i = 0; i < books.size(); i++) {
            bookNames.add(books.get(i).getInitials());
        }
        
        out.writeObject(bookNames);
        out.writeObject(bookViewer.getKey());
        out.writeObject(bookViewer.getSearchString());
        out.writeBoolean(bookViewer.isCompareView());
        out.writeObject(bookViewer.getViewerHints());
        out.writeObject(bookViewer.getName());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        
        List<String> bookNames = (List<String>)in.readObject();
        Key key = (Key)in.readObject();
        String searchString = (String)in.readObject();
        boolean compareView = in.readBoolean();
        ViewerHints<ViewerHints.Key,Object> viewerHints = (ViewerHints<ViewerHints.Key,Object>)in.readObject();
        String name = (String)in.readObject();
        
        viewerHints.setDefaults(ViewerHintsOptions.getInstance().getViewerHints());
        bookViewer.setViewerHints(viewerHints);
        bookViewer.setKey(key);
        bookViewer.setSearchString(searchString);
        bookViewer.setName(name);
        
        for (int i = 0; i < bookNames.size(); i++) {
            bookViewer.addBook(bookNames.get(i));
        }
        
        bookViewer.compareView(compareView);
        
        WindowManager.getDefault().invokeWhenUIReady(() -> {
            bookViewer.reload();
        });
    }
    
    @Override
    public void componentClosed() {
        if (bookViewer != null) {
            bookViewer.dispose();
        }
        super.componentClosed();
    }

    @Override
    public javax.swing.Action[] getActions() {
        List<Action> actionList = new ArrayList<>();
        
        // add 
        actionList.addAll(Arrays.asList(super.getActions()));
        actionList.add(2, new ReindexAction());
        
        return actionList.toArray(new Action[0]);
    }
    
    /** If you override this, please make sure to call super.initCustom() */
    protected void initCustom() {
        bookViewer = new ParallelBookViewerPane() {
            @Override
            public void reload() {
                super.reload();
                updateHistoryAction();
            }
        };
        add(BorderLayout.CENTER, (JComponent) bookViewer);

        bookViewerNode = new BookViewerNode(bookViewer);

        bookViewer.addPropertyChangeListener(BookViewer.VIEWER_NAME, (PropertyChangeEvent evt) -> {
            String name1 = (String) evt.getNewValue();
            setName(name1);
            setToolTipText(name1);
        });

        bookViewer.addHyperlinkListener((HyperlinkEvent evt) -> {
            ParallelBookTopComponent.this.hyperlinkUpdate(evt);
        });
        
        linkToolTipLocation = new Point();

        linkToolTipTimer = new Timer(500, (ActionEvent evt) -> {
            showToolTip(linkToolTipSwordURI);
        });

        linkToolTipTimer.setRepeats(false);
        linkToolTipTimer.setCoalesce(true);
        linkToolTipForceVisible = false;

        bookViewer.getBookRenderer().getComponent().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                linkToolTipLocation.setLocation(evt.getX(), evt.getY());

                int onMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
                if ((evt.getModifiersEx()& onMask) == onMask) {
                    linkToolTipForceVisible = true;
                } else {
                    hideToolTip();
                }
            }
        });

        bookViewer.getBookRenderer().getComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isMiddleMouseButton(evt)) {
                    linkToolTipForceVisible = true;

                    MouseEvent redirEvt = new MouseEvent(evt.getComponent(),
                                MouseEvent.MOUSE_CLICKED,
                                evt.getWhen(),
                                evt.getModifiersEx(),
                                evt.getX(),
                                evt.getY(),
                                evt.getClickCount(),
                                evt.isPopupTrigger(),
                                MouseEvent.BUTTON1);
                    
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(redirEvt);
                }
            }
        });

        ActionMap actionMap = getActionMap();
        //bookViewer.getActionMap().setParent(actionMap);
        
        CallbackSystemAction goBackAction = SystemAction.get(GoBackAction.class);
        CallbackSystemAction goForwardAction = SystemAction.get(GoForwardAction.class);
        CallbackSystemAction goPreviousAction = SystemAction.get(GoPreviousAction.class);
        CallbackSystemAction goNextAction = SystemAction.get(GoNextAction.class);
        CallbackSystemAction expand1Action = SystemAction.get(Expand1Action.class);
        CallbackSystemAction expand5Action = SystemAction.get(Expand5Action.class);
        CallbackSystemAction reloadAction = SystemAction.get(ReloadAction.class);
        CallbackSystemAction viewerHintsAction = SystemAction.get(ViewerHintsAction.class);
        CallbackSystemAction focusPassageComponentAction = SystemAction.get(FocusPassageComponentAction.class);
        CallbackSystemAction focusSearchComponentAction = SystemAction.get(FocusSearchComponentAction.class);

        goBackDelegateAction = new GoBackDelegateAction();
        goForwardDelegateAction = new GoForwardDelegateAction();
        goPreviousDelegateAction = new GoPreviousDelegateAction();
        goNextDelegateAction = new GoNextDelegateAction();

        actionMap.put(goBackAction.getActionMapKey(), goBackDelegateAction);
        actionMap.put(goForwardAction.getActionMapKey(), goForwardDelegateAction);
        actionMap.put(goPreviousAction.getActionMapKey(), goPreviousDelegateAction);
        actionMap.put(goNextAction.getActionMapKey(), goNextDelegateAction);
        actionMap.put(expand1Action.getActionMapKey(), new Expand1DelegateAction());
        actionMap.put(expand5Action.getActionMapKey(), new Expand5DelegateAction());
        actionMap.put(reloadAction.getActionMapKey(), new ReloadDelegateAction());
        actionMap.put(viewerHintsAction.getActionMapKey(), new ViewerHintsDelegateAction());
        actionMap.put(focusPassageComponentAction.getActionMapKey(), new FocusPassageComponentDelegateAction());
        actionMap.put(focusSearchComponentAction.getActionMapKey(), new FocusSearchComponentDelegateAction());

        // XXX workaround for HTMLEditorKit keybinding for CTRL-T
        /*Keymap gKeymap = Lookup.getDefault().lookup(Keymap.class);
        JTextComponent textComponent = (JTextComponent)bookViewer.getBookRenderer();
        textComponent.getKeymap().setResolveParent(gKeymap);
        */
    }
    
    @Override
    public void openURI(SwordURI uri, String info) {
        bookViewer.openURI(uri, info);
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
            Logger logger = Logger.getLogger(ParallelBookTopComponent.class.getName());
            logger.log(Level.WARNING, "invalid SwordURI: {0}", uri);
        }

        if (eventType.equals(HyperlinkEvent.EventType.ACTIVATED)) {
            //System.out.println("System.out.println ParallelBookTopComponent.hyperlinkUpdate ACTIVATED");
            String fragment = swordURI.getFragment();
            if (fragment.length() > 0) {
                if (fragment.charAt(0) == '#') {
                    return;
                }
            }
            // in this part linkTooltip will force to open new Tab if true
            BookViewerManager.getInstance().openURI(swordURI,null,linkToolTipForceVisible);
            linkToolTipTimer.stop();
            hideToolTip();

        } else if (eventType.equals(HyperlinkEvent.EventType.ENTERED)) {
            if (!linkToolTipForceVisible) {
                StatusDisplayer.getDefault().setStatusText(swordURI.toString());
                linkToolTipSwordURI = swordURI;
                linkToolTipTimer.restart();
                //showToolTip(swordURI);
            }
            
        } else if (eventType.equals(HyperlinkEvent.EventType.EXITED)) {
            if (!linkToolTipForceVisible) {
                linkToolTipTimer.stop();
                hideToolTip();
            }
        }
    }

    private void showToolTip(SwordURI swordURI) {
        if (swordURI == null) { return; }
        
        ViewerHints<ViewerHints.Key,Object> viewerHints = bookViewer.getViewerHints();
        Boolean val = (Boolean)viewerHints.get(ViewerHints.TOOLTIP_POPUP);
        if (val == false) {
            return;
        }

        Book book = getToolTipBook(swordURI);
        Key key = null;


        if (book != null) {
            key = book.getValidKey(swordURI.getFragment());
        }
        
        if (book == null || key == null) {
            return;
        }

        Point p = new Point(linkToolTipLocation.x, linkToolTipLocation.y);
        JComponent comp = bookViewer.getBookRenderer().getComponent();

        BookToolTipFactory.getInstance().getToolTip().show(book, key, comp, p.x, p.y);
    }

    private void hideToolTip() {
        BookToolTipFactory.getInstance().getToolTip().hide();
        linkToolTipForceVisible = false;
    }

    /** 
     * Return the book from the preferences. If not defined in preferences,
     * it will return in the following order
     * - return StrongsGreek, StrongsHebrew, Robinson <br/>
     * - return the first available book in the category list <br/>
     */
    private Book getToolTipBook(SwordURI swordURI) {
        Book book = null;

        if (swordURI == null) {
            return book;
        }

        String bookName = swordURI.getPath();

        if (bookName.equals("")) {
            switch (swordURI.getType()) {
                case BIBLE:
                    bookName = BookViewerOptions.getInstance().getDefaultBible();
                    if (bookName == null) {
                        List<?> books = Books.installed().getBooks(BookFilters.getBibles());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case GREEK_STRONGS:
                    bookName = BookViewerOptions.getInstance().getDefaultGreekStrongs();
                    if (bookName == null) {
                        List<?> books = Books.installed().getBooks(BookFilters.getGreekDefinitions());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case HEBREW_STRONGS:
                    bookName = BookViewerOptions.getInstance().getDefaultHebrewStrongs();
                    if (bookName == null) {
                        List<?> books = Books.installed().getBooks(BookFilters.getHebrewDefinitions());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case GREEK_MORPH:
                    bookName = BookViewerOptions.getInstance().getDefaultGreekMorph();
                    if (bookName == null) {
                        List<?> books = Books.installed().getBooks(BookFilters.getGreekParse());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case HEBREW_MORPH:
                    // TODO need to implement this
                    //bookName = BookViewerOptions.getInstance().getDefaultHebrewMorph();

                    List<?> books = Books.installed().getBooks(BookFilters.getHebrewParse());
                    if (!books.isEmpty()) {
                        bookName = ((Book) books.get(0)).getInitials();
                    }
                    break;
                default:
                    break;
            }
        }

        if (bookName != null) {
            if (!bookName.equals("")) {
                book = Books.installed().getBook(bookName);
            }
        }

        return book;
    }
    
    /**
     * Update history related UI action state eg: setEnabled(true/false)
     * This methods is called after {@link kiyut.alkitab.api.BookViewer#reload()} called
     */
    protected void updateHistoryAction() {
        HistoryManager historyManager = bookViewer.getHistoryManager();

        goBackDelegateAction.setEnabled(historyManager.hasBack());
        goForwardDelegateAction.setEnabled(historyManager.hasForward());

        History hist = historyManager.current();
        if (hist == null) {
            goPreviousDelegateAction.setEnabled(false);
            goNextDelegateAction.setEnabled(false);
        } else {
            goPreviousDelegateAction.setEnabled(hist.hasPrevious());
            goNextDelegateAction.setEnabled(hist.hasNext());
        }
    }

    public class ReindexAction extends AbstractAction {
        public ReindexAction() {
            putValue(Action.NAME, NbBundle.getMessage(ReindexAction.class, "CTL_ReindexAction"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            BookViewer bookViewer = getBookViewer();
            if (bookViewer == null) {
                return;
            }
            
            List<Book> books = bookViewer.getBooks();
            if (!books.isEmpty()) {
                Indexer.getInstance().createIndex(books.get(0), true);
            }
        }
    }

    public class GoBackDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.goBack();
        }
    }

    public class GoForwardDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.goForward();
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

    public class Expand1DelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.blur(1,RestrictionType.NONE);
        }
    }

    public class Expand5DelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.blur(5,RestrictionType.NONE);
        }
    }

    public class ReloadDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.reload();
        }
    }

    public class ViewerHintsDelegateAction extends AbstractAction {
        public ViewerHintsDelegateAction() {
            SystemAction action = SystemAction.get(ViewerHintsAction.class);
            putValue(Action.SMALL_ICON, action.getIcon());
            putValue(Action.SHORT_DESCRIPTION, action.getName());
            
        }
        
        @Override
        public void actionPerformed(ActionEvent evt) {
            ViewerHintsPane hintsPane = new ViewerHintsPane();
            hintsPane.setViewerHints(bookViewer.getViewerHints());
            int choice = hintsPane.showDialog(ParallelBookTopComponent.this);
            if (choice != JOptionPane.OK_OPTION) {
                return;
            }
            hintsPane.updateViewerHintsValue();
            bookViewer.reload();
        }
    }

    public class FocusPassageComponentDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.requestFocusForPassageComponent();
        }
    }

    public class FocusSearchComponentDelegateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            bookViewer.requestFocusForSearchComponent();
        }
    }
}
