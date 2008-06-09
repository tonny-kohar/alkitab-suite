/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;


public final class NewViewAction extends CallableSystemAction {
    public void performAction() {
        String name = null;
        Book book =  Books.installed().getBook(BookViewerOptions.getInstance().getDefaultBible());
        if (book != null) {
            name = book.getInitials();
        }

        SwordURI uri = SwordURI.createURI(SwordURI.BIBLE_SCHEME, name, null);
     
        BookViewManager.getInstance().openURI(uri, true);
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(NewViewAction.class, "CTL_NewViewAction");
    }
    
    @Override
    protected String iconResource() {
        return NbBundle.getMessage(NewViewAction.class, "ICON_NewViewAction");
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
}
