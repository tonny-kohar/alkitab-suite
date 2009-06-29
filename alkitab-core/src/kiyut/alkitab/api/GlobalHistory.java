/* This work has been placed into the public domain. */

package kiyut.alkitab.api;

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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Global History Manager and persist the data between session.
 * It is not complete and stable yet (still experimental), so it might change.
 * Currently it is only for verse or passage
 *
 * 
 */
public class GlobalHistory {
    private static String MILLIS = "millis";
    private static String FILENAME = "history.xml";

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
        String parent = System.getProperty("netbeans.user");
        File file = new File(parent + File.separator + "alkitab-history" + File.separator + FILENAME);

        if (!file.exists()) { return; }

        
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            List<Element> children = doc.getRootElement().getChildren();
            for (int i = 0; i < children.size(); i++) {
                Element elt = children.get(i);
                data.add(new Entry(Long.parseLong(elt.getAttributeValue(MILLIS)), elt.getTextTrim()));
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
        String parent = System.getProperty("netbeans.user");
        File file = new File(parent, "alkitab-history");
        if (!file.exists()) { 
            if (!file.mkdir()) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING, "unable to create folder to save history." );
                return; 
            }
        }

        file = new File(file, FILENAME);

        Element root = new Element("history");
        Document doc = new Document(root);
        
        for (int i=0; i<data.size(); i++) {
            Entry entry = data.get(i);
            Element elt = new Element("entry");
            elt.setAttribute(MILLIS, Long.toString(entry.getMillis()));
            elt.setText(entry.getHistory());
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

    public void add(String history) {
        
        // check last entry
        if (!data.isEmpty()) {
            Entry last = data.get(0);
            if (last.getHistory().equalsIgnoreCase(history)) {
                return;
            }
        }

        if (history == null) { return; }

        // java 6 only
        //if (history.isEmpty()) { return; }
        if (history.length() == 0) { return; }

        data.add(0,new Entry (System.currentTimeMillis(), history));
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

    public Entry getHistory(int index) {
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
        private String history;

        public Entry(long millis, String history) {
            this.millis = millis;
            this.history = history;
        }

        public long getMillis() {
            return millis;
        }

        public String getHistory() {
            return history;
        }
    }
}
