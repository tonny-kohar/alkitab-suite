/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import javax.swing.tree.TreeNode;
import org.crosswire.jsword.passage.Key;

/**
 *
 */
public interface KeyTreeNode extends TreeNode {
    public boolean isKey();
    
    public Key getKey();
    
    public Object getUserObject();
}
