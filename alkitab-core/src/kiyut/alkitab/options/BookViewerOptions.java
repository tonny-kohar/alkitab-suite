/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.io.File;
import java.util.prefs.Preferences;
import org.crosswire.jsword.book.sword.SwordBookPath;

/**
 *
 * 
 */
public final class BookViewerOptions extends AbstractOptions {
    private static BookViewerOptions instance; // The single instance
    static {
        instance = new BookViewerOptions();
    }
    
    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static BookViewerOptions getInstance() {
        return instance;
    }
    
    private static final String PARALLEL_BOOK_LIMIT = "parallel-book-limit";
    private static final String VERSES_LIMIT = "verses-limit";
    private static final String DEFAULT_SEARCH_LIMIT = "default-search-limit";
    private static final String DEFAULT_BIBLE = "default-bible";
    private static final String DEFAULT_DICTIONARY = "default-dictionary";
    private static final String DEFAULT_DAILY_DEVOTIONS = "default-daily-devotions";
    private static final String DEFAULT_GREEK_STRONGS = "default-greek-strongs";
    private static final String DEFAULT_HEBREW_STRONGS = "default-hebrew-strongs";
    private static final String DEFAULT_GREEK_MORPH = "default-greek-morph";
    private static final String BOOK_PATHS = "book-paths";
    private static final String DOWNLOAD_PATH = "download-path";
    
    private int parallelBookLimit;
    private int defaultSearchLimit;
    private int versesLimit;
    private String defaultBible;
    private String defaultDictionary;
    private String defaultDailyDevotions;
    private String defaultGreekStrongs;
    private String defaultHebrewStrongs;
    private String defaultGreekMorph;
    private File[] bookPaths;
    private File downloadPath;
    
    protected BookViewerOptions() {
        nodeName = "bookviewer";
        load();
    }
    
    public void load() {
        Preferences prefs = getPreferences();

        parallelBookLimit = prefs.getInt(PARALLEL_BOOK_LIMIT, 5);
        defaultSearchLimit = prefs.getInt(DEFAULT_SEARCH_LIMIT, 50);
        versesLimit = prefs.getInt(VERSES_LIMIT, 176); // default 176, because Psalm 119 (176 verses)
        defaultBible = prefs.get(DEFAULT_BIBLE, null);
        defaultDictionary = prefs.get(DEFAULT_DICTIONARY, null);
        defaultDailyDevotions = prefs.get(DEFAULT_DAILY_DEVOTIONS, null);
        defaultGreekStrongs = prefs.get(DEFAULT_GREEK_STRONGS, null);
        defaultHebrewStrongs = prefs.get(DEFAULT_HEBREW_STRONGS, null);
        defaultGreekMorph = prefs.get(DEFAULT_GREEK_MORPH, null);
        
        downloadPath = SwordBookPath.getDownloadDir();
        String path = prefs.get(DOWNLOAD_PATH, null);
        if (path != null) {
            downloadPath = new File(path);
        }
        
        bookPaths = SwordBookPath.getAugmentPath();
        
        String str = prefs.get(BOOK_PATHS, null);
        if (str != null) {
            String[] paths = str.split(File.pathSeparator);
            bookPaths = new File[paths.length];
            for (int i=0; i<paths.length; i++) {
                bookPaths[i] = new File(paths[i]);
            }
        }
    }
    
