/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.BookNavigatorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.BookNavigatorTopComponent BookNavigatorTopComponent}.
 */
@ActionID(id = "kiyut.alkitab.actions.BookNavigatorAction", category = "Window")
@ActionRegistration(displayName = "#CTL_BookNavigatorAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 120),
    @ActionReference(path = "Shortcuts", name = "DO-3")
})
public class BookNavigatorAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = BookNavigatorTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
