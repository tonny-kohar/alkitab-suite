/* This work has been placed into the public domain. */

package kiyut.swing.combo;

import java.awt.Component;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;


/** Extended ComboBox which have separator on its item.
 * The default separator is defined as String "--"
 * 
 * @author KIYUT
 */
public class SeparatorComboBox extends JComboBox<String> {
    public static final String DEFAULT_SEPARATOR = "--";

    protected String separator;
    
    public SeparatorComboBox() {
        this(DEFAULT_SEPARATOR);
        initCustom();
    }
    
    public SeparatorComboBox(ComboBoxModel<String> aModel) {
        super(aModel);
        initCustom();
        setSeparator(DEFAULT_SEPARATOR);
    }
        
    public SeparatorComboBox(final String[] items) {
        super(items);
        initCustom();
        setSeparator(DEFAULT_SEPARATOR);
    }

    public SeparatorComboBox(String separator) {
        super();
        initCustom();
        setSeparator(separator);
    }
    
    protected void initCustom() {
        ListCellRenderer<? super String> defaultRenderer = getRenderer();
        ListCellRenderer<String> wrappedRendered = new SeparatorListCellRenderer<>(defaultRenderer);
        setRenderer(wrappedRendered);
        //setRenderer(new SeparatorListCellRenderer(getRenderer()));
    }
    
    public void setSeparator(String separator) {
        if (separator == null) {
            this.separator = DEFAULT_SEPARATOR;
        } else {
            this.separator = separator;
        }
        
    }
    
    public String getSeparator() {
        return this.separator;
    }
    
    @Override
    public void setSelectedIndex(int index) {
        if (index < getItemCount() && index >=0) {
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
     * @param <String> the type of values this renderer can be used for
     */
    public class SeparatorListCellRenderer<String> implements ListCellRenderer<String> {
        protected ListCellRenderer<? super String> wrapped;
        protected Component separatorComp = new JSeparator();
        
        /** 
         * Constructs a renderer object for an item in a list.
         * @param listCellRenderer The original ListCellRenderer
         */
        public SeparatorListCellRenderer(ListCellRenderer<? super String> listCellRenderer) {
            this.wrapped = listCellRenderer;
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                if (value.equals(separator)) {
                    return separatorComp;
                }
            }
            
            return wrapped.getListCellRendererComponent( list, value, index,isSelected, cellHasFocus );
        }
    }
}
