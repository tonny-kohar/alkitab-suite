/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import kiyut.alkitab.api.BookViewer;
import kiyut.alkitab.api.BookViewerNode;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.options.BookViewerOptions;
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

    protected PropertyChangeListener backgroundPropertyChangeListener;
    
    public abstract BookViewer getBookViewer();
    
    public abstract void openURI(SwordURI uri, String info);

    public BookViewerTopComponent() {
        backgroundPropertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!evt.getPropertyName().equals(BookViewerOptions.BACKGROUND)) {
                    return;
                }
                BookViewer bookViewer = getBookViewer();
                if (bookViewer == null) {
                    return;
                }
                Color bg = null;
                if (evt.getNewValue() instanceof Color) {
                    bg = (Color) evt.getNewValue();
                }
                bookViewer.getViewerComponent().setBackground(bg);
            }
        };
    }

    @Override
    public void componentActivated() {
        if (bookViewerNode != null) {
            setActivatedNodes(new Node[]{bookViewerNode});
        }
    }

    @Override
    public void componentOpened() {
        BookViewerOptions.getInstance().addPropertyChangeListener(backgroundPropertyChangeListener);

        BookViewer bookViewer = getBookViewer();
        if (bookViewer != null) {
            Color bg = BookViewerOptions.getInstance().getBackground();
            //System.out.println("this is executed");
            bookViewer.getViewerComponent().setBackground(bg);
        }
    }

    
    @Override
    public void componentClosed() {
        if (bookViewerNode != null) {
            bookViewerNode.closed();
        }
        BookViewerOptions.getInstance().removePropertyChangeListener(backgroundPropertyChangeListener);
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
    
    public class ViewSourceAction extends AbstractAction {
        public ViewSourceAction() {
            putValue(Action.NAME, NbBundle.getMessage(ViewSourceAction.class, "CTL_ViewSourceAction"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            BookViewer bookViewer = getBookViewer();
            if (bookViewer == null) {
                return;
            }
            bookViewer.viewSource();
        }
    }
}
