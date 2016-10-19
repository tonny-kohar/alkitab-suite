/* This work has been placed into the public domain. */

package kiyut.alkitab.modules.branding;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import kiyut.alkitab.Application;
import kiyut.alkitab.bookviewer.BookViewerManager;
import kiyut.alkitab.bookviewer.SwordURI;
import kiyut.alkitab.history.GlobalHistory;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.util.ComponentOrientationSupport;
import kiyut.alkitab.util.IOUtilities;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.sword.SwordBookPath;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Core Module Install for Netbeans Platform
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class BrandingModuleInstall extends ModuleInstall {

    @Override
    public  void restored() {
        super.restored();

        String orientationKey = "alkitab.orientation";
        String strOrientation = System.getProperty(orientationKey);
        if (strOrientation == null) {
            strOrientation = "auto";
        } else {
            strOrientation = strOrientation.trim().toLowerCase();
        }

        //System.setProperty("netbeans.buildnumber", Application.VERSION);
        System.setProperty("alkitab.buildnumber", Application.getBuildNumber());
        System.setProperty("alkitab.version", Application.getVersion());
        System.setProperty(orientationKey, strOrientation);
        
        configureSwordPath();

        // XXX Register context menu (right click) cut/copy/paste
        // it is hack see:
        // http://www.javalobby.org/java/forums/t19867.html
        // http://tips4java.wordpress.com/2009/08/30/global-event-listeners/
        Toolkit.getDefaultToolkit().addAWTEventListener(new ExtendedMouseEventListener(), AWTEvent.MOUSE_EVENT_MASK);

        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                try {
                    doRun();
                } catch (Exception ex) {
                    Logger logger = Logger.getLogger(this.getClass().getName());
                    logger.log(Level.INFO, ex.getMessage());
                }
            }
        });
    }

    private void doRun() {
        ComponentOrientationSupport.applyComponentOrientation(WindowManager.getDefault().getMainWindow());

        boolean firstTime = GlobalHistory.getInstance().size() == 0;

        if (firstTime) {
            // open preferred Bible if exist and available
            String prefsBible = BookViewerOptions.getInstance().getDefaultBible();
            Book book = Books.installed().getBook(prefsBible);
            if (book != null) {
                SwordURI uri = SwordURI.createURI(book, null);
                if (uri != null) {
                    BookViewerManager.getInstance().openURI(uri, null, true);
                }
            }
        } /*else {
            // set any bookViewer to active if not active
            TopComponent activeTC = WindowManager.getDefault().getRegistry().getActivated();
            if (activeTC instanceof BookViewerTopComponent) {
                return;
            }

            Mode mode = WindowManager.getDefault().findMode("editor"); //NOI18N
            if (mode != null) {
                TopComponent selectedTC = mode.getSelectedTopComponent();
                if (selectedTC != null) {
                    selectedTC.requestActive();
                }
            }
        } */ 
    }
    
    @Override
    public boolean closing() {
        GlobalHistory.getInstance().save();
        return super.closing();
    }

    private void configureSwordPath() {
        Logger logger = Logger.getLogger(this.getClass().getName());

        // override user.dir variable to the
        IOUtilities.setUserDir(null);

        // disable jsword logger reapplying configuration for JSword > 20100619
        // XXX it is also workaround for JSword recent, hang on splash screen
        //org.crosswire.common.util.Logger.setDisableReadConfiguration(true);

        BookViewerOptions viewerOpts = BookViewerOptions.getInstance();

        // download path need to listed first
        File path = viewerOpts.getDownloadPath();
        if (path != null) {
            SwordBookPath.setDownloadDir(path);
        }

        File[] paths = viewerOpts.getBookPaths();
        try {
            SwordBookPath.setAugmentPath(paths);
        } catch (Exception ex) {
            logger.log(Level.WARNING,ex.getMessage(),ex);
        }

        String newline = System.getProperty("line.separator");

        StringBuilder sb = new StringBuilder("Sword Path Configuration").append(newline);

        File[] files;
        File file;

        sb.append("  AugmentPath:").append(newline);
        files = SwordBookPath.getAugmentPath();
        for (File file1 : files) {
            sb.append("\t").append(file1.toString()).append(newline);
        }

        sb.append("  DownloadDir:").append(newline);
        file = SwordBookPath.getDownloadDir();
        if (file != null) {
            sb.append("\t").append(file.toString()).append(newline);
        }

        sb.append("  SwordDownloadDir:").append(newline);
        file = SwordBookPath.getSwordDownloadDir();
        if (file != null) {
            sb.append("\t").append(file.toString()).append(newline);
        }
        
        sb.append("  SwordPath:").append(newline);
        files = SwordBookPath.getSwordPath();
        for (File file1 : files) {
            sb.append("\t").append(file1.toString()).append(newline);
        }

        sb.append("  Book Count: ").append(Books.installed().getBooks().size()).append(newline);

        // adding Orientation to the log
        //sb.append(orientationKey + ": " + strOrientation + "\n");

        sb.append("-------------------------------------------------------------------------------").append(newline);

        logger.log(Level.INFO,sb.toString());
    }


    private class ExtendedMouseEventListener implements AWTEventListener {

        @Override
        public void eventDispatched(AWTEvent event) {

            // interested only in mouseevents
            if (!(event instanceof MouseEvent)) {
                return;
            }

            MouseEvent me = (MouseEvent) event;

            // interested only in popuptriggers
            if (!me.isPopupTrigger()) {
                return;
            }

            // me.getComponent(...) retunrs the heavy weight component on which event occured
            Component comp = SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY());

            // interested only in textcomponents
            if (!(comp instanceof JTextComponent)) {
                return;
            }

            // no popup shown by user code
            if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) {
                return;
            }

            // create popup menu and show
            JTextComponent tc = (JTextComponent) comp;
            JPopupMenu menu = new JPopupMenu();
            menu.add(new CutAction(tc));
            menu.add(new CopyAction(tc));
            menu.add(new PasteAction(tc));
            menu.add(new DeleteAction(tc));
            //menu.addSeparator();
            //menu.add(new SelectAllAction(tc));

            Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
            menu.show(tc, pt.x, pt.y);
        }

    }

    private class CutAction extends AbstractAction {
        private JTextComponent comp;

        public CutAction(JTextComponent comp) {
            super("Cut");
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.cut();
        }

        @Override
        public boolean isEnabled() {
            return comp.isEditable()
                    && comp.isEnabled()
                    && comp.getSelectedText() != null;
        }
    }
    
    private class CopyAction extends AbstractAction {

        private JTextComponent comp;

        public CopyAction(JTextComponent comp) {
            super("Copy");
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.copy();
        }

        @Override
        public boolean isEnabled() {
            return comp.isEnabled()
                    && comp.getSelectedText() != null;
        }
    }

    private class PasteAction extends AbstractAction {

        private JTextComponent comp;

        public PasteAction(JTextComponent comp) {
            super("Paste");
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.paste();
        }

        @Override
        public boolean isEnabled() {
            if (comp.isEditable() && comp.isEnabled()) {
                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            } else {
                return false;
            }
        }
    }

    private class DeleteAction extends AbstractAction {

        private JTextComponent comp;

        public DeleteAction(JTextComponent comp) {
            super("Delete");
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.replaceSelection(null);
        }

        @Override
        public boolean isEnabled() {
            return comp.isEditable()
                    && comp.isEnabled()
                    && comp.getSelectedText() != null;
        }
    }
}
