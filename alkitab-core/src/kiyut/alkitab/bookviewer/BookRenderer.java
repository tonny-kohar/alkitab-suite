package kiyut.alkitab.bookviewer;

import java.util.List;
import javax.swing.JComponent;
import javax.swing.event.HyperlinkListener;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * 
 */
public interface BookRenderer {
    
    public List<Book> getBooks();
    public boolean isCompareView();
    public Key getKey();
    
    /** 
     * Return string of HTML source 
     * @return string of HTML source
     */
    public String getContentSource();
    
    /** 
     * Return the wrapper component
     * @return the wrapper component */
    public JComponent getComponent();
    
    /**
     * Reload or refresh content, 
     * equivalent with calling {@code refresh(false)}
     *
     * @see #reload(boolean)
     */
    public void reload();
    
    /**
     * Reload or refresh content either in separate thread or not.
     *
     * @param invokeLater if true perform the action using separate thread eg: EDT invokeLater thread
     */
    public void reload(boolean invokeLater);
    
    public void addHyperlinkListener(HyperlinkListener listener);
    
    public void removeHyperlinkListener(HyperlinkListener listener);
}
