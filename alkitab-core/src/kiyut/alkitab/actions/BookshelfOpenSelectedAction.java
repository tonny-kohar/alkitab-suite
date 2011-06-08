/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author tonny
 */
public class BookshelfOpenSelectedAction extends AbstractAction {

    public BookshelfOpenSelectedAction() {
        super(NbBundle.getMessage(BookshelfOpenSelectedAction.class, "CTL_BookshelfOpenSelectedAction"));
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
                 bs.openSelectedBook();
            }
        });
    }
}
