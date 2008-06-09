/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.BookshelfTopComponent BookshelfTopComponent}.
 */
public class BookshelfAction extends AbstractAction {

    public BookshelfAction() {
        super(NbBundle.getMessage(BookshelfAction.class, "CTL_BookshelfAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(BookshelfTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = BookshelfTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
