/* This work has been placed into the public domain. */

package kiyut.openide.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.openide.awt.Actions;
import org.openide.awt.Mnemonics;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.actions.BooleanStateAction;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

/**
 * Collection of Netbean Platform or OpenIDE Utilities
 * 
 */
public class NbUtilities {

    private NbUtilities() {
        throw new Error("NbUtilities is a utility class for static methods"); // NOI18N
    }
    
    /** 
     * Build Popup Menu from XML Layer declaration under speficied folder name
     * @param menu Menu Container eg: {@code JPopupMenu} or {@code JMenu}
     * @param folderName the folder name eg: Alkitab/Bookshelf/popupMenu
     */
    public static void createMenu(JComponent menu, String folderName) {
        if (menu == null || folderName == null) {
            throw new IllegalArgumentException("argument menu or folderName should not be null"); // NOI18N
        }
        
        //FileSystem fs = Repository.getDefault().getDefaultFileSystem();
        //FileObject fo = fs.getRoot().getFileObject(folderName);
        FileObject fo = FileUtil.getConfigRoot().getFileObject(folderName);

        if (fo == null) {
            return;
        }

        buildMenu(menu, fo);
    }
    
    /** Recursive Build Menu
     *@param comp Menu Container eg: JPopupMenu or JMenu
     *@param fo FileObject as the DataFolder
     */
    private static void buildMenu(JComponent menu, FileObject fo) {
        DataFolder df = DataFolder.findFolder(fo);
        DataObject[] childs = df.getChildren();
        DataObject dob;
        Object instanceObj;

        for (int i = 0; i < childs.length; i++) {
            dob = childs[i];
            if (dob.getPrimaryFile().isFolder()) {
                FileObject childFo = childs[i].getPrimaryFile();
                JMenu childMenu = new JMenu();
                Mnemonics.setLocalizedText(childMenu, dob.getNodeDelegate().getDisplayName());
                menu.add(childMenu);
                buildMenu(childMenu,childFo);
            } else {
                InstanceCookie ck = dob.getCookie(InstanceCookie.class);
                try {
                    instanceObj = ck.instanceCreate();
                } catch (Exception ex) {
                    instanceObj = null;
                    Logger logger = Logger.getLogger(NbUtilities.class.getName());
                    logger.log(Level.WARNING,ex.getMessage(), ex);
                }

                if (instanceObj == null) {
                    continue;
                }

                if (instanceObj instanceof JSeparator) {
                    // XXX JSeparator instance is shared between component, ordering will be fail,
                    // workaround create new JSeparator
                    menu.add(new JSeparator());
                /*} else if (instanceObj instanceof BooleanStateAction) {
                    //JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
                    //Actions.connect(menuItem, (BooleanStateAction) instanceObj, true);
                    menu.add(((BooleanStateAction)instanceObj).getMenuPresenter());
                    * 
                    */
                } else if (instanceObj instanceof Presenter.Popup) {
                    menu.add(((Presenter.Popup)instanceObj).getPopupPresenter());
                } else if (instanceObj instanceof Action) {
                    JMenuItem menuItem = new JMenuItem();
                    Actions.connect(menuItem, (Action) instanceObj, true);
                    menu.add(menuItem);
                    //System.out.println(instanceObj.getClass().toString());
                }
            }
        }
    }
}
