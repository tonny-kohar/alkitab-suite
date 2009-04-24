/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.CardLayout;
import javax.swing.event.ChangeEvent;
import kiyut.alkitab.api.BookViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.api.History;
import kiyut.alkitab.api.HistoryManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.ViewerHints;
import kiyut.alkitab.api.event.BookChangeEvent;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.options.ViewerHintsOptions;
import kiyut.alkitab.api.Indexer;
import kiyut.alkitab.util.SwordUtilities;
import kiyut.swing.combo.SeparatorComboBox;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.book.BookFilters;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.index.IndexStatus;
import org.crosswire.jsword.index.search.DefaultSearchModifier;
import org.crosswire.jsword.index.search.DefaultSearchRequest;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.Passage;
import org.crosswire.jsword.passage.PassageTally;
import org.crosswire.jsword.passage.RestrictionType;

/**
 * Implementation of {@link kiyut.alkitab.api.BookViewer BookViewer} which able to display parallel book
 * 
 */
public class ParallelBookViewerPane extends AbstractBookViewerPane {
    
    protected ResourceBundle bundle = ResourceBundle.getBundle(ParallelBookViewerPane.class.getName());
    
    protected BookTextPane bookTextPane;

    /** just a flag indicating searching mode */
    protected boolean searching;

    protected ActionListener bookComboActionListener;
    
    protected HistoryManager historyManager;

    /** just a flag indicating history is in progress */
    protected boolean historyInProgress;
    
    protected volatile boolean unindexedBooks; 
    protected volatile boolean indexInProgress;  
    //protected List<Progress> indexJobs;
    //protected IndexWorkListener indexWorkListener;

    protected ChangeListener indexChangeListener;

