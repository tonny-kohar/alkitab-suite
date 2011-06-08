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
@ActionID(id = "kiyut.alkitab.actions.ReloadAction", category = "View")
@ActionRegistration(displayName = "#CTL_ReloadAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/View", position = 0),
    @ActionReference(path = "Menu/View", position = 900),
    @ActionReference(path = "Shortcuts", name = "D-R")
})
public class ReloadAction extends CallbackSystemAction {

    @Override
    protected void initialize() {
        super.initialize();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(ReloadAction.class, "CTL_ReloadAction");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/reload.png";
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
