/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.api.BookViewManager;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.BooleanStateAction;

/**
 * Action which could change the state of Synchonize View
 */
public class SynchronizeViewAction extends BooleanStateAction  {

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
