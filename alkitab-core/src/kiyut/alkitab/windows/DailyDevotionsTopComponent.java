/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.bookviewer.DailyDevotionPane;
import kiyut.alkitab.bookviewer.DefinitionPane;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.openide.awt.StatusDisplayer;
import org.openide.awt.TabbedPaneFactory;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays {@link kiyut.alkitab.swing.DailyDevotionPane DailyDevotionPane}.
 */
public final class DailyDevotionsTopComponent extends TopComponent {

    private static DailyDevotionsTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "DailyDevotionsTopComponent";
    
    private JTabbedPane tabbedPane;
    
    private HyperlinkListener hyperlinkListener;

    private DailyDevotionsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(DailyDevotionsTopComponent.class, "CTL_DailyDevotionsTopComponent"));
        setToolTipText(NbBundle.getMessage(DailyDevotionsTopComponent.class, "HINT_DailyDevotionsTopComponent"));
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
    public static synchronized DailyDevotionsTopComponent getDefault() {
        if (instance == null) {
            instance = new DailyDevotionsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the DailyDevotionsTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized DailyDevotionsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(DailyDevotionsTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof DailyDevotionsTopComponent) {
            return (DailyDevotionsTopComponent) win;
        }
        Logger.getLogger(DailyDevotionsTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
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
            //return DailyDevotionsTopComponent.getDefault();
            final DailyDevotionsTopComponent result = DailyDevotionsTopComponent.getDefault();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // always close it at startup
                    result.close();
                }
            });
            return result;
        }
    }
    
    @Override
    public javax.swing.Action[] getActions() {
        List<Action> actionList = new ArrayList<Action>();
        
        // add 
        actionList.add(new ViewSourceAction());
        actionList.add(null);  // separator
        actionList.addAll(Arrays.asList(super.getActions()));
        
        return actionList.toArray(new Action[0]);
    }
    
    private void initCustom() {
        tabbedPane = TabbedPaneFactory.createCloseButtonTabbedPane();
        tabbedPane.addPropertyChangeListener( TabbedPaneFactory.PROP_CLOSE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JTabbedPane pane = (JTabbedPane)evt.getSource();
                Object obj = evt.getNewValue();
                if (obj == null || !(obj instanceof DailyDevotionPane)) {
                    return;
                }
                DailyDevotionPane dailyPane = (DailyDevotionPane)obj;
                dailyPane.removeHyperlinkListener(hyperlinkListener);
                pane.remove(dailyPane);
            }
        });
        this.add(BorderLayout.CENTER, tabbedPane);
        
        hyperlinkListener = new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                DailyDevotionsTopComponent.this.hyperlinkUpdate(evt);
            }
        };
        
        // this part make the loading slower
        BookViewerOptions opts = BookViewerOptions.getInstance();
        String name = opts.getDefaultDailyDevotions();
        if (name == null) { return; }
        SwordURI uri = SwordURI.createURI(SwordURI.DAILY_DEVOTION_SCHEME, name, null);
        openURI(uri, null);
    }
    
    public void openURI(SwordURI uri, String info) {
        if (uri.getType() != SwordURI.Type.DAILY_DEVOTION) { return; }
            
        Book daily =  Books.installed().getBook(uri.getPath());
        
        int index = -1;
        
        // find if it is already opened or not
        for (int i=0; i<tabbedPane.getTabCount(); i++) {
            DailyDevotionPane dailyPane = (DailyDevotionPane)tabbedPane.getComponentAt(i);
            if (dailyPane.getBook().getInitials().equals(daily.getInitials())) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            DailyDevotionPane dailyPane = new DailyDevotionPane(daily);
            dailyPane.addHyperlinkListener(hyperlinkListener);
            dailyPane.setName(daily.getInitials());
            
            // do not use addTab, there is bug for space between title and x button
            //tabbedPane.addTab(daily.getInitials() + "   ", null, dailyPane, daily.getName());
            tabbedPane.add(dailyPane);
            index = tabbedPane.getTabCount() - 1;
            tabbedPane.setToolTipTextAt(index, daily.getName());
        }
        
        tabbedPane.setSelectedIndex(index);
    }
    
    private void hyperlinkUpdate(HyperlinkEvent evt) {
        EventType eventType = evt.getEventType();
        String uri = evt.getDescription();
        SwordURI swordURI = SwordURI.createURI(uri);
        
        if (swordURI == null) {
            Logger logger = Logger.getLogger(DailyDevotionsTopComponent.class.getName());
            logger.log(Level.WARNING, "invalid SwordURI: " + uri);
            
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
    
    private class ViewSourceAction extends AbstractAction {
        public ViewSourceAction() {
            putValue(Action.NAME, NbBundle.getMessage(ViewSourceAction.class, "CTL_ViewSourceAction"));
        }

        public void actionPerformed(ActionEvent evt) {
            int i = tabbedPane.getSelectedIndex();
            if (i < 0) { return; }
            
            DefinitionPane defPane = (DefinitionPane)tabbedPane.getComponentAt(i);
            defPane.viewSource();
        }
    }
}