    public void save() {
        Preferences prefs = getPreferences();
        prefs.putInt(PARALLEL_BOOK_LIMIT, parallelBookLimit);
        prefs.putInt(DEFAULT_SEARCH_LIMIT, defaultSearchLimit);
        prefs.putInt(VERSES_LIMIT, versesLimit);
        
        if (defaultBible != null) {
            prefs.put(DEFAULT_BIBLE, defaultBible);
        } else {
            prefs.remove(DEFAULT_BIBLE);
        }
        if (defaultDictionary != null) {
            prefs.put(DEFAULT_DICTIONARY, defaultDictionary);
        } else {
            prefs.remove(DEFAULT_DICTIONARY);
        }
        if (defaultDailyDevotions != null) {
            prefs.put(DEFAULT_DAILY_DEVOTIONS, defaultDailyDevotions);
        } else {
            prefs.remove(DEFAULT_DAILY_DEVOTIONS);
        }
        if (defaultGreekStrongs != null) {
            prefs.put(DEFAULT_GREEK_STRONGS, defaultGreekStrongs);
        } else {
            prefs.remove(DEFAULT_GREEK_STRONGS);
        }
        if (defaultHebrewStrongs != null) {
            prefs.put(DEFAULT_HEBREW_STRONGS, defaultHebrewStrongs);
        } else {
            prefs.remove(DEFAULT_HEBREW_STRONGS);
        }
        if (defaultGreekMorph != null) {
            prefs.put(DEFAULT_GREEK_MORPH, defaultGreekMorph);
        } else {
            prefs.remove(DEFAULT_GREEK_MORPH);
        }
        
        if (downloadPath != null) {
            prefs.put(DOWNLOAD_PATH, downloadPath.getPath());
        } else {
            prefs.remove(DOWNLOAD_PATH);
        }
        
        if (bookPaths == null) {
            prefs.remove(BOOK_PATHS);
        } else {
            if (bookPaths.length == 0) {
                prefs.remove(BOOK_PATHS);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bookPaths.length; i++) {
                    sb.append(bookPaths[i].getPath() + File.pathSeparator);
                }
                sb.deleteCharAt(sb.length() - 1);
                prefs.put(BOOK_PATHS, sb.toString());
            }
        }
    }
 
    public void setParallelBookLimit(int limit) {
        if (limit < 2) {
            throw new IllegalArgumentException("limit should >= 2");
        }
        this.parallelBookLimit = limit;
    }
    
    public int getParallelBookLimit() {
        return this.parallelBookLimit;
    }
    
    /** Set default search limit. Zero (0) is unlimited 
     * @param limit int specifiy the limit
     */
    public void setDefaultSearchLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("negative limit is not allowed");
        }
        this.defaultSearchLimit = limit;
    }
    
    /** Return default search limit. Zero (0) is unlimited */
    public int getDefaultSearchLimit() {
        return this.defaultSearchLimit;
    }
    
    /** Set verses display limit. Zero (0) is unlimited 
     * @param limit int specifiy the limit*/
    public void setVersesLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("negative limit is not allowed");
        }
        this.versesLimit = limit;
    }
    
    /** Return verses display limit. Zero (0) is unlimited */
    public int getVersesLimit() {
        return this.versesLimit;
    }
    
    public void setDefaultBible(String initials) {
        this.defaultBible = initials;
    }
    
    public String getDefaultBible() {
        return this.defaultBible;
    }
    
    public void setDefaultDictionary(String initials) {
        this.defaultDictionary = initials;
    }
    
    public String getDefaultDictionary() {
        return this.defaultDictionary;
    }
    
    public void setDefaultDailyDevotions(String initials) {
        this.defaultDailyDevotions = initials;
    }
    
    public String getDefaultDailyDevotions() {
        return this.defaultDailyDevotions;
    }
    
    public void setDefaultGreekStrongs(String initials) {
        this.defaultGreekStrongs = initials;
    }
    
    public String getDefaultGreekStrongs() {
        return this.defaultGreekStrongs;
    }

    public void setDefaultHebrewStrongs(String initials) {
        this.defaultHebrewStrongs = initials;
    }
    
    public String getDefaultHebrewStrongs() {
        return this.defaultHebrewStrongs;
    }
    
    public void setDefaultGreekMorph(String initials) {
        this.defaultGreekMorph = initials;
    }
    
    public String getDefaultGreekMorph() {
        return this.defaultGreekMorph;
    }
    
    public void setDownloadPath(File path) {
        this.downloadPath = path;
    }
    
    public File getDownloadPath() {
        return downloadPath;
    }
    
    public void setBookPaths(File[] paths) {
        this.bookPaths = paths;
    }
    
    public File[] getBookPaths() {
        return this.bookPaths;
    }
}
