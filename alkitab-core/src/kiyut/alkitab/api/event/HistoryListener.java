/* This work has been placed into the public domain. */

package kiyut.alkitab.api.event;

import java.util.EventListener;

/**
 * Currently it is not used anywhere, is this API needed ?
 * 
 */
public interface HistoryListener extends EventListener {
    /** Invoked after goBack processed */
    public void goBack(HistoryEvent evt);
    
    /** Invoked after goForward processed */
    public void goForward(HistoryEvent evt);
    
    /** Invoked after goPrevious processed */
    public void goPrevious(HistoryEvent evt);
    
    /** Invoked after goNext processed */
    public void goNext(HistoryEvent evt);
}
