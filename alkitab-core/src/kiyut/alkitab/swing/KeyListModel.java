/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractListModel;
import org.crosswire.jsword.passage.Key;

/**
 * Implementation of {@code ListModel} for Book Passage Key
 * 
 */
public class KeyListModel extends AbstractListModel {
    private final ScheduledExecutorService scheduler =  Executors.newScheduledThreadPool(1);
    protected boolean searchCancelled = false;
    protected int searchResult = -1;
    protected Key keyList;
    
    public KeyListModel()  {
        this(null);
    }
    
    public KeyListModel(Key keyList)  {
        setKey(keyList);
    }
    
    /** Set the this model backing data
     * @param keyList backing data for this model
     */
    public void setKey(Key keyList) {
        Key old = this.keyList;
        if (old != null) {
            fireIntervalRemoved(this, 0, old.getCardinality());
        }
        
        this.keyList = keyList;
        if (keyList != null) {
            fireIntervalAdded(this, 0, keyList.getCardinality());
        }
    }
    
    /** 
     * Return backing data this model used
     * @return Key
     */
    public Key getKey() {
        return this.keyList;
    }
    
    public int getSize() {
        if (keyList == null) { return 0; }
        return keyList.getCardinality();
    }

    public Object getElementAt(int index) {
        if (keyList == null) { return null; }
        return keyList.get(index);
    }
}
