/* This work has been placed into the public domain. */
package kiyut.alkitab.modules.userguide;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;

/**
 * This action will open book help viewer.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.modules.userguide.HelpAction", category = "Help")
@ActionRegistration(displayName = "#CTL_HelpAction")
@ActionReferences({
    @ActionReference(path = "Menu/Help", position = 0),
    @ActionReference(path = "Shortcuts", name = "F1")
})
public class HelpAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            HelpViewer help = Lookup.getDefault().lookup(HelpViewer.class);
            if (help != null) {
                help.showHelp(null);
            } 
        });
    }
}
