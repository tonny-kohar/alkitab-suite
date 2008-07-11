/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.util.HashMap;
import org.crosswire.common.xml.TransformingSAXEventProvider;

/**
 * This class describe various hints for viewing books.
 * It also defines a way to pass transformer parameters for XSLT transform. 
 * 
 * 
 */
public class ViewerHints<K,V> extends HashMap<K,V> {
    
    public static final ViewerHints.Key START_VERSE_ON_NEWLINE = new BooleanKey("VLine");
    public static final ViewerHints.Key STRONGS_NUMBERS = new BooleanKey("Strongs");
    public static final ViewerHints.Key MORPH = new BooleanKey("Morph");
    public static final ViewerHints.Key VERSE_NUMBERS = new BooleanKey("VNum");
    public static final ViewerHints.Key CHAPTER_VERSE_NUMBERS = new BooleanKey("CVNum");
    public static final ViewerHints.Key BOOK_CHAPTER_VERSE_NUMBERS = new BooleanKey("BCVNum");
    public static final ViewerHints.Key NO_VERSE_NUMBERS = new BooleanKey("NoVNum");
    public static final ViewerHints.Key TINY_VERSE_NUMBERS = new BooleanKey("TinyVNum");
    public static final ViewerHints.Key HEADINGS = new BooleanKey("Headings");
    public static final ViewerHints.Key NOTES = new BooleanKey("Notes");
    public static final ViewerHints.Key XREF = new BooleanKey("XRef");
    //public static final ViewerHints.Key DIRECTION = new StringKey("direction");
    public static final ViewerHints.Key FONT = new StringKey("font");
    public static final ViewerHints.Key BASE_URL = new URLKey("baseURL");
    public static final ViewerHints.Key CSS = new URLKey("css");
    public static final ViewerHints.Key TOOLTIP_POPUP = new BooleanKey("TooltipPopup");
    
    private static final ViewerHints.Key[] KEYS = {
        STRONGS_NUMBERS,
        MORPH,
        START_VERSE_ON_NEWLINE,
        VERSE_NUMBERS,
        CHAPTER_VERSE_NUMBERS,
        BOOK_CHAPTER_VERSE_NUMBERS,
        NO_VERSE_NUMBERS,
        TINY_VERSE_NUMBERS,
        HEADINGS,
        NOTES,
        XREF,
        BASE_URL,
        /*DIRECTION,*/
        FONT,
        CSS,
        TOOLTIP_POPUP,
    };
    
    protected ViewerHints<K,V> defaults;
    
    public ViewerHints() {
        this(null);
    }
    
    public ViewerHints(ViewerHints<K,V> defaults) {
        super();
        this.defaults = defaults;
    }
    
    @Override
    public V put(K key, V value) {
        if (!((Key)key).accept(value)) {
            throw new IllegalArgumentException(value+ " is not valid value for "+ key);
        }
        return super.put(key, value);
    }
    
    @Override
    public V get(Object key) {
        if (!(key instanceof Key)) {
            throw new IllegalArgumentException("key should be instanceof ViewerHints.Key");
        }
        
        V v = super.get(key);
        if (v == null && defaults != null) {
            v = defaults.get(key);
        }
        return v;
    }
    
    /** Update the supplied provider parameter, with this ViewerHints 
     * @param provider the {@code TransformingSAXEventProvider}
     */
    public void updateProvider(TransformingSAXEventProvider provider) {
        for (int i = 0; i < KEYS.length; i++)  {
            Object obj = get(KEYS[i]);
            if (obj != null) {
                provider.setParameter(KEYS[i].getName(), obj.toString());
            }
        }
        
        
    }
    
    /**
     * Defines the base type of all keys used to control various
     * aspects of the XSLT transform.
     */
    public abstract static class Key {

        /**
         * Returns {@code true} if the specified object is a valid value for
         * this key, otherwise {@code false}.
         * @return {@code true} or {@code false}
         */
        public abstract boolean accept(Object val);
        
        public abstract String getName();
    }
    
    public static class BooleanKey extends Key {
        protected String name;
        
        public BooleanKey(String name) {
            this.name = name;
        }
        
        public boolean accept(Object v) {
            return (v != null && v instanceof Boolean);
        }
        
        public String getName() {
            return name;
        }
    }
    
    public static class StringKey extends Key {
        protected String name;
        
        public StringKey(String name) {
            this.name = name;
        }
        
        public boolean accept(Object v) {
            return (v != null && v instanceof String);
        }
        
        public String getName() {
            return name;
        }
    }
    
    public static class URLKey extends StringKey {
        public URLKey(String name) {
            super(name);
        }
    }
}
