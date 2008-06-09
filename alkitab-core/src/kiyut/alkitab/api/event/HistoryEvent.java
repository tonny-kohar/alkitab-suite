/* This work has been placed into the public domain. */

package kiyut.alkitab.api.event;

import java.util.EventObject;
import kiyut.alkitab.api.History;

/**
 *
 * 
 */
public class HistoryEvent extends EventObject {
    protected History history;
    
    public HistoryEvent(Object source, History history) {
        super(source);
        this.history = history;
    }
    
    public History getHistory() {
        return this.history;
    }
    
}
