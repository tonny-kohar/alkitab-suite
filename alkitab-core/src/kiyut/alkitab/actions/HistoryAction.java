/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.HistoryTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.HistoryTopComponent HistoryTopComponent}.
 */
public class HistoryAction extends AbstractAction {

    public HistoryAction() {
        super(NbBundle.getMessage(HistoryAction.class, "CTL_HistoryAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(HistoryActionTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = HistoryTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
