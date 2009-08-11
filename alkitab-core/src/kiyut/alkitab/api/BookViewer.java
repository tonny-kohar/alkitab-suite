/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.api.event.BookChangeListener;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * BookViewer Interface. 
 * 
 */
public interface BookViewer {
    
    public static final String VIEWER_NAME = "ViewerName";
    
    /** Return {@code JComponent} that acts as the viewer 
     * @return {@code JComponent}
     */
    public JComponent getViewerComponent();
    
    /** Return {@link java.util.Collections#unmodifiableList(List) unmodifiableList} of {@code Book}
     * @return unmodifiableList of {@code Book}
     */
    public List<Book> getBooks();
    
    /** Return number of allowable books, useful for parallel view 
     * @return number of allowable books
     */
    public int getMaximumBook();
    
    /** Return the number of book currently hold
     * @return number of book
     */
    public int getBookCount();
    
    /** Set {@code Key}
     * @param key the view key as string
     */
    public void setKey(Key key);
    
    /** Return {@code Key}
     * @return {@code Key} or {@code null}
     */
    public Key getKey();
    
    /** Redisplay or refresh content */
    public void refresh();
    
    /** Display the source (raw,OSIS,HTML) of the currently viewed book */
    public void viewSource();
    
    public void compareView(boolean compare);
    
    public boolean isCompareView();
    
    /** Open the specified {@code SwordURI}
     * @param uri the {@code SwordURI} to be opened
     * @see openURI(SwordURI,String)
     */
    public void openURI(SwordURI uri);

    /** Open the specified {@code SwordURI}
     * @param uri the {@code SwordURI} to be opened
     * @param info optional additional info eg: search term, etc
     * @see openURI(SwordURI)
     */
    public void openURI(SwordURI uri, String info);
    
    /** Return {@link kiyut.alkitab.api.ViewerHints ViewerHints} used by this BookViewer.
     * This method should not return null, it could return empty {@link kiyut.alkitab.api.ViewerHints ViewerHints}
     * but not null
     * @return {@code ViewerHints}
     */
    public ViewerHints<ViewerHints.Key,Object> getViewerHints();
    
    /** Is this methods needed ? */
    public ActionMap getActionMap();
    
    public void addBookChangeListener(BookChangeListener listener);
    public void removeBookChangeListener(BookChangeListener listener);
    
    public void addPropertyChangeListener(String name, PropertyChangeListener listener);
    public void removePropertyChangeListener(String name, PropertyChangeListener listener);
    
    public void addHyperlinkListener(HyperlinkListener listener);
    public void removeHyperlinkListener(HyperlinkListener listener);
}
