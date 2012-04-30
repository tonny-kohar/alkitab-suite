/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Font;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import kiyut.alkitab.api.BookFontStore;
import org.crosswire.common.swing.FontChooser;
import org.crosswire.jsword.book.Book;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Implementation of Bookshelf Change Font Action
 * 
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfChangeFontAction", category = "Bookshelf")
@ActionRegistration(displayName = "#CTL_BookshelfChangeFontAction")
@ActionReferences({
    @ActionReference(path = "Alkitab/Bookshelf/PopupMenu", position = 20)
})
public class BookshelfChangeFontAction extends BookshelfBookAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(BookshelfChangeFontAction.class, "CTL_BookshelfChangeFontAction");
    }

    @Override
    public void performAction() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                doChangeFont();
            }
        });
    }

    protected void doChangeFont() {
        Book book = bookshelfTopComponent.getSelectedBook();
        if (book == null) {
            return;
        }

        Frame mainWindow = WindowManager.getDefault().getMainWindow();

        BookFontStore fontStore = BookFontStore.getInstance();

        Font font = FontChooser.showDialog(mainWindow, "Change Font", fontStore.getFont(book));
        fontStore.setFont(book, font);

        /*
        // language font is not supported yet
        Language language = getLanguage(book);
        if (language != null) {
            font = FontChooser.showDialog(mainWindow, Msg.FONT_CHOOSER.toString(), fontStore.getFont(language));
            fontStore.setFont(language, font);
        }*/
    }
}
