/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import kiyut.alkitab.api.BookViewer;
import kiyut.alkitab.api.BookViewerNode;
import kiyut.alkitab.api.SwordURI;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * 
 * Top component which displays {@link kiyut.alkitab.api.BookViewer BookViewer}.
 * 
 */
public abstract class BookViewerTopComponent extends TopComponent {
    protected BookViewerNode bookViewerNode;
    
    public abstract BookViewer getBookViewer();
    
    public abstract void openURI(SwordURI uri);

    @Override
    public void componentActivated() {
        if (bookViewerNode != null) {
            setActivatedNodes(new Node[]{bookViewerNode});
        }
    }
    
    @Override
    public void componentClosed() {
        if (bookViewerNode != null) {
            bookViewerNode.closed();
        }
    }
    
    @Override
    public javax.swing.Action[] getActions() {
        List<Action> actionList = new ArrayList<Action>();
        
        // add 
        actionList.add(new ViewSourceAction());
        actionList.add(null);  // separator
        actionList.addAll(Arrays.asList(super.getActions()));
        
        return actionList.toArray(new Action[0]);
    }
    
    protected class ViewSourceAction extends AbstractAction {
        public ViewSourceAction() {
            putValue(Action.NAME, NbBundle.getMessage(ViewSourceAction.class, "CTL_ViewSourceAction"));
        }

        public void actionPerformed(ActionEvent evt) {
            BookViewer bookViewer = getBookViewer();
            if (bookViewer == null) {
                return;
            }
            bookViewer.viewSource();
        }
    }
}
