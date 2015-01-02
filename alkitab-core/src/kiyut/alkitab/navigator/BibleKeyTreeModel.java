/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import org.crosswire.common.icu.NumberShaper;
import org.crosswire.jsword.passage.NoSuchVerseException;
import org.crosswire.jsword.passage.Verse;
import org.crosswire.jsword.passage.VerseRange;
import org.crosswire.jsword.versification.BibleBook;
import org.crosswire.jsword.versification.Versification;
import org.crosswire.jsword.versification.system.Versifications;

/**
 * TreeModel for Bible (book, chapter, verse)
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class BibleKeyTreeModel extends DefaultTreeModel implements KeyTreeModel {
    
    public static final int LEVEL_BOOK = 1;
    public static final int LEVEL_CHAPTER = 2;
    public static final int LEVEL_VERSE = 3;
    public static final int LEVEL_ALL = Integer.MAX_VALUE;

    /** max level to be displayed by this model, default is LEVEL_ALL */
    protected int maxLevel = LEVEL_ALL;

    protected int beginFilter = 1;
    protected int endFilter = 66;
    
    protected Versification v11n = Versifications.instance().getVersification(Versifications.DEFAULT_V11N);
    protected NumberShaper shaper = new NumberShaper();
    
    public BibleKeyTreeModel() {
        this(LEVEL_ALL);
    }
    
    @Override
    public TreeCellRenderer getTreeCellRendererComponent() {
        return new BibleKeyTreeCellRenderer();
    }
    
    public BibleKeyTreeModel(int maxLevel) {
        super(null);
        this.maxLevel = maxLevel;
        buildModel();
    }
    
    /** Build all nodes for this model
     */
    protected void buildModel() {
        VerseRange range = v11n.getAllVerses();
        BibleKeyTreeNode rootNode = new BibleKeyTreeNode(range, BibleKeyTreeNode.BIBLE);
        
        ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());

        try {
            BibleBook sb = v11n.getBook(BibleBook.INTRO_BIBLE.ordinal());
            BibleBook eb = sb;
            buildBook(sb, eb, rootNode, bundle.getString("BibleIntroduction.Text"));
        } catch (NoSuchVerseException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }

        try {
            BibleBook sb = v11n.getBook(BibleBook.GEN.ordinal());
            BibleBook eb = v11n.getBook(BibleBook.MAL.ordinal());
            buildBook(sb, eb, rootNode, bundle.getString("OldTestament.Text"));
        } catch (NoSuchVerseException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }

        try {
            BibleBook sb = v11n.getBook(BibleBook.MATT.ordinal());
            BibleBook eb = v11n.getBook(BibleBook.REV.ordinal());
            buildBook(sb, eb, rootNode, bundle.getString("NewTestament.Text"));
        } catch (NoSuchVerseException ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }


        setRoot(rootNode);
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
            BibleBook b = range.getStart().getBook();
            int count = v11n.getBookCount(range.getStart(), range.getEnd());
            for (int i = 0; i < count; i++) {
                int ec = v11n.getLastChapter(b);
                int ev = v11n.getLastVerse(b, ec);
                Verse start = new Verse(v11n, b, 0, 0);
                Verse end = new Verse(v11n, b, ec, ev);
                VerseRange childRange = new VerseRange(v11n, start, end);
                String name = childRange.getVersification().getPreferredName(b);
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange,BibleKeyTreeNode.BOOK, name);
                node.add(child);
                buildModel(child, LEVEL_CHAPTER);
                b = v11n.getNextBook(b);
                if (b == null) {
                    break;
                }
            }
            
        } else if (level == LEVEL_CHAPTER) {
            VerseRange range = (VerseRange)node.getUserObject();
            BibleBook b = range.getStart().getBook();
            int count = v11n.getLastChapter(b);
            for (int c = 0; c <= count; c++) {
                int ev = v11n.getLastVerse(b, c);
                Verse start = new Verse(v11n, b, c, 0);
                Verse end = new Verse(v11n, b, c, ev);
                VerseRange childRange = new VerseRange(v11n, start, end);
                String name = shaper.shape(childRange.toString());
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange, BibleKeyTreeNode.CHAPTER, name);
                node.add(child);
                buildModel(child, LEVEL_VERSE);
            }
        } else if (level == LEVEL_VERSE) {
            VerseRange range = (VerseRange)node.getUserObject();
            BibleBook b = range.getStart().getBook(); // book
            int c = range.getStart().getChapter(); // chapter
            int count = v11n.getLastVerse(b, c);
            for (int v = 0; v <= count; v++) {
                Verse start = new Verse(v11n, b, c, v);
                Verse end = start;
                VerseRange childRange = new VerseRange(v11n,start, end);
                String name = shaper.shape(childRange.toString());
                BibleKeyTreeNode child = new BibleKeyTreeNode(childRange,BibleKeyTreeNode.VERSE, name);
                node.add(child);
            }
        }
    }

    
    
    /** Set the filter based on b index 1-66, default is display all books.
     * @param begin begin index
     * @param end end index
     * @deprecated not to be used anymore
     */
    public void setFilter(int begin, int end) {
               
    }
    
    /** Just a helper method to build model based on book */
    private void buildBook(BibleBook startBook, BibleBook endBook,  BibleKeyTreeNode node,  String name) throws NoSuchVerseException {
        BibleBook sb = startBook;
        BibleBook eb = endBook;
        int ec = v11n.getLastChapter(eb);
        int ev = v11n.getLastVerse(eb, ec);
        Verse start = new Verse(v11n, sb, 0, 0);
        Verse end = new Verse(v11n, eb, ec, ev);
        VerseRange range = new VerseRange(v11n, start, end);
        BibleKeyTreeNode childNode = new BibleKeyTreeNode(range, BibleKeyTreeNode.BIBLE, name);
        buildModel(childNode, LEVEL_BOOK);
        node.add(childNode);
    }
}
