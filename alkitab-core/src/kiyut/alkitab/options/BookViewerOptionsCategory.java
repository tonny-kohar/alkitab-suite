/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * 
 */
public class BookViewerOptionsCategory extends OptionsCategory {
    private String categoryName;
    private Icon icon;
    
    /** Creates a new instance of GeneralOptionsCategory */
    public BookViewerOptionsCategory() {
        categoryName = NbBundle.getMessage(BookViewerOptionsCategory.class, "CTL_BookViewerOptionsCategory");

        String iconPath = NbBundle.getMessage(BookViewerOptionsCategory.class, "ICON_BookViewerOptionsCategory");
        try {
            icon = new ImageIcon(Utilities.loadImage(iconPath, true));
        } catch (Exception ex) {
            // do nothing
        }
    }

    
    public String getCategoryName() {
        return categoryName;
    }

    
    public String getTitle() {
        return getCategoryName();
    }

    
    @Override
    public Icon getIcon() {
        return icon;
    }

    
    public OptionsPanelController create() {
        BookViewerOptionsPanelController controller = new BookViewerOptionsPanelController();
        return controller;
    }

    class BookViewerOptionsPanelController extends OptionsPanelController {

        private boolean changed;
        private BookViewerOptionsPane optionsPane;

        public BookViewerOptionsPanelController() {
            optionsPane = new BookViewerOptionsPane();
        }

        
        public void addPropertyChangeListener(PropertyChangeListener l) {
        }

        
        public void removePropertyChangeListener(PropertyChangeListener l) {
        }

        
        public HelpCtx getHelpCtx() {
            return null;
        }

        
        public void update() {
            optionsPane.refreshOptions();
            changed = false;
        }

        
        public void applyChanges() {
            optionsPane.applyChanges();
            changed = false;
        }

        
        public void cancel() {
            // do nothing
        }

        
        public boolean isValid() {
            return true;
        }

        
        public boolean isChanged() {
            return changed;
        }

        
        public JComponent getComponent(Lookup masterLookup) {
            /*JPanel pane = new JPanel();
            pane.setLayout(new BorderLayout());
            pane.add(optionsPane,BorderLayout.CENTER);
            pane.setBorder(new EmptyBorder(12, 12, 12, 12));
            return pane; */
            return optionsPane;
        }
    }
}
