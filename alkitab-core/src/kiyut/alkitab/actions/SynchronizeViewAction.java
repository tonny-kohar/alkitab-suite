/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.api.BookViewManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.BooleanStateAction;

/**
 * Action which could change the state of Synchonize View
 */
@ActionID(id = "kiyut.alkitab.actions.SynchronizeViewAction", category = "View")
@ActionRegistration(displayName = "#CTL_SynchronizeViewAction")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 110)
})
public class SynchronizeViewAction extends BooleanStateAction  {

    @Override
    protected void initialize() {
        super.initialize();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(SynchronizeViewAction.class, "CTL_SynchronizeViewAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean getBooleanState() {
        return BookViewManager.getInstance().isSynchronizeView();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        BookViewManager.getInstance().setSynchronizeView(!BookViewManager.getInstance().isSynchronizeView());
    }
}
