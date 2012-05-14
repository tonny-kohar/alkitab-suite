/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.xalan;

import org.openide.modules.ModuleInstall;

/**
 *
 * @author tonny
 */
public class XalanModuleInstall extends ModuleInstall {
    
    @Override
    public  void restored() {
        super.restored();
        
        // Intrepetive Transformer org.apache.xalan.processor.TransformerFactoryImpl
        // compiler Transformer org.apache.xalan.xsltc.trax.TransformerFactoryImpl
        // smart Transformer org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl
        
        System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        
    }
}
