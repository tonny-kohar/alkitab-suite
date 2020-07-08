/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.io.File;
import javax.xml.transform.TransformerException;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.openide.modules.InstalledFileLocator;

/**
 * Simple Swing-HTML stylesheet converter
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class HTMLConverter implements Converter {

    public static String DIRECTION = "direction";
    public static String BASE_URL = "baseURL";
    public static String FONT = "font";
    public static String CSS = "css";
    public static String BACKGROUND_COLOR = "background-color";
    
    
    @Override
    public SAXEventProvider convert(SAXEventProvider xmlsep) throws TransformerException {
        try {
            // simple.xml is located under [JSword module]/release/xsl/simple.xsl
            File file = InstalledFileLocator.getDefault().locate("xsl/simple.xsl","kiyut.alkitab.modules.jsword",false); 
            TransformingSAXEventProvider tsep = new TransformingSAXEventProvider(file.toURI(), xmlsep);
            return tsep;
        } catch (Exception ex) {
            throw new TransformerException(ex);
        }
    }
}
