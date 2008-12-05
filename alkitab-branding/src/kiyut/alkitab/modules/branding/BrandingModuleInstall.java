/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.branding;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import kiyut.alkitab.Application;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.util.ComponentOrientationSupport;
import kiyut.alkitab.util.IOUtilities;
import kiyut.alkitab.util.SwordUtilities;
import kiyut.alkitab.windows.BookViewerTopComponent;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.sword.SwordBookPath;
import org.openide.modules.ModuleInstall;
import org.openide.windows.Mode;
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

        String orientationKey = "alkitab.orientation";
        String strOrientation = System.getProperty(orientationKey);
        if (strOrientation == null) {
            strOrientation = "ltr";
        }

        //System.setProperty("netbeans.buildnumber", Application.VERSION);
        System.setProperty("alkitab.buildnumber", Application.getBuildNumber());
        System.setProperty("alkitab.version", Application.getVersion());
        System.setProperty(orientationKey, strOrientation);

        Logger logger = Logger.getLogger(this.getClass().getName());

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

        StringBuilder sb = new StringBuilder("Sword Path Configuration\n");
        
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

        String str = System.getProperty(orientationKey);
        if (str == null) {
            str = "ltr";
        }

        // adding Orientation to the log
        sb.append(orientationKey + ": " + strOrientation + "\n");

        sb.append("-------------------------------------------------------------------------------\n");
        
        logger.log(Level.INFO,sb.toString());

        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            public void run() {
                ComponentOrientationSupport.applyComponentOrientation(WindowManager.getDefault().getMainWindow());

                boolean session = BookViewerOptions.getInstance().isSessionPersistence();

                if (!session) {
                    // open KJV if exist
                    Book book = Books.installed().getBook("KJV");
                    if (book != null) {
                        SwordURI uri = SwordURI.createURI(book, null);
                        if (uri != null) {
                            BookViewManager.getInstance().openURI(uri, true);
                        }
                    }
                } else {
                    // set any bookViewer to active if not active
                    TopComponent activeTC = WindowManager.getDefault().getRegistry().getActivated();
                    if (activeTC instanceof BookViewerTopComponent) {
                        return;
                    }

                    Mode mode = WindowManager.getDefault().findMode("editor");
                    if (mode != null) {
                        TopComponent selectedTC = mode.getSelectedTopComponent();
                        if (selectedTC != null) {
                            selectedTC.requestActive();
                        }
                    }
                }
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
}
