/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import javax.swing.AbstractListModel;
import org.crosswire.jsword.passage.Key;

/**
 * Implementation of {@code ListModel} for Book Passage Key
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class KeyListModel extends AbstractListModel<Key> {
    //private final ScheduledExecutorService scheduler =  Executors.newScheduledThreadPool(1);
    protected boolean searchCancelled = false;
    protected int searchResult = -1;
    protected Key keyList;
    
    public KeyListModel()  {
        this(null);
    }
    
    public KeyListModel(Key keyList)  {
        setKey(keyList);
    }
    
    /** 
     * Set the this model backing data
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
    
    @Override
    public int getSize() {
        if (keyList == null) { return 0; }
        return keyList.getCardinality();
    }

    @Override
    public Key getElementAt(int index) {
        if (keyList == null) { return null; }
        return keyList.get(index);
    }
}
