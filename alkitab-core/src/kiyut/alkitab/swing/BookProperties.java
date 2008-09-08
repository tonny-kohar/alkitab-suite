/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit;
import kiyut.alkitab.api.SwingHTMLConverter;
import kiyut.alkitab.api.ViewerHints;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.JDOMSAXEventProvider;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.common.xml.XMLUtil;
import org.crosswire.jsword.book.Book;

/**
 * Book Properties (Metadata) component
 * 
 */
public class BookProperties extends javax.swing.JPanel {

    /** Creates new form BookProperties */
    public BookProperties() {
        initComponents();
        initCustomComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(textPane);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane textPane;
    // End of variables declaration//GEN-END:variables

    protected void initCustomComponents() {
        textPane.setEditable(false);
        textPane.setEditorKit(new HTMLEditorKit());

        Dimension dim = new Dimension(600,400);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();
        
        // for less than 800x600
        if (screenSize.width <= 800) {
            dim.setSize(500, 340);
        }
        
        this.setPreferredSize(dim);
    }
    
    public void setBook(Book book) {
        setBook(book, new ViewerHints());
    }
    
    public void setBook(Book book, ViewerHints viewerHints) {
        if (book == null) {
            textPane.setText(""); 
            return;
        }   
        
        try {
            SAXEventProvider osissep = new JDOMSAXEventProvider(book.toOSIS());
            Converter converter = new SwingHTMLConverter();
            TransformingSAXEventProvider htmlsep = (TransformingSAXEventProvider)converter.convert(osissep);
            if (viewerHints != null) {
                viewerHints.updateProvider(htmlsep);
            }
            String text = XMLUtil.writeToString(htmlsep);

            textPane.setText(text);
            textPane.setCaretPosition(0);
            
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, "",ex);
        }
    }
}