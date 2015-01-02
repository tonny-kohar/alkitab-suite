/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import kiyut.alkitab.installer.BookInstallerPane;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.windows.WindowManager;

/**
 * This action will open book installer dialog.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@ActionID(id = "kiyut.alkitab.actions.BookInstallerAction", category = "Tools")
@ActionRegistration(displayName = "#CTL_BookInstallerAction",
        iconBase = "kiyut/alkitab/actions/installer.png")
@ActionReference(path = "Menu/Tools", position = 0)
public class BookInstallerAction extends AbstractAction {
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame owner = WindowManager.getDefault().getMainWindow();
                BookInstallerPane installerPane = new BookInstallerPane();
                installerPane.showDialog(owner);

                if (installerPane.isBookInstalled()) {
                    BookshelfTopComponent tc = BookshelfTopComponent.findInstance();
                    tc.reload();
                }

                /*if (installerPane.isBookInstalled()) {
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
                }*/
            }
        });
    }    
}
