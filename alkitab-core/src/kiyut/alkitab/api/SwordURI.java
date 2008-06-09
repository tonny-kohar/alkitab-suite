/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import kiyut.alkitab.util.SwordUtilities;
import org.crosswire.jsword.book.Book;

/**
 * Sword URI in the format<br/>
 * [scheme://][path/][#]fragment
 * 
 * scheme = sword,bible,commentary,... <br/>
 * path = name of the book eg: KJV, ESV <br/>
 * fragment/Reference = the key eg: GEN 1 1<br/>
 * 
 * eg:
 * <pre>
 * bible://Gen 1 1 // open Gen 1:1 without care which bible
 * bible://KJV/Gen 1 1 // open  Gen 1:1 in KJV bible
 * sword://KJV/Gen 1 1 // open Gen 1:1 in KJV book
 * #Gen 1 1 // this is local anchor/fragment
 * </pre>
 * 
 * If the specified path (book) is not in the correct category, the behaviour is unspecified
 * eg:
 * <pre>
 * bible://Calvin Institute/  
 * </pre>
 * 
 * We know that Calvin Institute is not a bible category, it is a General Book category, as for now
 * the behaviour is unspecified (not forced by the API), do as you wish. However, it is recommended
 * to silently ignore by do not display anything, or show an error/message dialog
 * 
 * <strong>Important: </strong> this Sword URI spec is subject to change, it is not fixed yet
 * 
 * @see BookViewProvider
 */
public class SwordURI {
    
    /** enum of URI Type: FRAGMENT, SWORD, BIBLE, COMMENTARY, DAILY_DEVOTION, DICTIONARY, etc */
    public static enum Type {FRAGMENT, SWORD, BIBLE, COMMENTARY, DAILY_DEVOTION, 
                            DICTIONARY, GENERAL_BOOK, GLOSSARY, OTHER, QUESTIONABLE, 
                            HEBREW_STRONGS, GREEK_STRONGS, GREEK_MORPH, HEBREW_MORPH    }
    
    public static final String SWORD_SCHEME = "sword";
    public static final String BIBLE_SCHEME = "bible";
    public static final String COMMENTARY_SCHEME = "commentary";
    public static final String DAILY_DEVOTION_SCHEME = "daily";
    public static final String DICTIONARY_SCHEME = "dictionary";
    public static final String GENERAL_BOOK_SCHEME = "general";
    public static final String GLOSSARY_SCHEME = "glossary";
    public static final String OTHER_SCHEME = "other";
    public static final String QUESTIONABLE_SCHEME = "questionable";
    public static final String HEBREW_STRONGS_SCHEME = "hdef";
    public static final String GREEK_STRONGS_SCHEME = "gdef";
    public static final String GREEK_MORPH_SCHEME = "gmorph";
    public static final String HEBREW_MORPH_SCHEME = "hmorph";
    
    public static final Map<String,Type> SCHEME_TYPE_MAP = new HashMap<String,Type>();
    static {
        SCHEME_TYPE_MAP.put("", Type.FRAGMENT);
        SCHEME_TYPE_MAP.put(SWORD_SCHEME, Type.SWORD);
        SCHEME_TYPE_MAP.put(BIBLE_SCHEME, Type.BIBLE);
        SCHEME_TYPE_MAP.put(COMMENTARY_SCHEME, Type.COMMENTARY);
        SCHEME_TYPE_MAP.put(DAILY_DEVOTION_SCHEME, Type.DAILY_DEVOTION);
        SCHEME_TYPE_MAP.put(DICTIONARY_SCHEME, Type.DICTIONARY);
        SCHEME_TYPE_MAP.put(GENERAL_BOOK_SCHEME, Type.GENERAL_BOOK);
        SCHEME_TYPE_MAP.put(GLOSSARY_SCHEME, Type.GLOSSARY);
        SCHEME_TYPE_MAP.put(OTHER_SCHEME, Type.OTHER);
        SCHEME_TYPE_MAP.put(QUESTIONABLE_SCHEME, Type.QUESTIONABLE);
        SCHEME_TYPE_MAP.put(HEBREW_STRONGS_SCHEME, Type.HEBREW_STRONGS);
        SCHEME_TYPE_MAP.put(GREEK_STRONGS_SCHEME, Type.GREEK_STRONGS);
        SCHEME_TYPE_MAP.put(GREEK_MORPH_SCHEME, Type.GREEK_MORPH);
        SCHEME_TYPE_MAP.put(HEBREW_MORPH_SCHEME, Type.HEBREW_MORPH);
    }
    
    
    protected String uriString;
    protected String[] parts;
    protected Type type;
    
    
    public SwordURI(String string) throws URISyntaxException {
        parts = parseParts(string);
        this.uriString = string;
        type = SCHEME_TYPE_MAP.get(parts[0].toLowerCase());
        if (type == null) {
            type = Type.FRAGMENT;
        }
        
        //System.out.println(parts[0] + " " + type);
        
    }
    
