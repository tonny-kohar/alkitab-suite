/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import javax.swing.JPanel;
import kiyut.alkitab.bookviewer.event.BookChangeEvent;
import kiyut.alkitab.bookviewer.event.BookChangeListener;

/**
 * Abstract implementaion of {@link kiyut.alkitab.api.BookViewer BookViewer} 
 * 
 */
public abstract class AbstractBookViewerPane extends JPanel implements BookViewer {

    /** Maximum books allowed in this view. Default is 1 */
    protected int maximumBook = 1;
    
    /** book viewer name, if null (dynamically generated based on book) or explicitly set */
    protected String name = null;

    @Override
    public int getMaximumBook() {
        return maximumBook;
    }

    /** Return false
     * @return {@code false}
     */
    @Override
    public boolean isCompareView() {
        return false;
    }
    
    @Override
    public void setName(String name) {
        if (name != null) { 
            this.name = name; 
            super.setName(name);
            firePropertyChange(BookViewer.VIEWER_NAME, null, getName());
        }
    }
    
    @Override
    public void addBookChangeListener(BookChangeListener listener) {
        listenerList.add(BookChangeListener.class, listener);
    }

    @Override
    public void removeBookChangeListener(BookChangeListener listener) {
        listenerList.remove(BookChangeListener.class, listener);
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type.
     * @param event the event */
    protected void fireBookChange(BookChangeEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("argument event should not be null");
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==BookChangeListener.class) {
                ((BookChangeListener)listeners[i+1]).bookChanged(event);
            }
        }
    }
}


