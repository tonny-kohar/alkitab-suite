/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.util.Iterator;
import java.util.prefs.Preferences;
import kiyut.alkitab.bookviewer.ViewerHints;

/**
 * Implementation of ViewerHintsOptions
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public final class ViewerHintsOptions extends AbstractOptions {

    private static ViewerHintsOptions instance; // The single instance
    static {
        instance = new ViewerHintsOptions();
    }
    
    protected ViewerHints<ViewerHints.Key,Object> hints;
    
    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static ViewerHintsOptions getInstance() {
        return instance;
    }
    
    
    private ViewerHintsOptions() {
        nodeName = "viewerhints";
        load();
    }
    
    @Override
    public void load() {
        Preferences prefs = getPreferences();
        
        hints = new ViewerHints<ViewerHints.Key,Object>();
        hints.put(ViewerHints.STRONGS_NUMBERS, prefs.getBoolean(ViewerHints.STRONGS_NUMBERS.getName(), false));
        hints.put(ViewerHints.START_VERSE_ON_NEWLINE, prefs.getBoolean(ViewerHints.START_VERSE_ON_NEWLINE.getName(), false));
        hints.put(ViewerHints.MORPH, prefs.getBoolean(ViewerHints.MORPH.getName(), false));
        hints.put(ViewerHints.VERSE_NUMBERS, prefs.getBoolean(ViewerHints.VERSE_NUMBERS.getName(), true));
        hints.put(ViewerHints.NO_VERSE_NUMBERS, prefs.getBoolean(ViewerHints.NO_VERSE_NUMBERS.getName(), false));
        hints.put(ViewerHints.BOOK_CHAPTER_VERSE_NUMBERS, prefs.getBoolean(ViewerHints.BOOK_CHAPTER_VERSE_NUMBERS.getName(), false));
        hints.put(ViewerHints.CHAPTER_VERSE_NUMBERS, prefs.getBoolean(ViewerHints.CHAPTER_VERSE_NUMBERS.getName(), false));
        hints.put(ViewerHints.TINY_VERSE_NUMBERS, prefs.getBoolean(ViewerHints.TINY_VERSE_NUMBERS.getName(), true));
        hints.put(ViewerHints.HEADINGS, prefs.getBoolean(ViewerHints.HEADINGS.getName(), true));
        hints.put(ViewerHints.NOTES, prefs.getBoolean(ViewerHints.NOTES.getName(), true));
        hints.put(ViewerHints.XREF, prefs.getBoolean(ViewerHints.XREF.getName(), true));
        //hints.put(ViewerHints.BASE_URL, "");
        //hints.put(ViewerHints.CSS, "");
        //hints.put(ViewerHints.DIRECTION, "");
        hints.put(ViewerHints.TOOLTIP_POPUP, prefs.getBoolean(ViewerHints.TOOLTIP_POPUP.getName(), true));
    }
    
    @Override
    public void store() {
        Preferences prefs = getPreferences();
        Iterator<ViewerHints.Key> it =  hints.keySet().iterator();
        while(it.hasNext()) {
            ViewerHints.Key key  = it.next();
            Object val = hints.get(key);
            if (val instanceof String) {
                prefs.put(key.getName(), val.toString());
            } else if (val instanceof Boolean) {
                prefs.putBoolean(key.getName(), ((Boolean)val).booleanValue());
            } 
        }
    }
    
    public ViewerHints<ViewerHints.Key,Object> getViewerHints() {
        return hints;
    }
}
