/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import kiyut.alkitab.api.ViewerHints;
import kiyut.alkitab.options.ViewerHintsOptions;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * ToolTip Component. Internally, it is using {@link javax.swing.PopupFactory PopupFactory}
 * 
 */
public class ToolTip extends JComponent {

    protected BookTextPane bookTextPane;
    protected Popup popup;
    
    /** Just Point object act as cache for performance reason, so it does not always recreate Point object */
    protected Point point = new Point();
    
    public ToolTip() {
        ViewerHints<ViewerHints.Key,Object> viewerHints = new ViewerHints<ViewerHints.Key, Object>(ViewerHintsOptions.getInstance().getViewerHints());
        bookTextPane = new BookTextPane(viewerHints,false);
        setLayout(new BorderLayout());
        add(bookTextPane,BorderLayout.CENTER);
        
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
        
        Border border = UIManager.getBorder("Tooltip.border");
        if (border == null) {
            border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(76,79,83)), BorderFactory.createEmptyBorder(6,6,6,6));
        }
        bookTextPane.setBorder(border);
        
        //setPreferredSize(new Dimension(100, 100));
    }
    
    /** 
     * Display this component as Tooltip popup. 
     * Owner is used to determine which Window the new Popup will parent the 
     * Component the Popup creates to. 
     * A null owner implies there is no valid parent. 
     * x and y specify the preferred initial location to place the Popup at. 
     * Based on screen size, or other paramaters, the Popup may not display at x and y.
     * @param book {@code Book} to view
     * @param key {@code Key} to view
     * @param owner Component mouse coordinates are relative to, may be null
     * @param x Initial x screen coordinate
     * @param y Initial y screen coordinate
     *
     */
    public void show(Book book, Key key, Component owner, int x, int y) {
        if (book == null || key == null) {
            return;
        }
        
        List<Book> books = bookTextPane.getBooks();
        books.clear();
        
        books.add(book);
        bookTextPane.setKey(key);
        
        bookTextPane.refresh(false);
        
        // XXX workaround for pack/panel size problem
        //this.revalidate();
        //System.out.println(bookTextPane.getPreferredSize() + "  " + this.getPreferredSize());
        
        point.setLocation(x, y);
        
        getPreferredLocation(owner, point);
        
        popup = PopupFactory.getSharedInstance().getPopup(owner, this, point.x, point.y);
        popup.show();
    }
    
    @Override
    public void hide() {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (!visible) {
            hide();
        }
        super.setVisible(visible);
    }
    
    /** Return preferred popup location based on ScreenSize, etc so it does not overlap 
     * with cursor and Screen Size
     * @param owner Component mouse coordinates are relative to, maybe null
     * @param point inital point location that will be adjusted based on various factor
     * @return preferred popup location
     */
    protected Point getPreferredLocation(Component owner, Point point) {
        int adjust = 24; // some pixel adjustment for cursor size
        
        if (owner == null) {
            point.setLocation(point.x, point.y + adjust);
        }
        
        SwingUtilities.convertPointToScreen(point, owner);
        
        Dimension scDim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension popupDim = bookTextPane.getPreferredSize();
        
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
