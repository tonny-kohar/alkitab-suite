/* This work has been placed into the public domain. */

package kiyut.swing.text.xml;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.StyleContext;


/**
 * A pool of styles and their associated resources
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class XMLContext extends StyleContext {
    //public static final String DEFAULT_SYNTAX         = "default";
    public static final String XML_DECLARATION_STYLE  = "xml_declaration";
    public static final String DOCTYPE_STYLE          = "doctype";
    public static final String COMMENT_STYLE          = "comment";
    public static final String ELEMENT_STYLE          = "element";
    public static final String CHARACTER_DATA_STYLE   = "character_data";
    public static final String ATTRIBUTE_NAME_STYLE   = "attribute_name";
    public static final String ATTRIBUTE_VALUE_STYLE  = "attribute_value";
    public static final String CDATA_STYLE            = "cdata";
    
    /** Map<String, Color> */
    protected Map<String, Color> syntaxForegroundMap = null;
    
    /** Map<String, Font> */
    protected Map<String, Font> syntaxFontMap = null;

    public XMLContext() {
        this(null);
    }

    public XMLContext(Font defaultFont) {
        // initialize the default syntax highlight
       
        String syntaxName;
        Font font;
        Color fontForeground;
        syntaxFontMap = new HashMap<>();
        syntaxForegroundMap = new HashMap<>();        

        if (defaultFont == null) {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            int fontSize = (int)Math.round(12.0 * (dpi / 90.0));
            defaultFont = new Font("Monospaced", Font.PLAIN, fontSize);
        }
        
        syntaxName = XMLContext.DEFAULT_STYLE;
        font = defaultFont;
        fontForeground = Color.BLACK;
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);
        
        syntaxName = XMLContext.XML_DECLARATION_STYLE;
        font = defaultFont.deriveFont(Font.BOLD);
        fontForeground = new Color(0, 0, 124);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);

        syntaxName = XMLContext.DOCTYPE_STYLE;
        font = defaultFont.deriveFont(Font.BOLD);
        fontForeground = new Color(0, 0, 124);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);
        
        syntaxName = XMLContext.COMMENT_STYLE;
        font = defaultFont;
        fontForeground = new Color(128, 128, 128);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);
        
        syntaxName = XMLContext.ELEMENT_STYLE;
        font = defaultFont;
        fontForeground = new Color(0, 0, 255);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);

        syntaxName = XMLContext.CHARACTER_DATA_STYLE;
        font = defaultFont;
        fontForeground = Color.BLACK;
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);

        syntaxName = XMLContext.ATTRIBUTE_NAME_STYLE;
        font = defaultFont;
        fontForeground = new Color(0, 124, 0);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);

        syntaxName = XMLContext.ATTRIBUTE_VALUE_STYLE;
        font = defaultFont;
        fontForeground = new Color(153, 0, 107);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);

        syntaxName = XMLContext.CDATA_STYLE;
        font = defaultFont;
        fontForeground = new Color(124, 98, 0);
        syntaxFontMap.put(syntaxName, font);
        syntaxForegroundMap.put(syntaxName, fontForeground);
    }
    
    public XMLContext(Map<String,Font> syntaxFontMap, Map<String,Color> syntaxForegroundMap) {
        setSyntaxFont(syntaxFontMap);
        setSyntaxForeground(syntaxForegroundMap);
    }
    
    public void setSyntaxForeground(Map<String,Color> syntaxForegroundMap) {
        if (syntaxForegroundMap == null) {
            throw new IllegalArgumentException("syntaxForegroundMap can not be null");
        }
        this.syntaxForegroundMap = syntaxForegroundMap;
    }
    
    public void setSyntaxFont(Map<String,Font> syntaxFontMap) {
        if (syntaxFontMap == null) {
            throw new IllegalArgumentException("syntaxFontMap can not be null");
        }
        this.syntaxFontMap = syntaxFontMap;
    }
    
    public Color getSyntaxForeground(int ctx) {
        String name = getSyntaxName(ctx);
        return getSyntaxForeground(name);
    }
    
    public Color getSyntaxForeground(String name) {
        return syntaxForegroundMap.get(name);
    }
    
    public Font getSyntaxFont(int ctx) {
        String name = getSyntaxName(ctx);
        return getSyntaxFont(name);
    }
    
    public Font getSyntaxFont(String name) {
        return syntaxFontMap.get(name);
    }
    
    public String getSyntaxName(int ctx) {
        String name = DEFAULT_STYLE;
        switch (ctx) { 
            case XMLScanner.XML_DECLARATION_CONTEXT:
                name = XML_DECLARATION_STYLE;
                break;
            case XMLScanner.DOCTYPE_CONTEXT:
                name = DOCTYPE_STYLE;
                break;
            case XMLScanner.COMMENT_CONTEXT:
                name = COMMENT_STYLE;
                break;
            case XMLScanner.ELEMENT_CONTEXT:
                name = ELEMENT_STYLE;
                break;
            case XMLScanner.ATTRIBUTE_NAME_CONTEXT:
                name = ATTRIBUTE_NAME_STYLE;
                break;
            case XMLScanner.ATTRIBUTE_VALUE_CONTEXT:
                name = ATTRIBUTE_VALUE_STYLE;
                break;
            case XMLScanner.CDATA_CONTEXT:
                name = CDATA_STYLE;
                break;
            case XMLScanner.CHARACTER_DATA_CONTEXT:
                name = CHARACTER_DATA_STYLE;
                break;
            default:
                // should not go here, just incase
                name = DEFAULT_STYLE;
                break;
        }
        return name;
    }
}
