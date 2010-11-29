/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class GoBackAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(GoBackAction.class, "CTL_GoBackAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(GoBackAction.class, "ICON_GoBackAction");
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
