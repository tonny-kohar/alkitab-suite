/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class GoPreviousAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(GoBackAction.class, "CTL_GoPreviousAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(GoBackAction.class, "ICON_GoPreviousAction");
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
