/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import kiyut.alkitab.options.ViewerHintsOptions;
import kiyut.alkitab.bookviewer.BookTextPane;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * DefaultBookToolTip popup implementation. 
 * Internally, it is using {@link javax.swing.PopupFactory PopupFactory}.
 * This implementation is sharing the popup, so only 1 can be available at one time.
 * 
 */
public class DefaultBookToolTip implements BookToolTip {

    /** PHI of Golden Ratio */
    static double PHI = 1.618033988749895;

    protected BookTextPane bookTextPane;
    protected Popup popup;
    protected JScrollPane scrollPane;
    
    /** Just Point object act as cache for performance reason, so it does not always recreate Point object */
    protected Point prefPoint = new Point();

    public DefaultBookToolTip() {
        ViewerHints<ViewerHints.Key,Object> viewerHints = new ViewerHints<ViewerHints.Key, Object>(ViewerHintsOptions.getInstance().getViewerHints());

        // special viewer hints for tooltip eg: do not show notes, strongs, etc
        
        viewerHints.put(ViewerHints.STRONGS_NUMBERS, false);
        viewerHints.put(ViewerHints.START_VERSE_ON_NEWLINE, false);
        viewerHints.put(ViewerHints.MORPH, false);
        viewerHints.put(ViewerHints.VERSE_NUMBERS, true);
        viewerHints.put(ViewerHints.NO_VERSE_NUMBERS, false);
        viewerHints.put(ViewerHints.BOOK_CHAPTER_VERSE_NUMBERS, false);
        viewerHints.put(ViewerHints.CHAPTER_VERSE_NUMBERS, false);
        viewerHints.put(ViewerHints.TINY_VERSE_NUMBERS, true);
        viewerHints.put(ViewerHints.HEADINGS, true);
        viewerHints.put(ViewerHints.NOTES, false);
        
        bookTextPane = new BookTextPane(viewerHints);
        
        bookTextPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                hide();
            }
        });
        
        /*Color background = new ColorUIResource(255, 247, 200); // The color is #fff7c8.
        Border border = BorderFactory.createLineBorder(new Color(76,79,83)); // The color is #4c4f53.
        
        bookTextPane.setBackground(background);
        bookTextPane.setForeground(UIManager.getColor("Tooltip.foreground"));
        bookTextPane.setBorder(border);
         */
        
        //bookTextPane.setBackground(UIManager.getColor("Tooltip.background"));
        //bookTextPane.setForeground(UIManager.getColor("Tooltip.foreground"));
        
        bookTextPane.setBackground(UIManager.getColor("info"));
        bookTextPane.setForeground(UIManager.getColor("infoText"));
        
        
        // no longer need border because now it is contained in JScrollPane
        /*Border border = UIManager.getBorder("Tooltip.border");
        if (border == null) {
            border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(76,79,83)), BorderFactory.createEmptyBorder(6,6,6,6));
        }
        bookTextPane.setBorder(border);
         */


        //bookTextPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);

        scrollPane = new JScrollPane(bookTextPane);
        
    }
    
    @Override
    public void show(Book book, Key key, Component owner, int x, int y) {
        hide(); // hide the prev popup if any is opened
        
        if (book == null || key == null) {
            throw new IllegalArgumentException("book or key should not be null"); //NOI18N
        }
        
        List<Book> books = bookTextPane.getBooks();
        books.clear();
        
        books.add(book);
        bookTextPane.setKey(key);

        bookTextPane.reload(false);

        // XXX workaround for pack/panel prefSize problem
        // Note: it still have bug regarding the size

        bookTextPane.setSize(new Dimension(0,0));
        bookTextPane.setPreferredSize(null);
        scrollPane.setPreferredSize(null);
        bookTextPane.revalidate();
        bookTextPane.setVisible(true);

        // use Golden Ratio to specify the prefSize
        Dimension size = bookTextPane.getPreferredSize();
        if (size.width == 0 || size.height == 0) {
            size = bookTextPane.getSize();
        }


        int adjust = 25; // some adjustment to for estimating scroll bar size ~25px
        double maxW = 500-adjust;
        double maxH = maxW/PHI;
        if (size.getWidth() > maxW)  {
            if (size.getHeight() < maxH) {
                maxH = size.getHeight();
            }
        } else {
            maxW = size.getWidth();
        }
        
        if (size.getHeight() > maxH)  {
            if (size.getWidth() < maxW) {
                maxW = size.getWidth();
            }
        } else {
            maxH = size.getHeight();
        }

        size.setSize(maxW, maxH);
        bookTextPane.setPreferredSize(size);
        scrollPane.setPreferredSize(new Dimension(size.width + adjust, size.height + adjust));
        
        bookTextPane.revalidate();
            
        prefPoint.setLocation(x,y);
        getPreferredLocation(owner, prefPoint);
        
        //popup = PopupFactory.getSharedInstance().getPopup(owner, bookTextPane, prefPoint.x, prefPoint.y);
        popup = PopupFactory.getSharedInstance().getPopup(owner, scrollPane, prefPoint.x, prefPoint.y);
        popup.show();
    }
    
    @Override
    public void hide() {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }
    
    /** Return preferred popup location based on ScreenSize, etc so it does not overlap 
     * with cursor and Screen Size.
     * @param owner Component mouse coordinates are relative to, maybe null
     * @param point inital prefPoint location that will be adjusted based on various factor
     */
    protected Point getPreferredLocation(Component owner, Point point) {
        int adjust = 20; // some pixel adjustment for cursor prefSize
        int screenInset = 20; // some pixel adjustment for screen inset
        
        if (owner == null) {
            //point.setLocation(x, y + adjust);
            return point;
        }
        
        SwingUtilities.convertPointToScreen(prefPoint, owner);
        
        Dimension scDim = Toolkit.getDefaultToolkit().getScreenSize();
        scDim.setSize(scDim.width-screenInset, scDim.height-screenInset);
        //Dimension popupDim = bookTextPane.getSize();
        Dimension popupDim = scrollPane.getPreferredSize();
        
        int x = point.x;
        int y = point.y;
        
        if (x + popupDim.width  > scDim.width) {
            x = x - popupDim.width;
        }
        
        if (y + popupDim.height  > scDim.height) {
            y = y - (popupDim.height + adjust);
        } else {
            y = y + adjust;
        }
        
        point.setLocation(x,y);

        return point;
    }
}
