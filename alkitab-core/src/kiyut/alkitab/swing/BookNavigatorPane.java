/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import kiyut.alkitab.api.BookViewer;
import java.util.List;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import kiyut.alkitab.api.event.BookChangeEvent;
import kiyut.alkitab.api.event.BookChangeListener;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.KeyUtil;

/**
 * Panel which display book Key aka Table of Content. It display the content using
 * {@link kiyut.alkitab.swing.KeyTree KeyTree}
 * 
 * 
 */
public class BookNavigatorPane extends javax.swing.JPanel {
    
    protected KeyTree keyTree;
    protected BookViewer bookViewer;
    protected BookChangeListener bookChangeListener;
    protected TreeSelectionListener treeSelectionListener;
    protected Book book;
    
    /** Creates new BookNavigatorPane */
    public BookNavigatorPane() {
        initComponents();
        initCustom();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    
    protected void initCustom() {
        //keyTree = new KeyTree();
        //scrollPane.setViewportView(keyTree); 
        
        treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                keyValueChanged(evt);
            }
        };
        
        bookChangeListener = new BookChangeListener() {
            public void bookChanged(BookChangeEvent evt) {
                //System.out.println("BookNavigatorPane.bookChanged()");
                updateKeyTree();
            }
        };
    }
    
    public void setBookViewer(BookViewer bookViewer) {
        //System.out.println("BookNavigatorPane.setBookViewer()");
        BookViewer old = this.bookViewer;
        if (old != null) {
            old.removeBookChangeListener(bookChangeListener);
        }
        
        this.bookViewer = bookViewer;
        
        if (bookViewer != null) {
            bookViewer.addBookChangeListener(bookChangeListener);
            updateKeyTree();
        }
    }
    
    protected void updateKeyTree() {
        boolean removeView = false;
        
        if (bookViewer == null) {
            removeView = true;
        } else {
            List<Book> books = bookViewer.getBooks();
            if (books.isEmpty()) {
                removeView = true;
            }
        }
        
        if (removeView) {
            book = null;
            scrollPane.setViewportView(keyTree);
            if (keyTree != null) {
                keyTree.removeTreeSelectionListener(treeSelectionListener);
            }
            return;
        }

        Book theBook = bookViewer.getBooks().get(0);
        if (!theBook.equals(book)) {
            book = theBook;
            BookCategory bookCategory = book.getBookCategory();
            if (bookCategory.equals(BookCategory.BIBLE) || bookCategory.equals(BookCategory.COMMENTARY)) {
                if (keyTree != null) {
                    if (keyTree.getModel() instanceof BibleKeyTreeModel) {
                        // for bible do not need to recreate the verse tree
                        return;
                    }
                }
                keyTree = new KeyTree(new BibleKeyTreeModel(BibleKeyTreeModel.LEVEL_VERSE));
            } else {
                Key key = book.getGlobalKeyList();
                keyTree = new KeyTree(new DefaultKeyTreeModel(key));
            }
            scrollPane.setViewportView(keyTree);
            keyTree.addTreeSelectionListener(treeSelectionListener);
        }
    }
    
    protected void keyValueChanged(TreeSelectionEvent evt) {
        TreePath treePath = evt.getPath();
        
        KeyTreeNode node = (KeyTreeNode)treePath.getLastPathComponent();
        Key key = node.getKey();
        if (key == null) {
            return;
        }
        
        // XXX this parts is ugly, rewrite needed
        if (bookViewer != null) {
            if (keyTree.getModel() instanceof BibleKeyTreeModel) {
                if (treePath.getPathCount() <= 2) {
                    // this is book level, too big to be displayed
                    return;
                }
                // convert the key into Passage
                bookViewer.setKey(KeyUtil.getPassage(key));
                bookViewer.refresh();
            } else {
                //System.out.println(obj.toString());
                bookViewer.setKey(key);
                bookViewer.refresh();
            }
        }
    }
    
}
