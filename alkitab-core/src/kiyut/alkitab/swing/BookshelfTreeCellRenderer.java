/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.Component;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.crosswire.jsword.book.Book;

public class BookshelfTreeCellRenderer extends DefaultTreeCellRenderer {
    
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    
    protected Icon bookOpenIcon;
    protected Icon bookCloseIcon;
    protected Icon bookTopicIcon;
    
    public BookshelfTreeCellRenderer() {
        try {
            setOpenIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Open"))));
            setClosedIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Closed"))));
            setLeafIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Leaf"))));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
        Object obj = treeNode.getUserObject();
        
        if (obj instanceof Book) {
            Book book = (Book)obj;
            value = book.getInitials() + " - " + book.getName();
        } 
        
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        return this;
    }
}
