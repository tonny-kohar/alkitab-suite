/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 * This action request focus on the search component of the ParallelBookTopComponent
 * 
 * @author tonny
 */
public final class FocusSearchComponentAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(Expand1Action.class, "CTL_FocusSearchComponentAction");
    }

    @Override
    protected String iconResource() {
        return null;
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
