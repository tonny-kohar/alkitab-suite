/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Font;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import javax.swing.SwingUtilities;
import kiyut.alkitab.bookviewer.BookFontStore;
import org.crosswire.jsword.book.Book;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 * Implementation of Bookshelf Change Font Action
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfChangeFontAction", category = "Bookshelf")
@ActionRegistration(displayName = "#CTL_BookshelfChangeFontAction" , lazy = false)
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

        BookFontStore fontStore = BookFontStore.getInstance();
        
        PropertyEditor pe = PropertyEditorManager.findEditor(Font.class);
        pe.setValue(fontStore.getFont(book));
        DialogDescriptor dd = new DialogDescriptor(
                pe.getCustomEditor(),
                "Change Font" // NOI18N
                );

        DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
        if (dd.getValue() == DialogDescriptor.OK_OPTION) {
            Font font = (Font) pe.getValue();
            fontStore.setFont(book, font);
        }
    }
}
