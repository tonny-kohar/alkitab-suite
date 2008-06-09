/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DefaultKeyTreeCellRenderer extends DefaultTreeCellRenderer {
    
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    
    public DefaultKeyTreeCellRenderer() {
        try {
            setOpenIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Open"))));
            setClosedIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Closed"))));
            setLeafIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Leaf"))));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
}
