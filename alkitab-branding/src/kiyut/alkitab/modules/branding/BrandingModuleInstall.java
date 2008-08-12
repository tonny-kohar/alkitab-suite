/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.branding;

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
import kiyut.alkitab.util.SwordUtilities;
import kiyut.alkitab.windows.BookViewerTopComponent;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.BookException;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.sword.SwordBookPath;
import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Core Module Install for Netbeans Platform
 * 
 */
public class BrandingModuleInstall extends ModuleInstall {
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
        
        // download path need to listed first
        File path = viewerOpts.getDownloadPath();
        if (path != null) {
            SwordBookPath.setDownloadDir(path);
        }
        
        File[] paths = viewerOpts.getBookPaths();
        try {
            SwordUtilities.setAugmentPath(paths);
        } catch (Exception ex) {
            logger.log(Level.WARNING,ex.getMessage(),ex);
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
                
                if (BookViewerOptions.getInstance().isSessionPersistence()) {
                    // if using Session Persistence do not set Bookshelf focus
                    return;
                }
                
                final Frame mainWindow = WindowManager.getDefault().getMainWindow();
                mainWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent evt) {
                        // if sessionPersistence do not set the active TC, let Nb deal with it
                        if (BookViewerOptions.getInstance().isSessionPersistence()) {
                            return; 
                        }
                        
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
    
    @Override
    public boolean closing() {
        if (BookViewerOptions.getInstance().isSessionPersistence()) {
            return super.closing();
        }
               
        // if not session persistence. close all currently opened BookViewer
        TopComponent[] tcs = TopComponent.getRegistry().getOpened().toArray(new TopComponent[0]);
        for (int i=0; i<tcs.length; i++) {
            TopComponent tc = tcs[i];
            if (tc instanceof BookViewerTopComponent) {
                tc.close();
            }
        }
        
        return super.closing();
    }
    
    /** Set Default Sword Path for Alkitab. Make sure [user.home]/.sword is included */
    /*private void setDefaultSwordPath(File[] paths) throws BookException {
        boolean defPathFound = false;
        
        String str = System.getProperty("user.home");
        File defPath = new File(str + File.separator + ".sword");
        
        File[] files = SwordBookPath.getSwordPath();
        for (int i=0; i<files.length; i++) {
            if (files[i].equals(defPath)) {
                defPathFound = true;
                break;
            }
        }
        
        if (!defPathFound && paths != null) {
            files = paths;
            for (int i = 0; i < files.length; i++) {
                if (files[i].equals(defPath)) {
                    defPathFound = true;
                    break;
                }
            }
        }
        
        // if defPath is not there, make sure it is included as last entry
        if (!defPathFound) {
            if (paths == null) {
                files = new File[1];
            } else {
                files = new File[paths.length + 1];
                System.arraycopy(paths, 0, files, 0, paths.length);
            }
            paths = files;
            paths[paths.length-1] = defPath;
        }
        
        if (paths != null) {
            SwordBookPath.setAugmentPath(paths);
        }
    }*/
}
