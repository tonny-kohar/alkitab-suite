/* This work has been placed into the public domain. */

package kiyut.alkitab.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import kiyut.alkitab.Application;
import kiyut.alkitab.api.AbstractBookViewProvider;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.windows.BookViewerTopComponent;
import kiyut.alkitab.windows.SingleBookTopComponent;
import kiyut.alkitab.windows.DailyDevotionsTopComponent;
import kiyut.alkitab.windows.DefinitionsTopComponent;
import kiyut.alkitab.windows.ParallelBookTopComponent;
import org.openide.windows.TopComponent;

/**
 * Module Core {@link kiyut.alkitab.spi.BookViewProvider BookViewProvider} implementation
 * 
 */
public final class CoreBookViewProvider extends AbstractBookViewProvider {
    
    private BookViewerTopComponent bookViewerTC;
    
    public CoreBookViewProvider() {
        TopComponent.getRegistry().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                bookViewerTopComponentPropertyChangeListener(evt);
            }
        });
    }
    
    private void bookViewerTopComponentPropertyChangeListener(PropertyChangeEvent evt) {
        Object obj = evt.getNewValue();
        if (!(obj instanceof BookViewerTopComponent)) {
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (uri == null) { 
            logger.log(Level.WARNING, "openURI(uri," + newView + "): uri is null" );
            return;
        }
        
        if (Application.isDebug()) {
            logger.log(Level.INFO, "openURI(uri," + newView + "): " + uri);
        }
        
        //System.out.println(this.getClass().getSimpleName()+ ".openURI(uri," + newView + ")");
        //System.out.println("    " + uri);
        
        switch (uri.getType()) {
            case BIBLE:
            case COMMENTARY:
                openBible(uri, newView);
                break;
            case DAILY_DEVOTION:
                openDailyDevotion(uri, newView);
                break;
            case GENERAL_BOOK:
                openGeneralBook(uri, newView);
                break;
            case DICTIONARY:
            case GLOSSARY:
            case GREEK_STRONGS:
            case HEBREW_STRONGS:
            case GREEK_MORPH:
            case HEBREW_MORPH:
                openDefinition(uri, newView);
                break;
            default:
                break;
             
        }
    }
    
    protected void openBible(final SwordURI uri, boolean newView) {
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
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                finalTC.openURI(uri);
            }
        });
    }
    
    
    protected void openDefinition(final SwordURI uri, boolean newView) {
        DefinitionsTopComponent tc = DefinitionsTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.openURI(uri);
    }
    
    protected void openDailyDevotion(final SwordURI uri, boolean newView) {
        DailyDevotionsTopComponent tc = DailyDevotionsTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.openURI(uri);
    }
    
    /** openGeneralBook. this methods ignore the value of newView. It always open new View*/
    protected void openGeneralBook(final SwordURI uri, boolean newView) {
        final SingleBookTopComponent tc = new SingleBookTopComponent();
        tc.open();
        tc.requestActive();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tc.openURI(uri);
            }
        });
    }
}
