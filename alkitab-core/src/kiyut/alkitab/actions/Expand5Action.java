/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class Expand5Action extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(Expand5Action.class, "CTL_Expand5Action");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(Expand5Action.class, "ICON_Expand5Action");
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
