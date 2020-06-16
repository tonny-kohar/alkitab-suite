/* This work has been placed into the public domain. */
package kiyut.alkitab.modules.userguide;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.WindowPresentation;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 * Implementation of HelpViewer using JavaHelp
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ServiceProvider(service=HelpViewer.class)
public class JavaHelpViewer implements HelpViewer {

    public static final String MASTER_ID = "kiyut.alkitab.modules.userguide";
    public static final String MASTER_HS = "kiyut/alkitab/modules/userguide/docs/userguide-hs.xml";
    
    private Lookup.Result<HelpSet> lookupResult = null;
    
     private HelpBroker helpBroker;
    
    public JavaHelpViewer() {
        lookupResult = Lookup.getDefault().lookupResult(HelpSet.class);
        lookupResult.addLookupListener((LookupEvent ev) -> {
            // if helpSet changed
            helpBroker = null;
        });
    }
    
    @Override
    public synchronized void showHelp(String id) {
        if (helpBroker == null) { 
            createHelp();
        } 
        
        if (helpBroker == null) {
            return;
        }
        
        if (helpBroker.isDisplayed()) {
            // bring to front
            helpBroker.setDisplayed(true);
            return;
        }
        
        // display
        if (id == null ) {
            helpBroker.setCurrentID(MASTER_ID);
        } else {
            helpBroker.setCurrentID(id);
        }
        helpBroker.setDisplayed(true);
        helpBroker.setViewDisplayed(true);
    }
    
    protected void createHelp() {
        //String helpHS = "kiyut/sketsa/modules/userguide/docs/userguide-hs.xml";
        HelpSet master;
        ClassLoader cl = this.getClass().getClassLoader();
        //ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        //ClassLoader cl = Help.class.getClassLoader();
        try {
            URL hsURL = cl.getResource(MASTER_HS);
            master = new HelpSet(null, hsURL);
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING,ex.getMessage(),ex);
            return;
        }
        
        // merger all helpset
        List<HelpSet> list = getHelpSets();
        for (HelpSet hs : list) {
            master.add(hs);
            //logger.log(Level.INFO, ".....inside merge: " + hs.getTitle());
        }
        
        // Create a HelpBroker object:
        helpBroker = master.createHelpBroker();
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // Default HelpBroker size is too small, make bigger unless on anciente "VGA" resolution
        if (d.width >= 1024 && d.height >= 800) {
            helpBroker.setSize(new Dimension(1024, 700));
        }
        
        // center location
        int w = helpBroker.getSize().width;
        int h = helpBroker.getSize().height;
        int x = (d.width-w)/2;
        int y = (d.height-h)/2;
        helpBroker.setLocation(new Point(x,y));
        
        helpBroker.initPresentation();
        //helpBroker.setDisplayed(true);
        //helpBroker.setViewDisplayed(true);    
        
        if (helpBroker instanceof DefaultHelpBroker) {
            WindowPresentation windowPresentation = ((DefaultHelpBroker)helpBroker).getWindowPresentation();
            //windowPresentation.createHelpWindow();
            Window helpWindow = windowPresentation.getHelpWindow();
            List<Image> images = WindowManager.getDefault().getMainWindow().getIconImages();
            helpWindow.setIconImages(images);
        }
    }
    
    protected List<HelpSet> getHelpSets() {
        Collection<? extends HelpSet> c = lookupResult.allInstances();
        //logger.log(Level.INFO, ".....helpsets count: " + c.size());
        List<HelpSet> list = new ArrayList<>();
        for (HelpSet hs: c) {
            // make sure it is not double with the master
            if (MASTER_ID.equals(hs.getHomeID().getIDString())) {
                continue;
            }
            list.add(hs);
            //logger.log(Level.INFO, hs.getHomeID().getIDString());
        }
        return list;
    }
}
