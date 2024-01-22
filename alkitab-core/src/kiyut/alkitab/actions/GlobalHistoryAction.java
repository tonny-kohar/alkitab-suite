/* This work has been placed into the public domain. */
package kiyut.alkitab.actions;

import kiyut.alkitab.windows.GlobalHistoryTopComponent;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 * Abstract class for GlobalHistoryAction
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public abstract class GlobalHistoryAction extends CallableSystemAction {
    protected GlobalHistoryTopComponent globalHistoryTopComponent;
    
    public GlobalHistoryAction() {
        globalHistoryTopComponent = GlobalHistoryTopComponent.findInstance();
    }
    
    /** 
     * Overridden to return false
     * {@inheritDoc}
     */
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /** 
     * Overridden to return HelpCtx.DEFAULT_HELP
     * {@inheritDoc} 
     */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
