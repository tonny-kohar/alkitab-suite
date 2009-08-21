/* This work has been placed into the public domain. */

package kiyut.alkitab.navigator;

/**
 *
 * 
 */
public class BibleKeyTreeNode extends DefaultKeyTreeNode {

    /** Bible Category, it is used for anything other than BOOK, CHAPTER and VERSE*/
    public static final int BIBLE = 0;
    public static final int BOOK = 1;
    public static final int CHAPTER = 2;
    public static final int VERSE = 3;
    
    protected int category;
    protected String name = null;

    /**
     * Create new BibleKeyTreeNode
     * @param userObject a Key
     * @param category the category eg: BIBLE, BOOK, CHAPTER, VERSE
     */
    public BibleKeyTreeNode(Object userObject, int category) {
        this(userObject,category,null);
    }

    /**
     * Create new BibleKeyTreeNode
     * @param userObject a Key
     * @param category the category eg: BIBLE, BOOK, CHAPTER, VERSE
     * @param  name is used to overide the toString or getName
     */
    public BibleKeyTreeNode(Object userObject, int category, String name) {
        super(userObject);
        this.category = category;
        if (name != null) {
            this.name = name;
        }
    }

    public int getCategory() {
        return category;
    }

    public String getName() {
        return toString();
    }

    /** {@inheritDoc}
     * If in the constructor param name is specified,
     * this will return that param name instead
     */
    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        return super.toString();
    }
}
