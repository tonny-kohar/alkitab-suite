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


public final class NewTabAction extends AbstractAction {
    
    public NewTabAction() {
        super(NbBundle.getMessage(NewTabAction.class, "CTL_NewTabAction"));
        
        String iconPath = NbBundle.getMessage(NewTabAction.class, "ICON_NewTabAction");
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(iconPath, true)));
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        String name = null;
        Book book =  Books.installed().getBook(BookViewerOptions.getInstance().getDefaultBible());
        if (book != null) {
            name = book.getInitials();
        }

        SwordURI uri = SwordURI.createURI(SwordURI.BIBLE_SCHEME, name, null);
     
        BookViewManager.getInstance().openURI(uri, null, true);
    }
}
