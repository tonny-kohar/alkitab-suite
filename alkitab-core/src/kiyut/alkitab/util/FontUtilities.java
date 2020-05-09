/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.crosswire.common.util.StringUtil;

/**
 * Collections of Font Utilities.<br/>
 * <strong>Note:</strong> almost straight copy-paste with slight modification from
 * org.crosswire.common.swing.GuiConvert font section
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 * 
 */
public class FontUtilities {
    
    /**
     * Convert a String to a Font. Accepts one of two inputs:
     * FamilyName-STYLE-size, where STYLE is either PLAIN, BOLD, ITALIC or BOLDITALIC<br>
     * or<br>
     * FamilyName,style,size, where STYLE is 0 for PLAIN, 1 for BOLD, 2 for
     * ITALIC or 3 for BOLDITALIC.
     * 
     * @param value the thing to convert
     * @return the converted data
     */
    public static Font string2Font(String value) {
        if (value == null || "".equals(value)) {
            return null;
        }

        // new way
        if (value.indexOf(',') == -1) {
            return Font.decode(value);
        }

        // old way
        String[] values = StringUtil.split(value, ",");
        if (values.length != 3) {
            Logger logger = Logger.getLogger(FontUtilities.class.getName());
            logger.log(Level.CONFIG, "Illegal font name: {0}", value);
            return null;
        }
        return new Font(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }

    /**
     * Convert a Font to a String. Produces a format that can be read with
     * <code>Font.decode(String)</code>.
     * 
     * @param font the font to convert
     * @return the converted data
     */
    public static String font2String(Font font) {
        if (font == null) {
            return "";
        }

        String strStyle = "plain";

        if (font.isBold()) {
            strStyle = font.isItalic() ? "bolditalic" : "bold";
        } else if (font.isItalic()) {
            strStyle = "italic";
        }

        return font.getName() + "-" + strStyle + "-" + font.getSize();
    }

    /**
     * Create a font just like the another with regard to style and size, but
     * differing in font family.
     * 
     * @param fontspec the font to model
     * @param fontName the font to use
     * @return the font
     */
    public static Font deriveFont(String fontspec, String fontName) {
        Font font = string2Font(fontspec);
        Font derived = null;
        if (font != null && fontName != null) {
            derived = new Font(fontName, font.getStyle(), font.getSize());
        }
        return derived;
    }
}
