/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 * This action request focus on the passage component of the ParallelBookTopComponent
 *
 * @author tonny
 */
public final class FocusPassageComponentAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(Expand1Action.class, "CTL_FocusPassageComponentAction");
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
