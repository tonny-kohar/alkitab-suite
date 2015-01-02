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
@ActionID(id = "kiyut.alkitab.actions.GoPreviousAction", category = "Navigate")
@ActionRegistration(displayName = "#CTL_GoPreviousAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Toolbars/Navigate", position = 100),
    @ActionReference(path = "Menu/Navigate", position = 100),
    @ActionReference(path = "Shortcuts", name = "O-PAGE_UP")
})
public class GoPreviousAction extends CallbackSystemAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(GoPreviousAction.class, "CTL_GoPreviousAction");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/previous.png";
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
