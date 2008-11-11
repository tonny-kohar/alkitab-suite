/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import kiyut.alkitab.util.ImageUtilities;
import org.crosswire.jsword.book.Book;

public class BookshelfTreeCellRenderer extends DefaultTreeCellRenderer {
    
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    
    //protected Icon bookOpenIcon;
    //protected Icon bookCloseIcon;
    //protected Icon bookTopicIcon;
    protected Icon bookLockedIcon;
    
    public BookshelfTreeCellRenderer() {
        try {
            setOpenIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Open"))));
            setClosedIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Closed"))));
            setLeafIcon(new ImageIcon(getClass().getResource(bundle.getString("ICON_Leaf"))));
            
            // locked icon
            BufferedImage bookImage = ImageIO.read(getClass().getResource(bundle.getString("ICON_Leaf")));
            BufferedImage lockedImage = ImageIO.read(getClass().getResource(bundle.getString("ICON_locked")));
            bookLockedIcon = new ImageIcon(ImageUtilities.createCompositeImage(bookImage, lockedImage));
            
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

        if (obj instanceof Book) {
            Book book = (Book)obj;
            if (book.isLocked() && bookLockedIcon != null) {
                setIcon(bookLockedIcon);
            }
        }
        
        return this;
    }
}
