/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.crosswire.jsword.passage.Key;

/**
 * DefaultKeyTreeModel which arrange the data/theKey in tree format.
 * 
 */
public class DefaultKeyTreeModel extends DefaultTreeModel implements KeyTreeModel {
    public DefaultKeyTreeModel(Key key) {
        super(null);
        TreeNode rootNode = buildModel(key);
        setRoot(rootNode);
    }
    
    public TreeCellRenderer getTreeCellRendererComponent() {
        return new DefaultKeyTreeCellRenderer();
    }
        
    /** Recursive build this model 
     * @param key the {@code Key}
     */
    protected DefaultKeyTreeNode buildModel(Key key) {
        DefaultKeyTreeNode node = new DefaultKeyTreeNode(key);
        
        if (key == null) {
            return node;
        }
        
        for (int i=0; i<key.getChildCount(); i++) {
            Key childKey = key.get(i);
            DefaultKeyTreeNode childNode = buildModel(childKey);
            node.add(childNode);
        }
        
        return node;
    }
}
