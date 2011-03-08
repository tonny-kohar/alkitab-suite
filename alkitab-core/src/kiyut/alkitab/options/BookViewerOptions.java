/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.awt.Color;
import java.io.File;
import java.util.prefs.Preferences;
import org.crosswire.jsword.book.sword.SwordBookPath;

/**
 * Implementation of Book Viewer Options
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

    /*
     * TODO
     *  make all public and available from propertyChangeListener
     */
    public static final String SESSION_PERSISTENCE = "session-persistence";
    public static final String SYNCHRONIZE_VIEW = "synchronize-view";
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
    public static final String BACKGROUND = "background";
    
    private boolean sessionPersistence;
    private boolean synchronizeView;
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
    private Color background;
    
    protected BookViewerOptions() {
        nodeName = "bookviewer";
        load();
    }
    
    
    /** 
     * <strong>note: </strong> The following books are the default
     * KJV, StrongsGreek, StrongsHebrew, Robinson, ot1nt2 unless it is overwrite by the user. <br/>
     * Why those books? Because they included with the installer :)
     */
    @Override
    public void load() {
        Preferences prefs = getPreferences();

        sessionPersistence = prefs.getBoolean(SESSION_PERSISTENCE, true);
        synchronizeView = prefs.getBoolean(SYNCHRONIZE_VIEW, false);
        parallelBookLimit = prefs.getInt(PARALLEL_BOOK_LIMIT, 5);
        defaultSearchLimit = prefs.getInt(DEFAULT_SEARCH_LIMIT, 50);
        versesLimit = prefs.getInt(VERSES_LIMIT, 176); // default 176, because Psalm 119 (176 verses)
        defaultBible = prefs.get(DEFAULT_BIBLE, "KJV");
        defaultDictionary = prefs.get(DEFAULT_DICTIONARY, null);
        defaultDailyDevotions = prefs.get(DEFAULT_DAILY_DEVOTIONS, "ot1nt2");
        defaultGreekStrongs = prefs.get(DEFAULT_GREEK_STRONGS, "StrongsGreek");
        defaultHebrewStrongs = prefs.get(DEFAULT_HEBREW_STRONGS, "StrongsHebrew");
        defaultGreekMorph = prefs.get(DEFAULT_GREEK_MORPH, "Robinson");

        String value = prefs.get(BACKGROUND, null);
        background = OptionsUtilities.stringToColor(value, null);
        
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
    
    @Override
    public void store() {
        Preferences prefs = getPreferences();
        prefs.putBoolean(SESSION_PERSISTENCE, sessionPersistence);
        prefs.putInt(PARALLEL_BOOK_LIMIT, parallelBookLimit);
        prefs.putInt(DEFAULT_SEARCH_LIMIT, defaultSearchLimit);
        prefs.putInt(VERSES_LIMIT, versesLimit);

        if (background != null) {
            prefs.put(BACKGROUND, OptionsUtilities.colorToString(background));
        } else {
            prefs.remove(BACKGROUND);
        }
        
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
                    sb.append(bookPaths[i].getPath()).append(File.pathSeparator);
                }
                sb.deleteCharAt(sb.length() - 1);
                prefs.put(BOOK_PATHS, sb.toString());
            }
        }
    }
 
    public void setSessionPersistence(boolean b) {
        boolean old = this.sessionPersistence;
        this.sessionPersistence = b;
        firePropertyChange(SESSION_PERSISTENCE, old, this.sessionPersistence);
    }
    
    public boolean isSessionPersistence() {
        return this.sessionPersistence;
    }

    public void setSynchronizeView(boolean b) {
        boolean old = this.synchronizeView;
        this.synchronizeView = b;
        firePropertyChange(SYNCHRONIZE_VIEW, old, this.synchronizeView);

        // special case, this is handled independenly from the above store methods,
        // because it is not managed by OptionPane
        Preferences prefs = getPreferences();
        prefs.putBoolean(SYNCHRONIZE_VIEW, synchronizeView);
    }

    public boolean isSynchronizeView() {
        return this.synchronizeView;
    }

    public void setParallelBookLimit(int limit) {
        if (limit < 2) {
            throw new IllegalArgumentException("limit should >= 2");
        }

        int old = this.parallelBookLimit;
        this.parallelBookLimit = limit;
        firePropertyChange(PARALLEL_BOOK_LIMIT, old, this.parallelBookLimit);
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


        int old = this.defaultSearchLimit;
        this.defaultSearchLimit = limit;
        firePropertyChange(DEFAULT_SEARCH_LIMIT, old, this.defaultSearchLimit);
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


        int old = this.versesLimit;
        this.versesLimit = limit;
        firePropertyChange(VERSES_LIMIT, old, this.versesLimit);
    }
    
    /** Return verses display limit. Zero (0) is unlimited */
    public int getVersesLimit() {
        return this.versesLimit;
    }
    
    public void setDefaultBible(String initials) {
        String old = this.defaultBible;
        this.defaultBible = initials;
        firePropertyChange(DEFAULT_BIBLE, old, this.defaultBible);
    }
    
    public String getDefaultBible() {
        return this.defaultBible;
    }
    
    public void setDefaultDictionary(String initials) {
        String old = this.defaultDictionary;
        this.defaultDictionary = initials;
        firePropertyChange(DEFAULT_DICTIONARY, old, this.defaultDictionary);
    }
    
    public String getDefaultDictionary() {
        return this.defaultDictionary;
    }
    
    public void setDefaultDailyDevotions(String initials) {
        String old = this.defaultDailyDevotions;
        this.defaultDailyDevotions = initials;
        firePropertyChange(DEFAULT_DAILY_DEVOTIONS, old, this.defaultDailyDevotions);
    }
    
    public String getDefaultDailyDevotions() {
        return this.defaultDailyDevotions;
    }
    
    public void setDefaultGreekStrongs(String initials) {
        String old = this.defaultGreekStrongs;
        this.defaultGreekStrongs = initials;
        firePropertyChange(DEFAULT_GREEK_STRONGS, old, this.defaultGreekStrongs);
    }
    
    public String getDefaultGreekStrongs() {
        return this.defaultGreekStrongs;
    }

    public void setDefaultHebrewStrongs(String initials) {
        String old = this.defaultHebrewStrongs;
        this.defaultHebrewStrongs = initials;
        firePropertyChange(DEFAULT_HEBREW_STRONGS, old, this.defaultHebrewStrongs);
    }
    
    public String getDefaultHebrewStrongs() {
        return this.defaultHebrewStrongs;
    }
    
    public void setDefaultGreekMorph(String initials) {
        String old = this.defaultGreekMorph;
        this.defaultGreekMorph = initials;
        firePropertyChange(DEFAULT_GREEK_MORPH, old, this.defaultGreekMorph);
    }
    
    public String getDefaultGreekMorph() {
        return this.defaultGreekMorph;
    }
    
    public void setDownloadPath(File path) {
        File old = this.downloadPath;
        this.downloadPath = path;
        firePropertyChange(DOWNLOAD_PATH, old, this.downloadPath);
    }
    
    public File getDownloadPath() {
        return downloadPath;
    }
    
    public void setBookPaths(File[] paths) {
        File[] old = this.bookPaths;
        this.bookPaths = paths;
        firePropertyChange(BOOK_PATHS, old, this.bookPaths);
    }
    
    public File[] getBookPaths() {
        return this.bookPaths;
    }

    /** if set to null means use default */
    public void setBackground(Color color) {
        Color old = this.background;
        this.background = color;
        firePropertyChange(BACKGROUND, old, this.background);
    }

    public Color getBackground() {
        return background;
    }
}
