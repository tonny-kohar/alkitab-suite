/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.GlobalHistoryTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.GlobalHistoryTopComponent GlobalHistoryTopComponent}.
 */
public class GlobalHistoryAction extends AbstractAction {

    public GlobalHistoryAction() {
        super(NbBundle.getMessage(GlobalHistoryAction.class, "CTL_HistoryAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(HistoryActionTopComponent.ICON_PATH, true)));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = GlobalHistoryTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
