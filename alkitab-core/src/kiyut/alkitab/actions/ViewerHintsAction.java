/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 *
 * @author tonny
 */
public class ViewerHintsAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(ViewerHintsAction.class, "CTL_ViewerHintsAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(ViewerHintsAction.class, "ICON_ViewerHintsAction");
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