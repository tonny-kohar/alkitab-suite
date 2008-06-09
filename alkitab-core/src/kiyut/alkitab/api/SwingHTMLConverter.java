/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

import java.net.URL;
import java.util.MissingResourceException;
import javax.xml.transform.TransformerException;
import org.crosswire.common.util.NetUtil;
import org.crosswire.common.util.ResourceUtil;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;

/**
 * Simple Swing-HTML stylesheet converter
 *  
 * 
 */
public class SwingHTMLConverter implements Converter {

    /** The xsl path , by default it is kiyut/alkitab/modules/jsword/xsl/simple.xsl */
    private static String xslPath = "kiyut/alkitab/modules/jsword/xsl/simple.xsl"; 
    
    public SAXEventProvider convert(SAXEventProvider xmlsep) throws TransformerException {
        try {
            String path = xslPath;
            URL xslURL = ResourceUtil.getResource(path);

            TransformingSAXEventProvider tsep = new TransformingSAXEventProvider(NetUtil.toURI(xslURL), xmlsep);
            
            return tsep;
            
        } catch (MissingResourceException ex) {
            throw new TransformerException(ex);
        }
    }

}
