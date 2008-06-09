/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 *
 */
public interface KeyTreeModel extends TreeModel {
    /** Return TreeCellRenderer to render this model 
     * @return TreeCellRenderer or null
     */
    public TreeCellRenderer getTreeCellRendererComponent();
}
