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
 * This action request focus on the search component of the ParallelBookTopComponent
 * 
 * @author tonny
 */
@ActionID(id = "kiyut.alkitab.actions.FocusSearchComponentAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_FocusSearchComponentAction")
@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "D-F")
})
public class FocusSearchComponentAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(FocusSearchComponentAction.class, "CTL_FocusSearchComponentAction");
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
