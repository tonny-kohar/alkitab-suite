/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import org.crosswire.jsword.passage.NoSuchVerseException;
import org.crosswire.jsword.passage.Verse;
import org.crosswire.jsword.passage.VerseRange;
import org.crosswire.jsword.versification.BibleInfo;

/**
 * TreeModel for Bible (book, chapter, verse)
 */
public class BibleKeyTreeModel extends DefaultTreeModel implements KeyTreeModel {
    
    public static final int LEVEL_BOOK = 0;
    public static final int LEVEL_CHAPTER = 1;
    public static final int LEVEL_VERSE = 2;
    public static final int LEVEL_ALL = Integer.MAX_VALUE;
    
    /** max level to be displayed by this model */
    protected int maxLevel;

    protected int beginFilter = 1;
    protected int endFilter = 99;
    
    public BibleKeyTreeModel() {
        this(LEVEL_ALL);
    }
    
    public TreeCellRenderer getTreeCellRendererComponent() {
        return new BibleKeyTreeCellRenderer();
    }
    
    public BibleKeyTreeModel(int maxLevel) {
        super(null);
        
        this.maxLevel = maxLevel;
        
        VerseRange range = VerseRange.getWholeBibleVerseRange();
        
        DefaultKeyTreeNode rootNode = new DefaultKeyTreeNode(range);
        try {
            buildModel(rootNode,LEVEL_BOOK);
        } catch (NoSuchVerseException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(),ex);
        }
        setRoot(rootNode);
    }
    
    /** Recursive build this model 
     * @param key the {@code Key}
     */
    protected void buildModel(DefaultKeyTreeNode node, int level) throws NoSuchVerseException {
        if (maxLevel < level) {
            return;
        }
        if (level == LEVEL_BOOK) {
            int b = 1; // book
            int count = BibleInfo.booksInBible();
            for (int i = 0; i < count; i++) {
                b = i + 1;
                if (!(b >= beginFilter && b <= endFilter)) {
                    continue;
                }
                int ec = BibleInfo.chaptersInBook(b);
                int ev = BibleInfo.versesInChapter(b, ec);
                Verse start = new Verse(b, 1, 1);
                Verse end = new Verse(b, ec, ev);
                VerseRange childRange = new VerseRange(start, end);
                DefaultKeyTreeNode child = new DefaultKeyTreeNode(childRange);
                node.add(child);
                buildModel(child, LEVEL_CHAPTER);

            }
        } else if (level == LEVEL_CHAPTER) {
            VerseRange range = (VerseRange)node.getUserObject();
            Verse verse = range.getStart();
            int b = verse.getBook(); // book
            int c = 0; // chapter
            int count = BibleInfo.chaptersInBook(b);
            for (int i = 0; i < count; i++) {
                c = i + 1;
                int ev = BibleInfo.versesInChapter(b, c);
                Verse start = new Verse(b, c, 1);
                Verse end = new Verse(b, c, ev);
                VerseRange childRange = new VerseRange(start, end);
                DefaultKeyTreeNode child = new DefaultKeyTreeNode(childRange);
                node.add(child);
                buildModel(child, LEVEL_VERSE);
            }
        } else if (level == LEVEL_VERSE) {
            VerseRange range = (VerseRange)node.getUserObject();
            Verse verse = range.getStart();
            int b = verse.getBook(); // book
            int c = verse.getChapter(); // chapter
            int v = 0; // verse
            int count = BibleInfo.versesInChapter(b,c);
            for (int i = 0; i < count; i++) {
                v = i + 1;
                Verse start = new Verse(b, c, v);
                Verse end = start;
                VerseRange childRange = new VerseRange(start, end);
                DefaultKeyTreeNode child = new DefaultKeyTreeNode(childRange);
                node.add(child);
            }
        }
    }

    /** Set the filter based on book index 1-66, default is display all books.
     * @param begin begin index
     * @param end end index
     */
    public void setFilter(int begin, int end) {
        this.beginFilter = begin;
        this.endFilter = end;

        VerseRange range = VerseRange.getWholeBibleVerseRange();
        DefaultKeyTreeNode rootNode = new DefaultKeyTreeNode(range);

        try {
            buildModel(rootNode,LEVEL_BOOK);
        } catch (NoSuchVerseException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(),ex);
        }
        
        setRoot(rootNode);
    }
}
