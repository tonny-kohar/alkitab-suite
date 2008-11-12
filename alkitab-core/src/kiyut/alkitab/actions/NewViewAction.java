/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;


public final class NewViewAction extends AbstractAction {
    
    public NewViewAction() {
        super(NbBundle.getMessage(NewViewAction.class, "CTL_NewViewAction"));
        
        String iconPath = NbBundle.getMessage(NewViewAction.class, "ICON_NewViewAction");
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(iconPath, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
        String name = null;
        Book book =  Books.installed().getBook(BookViewerOptions.getInstance().getDefaultBible());
        if (book != null) {
            name = book.getInitials();
        }

        SwordURI uri = SwordURI.createURI(SwordURI.BIBLE_SCHEME, name, null);
     
        BookViewManager.getInstance().openURI(uri, true);
    }
}
