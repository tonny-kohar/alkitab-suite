/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

/**
 * Options (Preferences) Interface
 * 
 */
public interface Options {
    
    /** Load the Options or User Preferences from backing store */
    public void load();
    
    /** Store the Options or User Preferences to backing store */
    public void store();
}
