/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import kiyut.alkitab.api.TransformerHints;

/**
 * Implementation of TransformerHintsOptions
 * 
 */
public final class TransformerHintsOptions extends AbstractOptions {

    private static TransformerHintsOptions instance; // The single instance
    static {
        instance = new TransformerHintsOptions();
    }
    
    protected TransformerHints<TransformerHints.Key,Object> hints;
    
    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static TransformerHintsOptions getInstance() {
        return instance;
    }
    
    
    private TransformerHintsOptions() {
        nodeName = "Transformerhints";
        load();
    }
    
    public void load() {
        Preferences prefs = getPreferences();
        
        hints = new TransformerHints<TransformerHints.Key,Object>();
        hints.put(TransformerHints.STRONGS_NUMBERS, prefs.getBoolean(TransformerHints.STRONGS_NUMBERS.getName(), false));
        hints.put(TransformerHints.START_VERSE_ON_NEWLINE, prefs.getBoolean(TransformerHints.START_VERSE_ON_NEWLINE.getName(), false));
        hints.put(TransformerHints.MORPH, prefs.getBoolean(TransformerHints.MORPH.getName(), false));
        hints.put(TransformerHints.VERSE_NUMBERS, prefs.getBoolean(TransformerHints.VERSE_NUMBERS.getName(), true));
        hints.put(TransformerHints.NO_VERSE_NUMBERS, prefs.getBoolean(TransformerHints.NO_VERSE_NUMBERS.getName(), false));
        hints.put(TransformerHints.BOOK_CHAPTER_VERSE_NUMBERS, prefs.getBoolean(TransformerHints.BOOK_CHAPTER_VERSE_NUMBERS.getName(), false));
        hints.put(TransformerHints.CHAPTER_VERSE_NUMBERS, prefs.getBoolean(TransformerHints.CHAPTER_VERSE_NUMBERS.getName(), false));
        hints.put(TransformerHints.TINY_VERSE_NUMBERS, prefs.getBoolean(TransformerHints.TINY_VERSE_NUMBERS.getName(), true));
        hints.put(TransformerHints.HEADINGS, prefs.getBoolean(TransformerHints.HEADINGS.getName(), true));
        hints.put(TransformerHints.NOTES, prefs.getBoolean(TransformerHints.NOTES.getName(), true));
        hints.put(TransformerHints.XREF, prefs.getBoolean(TransformerHints.XREF.getName(), true));
        //hints.put(TransformerHints.BASE_URL, "");
        //hints.put(TransformerHints.CSS, "");
        //hints.put(TransformerHints.DIRECTION, "");
        //hints.put(TransformerHints.FONT, prefs.get(TransformerHints.FONT.getName(), "serif,0,14"));
        hints.put(TransformerHints.FONT, prefs.get(TransformerHints.FONT.getName(), createDefaultFont()));
    }
    
    public void save() {
        Preferences prefs = getPreferences();
        Iterator<TransformerHints.Key> it =  hints.keySet().iterator();
        while(it.hasNext()) {
            TransformerHints.Key key  = it.next();
            Object val = hints.get(key);
            if (val instanceof String) {
                prefs.put(key.getName(), val.toString());
            } else if (val instanceof Boolean) {
                prefs.putBoolean(key.getName(), ((Boolean)val).booleanValue());
            } 
        }
    }
    
    public TransformerHints<TransformerHints.Key,Object> getTransformerHints() {
        return hints;
    }
    
    protected String createDefaultFont() {
        Font font = UIManager.getFont("EditorPane.font");
        if (font == null) {
            return "SansSerif,0,13";
        }
        
        int fontSize = font.getSize();
        
        // XXX is this correct way to scale font based on DPI
        if (fontSize < 12) {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            fontSize = (int)Math.round((double)fontSize * dpi / 72.0);
        }
        
        //System.out.println(font.getFamily() + " " + fontSize + " " + dpi);
        
        return font.getFamily() + ",0," + fontSize;
        
    }
}
