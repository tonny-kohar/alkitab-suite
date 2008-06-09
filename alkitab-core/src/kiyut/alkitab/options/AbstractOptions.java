/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Abstract Preferences Option. 
 * if nodeName is null, it will use DEFAULT_NODE_NAME
 * It is using {@code NbPreferences} as the backing store.
 *
 */
public abstract class AbstractOptions implements Options {
    protected static String DEFAULT_NODE_NAME = "prefs";
    protected String nodeName = null;
    
    /** Return the backing store Preferences
     * @return Preferences
     */
    protected final Preferences getPreferences() {
        String name = DEFAULT_NODE_NAME;
        if (nodeName != null) {
            name = nodeName;
        }
        
        Preferences prefs = NbPreferences.forModule(this.getClass()).node("options").node(name);
        
        return prefs;
    }
}
