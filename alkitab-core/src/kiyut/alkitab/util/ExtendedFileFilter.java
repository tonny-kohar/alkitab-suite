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
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class ExtendedFileFilter extends FileFilter {

    protected List<String> filters = null;
    protected String description = null;
    protected String fullDescription = null;
    protected boolean useExtensionsInDescription = true;
    
    public ExtendedFileFilter() {
        this(null,null);
    }
    
    public ExtendedFileFilter(String description, String[] extensions) {
        this.filters = new ArrayList<>(2);
        
        if (extensions != null) {
            for (String extension : extensions) {
                addExtension(extension);
            }
        }
        
        if (description != null) {
            setDescription(description);
        }
    }
    
    @Override
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
     * @param f the File
     * @return the extension portion of the file's name.
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
     *
     * Note that the "." before the extension is not needed and will be ignored.
     * @param extension the file extension
     */
    public void addExtension(String extension) {
        if(filters == null) {
            filters = new ArrayList<>(2);
        }
        filters.add(extension.toLowerCase());
        fullDescription = null;
    }
    
    
    /**
     * {@inheritDoc}
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @return description
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     */
    @Override
    public String getDescription() {
        if(fullDescription == null) {
            if(description == null || isExtensionListInDescription()) {
                fullDescription = description==null ? "(" : description + " (";
                Iterator<String> it = filters.iterator();
                if(it != null) {
                    fullDescription += "." + it.next();
                    while (it.hasNext()) {
                        fullDescription += ", ." + it.next();
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
     * @param description description
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
     * Only relevant if a description was provided in the constructor
     * or using setDescription();
     *
     * @param b true or false
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
     * Only relevant if a description was provided in the constructor
     * or using setDescription();
     *
     * @return true or false
     * @see #getDescription
     * @see #setDescription
     * @see #setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }
    
}
