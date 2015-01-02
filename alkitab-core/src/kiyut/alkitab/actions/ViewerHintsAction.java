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
@ActionID(id = "kiyut.alkitab.actions.ViewerHintsAction", category = "View")
@ActionRegistration(displayName = "#CTL_ViewerHintsAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Toolbars/View", position = 100),
    @ActionReference(path = "Menu/View", position = 120)
})
public class ViewerHintsAction extends CallbackSystemAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(ViewerHintsAction.class, "CTL_ViewerHintsAction");
    }

    @Override
    protected String iconResource() {
        return "kiyut/alkitab/actions/viewer-hints.png";
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