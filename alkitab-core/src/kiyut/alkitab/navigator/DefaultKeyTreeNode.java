/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import javax.swing.tree.DefaultMutableTreeNode;
import org.crosswire.jsword.passage.Key;

/**
 * Default implementation of KeyTreeNode
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class DefaultKeyTreeNode extends DefaultMutableTreeNode implements KeyTreeNode {

    public DefaultKeyTreeNode(Object userObject) {
        super(userObject);
    }

    @Override
    public Object getUserObject() {
        return userObject;
    }
    
    public boolean isKey() {
        if (userObject instanceof Key) {
            return true;
        }
        return false;
    }
    
    public Key getKey() {
        if (isKey()) {
            return (Key)userObject;
        }
        return null;
    }
}
