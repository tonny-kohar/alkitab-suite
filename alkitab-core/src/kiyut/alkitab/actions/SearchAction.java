/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kiyut.alkitab.windows.ParallelBookTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This action request focus on the search component of the ParallelBookTopComponent
 * 
 * @author tonny
 */
public final class SearchAction extends AbstractAction {

    public SearchAction() {
        super(NbBundle.getMessage(PassageAction.class, "CTL_SearchAction"));
    }

    public void actionPerformed(ActionEvent evt) {
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode == null) { return; }
        TopComponent tc = mode.getSelectedTopComponent();
        if (tc == null) { return; }

        if (tc instanceof ParallelBookTopComponent) {
            ((ParallelBookTopComponent)tc).requestFocusForSearchComponent();
        }
    }
}
