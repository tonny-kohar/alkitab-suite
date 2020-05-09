/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import kiyut.alkitab.bookviewer.BookViewerManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.BooleanStateAction;

/**
 * Action which could change the state of Synchronize View
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.SynchronizeViewAction", category = "View")
@ActionRegistration(displayName = "#CTL_SynchronizeViewAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 110)
})
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
        return BookViewerManager.getInstance().isSynchronizeView();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        BookViewerManager.getInstance().setSynchronizeView(!BookViewerManager.getInstance().isSynchronizeView());
    }
}
