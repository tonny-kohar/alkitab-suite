/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import kiyut.alkitab.util.FontUtilities;
import org.crosswire.common.util.CWProject;
import org.crosswire.common.util.FileUtil;
import org.crosswire.common.util.Language;
import org.crosswire.common.util.NetUtil;
import org.crosswire.common.util.PropertyMap;
import org.crosswire.common.util.ResourceUtil;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookMetaData;

/**
 * Reimplementation of org.crosswire.common.swing.FontStore <br/>
 * It is used to specify each book font.<br/>
 * The persistence location is same with JSword<br/>
 *
 */
public class BookFontStore {

    private static BookFontStore instance; // The single instance

    static {
        instance = new BookFontStore();
    }

    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static BookFontStore getInstance() {
        return instance;
    }
    
    protected static final String DEFAULT_FONT = "Dialog-PLAIN-12";
    protected static final String LANG_KEY_PREFIX = "lang.";
    protected static final String DEFAULT_KEY = "default";
    
    protected String storeName;
    protected String defaultFont;
    protected URI fontStore;
    protected boolean loaded;
    protected PropertyMap fontMap;

    /**
     * Create a persistent Book Font Store. The persistence location is same with JSword
     *
     */
    private BookFontStore() {
        // use the same persistence location as JSword
        this("BookFonts", CWProject.instance().getWritableProjectDir());
    }

    private BookFontStore(String storeName, URI fontDir) {
        if (fontDir == null) {
            throw new IllegalArgumentException("fontStore cannot be null");
        }
        this.storeName = storeName;
        this.fontStore = NetUtil.lengthenURI(fontDir, this.storeName + FileUtil.EXTENSION_PROPERTIES);
        this.fontMap = new PropertyMap();
    }

    /**
     * @param defaultFont the defaultFont to set
     */
    public void setDefaultFont(String defaultFont) {
        load();
        this.defaultFont = defaultFont;
        fontMap.put(DEFAULT_KEY, defaultFont);
        store();
    }
    
    /**
     * @return the defaultFont
     */
    public String getDefaultFont() {
        load();
        defaultFont = fontMap.get(DEFAULT_KEY, DEFAULT_FONT);
        return defaultFont;
    }
    
    /**
     * Remove the font settings for a given key
     * 
     * @param key
     *            the book initials or language code
     */
    public void resetFont(String key) {
        load();
        fontMap.remove(key);
        store();
    }
    
    /**
     * Only convenience method for setFont(String resource, Font font) 
     * the resources is book.getInitials
     *
     * @param book the book
     * @param font the font
     */
    public void setFont(Book book, Font font) {
        setFont(book.getInitials(), font);
    }

    public void setFont(String resource, Font font) {
        if (resource == null || font == null) {
            return;
        }
        load();
        fontMap.put(resource, FontUtilities.font2String(font));
        store();
    }

    /**
     * Get the most appropriate font for the book.<br/>
     *
     * @param book the book
     * @return the font
     */
    public Font getFont(Book book) {
        // almost straight copy-paste with slight modification from
        // org.crosswire.bibledesktop.book.install.BookFont

        String fontName = (String) book.getBookMetaData().getProperty(BookMetaData.KEY_FONT);
        String fontSpec = getDefaultFont();
        if (fontName != null) {
            Font bookFont = FontUtilities.deriveFont(fontSpec, fontName);
            // Make sure it is installed. Java does substitution. Make sure we got what we wanted.
            if (bookFont.getFamily().equalsIgnoreCase(fontName)) {
                fontSpec = FontUtilities.font2String(bookFont);
            }
        }

        return getFont(book.getInitials(), book.getLanguage(), fontSpec);
    }

    public Font getFont(String resource, Language lang, String fallback) {
        load();

        String fontSpec = null;
        if (resource != null) {
            fontSpec = fontMap.get(resource);
        }

        if (fontSpec != null) {
            Font obtainedFont = obtainFont(fontSpec);
            if (obtainedFont != null) {
                return obtainedFont;
            }
            fontSpec = null;
        }

        if (lang != null) {
            fontSpec = fontMap.get(new StringBuilder(LANG_KEY_PREFIX).append(lang.getCode()).toString());
        }

        if (fontSpec != null) {
            Font obtainedFont = obtainFont(fontSpec);
            if (obtainedFont != null) {
                return obtainedFont;
            }
        }

        fontSpec = fallback;
        if (fontSpec != null) {
            Font obtainedFont = obtainFont(fontSpec);
            if (obtainedFont != null) {
                return obtainedFont;
            }
        }

        return FontUtilities.string2Font(defaultFont);
    }

    protected Font obtainFont(String fontSpec) {
        if (fontSpec != null) {
            // Creating a font never fails. Java just silently does
            // substitution.
            // Ensure that substitution does not happen.
            Font obtainedFont = FontUtilities.string2Font(fontSpec);
            String obtainedFontSpec = FontUtilities.font2String(obtainedFont);
            if (obtainedFontSpec != null && obtainedFontSpec.equalsIgnoreCase(fontSpec)) {
                return obtainedFont;
            }
        }
        return null;
    }

    /**
     * Load the store, if it has not been loaded.
     */
    protected void load() {
        if (loaded) {
            return;
        }

        try {
            fontMap = ResourceUtil.getProperties(storeName);
            loaded = true;
        } catch (IOException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage());
            fontMap = new PropertyMap();
        }
    }

    /**
     * Store the store, if it exists.
     */
    protected void store() {
        load();

        try {
            NetUtil.storeProperties(fontMap, fontStore, storeName);
        } catch (IOException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
}
