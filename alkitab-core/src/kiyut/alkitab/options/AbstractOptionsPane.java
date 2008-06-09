/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import javax.swing.JPanel;


/**
 * Abstract implementaion of Preferences Panel
 *
 */
public abstract class AbstractOptionsPane extends JPanel {
    /** load / refresh UI based on the value from the backing store */
    public abstract void refreshOptions();
    /** apply changes to backing store */
    public abstract void applyChanges();
}
