/* This work has been placed into the public domain. */

package kiyut.swing.combo;

import java.awt.Component;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;


/** 
 * Extended ComboBox which have separator on its item.
 * The default separator is defined as String "--"
 *
 */
public class SeparatorComboBox extends JComboBox {
    public static final String DEFAULT_SEPARATOR = "--";

    protected Object separator;
    
    public SeparatorComboBox() {
        this(DEFAULT_SEPARATOR);
        initCustom();
    }
    
    public SeparatorComboBox(ComboBoxModel aModel) {
        super(aModel);
        initCustom();
        setSeparator(DEFAULT_SEPARATOR);
    }
        
    public SeparatorComboBox(final Object[] items) {
        super(items);
        initCustom();
        setSeparator(DEFAULT_SEPARATOR);
    }

    public SeparatorComboBox(Object separator) {
        super();
        initCustom();
        setSeparator(separator);
    }
    
    protected void initCustom() {
        setRenderer(new SeparatorListCellRenderer(getRenderer()));
    }
    
    public void setSeparator(Object separator) {
        if (separator == null) {
            this.separator = DEFAULT_SEPARATOR;
        } else {
            this.separator = separator;
        }
    }
    
    public Object getSeparator() {
        return this.separator;
    }
    
    @Override
    public void setSelectedIndex(int index) {
        if (index < getItemCount()) {
            Object obj = getItemAt(index);
            if (obj.equals(separator)) {
                int oldIndex = getSelectedIndex();
                if ( index < oldIndex ) {
                    // Select the item before the separator.
                    if ( index - 1 >= 0 ) {
                        index = index - 1;
                    }
                } else if ( index > oldIndex ) {
                    // Select the item after the separator.
                    if ( index + 1 < getItemCount() ) {
                        index = index + 1;
                    }
                }
            }
        }
        super.setSelectedIndex(index);
    }
    
    /** 
     * ListCellRenderer for {@link kiyut.swing.combo.SeparatorComboBox SeparatorComboBox}. 
     * <strong>Implementation Note:</strong> This class is simply a wrapper for original ListCellRenderer and
     * using return JSeparator if the value is the separator.
     */
    public class SeparatorListCellRenderer implements ListCellRenderer {
        protected ListCellRenderer wrapped;
        protected Component separatorComp = new JSeparator();
        
        /** 
         * Constructs a renderer object for an item in a list.
         * @param listCellRenderer The original ListCellRenderer
         */
        public SeparatorListCellRenderer(ListCellRenderer listCellRenderer) {
            this.wrapped = listCellRenderer;
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                if (value.equals(separator)) {
                    return separatorComp;
                }
            }
            return wrapped.getListCellRendererComponent( list, value, index,isSelected, cellHasFocus );
        }
    }
}
