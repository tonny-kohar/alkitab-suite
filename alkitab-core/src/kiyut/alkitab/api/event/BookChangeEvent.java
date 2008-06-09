/* This work has been placed into the public domain. */

package kiyut.alkitab.api.event;

import java.util.EventObject;

/**
 * An Event that describe BookChangeEvent
 */
public class BookChangeEvent extends EventObject {
    
    public BookChangeEvent(Object source) {
        super(source);
    }
}
