/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.DefinitionsTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.DefinitionsTopComponent DefinitionsTopComponent}.
 */
public class DefinitionsAction extends AbstractAction {

    public DefinitionsAction() {
        super(NbBundle.getMessage(DefinitionsAction.class, "CTL_DefinitionsAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(DefinitionsTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = DefinitionsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
