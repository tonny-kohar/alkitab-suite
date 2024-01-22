/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 * Implementation of Global History Delete Action
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.GlobalHistoryDeleteAction", category = "History")
@ActionRegistration(displayName = "#CTL_GlobalHistoryDeleteAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Alkitab/GlobalHistory/PopupMenu", position = 10)
})
public class GlobalHistoryDeleteAction extends GlobalHistoryAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(GlobalHistoryClearAllAction.class, "CTL_GlobalHistoryDeleteAction");
    }
    
    @Override
    public void performAction() {
        SwingUtilities.invokeLater(() -> {
            globalHistoryTopComponent.deleteSelected();
        });
    }
}
