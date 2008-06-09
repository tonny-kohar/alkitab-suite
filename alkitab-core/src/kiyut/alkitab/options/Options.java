/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

/**
 * Options (Preferences) Interface
 * 
 */
public interface Options {
    
    /** load the Options or User Preferences from backing store */
    public void load();
    
    /** Save the Options or User Preferences to backing store */
    public void save();
}
