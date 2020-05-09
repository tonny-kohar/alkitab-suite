/* This work has been placed into the public domain. */
package kiyut.alkitab.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import net.java.swingfx.waitwithstyle.CancelableProgessAdapter;
import net.java.swingfx.waitwithstyle.PerformanceCancelableProgressPanel;
import org.crosswire.common.progress.JobManager;
import org.crosswire.common.progress.Progress;
import org.crosswire.common.progress.WorkEvent;
import org.crosswire.common.progress.WorkListener;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.book.BookException;
import org.crosswire.jsword.index.IndexManagerFactory;
import org.openide.windows.WindowManager;

/**
 * This is a wrapper for JSword IndexManager. 
 * It is a utility class and providing nice index progress using SwingFX.
 * 
 * Note: for plugin developer, if you want to create index, please use this class
 * rather than invoking JSword IndexManager directly.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public final class Indexer {

    private static final Indexer instance; // The single instance
    static {
        instance = new Indexer();
    }
    
    private EventListenerList listenerList;

    /**
     * Returns singleton instance accessor method for indexer
     *
     * @return The single instance.
     */
    public static Indexer getInstance() {
        return instance;
    }

    // to prevent instantiation
    private Indexer() {
        listenerList = new EventListenerList();
    }
    
    /** Registers listener so that it will receive ChangeEvent when createIndex or removeIndex.
     * The ChangeEvent source will be this Indexer.
     * Note: to keep everything simple, it will trigger the notifcation does not matter when it is failed/success.
     * It just fire the notication at the end of the process (not per book but per process)
     *@param listener the ProgressListener to register
     */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }
    
    /** Unregisters listener so it will not receive ChangeEvent when createIndex or removeIndex. */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }
    
    /** Notifies all listeners that have registered interest for notification on this event type. */
    private void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        ChangeEvent event = new ChangeEvent(this);
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                ((ChangeListener)listeners[i+1]).stateChanged(event);
            }
        }
    }

    /** 
     * Create index for the specified books for category Bible and Commentary only.
     * It will indexing the books which does not have index yet. 
     * It is the same as createIndex(books,false)
     * @param books List of Books to be indexed
     * @see #createIndex(List, boolean)
     */
    public void createIndex(List<Book> books) {
        createIndex(books, false);
    }

    /** 
     * Create index for the specified books for category Bible and Commentary only.
     * It will indexing/re-indexing the books. This methods also block the GUI using SwingFX
     * @param books List of Books to be indexed
     * @param reindex flag for reindex
     * @see #createIndex(List)
     */
    public void createIndex(List<Book> books, boolean reindex) {
        IndexerProgress progress = new IndexerProgress();
        progress.createIndex(books, reindex);
    }

    /**
     * Only convenience method to create index for single book
     * @param book the Book to be Indexed
     *  @see #createIndex(List, boolean)
     *  @see #createIndex(Book, boolean)
     */
    public void createIndex(Book book) {
        createIndex(book, false);
    }

    /**
     *  Only convenience method to create index for single book
     *  @param book the Book to be Indexed
     *  @param reindex flag for reindex
     *  @see #createIndex(List, boolean)
     */
    public void createIndex(Book book, boolean reindex) {
        List<Book> books = new ArrayList<>(1);
        books.add(book);

        IndexerProgress progress = new IndexerProgress();
        progress.createIndex(books, reindex);
    }

    public void removeIndex(Book book) throws BookException {
        IndexManagerFactory.getIndexManager().deleteIndex(book);
    }

    /** Implementation of indexing progress using SwingFX */
    class IndexerProgress {

        private PerformanceCancelableProgressPanel progressPane;
        private CancelableProgessAdapter progressAdapter;
        private List<Progress> indexJobs;
        private boolean displayed = false;
        private WorkListener workListener = null;
        private double totalWork = 0;
        private double subTotalWork = 0;

        // used to restore to the original
        private JRootPane rootPane = null;
        private Component origGlassPane = null;
        
        // XXX workaround for PerformanceProgressPanel drawTextAt, because it does not write text
        private double textY;

        public IndexerProgress() {
            indexJobs = Collections.synchronizedList(new ArrayList<>());
            workListener = createWorkListener();
            JobManager.addWorkListener(workListener);

            progressAdapter = new CancelableProgessAdapter(null) {
                @Override
                public void paintSubComponents(double maxY) {
                    // XXX workaround for PerformanceProgressPanel drawTextAt, because it does not write text
                    super.paintSubComponents(maxY);
                    textY = maxY + 30;
                }
            };
            
            // XXX workaround for Java 1.5 useBackBuffer should be false if not it will have screen artifact
            boolean useBackBuffer = true;
            //System.out.println(System.getProperty("java.version"));
            if (System.getProperty("java.version").startsWith("1.5")) {
                useBackBuffer = false;
            }
            progressPane = new PerformanceCancelableProgressPanel(useBackBuffer,progressAdapter) {
                private Rectangle2D oldBounds = null;

                @Override
                public void paintComponent(Graphics g) {
                    // XXX workaround for PerformanceProgressPanel drawTextAt, because it does not write text
                    super.paintComponent(g);

                    // clear the old area
                    //Graphics2D g2 = (Graphics2D)g.create();
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (oldBounds != null) {
                        g2.setColor(Color.WHITE);
                        g2.fill(oldBounds);
                        //g2.setColor(getForeground());
                        //g2.draw(oldBounds);
                    }
                    
                    double y = textY;
                    FontRenderContext context = g2.getFontRenderContext();
                    TextLayout layout = new TextLayout(getText(), getFont(), context);
                    Rectangle2D bounds = layout.getBounds();

                    g2.setColor(getForeground());
                    float textX = (float) (getWidth() - bounds.getWidth()) / 2;
                    y = (float) (y + layout.getLeading() + 2 * layout.getAscent());
                    layout.draw(g2, textX, (float)y);

                    oldBounds = bounds;
                    oldBounds.setRect(textX-2, y-oldBounds.getHeight()-2, oldBounds.getWidth() + 5,oldBounds.getHeight() + 5);
                }
            }; 
            

            // make the font bigger
            Font font = UIManager.getFont("Panel.font");
            if (font != null) {
                progressPane.setFont(font.deriveFont(font.getSize2D() + 2));
            }

            progressPane.addCancelListener((ActionEvent evt) -> {
                cancelIndexing();
            });
        }

        public void createIndex(List<Book> books, boolean reindex) {
            if (books == null) {
                throw new IllegalArgumentException("books should not be null"); // NOI18N

            }

            List<Book> list = filterBooks(books, reindex);

            if (books.isEmpty()) {
                return;
            }

            show();
            for (int i = 0; i < list.size(); i++) {
                IndexManagerFactory.getIndexManager().scheduleIndexCreation(list.get(i));
            }
        }

        /** Filter the book that meet criteria */
        private List<Book> filterBooks(List<Book> books, boolean reindex) {
            List<Book> list = new ArrayList<>();

            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                if (book.getBookCategory().equals(BookCategory.BIBLE) || book.getBookCategory().equals(BookCategory.COMMENTARY)) {
                    if (!IndexManagerFactory.getIndexManager().isIndexed(book) || reindex) {
                        list.add(book);
                    }
                }
            }

            return list;
        }

        private synchronized void show() {
            Runnable runner = () -> {
                Frame mainWindow = WindowManager.getDefault().getMainWindow();
                if (mainWindow instanceof JFrame) {
                    JFrame owner = (JFrame) mainWindow;
                    rootPane = owner.getRootPane();
                    origGlassPane = owner.getGlassPane();
                    rootPane.setGlassPane(progressPane);
                    rootPane.revalidate();
                }
                
                progressPane.setText("Indexing");
                
                progressAdapter.setAdaptee(progressPane);
                progressPane.setVisible(true);
                displayed = true;
                //System.out.println("show");
            };

            if (SwingUtilities.isEventDispatchThread()) {
                runner.run();
            } else {
                SwingUtilities.invokeLater(runner);
            }
        }

        private synchronized void hide() {
            Runnable runner = () -> {
                //progressPane.stop();
                progressPane.setVisible(false);  // for PerformanceCancellableProgressPanel
                
                if (rootPane != null && origGlassPane != null) {
                    rootPane.setGlassPane(origGlassPane);
                }
                
                if (workListener != null) {
                    JobManager.removeWorkListener(workListener);
                    workListener = null;
                }
                
                displayed = false;
                fireStateChanged();
                //System.out.println("hide");
            };

            if (SwingUtilities.isEventDispatchThread()) {
                runner.run();
            } else {
                SwingUtilities.invokeLater(runner);
            }
        }

        private WorkListener createWorkListener() {
            WorkListener listener = new WorkListener() {
                @Override
                public void workProgressed(WorkEvent evt) {
                    synchronized (indexJobs) {
                        Progress job = evt.getJob();
                        if (job.isFinished()) {
                            indexJobs.remove(job);
                            subTotalWork = subTotalWork + job.getTotalWork();
                        } else {
                            if (!indexJobs.contains(job)) {
                                indexJobs.add(job);
                                totalWork = totalWork + job.getTotalWork();
                            }
                        }
                        
                        double completed = subTotalWork;
                        for (int i=0; i <indexJobs.size(); i++) {
                            job = indexJobs.get(i);
                            completed = completed + job.getWork();
                        }
                        
                        // avoid division by zero 
                        if (totalWork == 0) { 
                            completed = 0;
                        } else {
                            completed = (completed/totalWork) * 100;
                        }
                        
                        updateProgress(completed);
                    }
                }

                @Override
                public void workStateChanged(WorkEvent evt) {
                    // do nothing
                }
            };
            return listener;
        }

        private void updateProgress(double completed) {
            if (!displayed) {
                return;
            }

            if (indexJobs.isEmpty()) {
                hide();
                return;
            }

            progressPane.setText((int)completed + "% completed");
            //System.out.println((int)completed + "% updateProgress");
        }

        protected synchronized void cancelIndexing() {
            synchronized (indexJobs) {
                while (!indexJobs.isEmpty()) {
                    indexJobs.get(0).cancel();
                }
            }
        }
    }
}
