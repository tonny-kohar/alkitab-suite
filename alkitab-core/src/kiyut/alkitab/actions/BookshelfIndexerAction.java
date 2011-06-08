/* This work has been placed into the public domain. */

package kiyut.alkitab.actions;

import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import kiyut.alkitab.api.Indexer;
import kiyut.alkitab.windows.BookshelfTopComponent;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.openide.util.NbBundle;

/**
 * Implementation of Bookshelf Index or Re-index Action
 * 
 */
public class BookshelfIndexerAction extends AbstractAction {

    private TreeSelectionListener treeSelectionListener;

    public BookshelfIndexerAction() {
        super(NbBundle.getMessage(BookshelfIndexerAction.class, "CTL_BookshelfIndexerAction"));

        treeSelectionListener = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent evt) {
                TreePath treePath = evt.getPath();
                if (treePath == null) {
                    setEnabled(false);
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

                Object obj = node.getUserObject();
                if (!(obj instanceof Book)) {
                    setEnabled(false);
                    return;
                }

                Book book = (Book)obj;
                if (!(book.getBookCategory().equals(BookCategory.BIBLE) || book.getBookCategory().equals(BookCategory.COMMENTARY))) {
                    setEnabled(false);
                    return;
                }

                setEnabled(true);
            }
        };

        BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
        bs.getBookshelf().getSelectionModel().addTreeSelectionListener(treeSelectionListener);
    }

    @Override
    protected void finalize() throws Throwable {
        BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
        if (bs != null) {
            JTree tree = bs.getBookshelf();
            tree.getSelectionModel().removeTreeSelectionListener(null);
        }
        treeSelectionListener = null;

        super.finalize();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                doCreateIndex();
            }
        });
    }

    protected void doCreateIndex() {
        BookshelfTopComponent bs = BookshelfTopComponent.findInstance();
        Book book = bs.getSelectedBook();
        if (book == null) {
            return;
        }
      
        Indexer.getInstance().createIndex(book, true);
        
    }
}
