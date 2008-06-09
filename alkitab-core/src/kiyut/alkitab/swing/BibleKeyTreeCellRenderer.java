/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.Component;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * 
 */
public class BibleKeyTreeCellRenderer extends DefaultTreeCellRenderer {

    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    
    protected Icon bookClosedIcon;
    protected Icon bookOpenIcon;
    protected Icon chapterIcon;
    protected Icon verseIcon;
    
    public BibleKeyTreeCellRenderer() {
        try {
            bookClosedIcon = new ImageIcon(getClass().getResource(bundle.getString("ICON_BookClosed")));
            bookOpenIcon = new ImageIcon(getClass().getResource(bundle.getString("ICON_BookOpen")));
            chapterIcon = new ImageIcon(getClass().getResource(bundle.getString("ICON_Chapter")));
            verseIcon = new ImageIcon(getClass().getResource(bundle.getString("ICON_Verse")));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Icon icon = bookClosedIcon;
        
        if (leaf) {
            icon = verseIcon;
        } else {
            TreePath path = tree.getPathForRow(row);
            if (path != null) {
                if (path.getPathCount() == 2 && expanded) {
                    icon = bookOpenIcon;
                } else if (path.getPathCount() == 3) {
                    icon = chapterIcon;
                }
            }
        }
        
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        if (icon != null) {
            setIcon(icon);
        }
        
        return this;
    }
}
