/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer.history;

import java.util.ArrayList;
import java.util.List;
import kiyut.alkitab.api.History;
import kiyut.alkitab.options.BookViewerOptions;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.Passage;
import org.crosswire.jsword.passage.RestrictionType;
import org.crosswire.jsword.passage.Verse;

/**
 * An implementation of History for the {@code Book} {@code Key}
 * 
 */
public class BookViewerHistory implements History {
    protected Key key;
    protected List<Key> keyList;
    protected int index;
    protected int verseLimit;
    protected String search;

    public BookViewerHistory(Key key, String search) {
        this.key = key;
        this.search = search;

        keyList = new ArrayList<Key>();
        index = 0;

        if (key instanceof Passage) {
            //System.out.println("Passage");
            buildPassageKey(key);
        } else {
            //System.out.println("Non Passage");
            buildTreeKey(key);
        }
    }

    private void buildPassageKey(Key key) {
        verseLimit = BookViewerOptions.getInstance().getVersesLimit();
        if (verseLimit == 0) { // unlimited
            keyList.add(key);
            return;
        }
        
        Passage pass = (Passage)key.clone();
        Passage nextPass = pass;
        while (nextPass != null) {
            pass = nextPass;
            nextPass = pass.trimVerses(verseLimit);
            keyList.add(pass);
        }
    }
    
    public void buildTreeKey(Key key) {
        for (int i=0; i<key.getChildCount(); i++) {
            Key child = key.get(i);
            keyList.add(child);
            buildTreeKey(child);
        }
    }
    
    public Key getKey() {
        return this.key;
    }

    public String getSearch() {
        return this.search;
    }


    public Key current() {
        //System.out.println(index + " size: " + keyList.size());
        if (index >= keyList.size()) {
            return null;
        }
        return keyList.get(index);
    }
    
    public Key first() {
        index = 0;
        return current();
    }

    public Key previous() {
        if (!hasPrevious()) {
            return null;
        }

        index--;
        Key currentKey = keyList.get(index);

        return currentKey;
    }

    public Key next() {
        if (!hasNext()) {
            return null;
        }

        index++;
        Key currentKey = keyList.get(index);

        return currentKey;
    }

    public boolean hasNext() {
        if (index < keyList.size() - 1) {
            return true;
        }
        return false;
    }

    public boolean hasPrevious() {
        if (index > 0) {
            return true;
        }
        return false;
    }
    
    public History blur(int by, RestrictionType restrict) {
        if (!(key instanceof Passage)) {
            return null;
        }
        
        Verse oldCurrentVerse = null;
        
        Passage oldCurrentKey = (Passage)current().clone();
        try {
            oldCurrentVerse = oldCurrentKey.getVerseAt(0);
        } catch (Exception ex) {
            oldCurrentVerse = null;
        }
        
        Key tKey = (Key)key.clone();
        tKey.blur(by, restrict);

        BookViewerHistory history = new BookViewerHistory(tKey, null);
        
        /*key = tKey;
        keyList.clear();
        buildPassageKey(key);
         */
        
        int tIndex = 0;
        //boolean found = false;
        if (oldCurrentVerse != null) {
            Passage tPass;
            for (int i = 0; i < history.keyList.size(); i++) {
                tPass = (Passage) history.keyList.get(i);
                if (tPass.contains(oldCurrentVerse)) {
                    tIndex = i;
                    //found = true;
                    break;
                }
            }
        }
        
        /*if (!found) {
            tIndex = 0;
        }*/
        history.index = tIndex;

        return history;
    }
}
