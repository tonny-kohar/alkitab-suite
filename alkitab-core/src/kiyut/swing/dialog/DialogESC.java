/* This work has been placed into the public domain. */

package kiyut.swing.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * {@code JDialog} which is closeable by pressing ESC key
 *
 */
public class DialogESC extends JDialog {
    
    /** Flag for to allow ESC key to close, Default is True */
    protected boolean allowESC = true;
    
    public DialogESC() {
        super();
    }
    
    public DialogESC(Dialog owner) {
        super(owner);
    }
    
    public DialogESC(Dialog owner, boolean modal) {
        super(owner,modal);
    }
    
    public DialogESC(Dialog owner, String title) {
        super(owner,title);
    }
    
    public DialogESC(Dialog owner, String title, boolean modal) {
        super(owner,title,modal);
    }
    
    public DialogESC(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner,title,modal,gc);
    }

    public DialogESC(Frame owner) {
        super(owner);
    }
    
    public DialogESC(Frame owner, boolean modal) {
        super(owner, modal);
    }
    
    public DialogESC(Frame owner, String title) {
        super(owner,title);
    }
    
    public DialogESC(Frame owner, String title, boolean modal) {
        super(owner,title,modal);
    }
    
    public DialogESC(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner,title,modal,gc);
    }

    @Override
    protected JRootPane createRootPane() {
        JRootPane theRootPane = super.createRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (!allowESC) {
                    return;
                }
                setVisible(false);
            }
        };
        InputMap inputMap = theRootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, "ESCAPE");
        theRootPane.getActionMap().put("ESCAPE", actionListener);

        return theRootPane;
    }
    
    /** Setting allow Escape key to close, default is true */
    public void setAllowESC(boolean allow) {
        allowESC = allow;
    }
    
    public boolean isAllowESC() {
        return allowESC;
    }
}
