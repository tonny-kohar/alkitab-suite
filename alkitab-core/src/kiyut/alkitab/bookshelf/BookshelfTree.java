/* This work has been placed into the public domain. */

package kiyut.alkitab.bookshelf;

import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

/**
 * JTree component which display {@code Books} arranged according to {@code groupBy}
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class BookshelfTree extends JTree {
    
    /** The model used by this component */
    protected BookshelfTreeModel booksTreeModel;
    
    public BookshelfTree() {
        booksTreeModel = new BookshelfTreeModel();
        this.setModel(booksTreeModel);
        putClientProperty("JTree.lineStyle", "Angled"); 
        setRootVisible(false);
        setShowsRootHandles(true);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setCellRenderer(new BookshelfTreeCellRenderer());
    }
    
    /** 
     * Convenience methods to set groupBy list
     * @see BookshelfTreeModel#setGroupBy(List)
     */
    public void setGroupBy(List<String> groupBy) {
        booksTreeModel.setGroupBy(groupBy);
    }
    
    /** 
     * Convenience methods to get groupBy list
     * @see BookshelfTreeModel#getGroupBy()
     */
    public List<String> getGroupBy() {
        return booksTreeModel.getGroupBy();
    }
}
