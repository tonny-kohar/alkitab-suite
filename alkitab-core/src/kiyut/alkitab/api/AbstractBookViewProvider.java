/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

/**
 * The abstract implementation of SwordProvider.
 * All methods is implemented as empty methods, only the class is marked as abstract,
 * for convenience during implementation of real provider
 * 
 */
public abstract class AbstractBookViewProvider implements BookViewProvider {
    
    public void openURI(SwordURI uri, boolean newView) {
        // do nothing
    }
}
