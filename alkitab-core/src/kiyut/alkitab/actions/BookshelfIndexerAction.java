/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.util.Collection;
import javax.swing.SwingUtilities;
import kiyut.alkitab.util.Indexer;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;

/**
 * Implementation of Bookshelf Reindex Action
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfIndexerAction", category = "Bookshelf")
@ActionRegistration(displayName = "#CTL_BookshelfIndexerAction" , lazy = false)
@ActionReferences({
    @ActionReference(path = "Alkitab/Bookshelf/PopupMenu", position = 30)
})
public class BookshelfIndexerAction extends BookshelfBookAction {
    
    protected Book book;
    
    @Override
    public String getName() {
        return NbBundle.getMessage(BookshelfIndexerAction.class, "CTL_BookshelfIndexerAction");
    }

    @Override
    protected void bookLookupResultChanged(LookupEvent evt) {
        boolean b = false;
        book = null;
        
        Collection c = bookLookupResult.allInstances();
        if (!c.isEmpty()) {
            book = (Book)c.iterator().next();
            b = allowCreateIndex(book);
        }        
        
        setEnabled(b);
    }
    
    @Override
    public void performAction() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                doCreateIndex();
            }
        });
    }
    
    protected void doCreateIndex() {
        if (book == null) {
            return;
        }
        
        if (!allowCreateIndex(book)) {
            return;
        }
      
        Indexer.getInstance().createIndex(book, true);
    }
    
    protected boolean allowCreateIndex(Book book) {
        if (book.getBookCategory().equals(BookCategory.BIBLE) || book.getBookCategory().equals(BookCategory.COMMENTARY)) {
            return true;
        }
        return false;
    }
}
