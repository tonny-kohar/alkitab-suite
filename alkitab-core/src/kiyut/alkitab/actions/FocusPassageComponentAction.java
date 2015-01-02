/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallbackSystemAction;

/**
 * This action request focus on the passage component of the ParallelBookTopComponent
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.FocusPassageComponentAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_FocusPassageComponentAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "D-L")
})
public class FocusPassageComponentAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(FocusPassageComponentAction.class, "CTL_FocusPassageComponentAction");
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
