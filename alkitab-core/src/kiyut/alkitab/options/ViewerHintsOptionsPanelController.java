/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(
    location="BookViewer",
    displayName="kiyut.alkitab.options.ViewerHintsOptionsPanel#DisplayName",
    keywords="kiyut.alkitab.options.ViewerHintsOptionsPanel#Keywords",
    keywordsCategory="BookViewer/ViewerHints"
)
public final class ViewerHintsOptionsPanelController extends OptionsPanelController {

    private ViewerHintsOptionsPanel panel;

    @Override
    public void update() {
        getOptionsPanel().update();
    }

    @Override
    public void applyChanges() {
        getOptionsPanel().applyChanges();
    }

    @Override
    public void cancel() {
        getOptionsPanel().cancel();
    }

    @Override
    public boolean isValid() {
        return getOptionsPanel().isOptionsValid();
    }

    @Override
    public boolean isChanged() {
        return getOptionsPanel().isChanged();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }
    
    private ViewerHintsOptionsPanel getOptionsPanel() {
        if (panel == null) {
            panel = new ViewerHintsOptionsPanel();
        }
        return panel;
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getOptionsPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        getOptionsPanel().addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        getOptionsPanel().removePropertyChangeListener(l);
    }    
}
