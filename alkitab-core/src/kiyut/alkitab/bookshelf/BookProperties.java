/* This work has been placed into the public domain. */

package kiyut.alkitab.bookshelf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;
import kiyut.alkitab.bookviewer.HTMLConverter;
import kiyut.alkitab.bookviewer.ViewerHints;
import org.crosswire.common.xml.Converter;
import org.crosswire.common.xml.JDOMSAXEventProvider;
import org.crosswire.common.xml.SAXEventProvider;
import org.crosswire.common.xml.TransformingSAXEventProvider;
import org.crosswire.common.xml.XMLUtil;
import org.crosswire.jsword.book.Book;

/**
 * Book Properties (Metadata) component
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com> 
 */
public class BookProperties extends javax.swing.JPanel {

    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());    
    
    /** Creates new form BookProperties */
    public BookProperties() {
        initComponents();
        initCustom();
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

    protected void initCustom() {
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
        
        setName(bundle.getString("CTL_Name.Text"));

        Color color = UIManager.getColor("TextPane.background");
        if (color != null) {
            if (!color.equals(getBackground())) {
                textPane.setBackground(color);
            }
        }

        //XXX workaround for Linux GTK lnf JEditorPane.setEditable(false) background color
        /*try {
            if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                Color color = UIManager.getColor("TextPane.background");
                if (color != null) {
                    if (!color.equals(getBackground())) {
                        textPane.setBackground(color);
                    }
                }
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.CONFIG,ex.getMessage(),ex);
        }*/
        
    }
    
    public void setBook(Book book) {
        setBook(book, new ViewerHints<>());
    }
    
    public void setBook(Book book, ViewerHints<ViewerHints.Key,Object> viewerHints) {
        if (book == null) {
            textPane.setText(""); 
            return;
        }   
        
        // TODO jsword2
        try {
            SAXEventProvider osissep = new JDOMSAXEventProvider(book.toOSIS());
            //Converter converter = new SwingHTMLConverter();
            Converter converter = new HTMLConverter();
            TransformingSAXEventProvider htmlsep = (TransformingSAXEventProvider)converter.convert(osissep);
            if (viewerHints != null) {
                viewerHints.updateProvider(htmlsep);
            }
            String text = XMLUtil.writeToString(htmlsep);

            textPane.setText(text);
            textPane.setCaretPosition(0);
            
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
