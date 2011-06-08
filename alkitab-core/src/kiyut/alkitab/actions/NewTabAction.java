/* This work has been placed into the public domain. */
package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;


/**
 *
 * @author tonny
 */
@ActionID(category = "File", id = "kiyut.alkitab.actions.NewTabAction")
@ActionRegistration(displayName = "#CTL_NewTabAction",
        iconBase = "kiyut/alkitab/actions/new-view.png")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 100),
    @ActionReference(path = "Toolbars/File", position = 100),
    @ActionReference(path = "Shortcuts", name = "D-T")
})
public final class NewTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent evt) {
        String name = null;
        Book book = Books.installed().getBook(BookViewerOptions.getInstance().getDefaultBible());
        if (book != null) {
            name = book.getInitials();
        }

        SwordURI uri = SwordURI.createURI(SwordURI.BIBLE_SCHEME, name, null);

        BookViewManager.getInstance().openURI(uri, null, true);
    }
}
