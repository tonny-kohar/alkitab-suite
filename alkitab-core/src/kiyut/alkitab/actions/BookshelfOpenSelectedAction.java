/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfOpenSelectedAction", category = "Bookshelf")
@ActionRegistration(displayName = "#CTL_BookshelfOpenSelectedAction" , lazy = false)
@ActionReferences({
    @ActionReference(path = "Alkitab/Bookshelf/PopupMenu", position = 10)
})
public class BookshelfOpenSelectedAction extends BookshelfBookAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(BookshelfOpenSelectedAction.class, "CTL_BookshelfOpenSelectedAction");
    }
    
    @Override
    public void performAction() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bookshelfTopComponent.openSelectedBook();                
            }
        });
    }
}
