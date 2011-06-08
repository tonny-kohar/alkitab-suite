/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.DailyDevotionsTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.DailyDevotionsTopComponent DailyDevotionsTopComponent}.
 */
@ActionID(id = "kiyut.alkitab.actions.DailyDevotionsAction", category = "Window")
@ActionRegistration(displayName = "#CTL_DailyDevotionsAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 130),
    @ActionReference(path = "Shortcuts", name = "DO-4")
})
public class DailyDevotionsAction extends AbstractAction {   

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = DailyDevotionsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
