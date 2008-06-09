/* This work has been placed into the public domain. */

package kiyut.alkitab.api.event;

import java.util.EventListener;

/**
 *
 */
public interface BookChangeListener extends EventListener {
        public void bookChanged(BookChangeEvent evt);
}
