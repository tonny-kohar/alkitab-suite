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
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class EscapeDialog extends JDialog {
    
    /** Flag for to allow ESC key to close, Default is True */
    protected boolean allowESC = true;
    
    public EscapeDialog() {
        super();
    }
    
    public EscapeDialog(Dialog owner) {
        super(owner);
    }
    
    public EscapeDialog(Dialog owner, boolean modal) {
        super(owner,modal);
    }
    
    public EscapeDialog(Dialog owner, String title) {
        super(owner,title);
    }
    
    public EscapeDialog(Dialog owner, String title, boolean modal) {
        super(owner,title,modal);
    }
    
    public EscapeDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner,title,modal,gc);
    }

    public EscapeDialog(Frame owner) {
        super(owner);
    }
    
    public EscapeDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }
    
    public EscapeDialog(Frame owner, String title) {
        super(owner,title);
    }
    
    public EscapeDialog(Frame owner, String title, boolean modal) {
        super(owner,title,modal);
    }
    
    public EscapeDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner,title,modal,gc);
    }

    @Override
    protected JRootPane createRootPane() {
        JRootPane theRootPane = super.createRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        Action actionListener = new AbstractAction() {
            @Override
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
