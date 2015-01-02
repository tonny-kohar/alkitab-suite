/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer.event;

import java.util.EventListener;

/**
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public interface BookChangeListener extends EventListener {
        public void bookChanged(BookChangeEvent evt);
}
