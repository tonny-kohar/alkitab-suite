/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.awt.Component;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * Book ToolTip popup
 * 
 */
public interface BookToolTip {

    /** 
     * Display this tooltip as popup. 
     * Owner is used to determine which Window the new Popup will parent the 
     * Component the Popup creates to. 
     * A null owner implies there is no valid parent. 
     * x and y specify the preferred initial location to place the Popup at. 
     * Based on screen size, or other paramaters, the Popup may not display at x and y.
     * @param book {@code Book} to view
     * @param key {@code Key} to view
     * @param owner Component mouse coordinates are relative to, may be null
     * @param x Initial x screen coordinate
     * @param y Initial y screen coordinate
     *
     */
    public void show(Book book, Key key, Component owner, int x, int y);
    
    /** Hides the Popup */
    public void hide();
}
