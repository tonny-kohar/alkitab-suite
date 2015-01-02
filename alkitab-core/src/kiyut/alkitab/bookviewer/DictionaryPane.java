/* This work has been placed into the public domain. */

package kiyut.alkitab.bookviewer;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import kiyut.alkitab.navigator.KeyListModel;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * Panel which display dictionary,lexicon, glossary, or something similar.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class DictionaryPane extends DefinitionPane {

    protected JTextField searchField;
    protected SearchTask searchTask = new SearchTask();
    protected boolean searching = false;

    public DictionaryPane(Book book) {
        super(book);

        searchField = new JTextField();
        indexPane.add(searchField,BorderLayout.PAGE_START);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                SwingUtilities.invokeLater(searchTask);
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
                        || keyCode == KeyEvent.VK_KP_UP || keyCode == KeyEvent.VK_KP_DOWN) {
                    indexList.requestFocusInWindow();
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(indexList, evt);
                }
            }

        });
    }

    @Override
    public void setKey(Key key) {
        super.setKey(key);
        if (key == null) { return; }

        // clear the searchField
        if (!searching && searchField != null) {
            searchField.setText(null);
        }
    }

    public class SearchTask implements Runnable {
        @Override
        public void run() {
            searching = true;
            try {
                searchImpl();
            } finally {
                searching = false;
            }
        }

        protected synchronized void searchImpl() {
            Book book = getBook();
            Key key = book.getValidKey(searchField.getText());

            KeyListModel keyListModel = (KeyListModel)indexList.getModel();
            int i = keyListModel.getKey().indexOf(key);

            //System.out.println("nearest: " + i);
            if (i != -1) {
                key = keyListModel.getKey().get(i);
                setKey(key);
            }
        }
    }
}
