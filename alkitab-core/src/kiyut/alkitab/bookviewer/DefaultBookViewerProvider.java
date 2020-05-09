/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import kiyut.alkitab.Application;
import kiyut.alkitab.windows.BookViewerTopComponent;
import kiyut.alkitab.windows.DailyDevotionsTopComponent;
import kiyut.alkitab.windows.DefinitionsTopComponent;
import kiyut.alkitab.windows.ParallelBookTopComponent;
import kiyut.alkitab.windows.SingleBookTopComponent;
import org.crosswire.jsword.passage.Key;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Default {@link kiyut.alkitab.api.BookViewerProvider BookViewerProvider} implementation
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com> * 
 */
@ServiceProvider(service=BookViewerProvider.class, path="Alkitab/BookViewerProvider")
public final class DefaultBookViewerProvider extends AbstractBookViewerProvider {
    
    private BookViewerTopComponent bookViewerTC;
    
    public DefaultBookViewerProvider() {
        TopComponent.getRegistry().addPropertyChangeListener((PropertyChangeEvent evt) -> {
            bookViewerTopComponentPropertyChangeListener(evt);
        });
    }
    
    private void bookViewerTopComponentPropertyChangeListener(PropertyChangeEvent evt) {
        Object obj = evt.getNewValue();
        if (!(obj instanceof ParallelBookTopComponent)) {
            return;
        }
        
        String propName = evt.getPropertyName();
        if (!(propName.equals(TopComponent.Registry.PROP_ACTIVATED)
                || propName.equals(TopComponent.Registry.PROP_TC_CLOSED))) {
            return;
        }
        
        BookViewerTopComponent tc = (BookViewerTopComponent)obj;
        
        if (propName.equals(TopComponent.Registry.PROP_TC_CLOSED)) {
            if (tc.equals(bookViewerTC)) {
                bookViewerTC = null;
            }
            return;
        }
        
        // if up to here, it means it is activated 
        bookViewerTC = tc;
    }
    
    @Override
    public void openURI(SwordURI uri, boolean newView) {
        openURI(uri,null,newView);
    }

    @Override
    public void openURI(SwordURI uri, String info, boolean newView) {
    
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (uri == null) { 
            logger.log(Level.WARNING, "openURI(uri,{0}): uri is null", newView);
            return;
        }
        
        if (Application.isDebug()) {
            logger.log(Level.INFO, "openURI(uri,{0}): {1}", new Object[]{newView, uri});
        }
        
        //System.out.println(this.getClass().getSimpleName()+ ".openURI(uri," + newView + ")");
        //System.out.println("    " + uri);
        
        switch (uri.getType()) {
            case BIBLE:
            case COMMENTARY:
                openBible(uri, info, newView);
                break;
            case DAILY_DEVOTION:
                openDailyDevotion(uri, info, newView);
                break;
            case GENERAL_BOOK:
            case MAPS:
                openGeneralBook(uri, info, newView);
                break;
            case DICTIONARY:
            case GLOSSARY:
            case GREEK_STRONGS:
            case HEBREW_STRONGS:
            case GREEK_MORPH:
            case HEBREW_MORPH:
                openDefinition(uri, info, newView);
                break;
            default:
                break;
             
        }
    }
    
    protected void openBible(final SwordURI uri, final String info, final boolean newView) {
        BookViewerTopComponent tc = null;
        if (bookViewerTC != null) {
            tc = bookViewerTC;
        }

        if (newView || tc == null) {
            tc = new ParallelBookTopComponent();
        }

        tc.open();
        tc.requestActive();
        
        final BookViewerTopComponent finalTC = tc;
        
        SwingUtilities.invokeLater(() -> {
            finalTC.openURI(uri, info);
        });
    }
    
    
    protected void openDefinition(final SwordURI uri, final String info, final boolean newView) {
        TopComponent obj = WindowManager.getDefault().findTopComponent("DefinitionsTopComponent");
        if (obj instanceof DefinitionsTopComponent) {
            DefinitionsTopComponent tc = (DefinitionsTopComponent)obj;
            tc.open();
            tc.requestActive();
            tc.openURI(uri, info);
        }
    }
    
    protected void openDailyDevotion(final SwordURI uri, final String info, final boolean newView) {
        TopComponent obj = WindowManager.getDefault().findTopComponent("DailyDevotionsTopComponent");
        if (obj instanceof DailyDevotionsTopComponent) {
            DailyDevotionsTopComponent tc = (DailyDevotionsTopComponent)obj;
            tc.open();
            tc.requestActive();
            tc.openURI(uri, info);
        }
    }
    
    /** openGeneralBook. this methods ignore the value of newView. It always open new View*/
    protected void openGeneralBook(final SwordURI uri, final String info, final boolean newView) {
        final SingleBookTopComponent tc = new SingleBookTopComponent();
        tc.open();
        tc.requestActive();
        
        SwingUtilities.invokeLater(() -> {
            tc.openURI(uri, info);
        });
    }

    @Override
    public void synchronizeView(Key key) {
        Iterator<TopComponent> it = TopComponent.getRegistry().getOpened().iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj.equals(bookViewerTC)) {
                continue;
            }

            if (obj instanceof ParallelBookTopComponent) {
                ParallelBookTopComponent tc = (ParallelBookTopComponent)obj;
                tc.getBookViewer().setKey(key);
                tc.getBookViewer().reload();
            }
        }

    }
}
