/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


@ActionID(id = "kiyut.alkitab.actions.BookViewerAction", category = "Window")
@ActionRegistration(displayName = "#CTL_BookViewerAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 110),
    @ActionReference(path = "Shortcuts", name = "DO-2")
})
public final class BookViewerAction extends AbstractAction {
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode == null) { return; }
        TopComponent tc = mode.getSelectedTopComponent();
        if (tc != null) {
            tc.requestActive();
        }
    }
}
