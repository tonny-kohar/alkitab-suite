/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public interface KeyTreeModel extends TreeModel {
    /** Return TreeCellRenderer to render this model 
     * @return TreeCellRenderer or null
     */
    public TreeCellRenderer getTreeCellRendererComponent();
}
