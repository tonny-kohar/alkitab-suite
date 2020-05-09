/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.util.Collection;
import java.util.Iterator;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.passage.Key;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.lookup.Lookups;

/**
 * Simple API which manage the user interaction by passing around the {@link SwordURI}
 * 
 * You can register your implementation by using @ServiceProvider annotation 
 * with path=&quot;Alkitab/BookViewProvider&quot;
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public final class BookViewerManager {

    /** The single instance */
    private static final BookViewerManager instance;
    static {
        instance = new BookViewerManager();
    }
    
    private BookViewerProvider bookViewerProvider;
    
    /** Synchronize view */
    private boolean synchronizeView;

    /** the synchronize Key */
    private Key synchronizeKey;

    /** Simple flag for synchronize in progress */
    private boolean synchronizeInProgress;
    
    /**
     * Returns the single instance.      *
     * @return The single instance.
     */
    public static BookViewerManager getInstance() {
        return instance;
    }

    private BookViewerManager() {
        
        final Lookup.Result<BookViewerProvider> result = Lookups.forPath("Alkitab/BookViewerProvider").lookupResult(BookViewerProvider.class);
        //Lookup.Result<BookViewProvider> result = Lookup.getDefault().lookupResult(BookViewerProvider.class);
        setBookViewerProvider(result.allInstances()); // needed to tell Nb that it is processed
        
        result.addLookupListener((LookupEvent evt) -> {
            Collection<? extends BookViewerProvider> c = result.allInstances();
            setBookViewerProvider(c);
        });

        // load from options, regarding synchronizeView
        synchronizeView = BookViewerOptions.getInstance().isSynchronizeView();
    }
    
    private void setBookViewerProvider(Collection<? extends BookViewerProvider> bookViewProviders) {
        Iterator<? extends BookViewerProvider> it = bookViewProviders.iterator();
        if (it.hasNext()) {
            bookViewerProvider = it.next();
        } else {
            bookViewerProvider = null;
        }
    }
    
    /** 
     * Open URI with newView is false
     * @param uri {@link SwordURI} to be opened
     * @see #openURI(SwordURI,String,boolean)
     */
    public void openURI(SwordURI uri) {
        openURI(uri,null, false);
    }
    
     /** 
      * Open URI
      * @param uri {@link SwordURI} to be opened
      * @param info optional additional info eg: search term, etc
      * @param newView only Hints indicating it will be opened in new view or replace existing view
      */
    public void openURI(SwordURI uri, String info, boolean newView) {
        if (bookViewerProvider == null) { return;}
        bookViewerProvider.openURI(uri,info,newView);
    }

    public boolean isSynchronizeView() {
        return synchronizeView;
    }

    public void setSynchronizeView(boolean b) {
        this.synchronizeView = b;
        BookViewerOptions.getInstance().setSynchronizeView(this.synchronizeView);
    }

    /** 
     * Return key or null 
     * @return key or null
     */
    public Key getSynchronizeKey() {
        if (!synchronizeView) {
            return null;
        }
        return synchronizeKey;
    }

    /** 
     * Synchronize all available view to display the same key.
     * The real process is depend on the isSynchronizeView(),
     * if it is false do nothing
     * @param key the Key to be displayed in all available view
     * @see #isSynchronizeView()
     */
    public void synchronizeView(Key key) {
        if (!synchronizeView) {
            this.synchronizeKey = null;
            return;
        }

        if (synchronizeInProgress) {
            return;
        }

        this.synchronizeKey = key;
        if (bookViewerProvider == null) { return;}
        
        try {
            synchronizeInProgress = true;
            bookViewerProvider.synchronizeView(key);
        } finally {
            synchronizeInProgress = false;
        }
    }
}
