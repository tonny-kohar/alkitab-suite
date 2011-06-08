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
 *
 * @author tonny
 */
@ActionID(id = "kiyut.alkitab.actions.GoNextAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_GoNextAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 110),
    @ActionReference(path = "Menu/Navigate", position = 110),
    @ActionReference(path = "Shortcuts", name = "O-PAGE_DOWN")
})
public class GoNextAction extends CallbackSystemAction {

    @Override
    protected void initialize() {
        super.initialize();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(GoNextAction.class, "CTL_GoNextAction");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/next.png";
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