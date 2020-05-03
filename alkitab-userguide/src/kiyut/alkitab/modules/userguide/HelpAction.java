/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.userguide;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
/**
 * This action will open book help viewer.
 * 
 * @author Tonny
 */
@ActionID(id = "kiyut.alkitab.modules.userguide.HelpAction", category = "Help")
@ActionRegistration(displayName = "#CTL_HelpAction")
@ActionReferences({
    @ActionReference(path = "Menu/Help", position = 0),
    @ActionReference(path = "Shortcuts", name = "F1")
})
public class HelpAction extends AbstractAction {

    private HelpBroker helpBroker;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayHelp();
            }
        });
    }
    
    private void displayHelp() {
        if (helpBroker == null) { 
            initHelp();
        } 
        
        if (helpBroker == null) {
            return;
        }
        
        if (helpBroker.isDisplayed()) {
            // bring to front
            helpBroker.setDisplayed(true);
            return;
        }
        
        // display homeID
        helpBroker.setCurrentID("kiyut.alkitab.modules.userguide");
        helpBroker.setDisplayed(true);
        helpBroker.setViewDisplayed(true);
    }
    
    private void initHelp() {
        String helpHS = "kiyut/alkitab/modules/userguide/docs/userguide-hs.xml";
        HelpSet hs;
        ClassLoader cl = this.getClass().getClassLoader();
        //ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        //ClassLoader cl = Help.class.getClassLoader();
        try {
            URL hsURL = cl.getResource(helpHS);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING,ex.getMessage(),ex);
            return;
        }
        
        // Create a HelpBroker object:
        helpBroker = hs.createHelpBroker();
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // Default HelpBroker size is too small, make bigger unless on anciente "VGA" resolution
        if (d.width >= 1024 && d.height >= 800) {
            helpBroker.setSize(new Dimension(1024, 700));
        }
        
        helpBroker.initPresentation();
        //helpBroker.setDisplayed(true);
        //helpBroker.setViewDisplayed(true);
    }
}
