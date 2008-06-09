/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


public final class BookViewerAction extends CallableSystemAction {
    public void performAction() {
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode == null) { return; }
        TopComponent tc = mode.getSelectedTopComponent();
        if (tc != null) {
            tc.requestActive();
        }
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(BookViewerAction.class, "CTL_BookViewerAction");
    }
    
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
