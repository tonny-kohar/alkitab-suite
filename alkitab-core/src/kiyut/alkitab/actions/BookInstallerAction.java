/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.swing.BookInstallerPane;
import kiyut.alkitab.util.SwordUtilities;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;

/**
 *
 * 
 */
public final class BookInstallerAction extends CallableSystemAction {

    public void performAction() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Frame owner = WindowManager.getDefault().getMainWindow();
                BookInstallerPane installerPane = new BookInstallerPane();
                installerPane.showDialog(owner);
                
                if (installerPane.isBookInstalled()) {
                    boolean pathOK = false;
                    
                    BookViewerOptions viewerOpts = BookViewerOptions.getInstance();
                    File[] paths = viewerOpts.getBookPaths();
                    try {
                        SwordUtilities.setAugmentPath(paths);
                        pathOK = true;
                    } catch (Exception ex) {
                        Logger logger = Logger.getLogger(this.getClass().getName());
                        logger.log(Level.WARNING,ex.getMessage(),ex);
                    }
                    
                    if (pathOK) {
                        BookshelfTopComponent tc = BookshelfTopComponent.findInstance();
                        tc.reload();
                    }
                }
            }
        });
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(BookInstallerAction.class, "CTL_BookInstallerAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
