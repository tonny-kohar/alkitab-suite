/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import javax.swing.JPanel;
import kiyut.alkitab.api.BookViewer;
import kiyut.alkitab.api.TransformerHints;
import kiyut.alkitab.api.event.BookChangeEvent;
import kiyut.alkitab.api.event.BookChangeListener;

/**
 * Abstract implementaion of {@link kiyut.alkitab.api.BookViewer BookViewer}
 */
public abstract class AbstractBookViewerPane extends JPanel implements BookViewer {

    /** Maximum books allowed in this view. Default is 1 */
    protected int maximumBook = 1;
    
    protected TransformerHints<TransformerHints.Key,Object> transformerHints;
    
    public int getMaximumBook() {
        return maximumBook;
    }

    /** Return false
     * @return {@code false}
     */
    public boolean isCompareView() {
        return false;
    }
    
    public TransformerHints<TransformerHints.Key,Object> getTransformerHints() {
        if (transformerHints == null) {
            transformerHints = new TransformerHints<TransformerHints.Key,Object>();
        }
        return transformerHints;
    }
    
    public void addBookChangeListener(BookChangeListener listener) {
        listenerList.add(BookChangeListener.class, listener);
    }

    public void removeBookChangeListener(BookChangeListener listener) {
        listenerList.remove(BookChangeListener.class, listener);
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type. */
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


