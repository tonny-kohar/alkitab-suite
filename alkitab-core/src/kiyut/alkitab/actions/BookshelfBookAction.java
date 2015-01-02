/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.Book;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.CallableSystemAction;

/**
 * Abstract class for BookshelfBookAction
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public abstract class BookshelfBookAction extends CallableSystemAction {
    
    protected BookshelfTopComponent bookshelfTopComponent;
    protected Lookup.Result<Book> bookLookupResult;
    
    public BookshelfBookAction() {
        bookshelfTopComponent = BookshelfTopComponent.findInstance();
        
        Lookup lookup = bookshelfTopComponent.getLookup();
        bookLookupResult = lookup.lookupResult(Book.class);
        bookLookupResult.allItems();  // THIS IS IMPORTANT
        bookLookupResult.addLookupListener(new LookupListener(){   
            @Override
            public void resultChanged(LookupEvent evt){
                bookLookupResultChanged(evt);
            }
        }); 
    }
    
    
    /** 
     * Overrides this, if you need.
     * This implementation is empty (do nothing)
     * @param evt event describing the change
     */
    protected void bookLookupResultChanged(LookupEvent evt) {
       // do nothing
    }
    
    /** Overidden to return false
     * {@inheritDoc}
     */
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /** Overidden to return HelpCtx.DEFAULT_HELP
     * {@inheritDoc} 
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
