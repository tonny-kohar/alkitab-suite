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
@ActionID(id = "kiyut.alkitab.actions.Expand5Action", category = "Navigate")
@ActionRegistration(displayName = "#CTL_Expand5Action")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 250),
    @ActionReference(path = "Menu/Navigate", position = 250)
})
public class Expand5Action extends CallbackSystemAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(Expand5Action.class, "CTL_Expand5Action");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/expand5n.png";
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
