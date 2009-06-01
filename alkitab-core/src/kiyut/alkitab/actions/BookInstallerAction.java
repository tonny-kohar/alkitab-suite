/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import kiyut.alkitab.swing.BookInstallerPane;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * This action will open book installer dialog.
 * 
 */
public final class BookInstallerAction extends AbstractAction {

    public BookInstallerAction() {
        super(NbBundle.getMessage(BookInstallerAction.class, "CTL_BookInstallerAction"));

        String iconPath = NbBundle.getMessage(BookInstallerAction.class, "ICON_BookInstallerAction");
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(iconPath, true)));
    }
    
    
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
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
