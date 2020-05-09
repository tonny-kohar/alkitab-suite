/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

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
        setFilter(1, 66);
    }
    
    /** Build all nodes for this model
     * use versification system JSWORD 2.0
     */
    /*protected void buildModel() {
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
    }*/
    
    /** Recursive build this model 
     * @param node the {@code KeyTreeNode}
     * @param level one of the: LEVEL_BOOK, LEVEL_CHAPTER, LEVEL_VERSE
     * @throws org.crosswire.jsword.passage.NoSuchVerseException
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

    
    
    /** 
     * Set the filter based on b index 1-66, default is display all books.
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
    
    /** Just a helper method to build model based on book, use versification system JSWORD 2.0 */
    /*private void buildBook(BibleBook startBook, BibleBook endBook,  BibleKeyTreeNode node,  String name) throws NoSuchVerseException {
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
    }*/
}
