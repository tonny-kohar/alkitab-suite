/* This work has been placed into the public domain. */
package kiyut.alkitab.api;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * <strong>Note:</strong> This is Netbeans Platform RCP specific API implementation
 * 
 * It is just simple Node wrapper for BookViewer. Anything is handled by {@link BookViewer} <br/>
 * Alternative architecture is this BookViewerNode handle JSword Book rather than BookViewer.
 * The drawback is the API will be very thight with Netbeans Platform RCP, but more integrated into
 * Netbeans Platform.
 * 
 * So which one to choose ? Currently it is using the first approach
 * 
 */
public class BookViewerNode extends AbstractNode {

    protected InstanceContent instanceContent = null;
    protected BookViewer bookViewer;

    public BookViewerNode(BookViewer bookViewer) {
        this(new InstanceContent(), bookViewer);
    }

    private BookViewerNode(InstanceContent instanceContent, BookViewer bookViewer) {
        super(Children.LEAF, new AbstractLookup(instanceContent));
        this.instanceContent = instanceContent;
        instanceContent.add(bookViewer);
        this.bookViewer = bookViewer;
    }

    public void closed() {
        //System.out.println("BookViewerNode.closed()");
        //bookViewer.closed();
        instanceContent.remove(bookViewer);
        this.bookViewer = null;
    }
}
