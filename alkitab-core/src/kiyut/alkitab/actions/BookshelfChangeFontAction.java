/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import kiyut.alkitab.api.BookFontStore;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.common.swing.FontChooser;
import org.crosswire.jsword.book.Book;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Implementation of Bookshelf Change Font Action
 * 
 */
public class BookshelfChangeFontAction extends AbstractAction {

    public BookshelfChangeFontAction() {
        super(NbBundle.getMessage(BookshelfChangeFontAction.class, "CTL_BookshelfChangeFontAction"));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                doChangeFont();
            }
        });
    }

    protected void doChangeFont() {
        BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
        Book book = bs.getSelectedBook();
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
