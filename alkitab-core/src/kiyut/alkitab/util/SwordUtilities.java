/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.io.File;
import java.util.Comparator;
import kiyut.alkitab.api.SwordURI;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.book.BookException;
import org.crosswire.jsword.book.sword.SwordBookPath;
import org.crosswire.jsword.passage.Key;

/**
 * Collections of Sword Utilities
 * 
 */
public class SwordUtilities {
    private static File[] initialCopyOfSwordPath = null;
    
    private SwordUtilities() {
        throw new Error("SwordUtilities is a utility class for static methods"); // NOI18N
    }
    
    public static String bookCategoryToURIScheme(BookCategory category) {
        String scheme = null;
        
        if (category == null) {
            return scheme;
        }
        
        if (category.equals(BookCategory.BIBLE)) {
            scheme = SwordURI.BIBLE_SCHEME;
        } else if (category.equals(BookCategory.COMMENTARY)) {
            scheme = SwordURI.COMMENTARY_SCHEME;
        } else if (category.equals(BookCategory.DAILY_DEVOTIONS)) {
            scheme = SwordURI.DAILY_DEVOTION_SCHEME;
        } else if (category.equals(BookCategory.DICTIONARY)) {
            scheme = SwordURI.DICTIONARY_SCHEME;
        } else if (category.equals(BookCategory.GENERAL_BOOK)) {
            scheme = SwordURI.GENERAL_BOOK_SCHEME;
        } else if (category.equals(BookCategory.GLOSSARY)) {
            scheme = SwordURI.GLOSSARY_SCHEME;
        } else if (category.equals(BookCategory.OTHER)) {
            scheme = SwordURI.OTHER_SCHEME;
        } else if (category.equals(BookCategory.QUESTIONABLE)) {
            scheme = SwordURI.QUESTIONABLE_SCHEME;
        } else if (category.equals(BookCategory.MAPS)) {
            scheme = SwordURI.MAPS_SCHEME;
        }
        return scheme;
    }
    
    /** Return the previous key in the sequence order 
     * @param key the {@code Key}
     * @return previous key or null
     */
    public static Key getPreviousKey(Key key) {
        Key prevKey = null;
        if (key == null) { return prevKey; }
        prevKey = getPreviousSibling(key);
        if (prevKey == null) {
            prevKey = key.getParent();
            return prevKey;
        }
        
        while (prevKey.getChildCount() > 0) {
            prevKey = prevKey.get(prevKey.getChildCount() - 1);
        }
        
        return prevKey;
    }
    
    /** Return the next key in the sequence order 
     * @param key the {@code Key}
     * @return next key or null
     */
    public static Key getNextKey(Key key) {
        Key nextKey = null;
        
        if (key == null) { return nextKey; }
        
        if (key.getChildCount() > 0) {
            nextKey = key.get(0);
            return nextKey;
        }
        
        nextKey = getNextSibling(key);
        if (nextKey != null) { return nextKey; }
        
        Key parentKey = key.getParent();
        Key curKey = key;
        
        while (parentKey != null) {
            int index = -1;
            for (int i=0; i<parentKey.getChildCount(); i++) {
                if (curKey.equals(parentKey.get(i))) {
                    index = i + 1;
                    break;
                }
            }
            
            if (index != -1 && index < parentKey.getChildCount()) {
                nextKey = parentKey.get(index);
                break;
            }
            curKey = parentKey;
            parentKey = parentKey.getParent();
        }
        
        return nextKey;
    }
    
    /** Return the previous sibling key under the same parent
     * @param key the {@code Key}
     * @return sibling key or null
     */
    public static Key getPreviousSibling(Key key) {
        Key siblingKey = null;
        if (key == null) { return siblingKey; }
        if (key.getParent() == null) { return siblingKey; }
        
        Key parentKey = key.getParent();
        int index = -1;
        for (int i=0; i<parentKey.getChildCount(); i++) {
            if (key.equals(parentKey.get(i))) {
                index = i - 1;
                break;
            }
        }
        
        if (index >= 0) {
             siblingKey = parentKey.get(index);
        }
        
        return siblingKey;
    }
    
    /** Return the next sibling key under the same parent
     * @param key the {@code Key}
     * @return sibling key or null
     */
    public static Key getNextSibling(Key key) {
        Key siblingKey = null;
        if (key == null) { return siblingKey; }
        if (key.getParent() == null) { return siblingKey; }
        
        Key parentKey = key.getParent();
        int index = -1;
        for (int i=0; i<parentKey.getChildCount(); i++) {
            if (key.equals(parentKey.get(i))) {
                index = i + 1;
                break;
            }
        }
        
        if (index != -1 && index < parentKey.getChildCount()) {
            siblingKey = parentKey.get(index);
        }
        
        return siblingKey;
    }
    
    /** just an implementation of genericfied book initial comparator */
    public static Comparator<Book> getBookInitialsComparator() {
        Comparator<Book> comp = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getInitials().compareTo(o2.getInitials());
            }
        };
        
        return comp;
    }
}
