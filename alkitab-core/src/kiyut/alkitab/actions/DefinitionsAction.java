/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.DefinitionsTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.DefinitionsTopComponent DefinitionsTopComponent}.
 */
@ActionID(id = "kiyut.alkitab.actions.DefinitionsAction", category = "Window")
@ActionRegistration(displayName = "#CTL_DefinitionsAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 140),
    @ActionReference(path = "Shortcuts", name = "DO-5")
})
public class DefinitionsAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = DefinitionsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
