/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.windows.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows DailyDevotions component.
 */
public class DailyDevotionsAction extends AbstractAction {

    public DailyDevotionsAction() {
        super(NbBundle.getMessage(DailyDevotionsAction.class, "CTL_DailyDevotionsAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(DailyDevotionsTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = DailyDevotionsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
