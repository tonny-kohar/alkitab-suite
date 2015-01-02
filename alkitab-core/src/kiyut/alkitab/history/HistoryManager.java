/* This work has been placed into the public domain. */

package kiyut.alkitab.history;

import org.crosswire.jsword.passage.RestrictionType;

/**
 * History manager that manage or consolidate the {@link History}
 * for single viewer instance, and able to go back or forward
 * one at the time.
 * It works similar to java.util.Iterator
 *
 * <strong>note:</strong> this is not Global History. 
 * It is intended for single viewer history only
 *
 * @see kiyut.alkitab.api.GlobalHistory
 * @author Tonny Kohar <tonny.kohar@gmail.com>
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
     * @param history {@link History}
     */
    public void add(History history);

    /** Expand / blur the current history by.
     * The result will be automatically managed by this HistoryManager
     * by creating a new entry for the current and move the existing to back list
     * <strong>note: </strong> only applicable to verse/passage key type
     * @param by The number of verses/keys to widen by
     * @param restrict The RestrictionType
     * @return the history if blur is success or null if failed
     */
    public History blur(int by, RestrictionType restrict);
    
    /** Return current {@link History} */
    public History current();
    
    public History back();
    public History forward();
    public boolean hasBack();
    public boolean hasForward();
    
}
