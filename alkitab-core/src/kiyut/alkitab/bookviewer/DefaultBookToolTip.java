/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import kiyut.alkitab.options.ViewerHintsOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;
import org.openide.util.lookup.ServiceProvider;

/**
 * DefaultBookToolTip popup implementation. 
 * Internally, it is using {@link javax.swing.PopupFactory PopupFactory}.
 * This implementation is sharing the popup. So only one popup can be displayed at a time.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ServiceProvider(service=BookToolTip.class, path="Alkitab/BookToolTip")
public class DefaultBookToolTip implements BookToolTip {

    /** PHI of Golden Ratio */
    static double PHI = 1.618033988749895;

    protected TextPaneRenderer renderer;
    protected Popup popup;
    //protected JScrollPane scrollPane;
    
    /** Just Point object act as cache for performance reason, so it does not always recreate Point object */
    protected Point prefPoint = new Point();

    public DefaultBookToolTip() {
        ViewerHints<ViewerHints.Key,Object> viewerHints = new ViewerHints<>(ViewerHintsOptions.getInstance().getViewerHints());

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
        
        renderer = new TextPaneRenderer(viewerHints);
        
        renderer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                hide();
            }
        });
        
        Border emptyBorder = BorderFactory.createEmptyBorder(2, 6, 6, 6);
        Border border = UIManager.getBorder("ToolTip.border");
        if (border != null) {
            border = BorderFactory.createCompoundBorder(border, emptyBorder);
        } else {
            border = emptyBorder;
        }
        renderer.setBorder(border);
        
        // TODO set it to Platform ToolTip.Background, Foreground, and Font (not font incase it is using other language eg: greek font)
        renderer.setBackground(UIManager.getColor("ToolTip.background"));
        renderer.setForeground(UIManager.getColor("ToolTip.foreground"));
        renderer.setFont(UIManager.getFont("ToolTip.font"));
        
        //bookTextPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
        
        //scrollPane = new JScrollPane(renderer);
    }
    
    @Override
    public void show(Book book, Key key, Component owner, int x, int y) {
        hide(); // hide the prev popup if any is opened
        
        if (book == null || key == null) {
            throw new IllegalArgumentException("book or key should not be null"); //NOI18N
        }
        
        List<Book> books = renderer.getBooks();
        books.clear();
        
        books.add(book);
        renderer.setKey(key);

        renderer.reload(false);

        // XXX workaround for pack/panel prefSize problem
        // Note: it still have bug regarding the size

        //renderer.setSize(new Dimension(0,0));
        //renderer.setPreferredSize(null);
        //scrollPane.setPreferredSize(null);
        //renderer.revalidate();
        //renderer.setVisible(true);

        // use Golden Ratio to specify the prefSize
        /*Dimension size = renderer.getPreferredSize();
        if (size.width == 0 || size.height == 0) {
            size = renderer.getSize();
        }


        int adjust = 25; // some adjustment to for estimating scroll bar size ~25px
        double maxW = 540-adjust;
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
        renderer.setPreferredSize(size);
        scrollPane.setPreferredSize(new Dimension(size.width + adjust, size.height + adjust));
        
        renderer.revalidate();
        scrollPane.revalidate();
        */
            
        prefPoint.setLocation(x,y);
        getPreferredLocation(owner, prefPoint);
        
        popup = PopupFactory.getSharedInstance().getPopup(owner, renderer, prefPoint.x, prefPoint.y);
        //popup = PopupFactory.getSharedInstance().getPopup(owner, scrollPane, prefPoint.x, prefPoint.y);
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
     * @return point
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
        Dimension popupDim = renderer.getSize();
        //Dimension popupDim = scrollPane.getPreferredSize();
        
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
