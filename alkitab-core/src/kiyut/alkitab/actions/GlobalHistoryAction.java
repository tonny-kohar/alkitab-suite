/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.GlobalHistoryTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.GlobalHistoryTopComponent GlobalHistoryTopComponent}.
 */
@ActionID(id = "kiyut.alkitab.actions.GlobalHistoryAction", category = "Window")
@ActionRegistration(displayName = "#CTL_HistoryAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 150),
    @ActionReference(path = "Shortcuts", name = "DS-H")
})
public class GlobalHistoryAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = GlobalHistoryTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
