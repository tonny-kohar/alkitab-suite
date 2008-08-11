/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.Frame;
import kiyut.alkitab.swing.BookInstallerPane;
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
        Frame owner = WindowManager.getDefault().getMainWindow();
        
        BookInstallerPane installerPane = new BookInstallerPane();
        installerPane.showDialog(owner);
        
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
