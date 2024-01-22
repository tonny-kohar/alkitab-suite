/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 * Implementation of Global History Clear All Action
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.GlobalHistoryClearAllAction", category = "History")
@ActionRegistration(displayName = "#CTL_GlobalHistoryClearAllAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Alkitab/GlobalHistory/PopupMenu", position = 20)
})
public class GlobalHistoryClearAllAction extends GlobalHistoryAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(GlobalHistoryClearAllAction.class, "CTL_GlobalHistoryClearAllAction");
    }
    
    @Override
    public void performAction() {
        SwingUtilities.invokeLater(() -> {
            globalHistoryTopComponent.clearAll();
        });
    }
}
