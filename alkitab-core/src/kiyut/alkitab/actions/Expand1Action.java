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
@ActionID(id = "kiyut.alkitab.actions.Expand1Action", category = "Navigate")
@ActionRegistration(displayName = "#CTL_Expand1Action")
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 210),
    @ActionReference(path = "Menu/Navigate", position = 210)
})
public class Expand1Action extends CallbackSystemAction {

    @Override
    protected void initialize() {
        super.initialize();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(Expand1Action.class, "CTL_Expand1Action");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/expand1n.png";
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
