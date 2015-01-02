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
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */

@ActionID(id = "kiyut.alkitab.actions.GoBackAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_GoBackAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 0),
    @ActionReference(path = "Menu/Navigate", position = 0),
    @ActionReference(path = "Shortcuts", name = "O-LEFT")
})
public class GoBackAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(GoBackAction.class, "CTL_GoBackAction");
    }
    
    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/back.png";
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
