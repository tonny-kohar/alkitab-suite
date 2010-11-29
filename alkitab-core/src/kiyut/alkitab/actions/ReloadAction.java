/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class ReloadAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(ReloadAction.class, "CTL_ReloadAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(ReloadAction.class, "ICON_ReloadAction");
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
