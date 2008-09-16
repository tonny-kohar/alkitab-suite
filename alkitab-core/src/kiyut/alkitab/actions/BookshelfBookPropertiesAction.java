/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import kiyut.alkitab.swing.BookProperties;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.Book;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Implementation of Bookshelf Book Properties Action
 * 
 */
public class BookshelfBookPropertiesAction extends AbstractAction {

    public BookshelfBookPropertiesAction() {
        super(NbBundle.getMessage(BookshelfBookPropertiesAction.class, "CTL_BookshelfBookPropertiesAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showPropertiesDialog();
            }
        });
    }
    
    protected void showPropertiesDialog() {
        BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
        Book book = bs.getSelectedBook();
        if (book == null) {
            return;
        }
        
        BookProperties propsPane = new BookProperties();
        propsPane.setBook(book);
        
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        
        JOptionPane.showMessageDialog(mainWindow,propsPane,propsPane.getName(),JOptionPane.PLAIN_MESSAGE);
    }
}
