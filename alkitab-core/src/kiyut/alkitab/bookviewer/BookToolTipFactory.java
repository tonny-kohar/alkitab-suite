/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.util.Collection;
import java.util.Iterator;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.lookup.Lookups;

/**
 * Factory for BookToolTip,
 * 
 * You can register your implementation by using @ServiceProvider annotation 
 * with path=&quot;Alkitab/BookTooTip&quot;
 * 
 * @see BookToolTip
 */
public final class BookToolTipFactory {
    private static BookToolTipFactory instance; // The single instance
    
    /**
     * Returns the single instance
     * @return The single instance.
     */
    public synchronized static BookToolTipFactory getInstance() {
        if (instance == null) {
            instance = new BookToolTipFactory();
        }
        return instance;
    }
    
    private BookToolTip toolTip = null;
    
    private BookToolTipFactory() {
        final Lookup.Result<BookToolTip> result = Lookups.forPath("Alkitab/BookToolTip").lookupResult(BookToolTip.class);
        setBookToolTip(result.allInstances()); // needed to tell Nb that it is processed
        
        result.addLookupListener((LookupEvent evt) -> {
            Collection<? extends BookToolTip> c = result.allInstances();
            setBookToolTip(c);
        });
    }
    
    private void setBookToolTip(Collection<? extends BookToolTip> toolTips) {
        Iterator<? extends BookToolTip> it = toolTips.iterator();
        if (it.hasNext()) {
            toolTip = it.next();
        } else {
            toolTip = null;
        }
    }
    
    public BookToolTip getToolTip() {
        return toolTip;
    }
}
