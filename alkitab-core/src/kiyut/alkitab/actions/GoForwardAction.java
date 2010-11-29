/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class GoForwardAction extends CallbackSystemAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(GoForwardAction.class, "CTL_GoForwardAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(GoForwardAction.class, "ICON_GoForwardAction");
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
