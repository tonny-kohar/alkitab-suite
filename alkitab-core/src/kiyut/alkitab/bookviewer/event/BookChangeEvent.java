/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer.event;

import java.util.EventObject;

/**
 * An Event that describe BookChangeEvent
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class BookChangeEvent extends EventObject {
    
    public BookChangeEvent(Object source) {
        super(source);
    }
}
