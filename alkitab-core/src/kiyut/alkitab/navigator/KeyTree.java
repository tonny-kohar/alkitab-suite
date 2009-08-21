/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * Key Tree for a book. It display all the key for single book in Tree Model
 * 
 */
public class KeyTree extends JTree {

    protected KeyTreeModel keyTreeModel;
    
    public KeyTree() {
        this(null);
    }
    
    public KeyTree(KeyTreeModel keyTreeModel) {
        putClientProperty("JTree.lineStyle", "Angled"); 
        setRootVisible(false);
        setShowsRootHandles(true);
        setExpandsSelectedPaths(true);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        if (keyTreeModel != null) {
            this.keyTreeModel = keyTreeModel;
            this.setModel(keyTreeModel);
        }
        
        //setCellRenderer(new BooksTreeCellRenderer());
    }
    
    @Override
    public void setModel(TreeModel model) {
        super.setModel(model);
        
        if (model instanceof KeyTreeModel) {
            KeyTreeModel keyModel = (KeyTreeModel)model;
            TreeCellRenderer renderer = keyModel.getTreeCellRendererComponent();
            if (renderer != null) {
                this.setCellRenderer(renderer);
            }
        }
    }
    
}
