/* This work has been placed into the public domain. */
package kiyut.alkitab.modules.userguide;

import java.io.IOException;
import java.net.URL;
import org.openide.loaders.DataObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.help.HelpSet;
//import org.netbeans.modules.javahelp.NbDocsStreamHandler;
import org.openide.cookies.InstanceCookie;

import org.openide.loaders.Environment;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.xml.EntityCatalog;
import org.openide.xml.XMLUtil;
import org.xml.sax.InputSource;


/**
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class JavaHelpProcessor implements Environment.Provider {

    @Override
    public Lookup getEnvironment(final DataObject obj) {
        InstanceCookie cookie = new InstanceCookie() {
            @Override
            public String instanceName() {
                return obj.getName();
            }

            @Override
            public Class<?> instanceClass() throws IOException, ClassNotFoundException {
                return HelpSet.class;
            }

            @Override
            public Object instanceCreate() throws IOException, ClassNotFoundException {
                try {
                    Document doc = XMLUtil.parse(new InputSource(obj.getPrimaryFile().toURL().toString()), true, false, XMLUtil.defaultErrorHandler(), EntityCatalog.getDefault());
                    Element el = doc.getDocumentElement();
                    if (!el.getNodeName().equals("helpsetref")) { // NOI18N
                        throw new IOException();
                    }
                    String url = el.getAttribute("url"); // NOI18N
                    if (url == null || url.isEmpty()) {
                        throw new IOException("no url attr on <helpsetref>! doc.class=" + doc.getClass().getName() + " doc.documentElement=" + el);
                    }
                    HelpSet hs = new HelpSet(Lookup.getDefault().lookup(ClassLoader.class), new URL(url));
                    return hs;
                    
                } catch (IOException x) {
                    throw x;
                } catch (Exception x) {
                    throw new IOException(x);
                }
            }
        };
                
        return Lookups.singleton(cookie);
    }
    
}
