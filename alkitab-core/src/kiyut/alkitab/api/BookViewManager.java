/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.util.Collection;
import java.util.Iterator;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 * Simple API which manage the user interaction by passing around the {@link SwordURI}
 * 
 */
public final class BookViewManager {

    /** The single instance */
    private static BookViewManager instance;
    static {
        instance = new BookViewManager();
    }
    
    private Collection<? extends BookViewProvider> bookViewProviders;
    
    /**
     * Returns the single instance. 
     *
     * @return The single instance.
     */
    public static BookViewManager getInstance() {
        return instance;
    }
    
    private BookViewManager() {
        
        Lookup.Result<BookViewProvider> result = Lookup.getDefault().lookupResult(BookViewProvider.class);
        bookViewProviders = result.allInstances();
        
        result.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent evt) {
                Object obj = evt.getSource();
                if (obj == null) { return; }
                Lookup.Result<BookViewProvider> r = (Lookup.Result<BookViewProvider>)obj;
                bookViewProviders = r.allInstances();
            }
        });
        result.allInstances(); // needed to tell Nb that it is processed
    }
    
    /** Open URI with new view is false
     * @param uri {@link SwordURI} to be opened
     * @see #openURI(SwordURI,boolean)
     */
    public void openURI(SwordURI uri) {
        openURI(uri,false);
    }
    
     /** Open URI
     * @param uri {@link SwordURI} to be opened
     * @param newView only Hints indicating it will be opened in new view or replace existing view
     */
    public void openURI(SwordURI uri, boolean newView) {
        Iterator<? extends BookViewProvider> it = bookViewProviders.iterator();
        while (it.hasNext()) {
            it.next().openURI(uri, newView);
        }
    }
}
