/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import kiyut.alkitab.history.GlobalHistoryPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * TopComponent which displays {@link kiyut.alkitab.history.GlobalHistoryPane GlobalHistoryPane}.
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
@TopComponent.Description(preferredID = "GlobalHistoryTopComponent",
    //iconBase="SET/PATH/TO/ICON/HERE", 
    persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = false, position=120)
@ActionID(category = "Window", id = "kiyut.alkitab.actions.GlobalHistoryAction")
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 150),
    @ActionReference(path = "Shortcuts", name = "DS-H")
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_GlobalHistoryAction",
    preferredID = "GlobalHistoryTopComponent")
public final class GlobalHistoryTopComponent extends TopComponent {

    private transient GlobalHistoryPane historyPane;

    public GlobalHistoryTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(GlobalHistoryTopComponent.class, "CTL_GlobalHistoryTopComponent"));
        setToolTipText(NbBundle.getMessage(GlobalHistoryTopComponent.class, "HINT_GlobalHistoryTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        initCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Obtain the GlobalHistoryTopComponent instance.
     * @return GlobalHistoryTopComponent instance.
     */
    public static synchronized GlobalHistoryTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent("GlobalHistoryTopComponent");
        if (win instanceof GlobalHistoryTopComponent) {
            return (GlobalHistoryTopComponent) win;
        }
        
        return null;
    }
    
    private void initCustom() {
        historyPane = new GlobalHistoryPane();
        this.add(historyPane, BorderLayout.CENTER);
    }
    
    public void deleteSelected() {
        historyPane.deleteSelected();
    }
    
    public void clearAll() {
        historyPane.clearAll();
    }
}
