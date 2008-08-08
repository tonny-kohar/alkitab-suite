/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;


/** Collection of I/O Utilities
 *
 */
public class IOUtilities {
    //private  static JFileChooser fc;
    
    private IOUtilities() {
        throw new Error("IOUtilities is a utility class for static methods"); // NOI18N
    }
    
    /** Return JFileChooser that remember the last opened directory based on
     * {@code System.getProperty("user.dir")} value
     *@return {@code JFileChooser}
     */
    public static JFileChooser getFileChooser() {
        //String def = System.getProperty("user.home");
        String def =  FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        String str = System.getProperty("user.dir", def);
        //System.out.println(IOUtilities str);
        File dir = new File(str);
        JFileChooser fc = new JFileChooser(dir);
        return fc;
    }
    
    /** Convenience methods to set user's current working directory 
     * aka {@code System.getProperty("user.dir")}.
     * If the param is not a directory it will look for the contained directory, 
     * if null set as {@code FileSystemView.getFileSystemView().getDefaultDirectory()},
     * otherwise just set as it is. 
     * @param file {@code File}
     */
    public static void setUserDir(File file) {
        if (file == null) {
            String def =  FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            System.setProperty("user.dir", def);
            return;
        }
        
        try {
            if (!file.isDirectory()) {
                file = file.getParentFile();
            }
        
            if (file != null) {
                System.setProperty("user.dir", file.getAbsolutePath());
            }
        } catch (Exception ex) {
            // do nothing
        }
    }
    
    /** Return a new instance of FileFilter preconfigured for Zip file
     * @return FileFilter
     */
    public static FileFilter getZipFileFilter() {
        ExtendedFileFilter filter = new ExtendedFileFilter();
        
        filter.addExtension("zip");
        filter.setDescription("ZIP Files");
        
        return filter;
    }
}

