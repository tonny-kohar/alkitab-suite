/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import kiyut.alkitab.api.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */
public class BookViewerHistoryManager implements HistoryManager {
    /** Default 100 */
    protected int limit;
    
    protected List<History> backList;
    protected List<History> forwardList;
    
    public BookViewerHistoryManager() {
        this(100);
    }
    
    /**
     * 
     * @param limit the maximum limit
     */
    public BookViewerHistoryManager(int limit) {
        setLimit(limit);
        
        backList = Collections.synchronizedList(new ArrayList<History>());
        forwardList = Collections.synchronizedList(new ArrayList<History>());
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit should not < 1");
        }
        this.limit = limit;
    }
    
    public void add(History history) {
        if (history == null) {
            throw new IllegalArgumentException("history should not be null");
        }
        
        backList.add(0, history);
        forwardList.clear();
        
        while(backList.size() > limit) {
            backList.remove(backList.size() - 1);
        }
    }
    
    public History current() {
        if (backList.size() < 1) {
            return null;
        }
        return backList.get(0);
    }
    
    public synchronized History back() {
        if (!hasBack()) {
            return null;
        }        
        
        History hist = backList.remove(0);
        forwardList.add(0, hist);
        
        return current();
    }
    
    public synchronized History forward() {
        if (forwardList.size() == 0) {
            return null;
        }
        
        History hist = forwardList.remove(0);
        backList.add(0, hist);
        
        return current();
    }
    
    public boolean hasBack() {
        // need > 1, because index 0 is current
        return backList.size() > 1;
    }
    
    public boolean hasForward() {
        return forwardList.size() > 0;
    }
}
