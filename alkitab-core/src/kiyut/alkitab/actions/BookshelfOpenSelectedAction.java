/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.util.NbBundle;

/**
 *
 */
public class BookshelfOpenSelectedAction extends AbstractAction {

    public BookshelfOpenSelectedAction() {
        super(NbBundle.getMessage(BookshelfBookPropertiesAction.class, "CTL_BookshelfOpenSelectedAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
                 bs.openSelectedBook();
            }
        });
    }
}
