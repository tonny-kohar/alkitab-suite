/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.filechooser.FileFilter;

/** 
 * A convenience class to create {@code FileFilter}
 * 
 * How to use:
 * <pre>
 * ExtendedFileFilter filter = new ExtendedFileFilter();
 * filter.addExtension("gif");
 * filter.addExtension("jpg");
 * filter.addExtension("png");
 * filter.setDescription("Image Files");
 * </pre>
 * 
 */
public class ExtendedFileFilter extends FileFilter {
    protected static final String TYPE_UNKNOWN = "Type Unknown";
    protected static final String HIDDEN_FILE = "Hidden File";
    
    protected List<String> filters = null;
    protected String description = null;
    protected String fullDescription = null;
    protected boolean useExtensionsInDescription = true;
    
    public ExtendedFileFilter() {
        this.filters = new ArrayList<String>(2);
    }
    
    public boolean accept(File f) {
        if(f != null) {
            if(f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if(extension != null && filters.contains(getExtension(f))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return the extension portion of the file's name.
     *
     * @see #getExtension
     * @see #accept
     */
    public String getExtension(File f) {
        if(f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if(i>0 && i<filename.length()-1) {
                return filename.substring(i+1).toLowerCase();
            }
        }
        return null;
    }
    
    /**
     * Adds a filetype "dot" extension to filter against.
     *
     * For example: the following code will create a filter that filters
     * out all files except those that end in ".jpg" and ".tif":
     * <pre>
     *   ExampleFileFilter filter = new ExampleFileFilter();
     *   filter.addExtension("jpg");
     *   filter.addExtension("tif");
     *</pre>
     *
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public void addExtension(String extension) {
        if(filters == null) {
            filters = new ArrayList<String>(2);
        }
        filters.add(extension.toLowerCase());
        fullDescription = null;
    }
    
    
    /**
     *  {@inheritDoc}
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @return description
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     */
    public String getDescription() {
        if(fullDescription == null) {
            if(description == null || isExtensionListInDescription()) {
                fullDescription = description==null ? "(" : description + " (";
                // build the description from the extension list
                /*Enumeration extensions = filters.keysS();
                if(extensions != null) {
                    fullDescription += "." + (String) extensions.nextElement();
                    while (extensions.hasMoreElements()) {
                        fullDescription += ", ." + (String) extensions.nextElement();
                    }
                }*/
                Iterator it = filters.iterator();
                if(it != null) {
                    fullDescription += "." + (String) it.next();
                    while (it.hasNext()) {
                        fullDescription += ", ." + (String) it.next();
                    }
                }
                fullDescription += ")";
            } else {
                fullDescription = description;
            }
        }
        return fullDescription;
    }
    
    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription("Gif and JPG Images");
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     */
    public void setDescription(String description) {
        this.description = description;
        fullDescription = null;
    }
    
    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see #getDescription
     * @see #setDescription
     * @see #isExtensionListInDescription
     */
    public void setExtensionListInDescription(boolean b) {
        useExtensionsInDescription = b;
        fullDescription = null;
    }
    
    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see #getDescription
     * @see #setDescription
     * @see #setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }
    
}