    /** Creates new ParallelBookViewerPane. */
    public ParallelBookViewerPane() {
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
        java.awt.GridBagConstraints gridBagConstraints;

        splitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        addBookButton = new javax.swing.JButton();
        removeBookButton = new javax.swing.JButton();
        booksComboPane = new javax.swing.JPanel();
        compareCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passagePane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        passageTextArea = new javax.swing.JTextArea();
        jToolBar2 = new javax.swing.JToolBar();
        passageGoButton = new javax.swing.JButton();
        passageChooserButton = new javax.swing.JButton();
        searchPane = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        searchTextArea = new javax.swing.JTextArea();
        jToolBar3 = new javax.swing.JToolBar();
        searchGoButton = new javax.swing.JButton();
        advancedSearchButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        indexButton = new javax.swing.JButton();
        bookScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(bundle.getString("CTL_Book.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        jPanel1.add(jLabel1, gridBagConstraints);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        addBookButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/plus.png"))); // NOI18N
        addBookButton.setToolTipText(bundle.getString("HINT_AddBook.Text")); // NOI18N
        addBookButton.setFocusable(false);
        addBookButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addBookButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addBookButton);

        removeBookButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/minus.png"))); // NOI18N
        removeBookButton.setToolTipText(bundle.getString("HINT_RemoveBook.Text")); // NOI18N
        removeBookButton.setFocusable(false);
        removeBookButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeBookButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(removeBookButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(jToolBar1, gridBagConstraints);

        booksComboPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(booksComboPane, gridBagConstraints);

        compareCheckBox.setText(bundle.getString("CTL_Compare.Text")); // NOI18N
        compareCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        jPanel1.add(compareCheckBox, gridBagConstraints);

        jLabel2.setText(bundle.getString("CTL_Search.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 6);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText(bundle.getString("CTL_Passsage.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 6);
        jPanel1.add(jLabel3, gridBagConstraints);

        passagePane.setLayout(new java.awt.GridBagLayout());

        passageTextArea.setLineWrap(true);
        passageTextArea.setRows(1);
        passageTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(passageTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        passagePane.add(jScrollPane1, gridBagConstraints);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        passageGoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/go.png"))); // NOI18N
        passageGoButton.setToolTipText(bundle.getString("HINT_PassageGo.Text")); // NOI18N
        passageGoButton.setFocusable(false);
        passageGoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        passageGoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(passageGoButton);

        passageChooserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/edit.png"))); // NOI18N
        passageChooserButton.setToolTipText(bundle.getString("HINT_PassageChooser.Text")); // NOI18N
        passageChooserButton.setFocusable(false);
        passageChooserButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        passageChooserButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(passageChooserButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 0, 3);
        passagePane.add(jToolBar2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(passagePane, gridBagConstraints);

        searchPane.setLayout(new java.awt.CardLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        searchTextArea.setLineWrap(true);
        searchTextArea.setToolTipText(bundle.getString("HINT_Search.Text")); // NOI18N
        searchTextArea.setWrapStyleWord(true);
        jScrollPane2.setViewportView(searchTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane2, gridBagConstraints);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        searchGoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/go.png"))); // NOI18N
        searchGoButton.setToolTipText(bundle.getString("HINT_PassageGo.Text")); // NOI18N
        searchGoButton.setFocusable(false);
        searchGoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        searchGoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(searchGoButton);

        advancedSearchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kiyut/alkitab/swing/edit.png"))); // NOI18N
        advancedSearchButton.setToolTipText(bundle.getString("HINT_AdvancedSearch.Text")); // NOI18N
        advancedSearchButton.setFocusable(false);
        advancedSearchButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        advancedSearchButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(advancedSearchButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 0, 3);
        jPanel4.add(jToolBar3, gridBagConstraints);

        searchPane.add(jPanel4, "search-layout");

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        indexButton.setText(bundle.getString("CTL_NoIndex.Text")); // NOI18N
        jPanel5.add(indexButton);

        searchPane.add(jPanel5, "index-layout");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(searchPane, gridBagConstraints);

        splitPane.setLeftComponent(jPanel1);
        splitPane.setRightComponent(bookScrollPane);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBookButton;
    private javax.swing.JButton advancedSearchButton;
    private javax.swing.JScrollPane bookScrollPane;
    private javax.swing.JPanel booksComboPane;
    private javax.swing.JCheckBox compareCheckBox;
    private javax.swing.JButton indexButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JButton passageChooserButton;
    private javax.swing.JButton passageGoButton;
    private javax.swing.JPanel passagePane;
    private javax.swing.JTextArea passageTextArea;
    private javax.swing.JButton removeBookButton;
    private javax.swing.JButton searchGoButton;
    private javax.swing.JPanel searchPane;
    private javax.swing.JTextArea searchTextArea;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables
 
    protected void initCustom() {
        this.viewerHints = new ViewerHints<ViewerHints.Key,Object>(ViewerHintsOptions.getInstance().getViewerHints());
        unindexedBooks = false;
        indexInProgress = false;
        
        BookViewerOptions opts = BookViewerOptions.getInstance();
        maximumBook = opts.getParallelBookLimit();
        historyManager = new BookViewerHistoryManager();
               
        //splitPane.setOneTouchExpandable(true);
        
        bookTextPane = new BookTextPane(viewerHints);
        bookScrollPane.setViewportView(bookTextPane);
        
        //getActionMap().setParent(bookTextPane.getActionMap());
        
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addBook(null);
                refresh();
            }
        });
        
        removeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeBook(booksComboPane.getComponentCount()-1);
                refresh();
            }
        });
        
        bookComboActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JComboBox comboBox = (JComboBox) evt.getSource();
                int index = -1;
                for (int i = 0; i < booksComboPane.getComponentCount(); i++) {
                    if (comboBox.equals(booksComboPane.getComponent(i))) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    return;
                }

                String bookName = comboBox.getSelectedItem().toString();
                setBook(index, bookName);
                refresh();
            }
        };
        
        compareCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compareView(compareCheckBox.isSelected());
                refresh();
            }
        });
        
        passageChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PassageChooser passageChooser = new PassageChooser();
                passageChooser.setPassage(passageTextArea.getText());
                int choice = passageChooser.showDialog(ParallelBookViewerPane.this);
                if (choice != JOptionPane.OK_OPTION) {
                    return;
                }
                Key key = passageChooser.getKey();
                setKey(key);
                refresh();
            }
        });
        
        passageGoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String str = passageTextArea.getText();
                setKey(str);
                refresh();
            }
        });
        
        advancedSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchPane searchPane = new SearchPane();
                searchPane.setRanked(false);
                searchPane.setSearchLimit(BookViewerOptions.getInstance().getDefaultSearchLimit());
                int choice = searchPane.showDialog(ParallelBookViewerPane.this);
                if (choice != JOptionPane.OK_OPTION) {
                    return;
                }
                boolean ranked = searchPane.isRanked();
                int searchLimit = searchPane.getSearchLimit();
                searchTextArea.setText(searchPane.getSearchString());
                search(searchTextArea.getText(),ranked,searchLimit);
            }
        });
        
        searchGoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                search(searchTextArea.getText());
            }
        });
        
        passageTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                evt.consume();
                
                boolean ctrlDown = false;
                if ((evt.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) {
                    ctrlDown = true;
                }
                
                if (ctrlDown) {
                    passageChooserButton.doClick();
                } else {
                    setKey(passageTextArea.getText());
                    refresh();

                }
            }
        });
        
        searchTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                
                evt.consume();
                
                boolean ctrlDown = false;
                if ((evt.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) {
                    ctrlDown = true;
                }
                
                if (ctrlDown) {
                    advancedSearchButton.doClick();
                } else {
                    search(searchTextArea.getText());
                    refresh();
                }
            }
        });
        
        indexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                List<Book> books = bookTextPane.getBooks();
                Indexer.getInstance().createIndex(books,true);
            }
        });

        indexChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                checkIndexStatus();
            }
        };
        
        Indexer.getInstance().addChangeListener(indexChangeListener);
    }

    
    /** cleanup resource and this component should not be used after a call to this */
    public void dispose() {
        if (indexChangeListener != null)  {
            Indexer.getInstance().removeChangeListener(indexChangeListener);
        }
    }
    
    @Override
    public String getName() {
        if (bookTextPane == null) {
            return "";
        }
        
        List<Book> books = bookTextPane.getBooks();
        
        if (books.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (int i=0;i <books.size(); i++) {
            if (i > 0) {
                sb.append("=");
            } 
            sb.append(books.get(i).getInitials());
        }
        
        return sb.toString();
    }
    
    public JComponent getViewerComponent() {
        return bookTextPane;
    }
    
    public void addHyperlinkListener(HyperlinkListener listener) {
        bookTextPane.addHyperlinkListener(listener);
    }
    
    public void removeHyperlinkListener(HyperlinkListener listener) {
        bookTextPane.removeHyperlinkListener(listener);
    }
    
    public void openURI(SwordURI uri) {
        //System.out.println("ParallelBookViewerPane.openURI()");
        
        if (uri.getPath().equals("")) {
            if (bookTextPane.getBooks().isEmpty()) {
                // try to open preferences default bible
                Book book =  null;
                String bookName = BookViewerOptions.getInstance().getDefaultBible();
                if (bookName != null) {
                    book =  Books.installed().getBook(bookName);
                }
                if (book == null) { 
                    addBookButton.doClick();
                } else {
                    addBook(book.getInitials());
                }
            }
        } else {
            Book book =  Books.installed().getBook(uri.getPath());
            if (book == null) { return; }
            addBook(book.getInitials());
        }
        
        if (!uri.getFragment().equals("")) {
            passageTextArea.setText(uri.getFragment());
            setKey(passageTextArea.getText());
        }
        refresh();
    }
    
    /** Add Book
     * @param bookName Book Initials or Book Name
     */
    public void addBook(String bookName) {
        maximumBook = BookViewerOptions.getInstance().getParallelBookLimit();
        int count = booksComboPane.getComponentCount();
        if (count >= maximumBook) {
            return;
        }
        
        JComboBox comboBox = createBookComboBox();

        if (bookName == null) {
            bookName = comboBox.getItemAt(0).toString();
        }
        
        Book book = Books.installed().getBook(bookName);
        if (book == null) {
            return;
        }
        
        comboBox.setSelectedItem(bookName);
        
        bookTextPane.getBooks().add(book);
        checkIndexStatus();

        booksComboPane.add(comboBox);
        booksComboPane.revalidate();
        booksComboPane.repaint();
        
        comboBox.addActionListener(bookComboActionListener);

        // try to display something, if this is first book displayed
        if (count == 0 && getKey() == null) {
            initialView(book);
        }
        
        firePropertyChange(BookViewer.VIEWER_NAME, null, getName());
        fireBookChange(new BookChangeEvent(this));
    }

    /** This is only called if it is the first book displayed and have not specified
     * any key to be displayed. Try to display something eg: display Gen 1 for bible or commentary
     * @param book Book
     */
    protected void initialView(Book book) {
        Key initialKey = null;

        BookCategory cat = book.getBookCategory();
        if (BookCategory.BIBLE.equals(cat) || BookCategory.COMMENTARY.equals(cat)) {
            // XXX workaround for book.getGlobalKeyList() bug with key,
            // if the locale is not english eg: fa
            // try to display the Gen 1 (chapter)

            // note: need to getValidKey, so history will work
            // getValidKey will convert Verse to Passage
            // see BookViewerHistory constructor
            initialKey = book.getValidKey("Gen 1");

        } else {
            initialKey = book.getGlobalKeyList();
            if (initialKey != null) {
                if (initialKey.getCardinality() > 0) {
                    initialKey = initialKey.get(0);
                }
            }
        }

        if (initialKey == null) {
            return;
        }

        setKey(initialKey);
    }
    
    /** Remove book at particular index.
     *  It do nothing if there is only one book left
     * @param index index at which the specified book is to be removed.
     * @throws ArrayIndexOutOfBoundsException - if the index value does not exist.
     */
    public void removeBook(int index) {
        int count = booksComboPane.getComponentCount();
        if (count <= 1) {
            return;
        }
        
        //comboBox.addActionListener(bookComboActionListener);
        JComboBox comboBox = (JComboBox) booksComboPane.getComponent(index);
        comboBox.removeActionListener(bookComboActionListener);
        booksComboPane.remove(index);
        booksComboPane.revalidate();
        booksComboPane.repaint();

        bookTextPane.getBooks().remove(index);
        checkIndexStatus();
        
        firePropertyChange(BookViewer.VIEWER_NAME, null, getName());
        fireBookChange(new BookChangeEvent(this));

    }
    
    /** Replaces book at particular index
     * @param index index at which the specified book is to be replaced.
     * @param bookName Book Initials or Book Name
     */
    public void setBook(int index, String bookName) {
        Book book = Books.installed().getBook(bookName);
        if (book == null) {
            return;
        }

        bookTextPane.getBooks().set(index, book);
        checkIndexStatus();

        firePropertyChange(BookViewer.VIEWER_NAME, null, getName());
        fireBookChange(new BookChangeEvent(this));
    }
    
    protected synchronized void checkIndexStatus() {
        unindexedBooks = false;
        List<Book> books = bookTextPane.getBooks();
        for (int i=0; i<books.size(); i++) {
            IndexStatus status = books.get(i).getIndexStatus();
            if (!status.equals(IndexStatus.DONE)) {
                unindexedBooks = true;
                break;
            }
        }
        
        CardLayout cl = (CardLayout)searchPane.getLayout();
        if (unindexedBooks) {
            cl.show(searchPane, "index-layout");
        } else {
            cl.show(searchPane, "search-layout");
        }
        searchPane.revalidate();
    }
    
    public List<Book> getBooks() {
        List<Book> srcBooks = bookTextPane.getBooks();
        return Collections.unmodifiableList(srcBooks);
    }

    public int getBookCount() {
        return bookTextPane.getBooks().size();
    }

    public void viewSource() {
        SourceCodePane sourcePane = new SourceCodePane();
        sourcePane.setText(bookTextPane.getRawText(), bookTextPane.getOSISText(), bookTextPane.getHTMLText());
        sourcePane.showDialog(this,true);
    }
    
    public void compareView(boolean compare) {
        bookTextPane.setCompareView(compare);
        if (compare != compareCheckBox.isSelected()) {
            compareCheckBox.setSelected(compare);
        }
    }

    /** Return {@code true} or {@code false}
     * @return {@code true} or {@code false}
     */
    @Override
    public boolean isCompareView() {
        return bookTextPane.isCompareView();
    }
    
    protected void setKey(String keyString) {
        Key key = null;
        List<Book> books = bookTextPane.getBooks();
        if (!books.isEmpty()) {
            key = books.get(0).getValidKey(keyString);
            setKey(key);
        } 
    }
    
    public void setKey(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument key can't be null");
        }
        
        passageTextArea.setText(key.getName());
        passageTextArea.setCaretPosition(0);

        Key displayKey = key;
        if (!historyInProgress) {
            BookViewerHistory hist = new BookViewerHistory(key);
            historyManager.add(hist);
            displayKey = hist.current();
        }
        
        bookTextPane.setKey(displayKey);

        if (!searching) {
            searchTextArea.setText(null);
        }
    }
    
    public Key getKey() {
        return bookTextPane.getKey();
    }

    public void refresh() {
        //System.out.println("ParallelBookViewerPane.refresh()");
        bookTextPane.refresh(true);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
    
    public synchronized void goBack() {
        if (!historyManager.hasBack()) {
            return;
        }
        
        historyInProgress = true;
        try {
            History hist = historyManager.back();
            setKey(hist.getKey());
        
            if (hist.getKey() instanceof Passage) {
                bookTextPane.setKey(hist.current());
            }
            
        } finally {
            historyInProgress = false;
        }
        
        refresh();
    }
    
    public synchronized void goForward() {
        if (!historyManager.hasForward()) {
            return;
        }
        
        historyInProgress = true;
        try {
            History hist = historyManager.forward();
            setKey(hist.getKey());
        
            if (hist.getKey() instanceof Passage) {
                bookTextPane.setKey(hist.current());
            }
        } finally {
            historyInProgress = false;
        }
        refresh();
    }
    
    public synchronized void goPrevious() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasPrevious()) {
            return;
        }
        
        historyInProgress = true;
        try {
            bookTextPane.setKey(hist.previous());
        } finally {
            historyInProgress = false;
        }
        
        refresh();
    }
    
    public synchronized void goNext() {
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        if (!hist.hasNext()) {
            return;
        }
        
        historyInProgress = true;
        try {
            bookTextPane.setKey(hist.next());
        } finally {
            historyInProgress = false;
        }
        refresh();
    }
    
    /** Expand/Widen currently displayed passage by
     * @param by The number of verses/keys to widen by
     * @param restrict The RestrictionType
     */
    public synchronized void blur(int by, RestrictionType restrict) {
        if (restrict == null) {
            restrict = RestrictionType.NONE;
        }
        
        History hist = historyManager.current();
        if (hist == null) {
            return;
        }
        
        historyInProgress = true;
        try {
            hist.blur(by, restrict);
            bookTextPane.setKey(hist.current());
            passageTextArea.setText(hist.getKey().getName());
            passageTextArea.setCaretPosition(0);
        } finally {
            historyInProgress = false;
        }
        refresh();
    }

    public void requestFocusForPassageComponent() {
        passageTextArea.selectAll();
        passageTextArea.requestFocusInWindow();
    }

    public void requestFocusForSearchComponent() {
        searchTextArea.selectAll();
        searchTextArea.requestFocusInWindow();
    }

    /** Search the specified String which is not ranked
     * @param searchString String to search
     * @see #search(String,boolean,int)
     */
    protected void search(String searchString) {
        search(searchString,false,BookViewerOptions.getInstance().getDefaultSearchLimit());
    }
    
    /** Search the specified String
     * @param searchString String to search
     * @param ranked true or false
     * @param searchLimit only applicable if ranked is true
     * @see #search(String,boolean,int)
     */
    protected void search(String searchString, boolean ranked, int searchLimit) {
        searching = true;
        try {
            searchImpl(searchString, ranked, searchLimit);
        } finally {
            searching = false;
        }
    }

    private void searchImpl(String searchString, boolean ranked, int searchLimit) {
        if (searchString == null || searchString.length() == 0) {
            return;
        }
        
        List<Book> books = bookTextPane.getBooks();
        if (books.isEmpty()) { 
            JOptionPane.showMessageDialog(this, bundle.getString("MSG_EmptyBooks.Text"), bundle.getString("MSG_EmptyBooks.Title"), JOptionPane.ERROR_MESSAGE);
            return; 
        }

        //boolean ranked = searchLimit <= 0 ? false : true;

        DefaultSearchModifier modifier = new DefaultSearchModifier();
        modifier.setRanked(ranked);
        
        Key results = null;
        int total = 0;
        
        try {
            results =  books.get(0).find(new DefaultSearchRequest(searchString, modifier));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return;
        }
        
        total = results.getCardinality();
        int partial = total;
        
        if (total == 0) {
            Object[] args = {searchString};
            String msg = MessageFormat.format(bundle.getString("MSG_SearchNoHits.Text"), args);
            JOptionPane.showMessageDialog(this, msg , bundle.getString("MSG_SearchHits.Title"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (results instanceof PassageTally || ranked)  {
            PassageTally tally = (PassageTally) results;
            tally.setOrdering(PassageTally.ORDER_TALLY);
            
            if (searchLimit > 0 && searchLimit < total)  {
                tally.trimRanges(searchLimit, RestrictionType.NONE);
                partial = searchLimit;
            }
        }

        if (total == partial) {
            Object[] args = {searchString,new Integer(total)};
            String msg = MessageFormat.format(bundle.getString("MSG_SearchHits.Text"), args);
            JOptionPane.showMessageDialog(this, msg , bundle.getString("MSG_SearchHits.Title"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] args = {searchString,new Integer(partial),new Integer(total)};
            String msg = MessageFormat.format(bundle.getString("MSG_SearchPartialHits.Text"), args);
            JOptionPane.showMessageDialog(this, msg , bundle.getString("MSG_SearchHits.Title"), JOptionPane.INFORMATION_MESSAGE);
        }
        
        setKey(results);
        refresh();
    }
    
    /** Return JComboBox with book initials sorted by Bible then Commentary 
     * @return JComboBox
     */
    @SuppressWarnings("unchecked")
    protected JComboBox createBookComboBox() {
        List<Book> books = new ArrayList<Book>();
        List<String> bookInitials = new ArrayList<String>();
        Comparator<Book> comparator = SwordUtilities.getBookInitialsComparator();
        
        // bible
        List tBooks = Books.installed().getBooks(BookFilters.getOnlyBibles());
        books.addAll(tBooks);
        Collections.sort(books, comparator);
        for (int i=0; i<books.size(); i++) {
            bookInitials.add(books.get(i).getInitials());
        }
        
        // Commentary
        books.clear();
        tBooks = Books.installed().getBooks(BookFilters.getCommentaries());
        books.addAll(tBooks);
        Collections.sort(books, comparator);
        if (books.size() > 0) {
            bookInitials.add(SeparatorComboBox.DEFAULT_SEPARATOR);
        }
        for (int i=0; i<books.size(); i++) {
            Book book = books.get(i);
            
            // XXX do not include personal commentary, not supported yet
            if (book.getInitials().toLowerCase().equals("personal")) {
                continue;
            }
            bookInitials.add(book.getInitials());
        }
        
        SeparatorComboBox comboBox = new SeparatorComboBox(bookInitials.toArray());
        comboBox.setPrototypeDisplayValue("BOOKINITIALS"); //NOI18N
        return comboBox;
    }
}
