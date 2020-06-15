/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.UIManager;
import org.crosswire.common.swing.FontStore;
import org.crosswire.common.swing.GuiConvert;
import org.crosswire.common.util.CWProject;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookMetaData;

/**
 * Reimplementation of org.crosswire.common.swing.FontStore <br>
 * It is used to specify each book font.<br>
 * The persistence location is same with JSword<br>
 *
 */
public class BookFontStore extends FontStore {

    private static BookFontStore instance; // The single instance

    /**
     * Returns the single instance
     * @return The single instance.
     */
    public synchronized static BookFontStore getInstance() {
        if (instance == null) {
            instance = new BookFontStore();
        }
        return instance;
    }

    protected String defaultFontSpec;

    /**
     * Create a persistent Book Font Store.
     * The persistence location is same with JSword
     *
     */
    private BookFontStore() {
        // use the same persistence location as JSword
        super("BookFonts", CWProject.instance().getWritableProjectDir());
        defaultFontSpec = createDefaultFontSpec();
    }

    /**
     * Only convenience method for setFont(String resource, Font font)
     * the resources is book.getInitials
     *
     * @param book the book
     * @param font the font
     */
    public void setFont(Book book, Font font) {
        super.setFont(book.getInitials(), font);
    }

    /**
     * Get the most appropriate font for the book.<br>
     *
     * @param book the book
     * @return the font
     */
    public Font getFont(Book book) {
        // almost straight copy-paste with slight modification from
        // org.crosswire.bibledesktop.book.install.BookFont

        String fontName = (String) book.getBookMetaData().getProperty(BookMetaData.KEY_FONT);
        String fontSpec = getDefaultFont();
        if (fontName != null) {
            Font bookFont = GuiConvert.deriveFont(fontSpec, fontName);
            // Make sure it is installed. Java does substitution. Make sure we got what we wanted.
            if (bookFont.getFamily().equalsIgnoreCase(fontName)) {
                fontSpec = GuiConvert.font2String(bookFont);
            }
        }
        
        return getFont(book.getInitials(), book.getLanguage(), fontSpec);
    }

    /**
     * Override to provide its own default font
     * which is UIManager.getFont("EditorPane.font");
     * @return the defaultFont
     */
    @Override
    public String getDefaultFont() {
        Font defaultFont = getFont("default", null, defaultFontSpec);
        return GuiConvert.font2String(defaultFont);
    }

    protected Font createDefaultFont() {
        Font font = UIManager.getFont("EditorPane.font");
        if (font == null) {
            // serif,0,14
            return new Font("SansSerif",Font.PLAIN,13);
        }

        float fontSize = font.getSize();

        // XXX is this correct way to scale font based on DPI?
        if (fontSize < 12) {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            fontSize = (int)Math.round((double)fontSize * dpi / 72.0);
        }

        font = font.deriveFont(fontSize);

        return font;
    }

     protected String createDefaultFontSpec() {
         return GuiConvert.font2String(createDefaultFont());
     }
}
