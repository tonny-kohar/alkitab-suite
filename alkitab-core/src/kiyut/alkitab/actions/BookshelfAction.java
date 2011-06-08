/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

/**
 * Action which shows {@link kiyut.alkitab.windows.BookshelfTopComponent BookshelfTopComponent}.
 */
@ActionID(id = "kiyut.alkitab.actions.BookshelfAction", category = "Window")
@ActionRegistration(displayName = "#CTL_BookshelfAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 100),
    @ActionReference(path = "Shortcuts", name = "DO-1")
})
public class BookshelfAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = BookshelfTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
