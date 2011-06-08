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
@ActionID(id = "kiyut.alkitab.actions.GoForwardAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_GoForwardAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 10),
    @ActionReference(path = "Menu/Navigate", position = 10),
    @ActionReference(path = "Shortcuts", name = "O-RIGHT")
})
public class GoForwardAction extends CallbackSystemAction {
    
    @Override
    protected void initialize() {
        super.initialize();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(GoForwardAction.class, "CTL_GoForwardAction");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/forward.png";
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
