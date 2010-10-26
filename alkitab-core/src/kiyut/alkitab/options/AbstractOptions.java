/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;
import javax.swing.event.EventListenerList;
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

    private EventListenerList listenerList;

    public AbstractOptions() {
        listenerList = new EventListenerList();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.add(PropertyChangeListener.class, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerList.remove(PropertyChangeListener.class, listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                ((PropertyChangeListener) listeners[i+1]).propertyChange(event);
            }
        }
    }
    
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
