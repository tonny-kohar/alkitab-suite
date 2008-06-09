/* This work has been placed into the public domain. */

package kiyut.alkitab.core;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import kiyut.alkitab.Application;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.util.IOUtilities;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.sword.SwordBookPath;
import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Core Module Install for Netbeans Platform
 * 
 */
public class CoreModuleInstall extends ModuleInstall {
    @Override
    public  void restored() {
        super.restored();
        
        Logger logger = Logger.getLogger(this.getClass().getName());
        
        //System.setProperty("netbeans.buildnumber", Application.VERSION);
        System.setProperty("alkitab.buildnumber", Application.getBuildNumber());
        System.setProperty("alkitab.version", Application.getVersion());
        
        // override user.dir variable to the 
        IOUtilities.setUserDir(null);
        
        BookViewerOptions viewerOpts = BookViewerOptions.getInstance();
        
        File[] paths = viewerOpts.getBookPaths();
        try {
            if (paths != null) {
               SwordBookPath.setAugmentPath(paths);
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING,ex.getMessage(),ex);
        }
        
        File path = viewerOpts.getDownloadPath();
        if (path != null) {
            SwordBookPath.setDownloadDir(path);
        }
        
        StringBuilder sb = new StringBuilder();
        
        File[] files;
        File file;
        
        sb.append("  AugmentPath:\n");
        files = SwordBookPath.getAugmentPath();
        for (int i=0; i<files.length; i++) {
            sb.append("\t" + files[i].toString() + "\n");
        }
        
        sb.append("  DownloadDir:\n");
        file = SwordBookPath.getDownloadDir();
        if (file != null) {
            sb.append("\t" + file.toString() + "\n");
        }
        
        sb.append("  SwordDownloadDir:\n");
        file = SwordBookPath.getSwordDownloadDir();
        if (file != null) {
            sb.append("\t" + file.toString() + "\n");
        }
        
        sb.append("  SwordPath:\n");
        files = SwordBookPath.getSwordPath();
        for (int i=0; i<files.length; i++) {
            sb.append("\t" + files[i].toString() + "\n");
        }
        
        sb.append("  Book Count: " + Books.installed().getBooks().size() + "\n");
        
        sb.append("-------------------------------------------------------------------------------\n");
        
        
        logger.log(Level.INFO,"Sword Path");
        logger.log(Level.INFO,sb.toString());
        
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            public void run() {
                final Frame mainWindow = WindowManager.getDefault().getMainWindow();
                mainWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent evt) {
                        // set active BookshelfTopComponent if opened
                        Set set = WindowManager.getDefault().getRegistry().getOpened();
                        TopComponent tc = null;
                        Iterator it = set.iterator();
                        while (it.hasNext()) {
                            tc = (TopComponent)it.next();
                            if (tc instanceof BookshelfTopComponent) {
                                if (tc.isOpened()) {
                                    requestActiveTopComponent(tc);
                                }
                                break;
                            }
                        }
                    }
                });
            }
        });
    }
    
    private void requestActiveTopComponent(final TopComponent tc) {
        if (tc == null) { return; }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tc.requestActive();
            }
        });
        
    }
}
