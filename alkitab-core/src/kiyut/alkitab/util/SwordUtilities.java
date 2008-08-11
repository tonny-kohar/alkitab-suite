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
            public int compare(Book o1, Book o2) {
                return o1.getInitials().compareTo(o2.getInitials());
            }
        };
        
        return comp;
    }
    
    /** A methods to set sword path (augment path),
     * Please do not call SwordBookPath.setAugmentPath() directly, 
     * use this static utilities methods.
     * This methods also make sure [user.home]/.sword is included, 
     * whether specified in the param or not. If not specified it will 
     * add the [user.home]/.sword in the last entry
     * @param paths an arrays of path
     */
    public static void setSwordPath(File[] paths) throws BookException {
        boolean defPathFound = false;
        
        String str = System.getProperty("user.home");
        File defPath = new File(str + File.separator + ".sword");
        File[] files;
        
        // first copy the initial SwordBookPath
        if (initialCopyOfSwordPath == null) {
            files = SwordBookPath.getSwordPath();
            initialCopyOfSwordPath = new File[files.length];
            System.arraycopy(files, 0, initialCopyOfSwordPath, 0, files.length);
        }
        
        // search defPath in the initialCopyOfSwordPath
        //files = SwordBookPath.getSwordPath();
        files = initialCopyOfSwordPath;
        for (int i=0; i<files.length; i++) {
            if (files[i].equals(defPath)) {
                defPathFound = true;
                break;
            }
        }
        
        // search defPath in the paths array supplied as param
        if (!defPathFound && paths != null) {
            files = paths;
            for (int i = 0; i < files.length; i++) {
                if (files[i].equals(defPath)) {
                    defPathFound = true;
                    break;
                }
            }
        }
        
        // if defPath is not there, make sure it is included as last entry
        if (!defPathFound) {
            if (paths == null) {
                files = new File[1];
            } else {
                files = new File[paths.length + 1];
                System.arraycopy(paths, 0, files, 0, paths.length);
            }
            paths = files;
            paths[paths.length-1] = defPath;
        }
        
        if (paths != null) {
            SwordBookPath.setAugmentPath(paths);
        }
    }
}
