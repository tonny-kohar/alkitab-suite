/* This work has been placed into the public domain. */

package kiyut.alkitab.history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.Places;

/**
 * Global History Manager and persist the data between session.
 * It is not complete and stable yet (still experimental), so it might change.
 * Currently it is only for verse or text
 *
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class GlobalHistory {
    private static final String ENTRY = "entry";
    private static final String MILLIS = "millis";
    private static final String TEXT = "text";
    private static final String SEARCH = "search";

    private static final String FILENAME = "history.xml";

    private static GlobalHistory instance; // The single instance
    static {
        instance = new GlobalHistory();
    }

    protected List<Entry> data;
    protected EventListenerList listenerList;
    protected boolean modified;


    /**
     * Returns the single instance
     *
     * @return The single instance.
     */
    public static GlobalHistory getInstance() {
        return instance;
    }

    private GlobalHistory() {
        listenerList = new EventListenerList();
        data = new ArrayList<Entry>();
        load();
        modified = false;
    }

    @SuppressWarnings("unchecked")
    protected synchronized void load() {
        //InstalledFileLocator fileLocator = InstalledFileLocator.getDefault();
        //String parent = System.getProperty("netbeans.user");
        //File file = new File(parent + File.separator + "alkitab-history" + File.separator + FILENAME);
        //if (!file.exists()) { return; }
        
        File file = InstalledFileLocator.getDefault().locate("alkitab-history/" + FILENAME, "", false);
        if (file == null) { 
            return; 
        }
        
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            List<Element> children = doc.getRootElement().getChildren();
            String text;
            String search;
            Element child;

            for (int i = 0; i < children.size(); i++) {
                Element elt = children.get(i);

                text = null;
                search = null;

                child = elt.getChild(TEXT);
                if (child != null) {
                    text = child.getTextTrim();
                }

                child = elt.getChild(SEARCH);
                if (child != null) {
                    search = child.getTextTrim();
                    if (search.length() == 0) {
                        search = null;
                    }
                }

                // just precautuion to make sure the entry is not null
                if (text != null) {
                    data.add(new Entry(Long.parseLong(elt.getAttributeValue(MILLIS)), text, search));
                }
            }

        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage() );
            return;
        }

        fireIntervalAdded(0, data.size()-1);
        
    }

    public synchronized void save() {
        if (!modified) { return; }

        // check folder
        File file = InstalledFileLocator.getDefault().locate("alkitab-history", "", false);
        if (file == null) {
            //String parent = System.getProperty("netbeans.user");
            File parent = Places.getUserDirectory();
            file = new File(parent, "alkitab-history");
            if (!file.exists()) { 
                if (!file.mkdir()) {
                    Logger logger = Logger.getLogger(this.getClass().getName());
                    logger.log(Level.WARNING, "unable to create folder to save history." );
                    return; 
                }
            }
        }
        
        file = new File(file, FILENAME);

        Element root = new Element("history");
        Document doc = new Document(root);
        
        for (int i=0; i<data.size(); i++) {
            Entry entry = data.get(i);
            Element elt = new Element(ENTRY);
            elt.setAttribute(MILLIS, Long.toString(entry.getMillis()));

            Element childElt = new Element(TEXT);
            childElt.setText(entry.getText());
            elt.addContent(childElt);

            childElt = new Element(SEARCH);
            if (entry.getSearch() != null) {
                childElt.setText(entry.getSearch());
            } 
            elt.addContent(childElt);

            root.addContent(elt);
        }

        try {
            Format format = Format.getPrettyFormat();
            format.setIndent("    "); // use 4 space indent
            format.setEncoding("UTF-8");
            format.setLineSeparator(System.getProperty("line.separator"));

            XMLOutputter serializer = new XMLOutputter(format);
            serializer.output(doc, new FileOutputStream(file));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage() );
            return;
        }

        modified = false;
        
    }

    /** Add the passed text into text.
     * @param text the passage as String
     * @see #add(String,String)
     */
    public void add(String text) {
        add(text, null);
    }

    /** Add the passed text and search into text
     * @param text the passage as String
     * @param search the search String or {@code null}
     */
    public void add(String text, String search) {
        // check last entry
        if (!data.isEmpty()) {
            Entry last = data.get(0);
            if (last.getText().equalsIgnoreCase(text)) {
                return;
            }
        }

        if (text == null) { return; }
      
        if (text.isEmpty()) { return; }

        data.add(0,new Entry (System.currentTimeMillis(), text, search));
        fireIntervalAdded(0, 0);

        int first = -1;
        int last = data.size() - 1;
        while (data.size() > 1000) {
            first = data.size()-1;
            data.remove(data.size()-1);
        }

        if (first != -1) {
            fireIntervalRemoved(first, last);
        }

        modified = true;
    }

    public int size() {
        return data.size();
    }

    /**
     * Return Entry or null if index is invalid
     * @param index the index of Entry
     * @return Entry or null
     */
    public Entry getHistory(int index) {
        if (size() <= 0 || size() <= index) {
            return null;
        }
        
        return data.get(index);
    }

    public void addListDataListener(ListDataListener listener) {
        listenerList.add(ListDataListener.class, listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        listenerList.remove(ListDataListener.class, listener);
    }

    protected void fireIntervalAdded(int index0, int index1) {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                ((ListDataListener) listeners[i + 1]).intervalAdded(event);
            }
        }
    }

    protected void fireIntervalRemoved(int index0, int index1) {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                ((ListDataListener) listeners[i + 1]).intervalRemoved(event);
            }
        }
    }


    public class Entry {
        private long millis;
        private String text;
        private String search;

        public Entry(long millis, String text, String search) {
            this.millis = millis;
            this.text = text;
            this.search = search;
        }

        public long getMillis() {
            return millis;
        }

        public String getText() {
            return text;
        }

        public String getSearch() {
            return search;
        }
    }
}
