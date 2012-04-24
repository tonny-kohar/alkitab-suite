/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import kiyut.alkitab.bookshelf.BookProperties;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.Book;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Implementation of Bookshelf Book Properties Action
 * 
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfBookPropertiesAction", category = "Bookshelf")
@ActionRegistration(displayName = "#CTL_BookshelfBookPropertiesAction")
@ActionReferences({
    @ActionReference(path = "Alkitab/Bookshelf/PopupMenu", position = 40)
})
public class BookshelfBookPropertiesAction extends AbstractAction {

    public BookshelfBookPropertiesAction() {
        super(NbBundle.getMessage(BookshelfBookPropertiesAction.class, "CTL_BookshelfBookPropertiesAction"));
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showPropertiesDialog();
            }
        });
    }
    
    protected void showPropertiesDialog() {
        BookshelfTopComponent tc = BookshelfTopComponent.findInstance();
        Book book = tc.getSelectedBook();
        if (book == null) {
            return;
        }
        
        BookProperties propsPane = new BookProperties();
        propsPane.setBook(book);
        
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        
        JOptionPane.showMessageDialog(mainWindow,propsPane,propsPane.getName(),JOptionPane.PLAIN_MESSAGE);
    }
}
