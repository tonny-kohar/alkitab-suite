/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.util.Collection;
import java.util.Iterator;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
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
    static {
        instance = new BookToolTipFactory();
    }
    
    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static BookToolTipFactory getInstance() {
        return instance;
    }
    
    private BookToolTip toolTip = null;
    
    private BookToolTipFactory() {
        Lookup.Result<BookToolTip> result = Lookups.forPath("Alkitab/BookToolTip").lookupResult(BookToolTip.class);
        setBookToolTip(result.allInstances()); // needed to tell Nb that it is processed
        
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent evt) {
                Object obj = evt.getSource();
                if (obj == null) { return; }
                Lookup.Result<BookToolTip> r = (Lookup.Result<BookToolTip>)obj;
                setBookToolTip(r.allInstances());
            }
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
