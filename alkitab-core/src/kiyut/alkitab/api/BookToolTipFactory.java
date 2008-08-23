/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

/**
 * Factory for BookToolTip, 
 * Currently:
 * <pre>
 * - it return DefaultBookToolTip as default.
 * - there is no way to change the implementation without modifying source code
 * - some idea, use Java service META-INF or properties file for setting implementation
 * </pre>
 * 
 * 
 */
public final class BookToolTipFactory {
    private static BookToolTipFactory instance; // The single instance
    static {
        instance = new BookToolTipFactory();
    }
    
    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static BookToolTipFactory getInstance() {
        return instance;
    }
    
    private BookToolTip toolTip = null;
    
    private BookToolTipFactory() {
        // private for singleton
    }
    
    
    public BookToolTip getToolTip() {
        if (toolTip == null) {
            toolTip = new DefaultBookToolTip();
        }
        return toolTip;
    }
}
