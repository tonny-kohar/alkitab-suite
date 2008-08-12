/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookMetaData;
import org.crosswire.jsword.book.BookSet;
import org.crosswire.jsword.book.Books;

/**
 * BookshelfTreeModel which arrange the content by {@link #groupBy}
 * 
 */
public class BookshelfTreeModel extends DefaultTreeModel {
    
    /** 
     * {@code List} of {@code BookMetaData} property key to group this model.
     * By default it is ({@code BookMetaData.KEY_CATEGORY, BookMetaData.KEY_LANGUAGE}) 
     */
    protected List<String> groupBy;
    protected Object rootObject;
    
    public BookshelfTreeModel() {
        super(null);
        
        groupBy = new ArrayList<String>(2);
        groupBy.add(BookMetaData.KEY_CATEGORY);
        groupBy.add(BookMetaData.KEY_XML_LANG);
        
        /*rootObject = new String("root");
        root = new DefaultMutableTreeNode(rootObject);
        setRoot(root);
        reload(root);
         */
        reload();
    }
    
    @Override
    public void reload() {
        rootObject = new String("root");
        root = new DefaultMutableTreeNode(rootObject);
        setRoot(root);
        reload(root);
    }
    
    @Override
    public void reload(TreeNode node) {
        if (node == null) { return; }
        
        boolean load = false;
        
        DefaultMutableTreeNode mutableNode = (DefaultMutableTreeNode)node;
        Object object = mutableNode.getUserObject();
        BookSet books  = new BookSet(Books.installed().getBooks());
        if (object.equals(rootObject)) {
            load = true;
        } else {
            if (!(object instanceof Book)) {
                Object[] objects = mutableNode.getUserObjectPath();
                for (int i=0; i<objects.length; i++) {
                    books = books.filter(groupBy.get(i),objects[i].toString());
                }
                load = true;
            }
        }
        
        if (load) {
            reload(mutableNode,books);
            super.reload(mutableNode);
        }
    }  
    
    protected void reload(TreeNode node, BookSet books) {
        if (node == null) { return; }
        
        DefaultMutableTreeNode mutableNode = (DefaultMutableTreeNode)node;
        mutableNode.removeAllChildren();
        
        int level = mutableNode.getLevel();
        
        if (level < groupBy.size()) {
            String key = groupBy.get(level);
            Set group = books.getGroup(key);
            Iterator iter = group.iterator();
            while (iter.hasNext()) {
                Object value = iter.next();
                BookSet subBooks = books.filter(key, value);
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(value);
                mutableNode.add(childNode);
                reload(childNode,subBooks);
            }
        } else {
            if (books == null) {
                return;
            }
            Iterator iter = books.iterator();
            while (iter.hasNext()) {
                Book book =(Book)iter.next();
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(book);
                mutableNode.add(childNode);
            }
        }
    }
    
    /** 
     * Set the list of groupBy which used to arrange this model content.
     * 
     * <strong>Note: </strong>this methods does not automatically reload this model
     * 
     * @param groupBy {@code List} of {@code BookMetaData} property key
     */
    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }
    
    
    /** 
     * Return list of groupBy which used to arrange this model content.
     * @return list of groupBy
     */
    public List<String> getGroupBy() {
        return groupBy;
    }
}
