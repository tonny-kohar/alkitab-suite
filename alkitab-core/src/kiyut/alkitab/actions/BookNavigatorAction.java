/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.windows.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.BookNavigatorTopComponent BookNavigatorTopComponent}.
 */
public class BookNavigatorAction extends AbstractAction {

    public BookNavigatorAction() {
        super(NbBundle.getMessage(BookNavigatorAction.class, "CTL_BookNavigatorAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(BookNavigatorTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = BookNavigatorTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
