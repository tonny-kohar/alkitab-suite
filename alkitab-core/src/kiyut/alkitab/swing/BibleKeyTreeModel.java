/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.util.ResourceBundle;
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

    /** max level to be displayed by this model, default is LEVEL_ALL */
    protected int maxLevel;

    protected int beginFilter = 1;
    protected int endFilter = 66;
    
    public BibleKeyTreeModel() {
        this(LEVEL_ALL);
    }
    
    public TreeCellRenderer getTreeCellRendererComponent() {
        return new BibleKeyTreeCellRenderer();
    }
    
    public BibleKeyTreeModel(int maxLevel) {
        super(null);
        this.maxLevel = maxLevel;
        setFilter(1,66);
    }
    
    /** Recursive build this model 
     * @param node the {@code KeyTreeNode}
     * @param level one of the: LEVEL_BOOK, LEVEL_CHAPTER, LEVEL_VERSE
     */
    protected void buildModel(DefaultKeyTreeNode node, int level) throws NoSuchVerseException {
        if (maxLevel < level) {
            return;
        }
        
        if (level == LEVEL_BOOK) {
            VerseRange range = (VerseRange)node.getUserObject();
            Verse pStart = range.getStart();
            Verse pEnd = range.getEnd();
            
            int b = 1;
            
            for (int i = pStart.getBook(); i <= pEnd.getBook(); i++) {
                b = i;

                if (!(b >= beginFilter && b <= endFilter)) {
                    continue;
                }

                int ec = BibleInfo.chaptersInBook(b);
                int ev = BibleInfo.versesInChapter(b, ec);
                Verse start = new Verse(b, 1, 1);
                Verse end = new Verse(b, ec, ev);
                VerseRange childRange = new VerseRange(start, end);
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange,BibleKeyTreeNode.BOOK);
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
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange,BibleKeyTreeNode.CHAPTER);
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
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange,BibleKeyTreeNode.VERSE);
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

        // split OT and NT
        boolean split = false;
        if (begin <= 1 && end >= 66) {
            split = true;
        }

        VerseRange range = VerseRange.getWholeBibleVerseRange();
        BibleKeyTreeNode rootNode = new BibleKeyTreeNode(range, BibleKeyTreeNode.BIBLE);

        if (!split) {
            try {
                buildModel(rootNode, LEVEL_BOOK);
            } catch (NoSuchVerseException ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, ex.getMessage(), ex);
            }
            
        } else {

            ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());    

            try {
                Verse rStart = new Verse(1, 1, 1);
                Verse rEnd = new Verse(39, 4, 6);
                range = new VerseRange(rStart, rEnd);
                BibleKeyTreeNode otNode = new BibleKeyTreeNode(range, BibleKeyTreeNode.BIBLE, bundle.getString("OldTestament.Text"));
                buildModel(otNode, LEVEL_BOOK);
                rootNode.add(otNode);

            } catch (NoSuchVerseException ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, ex.getMessage(), ex);
            }

            try {
                Verse rStart = new Verse(40, 1, 1);
                Verse rEnd = new Verse(66, 22, 21);
                range = new VerseRange(rStart, rEnd);
                BibleKeyTreeNode ntNode = new BibleKeyTreeNode(range, BibleKeyTreeNode.BIBLE, bundle.getString("NewTestament.Text"));
                buildModel(ntNode, LEVEL_BOOK);
                rootNode.add(ntNode);
            } catch (NoSuchVerseException ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        setRoot(rootNode);
    }
}
