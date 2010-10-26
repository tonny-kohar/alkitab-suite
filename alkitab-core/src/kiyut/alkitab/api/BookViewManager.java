/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.util.Collection;
import java.util.Iterator;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.passage.Key;
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

    /** Synchronize view */
    private boolean synchronizeView;

    /** the synchronize Key */
    private Key synchronizeKey;

    /** Simple flag for synchronize in progress */
    private boolean synchronizeInProgress;
    
    /**
     * Returns the single instance. 
     *
     * @return The single instance.
     */
    public static BookViewManager getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    private BookViewManager() {
        
        Lookup.Result<BookViewProvider> result = Lookup.getDefault().lookupResult(BookViewProvider.class);
        bookViewProviders = result.allInstances();
        
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent evt) {
                Object obj = evt.getSource();
                if (obj == null) { return; }
                Lookup.Result<BookViewProvider> r = (Lookup.Result<BookViewProvider>)obj;
                bookViewProviders = r.allInstances();
            }
        });
        result.allInstances(); // needed to tell Nb that it is processed

        // load from options, regarding synchronizeView
        synchronizeView = BookViewerOptions.getInstance().isSynchronizeView();
    }
    
    /** Open URI with newView is false
     * @param uri {@link SwordURI} to be opened
     * @see #openURI(SwordURI,String,boolean)
     */
    public void openURI(SwordURI uri) {
        openURI(uri,null, false);
    }
    
     /** Open URI
      * @param uri {@link SwordURI} to be opened
      * @param info optional additional info eg: search term, etc
      * @param newView only Hints indicating it will be opened in new view or replace existing view
      */
    public void openURI(SwordURI uri, String info, boolean newView) {
        Iterator<? extends BookViewProvider> it = bookViewProviders.iterator();
        while (it.hasNext()) {
            it.next().openURI(uri, info, newView);
        }
    }

    public boolean isSynchronizeView() {
        return synchronizeView;
    }

    public void setSynchronizeView(boolean b) {
        this.synchronizeView = b;
        BookViewerOptions.getInstance().setSynchronizeView(this.synchronizeView);
    }

    /** Return key or null 
     * @return key or null
     */
    public Key getSynchronizeKey() {
        if (!synchronizeView) {
            return null;
        }
        return synchronizeKey;
    }

    /** Synchronize all available view to display the same key.
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

        try {
            synchronizeInProgress = true;
            Iterator<? extends BookViewProvider> it = bookViewProviders.iterator();
            while (it.hasNext()) {
                it.next().synchronizeView(key);
            }
        } finally {
            synchronizeInProgress = false;
        }
    }
}
