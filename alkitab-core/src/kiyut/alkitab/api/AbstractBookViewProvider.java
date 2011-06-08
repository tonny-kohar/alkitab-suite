/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import org.crosswire.jsword.passage.Key;

/**
 * The abstract implementation of SwordProvider.
 * All methods is implemented as empty methods, only the class is marked as abstract,
 * for convenience during implementation of real provider
 * 
 */
public abstract class AbstractBookViewProvider implements BookViewProvider {
    
    @Override
    public void openURI(SwordURI uri, boolean newView) {
        openURI(uri,null,newView);
    }
    
    @Override
    public void openURI(SwordURI uri, String info, boolean newView) {
        // do nothing
    }

    @Override
    public void synchronizeView(Key key) {
        // do nothing
    }

}
