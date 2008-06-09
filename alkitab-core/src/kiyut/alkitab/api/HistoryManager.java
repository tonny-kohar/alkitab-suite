/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

/**
 * History manager that manage/ consolidate the {@link History} 
 * from a variety of sources, and able to go back or forward one at the time.
 * It works similar to java.util.Iterator
 * 
 */
public interface HistoryManager {
    
    /** Returns the maximum number of histories this HistoryManager will hold. 
     * Default value is 100.
     * @return maximum number of histories
     * @see #setLimit(int) 
     */
    public int getLimit();
    
    /**
     * Set the maximum number of histories this HistoryManager will hold. 
     * @param limit the maximum limit
     * @see #getLimit()
     */
    public void setLimit(int limit);
    
    /** Add history.
     * Please check {@link #isInProgress()} state before add a history
     * @param history {@link History}
     */
    public void add(History history);
    
    /** Return current {@link History} */
    public History current();
    
    public History back();
    public History forward();
    public boolean hasBack();
    public boolean hasForward();
    
}
