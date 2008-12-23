/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.passage.Key;

/**
 * Panel which display dictionary,lexicon, glossary, or something similar.
 */
public class DictionaryPane extends DefinitionPane {

    protected JTextField searchField;
    protected SearchTask searchTask = new SearchTask();

    public DictionaryPane(Book book) {
        super(book);

        searchField = new JTextField();
        indexPane.add(searchField,BorderLayout.PAGE_START);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                SwingUtilities.invokeLater(searchTask);
            };

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

    public class SearchTask implements Runnable {
        public void run() {
            Book book = getBook();
            Key key = book.getValidKey(searchField.getText());
            int i = ((KeyListModel) indexList.getModel()).getKey().indexOf(key);
            //System.out.println("nearest: " + i);
            if (i != -1) {
                indexList.setSelectedIndex(i);
                indexList.ensureIndexIsVisible(i);
            }
        }
    }
}