    public Type getType() {
        return type;
    }
    
    public String getScheme() {
        return parts[0];
    }
    
    public String getPath() {
        return parts[1];
    }
    
    public String getFragment() {
        return parts[2];
    }
    
    @Override
    public String toString() {
        return uriString;
    }
    
    /** parse the specified URI String into 3 parts 
     * <pre>
     * part[0] = scheme part or empty string
     * part[1] = path/book name part or empty string
     * part[2] = fragment/reference part or empty string
     * </pre>
     * @return String[] of parts
     * @throws {@code URISyntaxException} if parsing fail
     */
    public static String[] parseParts(String uri) throws URISyntaxException {
        String scheme = "";
        String path = "";
        String data = uri;
        int indexOf = data.indexOf(':');
        if (indexOf < 0) {
            // check for relative fragment
            if (data.charAt(0) != '#') {
                throw new URISyntaxException(uri, "Malformed URI Syntax");
            }
        } else {
            scheme = data.substring(0, indexOf).toLowerCase();
            data = data.substring(indexOf + 1);
        }

        if (data.startsWith("//")) {
            data = data.substring(2);
        } 
        
        indexOf = data.indexOf('/');
        if (indexOf > 0) {
            path = data.substring(0, indexOf);
            data = data.substring(indexOf + 1);
        }
        
        return new String[] { scheme, path, data  };
    }
    
    /** Construct {@code SwordURI} based on the parameter.
     * If success will return {@code SwordURI} else return null, 
     * it swallow {@code URISyntaxException} to make it simple
     * @param scheme the scheme or book category
     * @param book the book initials or book name
     * @param fragment the fragment
     * @return {@code SwordURI} or null if fail to construct
     * @see createURI(Book,String)
     */
    public static SwordURI createURI(String scheme, String book, String fragment) {
        // TODO improve implementation for various condition
        
        //System.out.println(scheme);
        
        StringBuilder sb = new StringBuilder();
        
        if (scheme != null && !scheme.equals("")) {
            Object obj = SCHEME_TYPE_MAP.get(scheme);
            if (obj == null) {
                return null;
            }
            sb.append(scheme.toLowerCase() + "://");
        }
        
        if (book != null && !book.equals("")) {
            sb.append(book + "/");
        }
        
        if (fragment != null) {
            sb.append(fragment);
        }
        
        SwordURI uri = null;
        try {
            //System.out.println(sb.toString());
            uri = new SwordURI(sb.toString());
        } catch (URISyntaxException ex) {
            // do nothing
        }
        
        //System.out.println(uri);
        
        return uri;
    }
    
    /** 
     * Construct {@code SwordURI} based on the parameter.
     * @param book the {@code Book}
     * @param fragment the fragment
     * @return {@code SwordURI} or null if fail to construct
     * @see #createURI(String,String,String)
     */
    public static SwordURI createURI(Book book, String fragment) {
        String scheme = SwordUtilities.bookCategoryToURIScheme(book.getBookCategory());
        return createURI(scheme,book.getInitials(),fragment);
    }
    
    /** Construct {@code SwordURI} based on the parameter.
     * @param uri unparsed uri
     * @return {@code SwordURI} or null if fail to construct
     */
    public static SwordURI createURI(String uri) {
        String[] parts = null;
        try {
            parts = SwordURI.parseParts(uri);
        } catch (URISyntaxException ex) {
            // do nothing
        }
        
        if (parts == null) { return null; }
        
        return createURI(parts[0],parts[1],parts[2]);
    }
}
