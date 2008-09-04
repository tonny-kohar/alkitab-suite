/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


public final class BookViewerAction extends AbstractAction {
    
    public BookViewerAction() {
        super(NbBundle.getMessage(BookViewerAction.class, "CTL_BookViewerAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode == null) { return; }
        TopComponent tc = mode.getSelectedTopComponent();
        if (tc != null) {
            tc.requestActive();
        }
    }
}
