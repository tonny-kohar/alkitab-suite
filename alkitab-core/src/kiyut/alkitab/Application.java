/* This work has been placed into the public domain. */

package kiyut.alkitab;

import java.awt.Image;
import java.util.ResourceBundle;
import org.openide.windows.WindowManager;

/**
 * Application Class. This class contains static information about the application
 * eg: name, version. etc
 */
public class Application {

    private String name;
    private String fullName;
    private String version;
    private String buildNumber;
    private boolean beta = false;
    private boolean debug = false;
    private Image iconImage;
    private ResourceBundle bundle;

    private static Application instance;
    static {
        instance = new Application();
    }

    private Application() {
        bundle = ResourceBundle.getBundle(this.getClass().getName());
        name = bundle.getString("title");
        fullName = bundle.getString("title.full");
        version = bundle.getString("version");
        buildNumber = bundle.getString("buildnumber");
        debug = Boolean.parseBoolean(bundle.getString("debug"));
        
        if (version.indexOf("beta") >= 0) {
            beta = true;
        }
        
        //bundle = null;
    }

    /** Application Global Message localization bundle
     * @param resName Resources Name
     * @return String
     */
    public static String getMessage(String resName) {
        return instance.bundle.getString(resName);
    }

    /** Return Application Name
     * @return name
     */
    public static String getName() {
        return instance.name;
    }

    /** Return Application Full Name
     * @return Full Name
     */
    public static String getFullName() {
        return instance.fullName;
    }
    
    /** Convenience method for getting application full name and version
     * @return full name with version appended
     */
    public static String getFullNameVersion() {
        return instance.fullName + " " + instance.version;
    }

    /** Return Application Version
     * @return version
     */
    public static String getVersion() {
        return instance.version;
    }

    /** Return Build Number in the format YYYYMMDDHHMM
     * @return buildNumber
     */
    public static String getBuildNumber() {
        return instance.buildNumber;
    }

    /** Return beta version or not
     * @return true if beta, otherwise false
     */
    public static boolean isBeta() {
        return instance.beta;
    }
    
    /** Return it is debug session or not
     * @return true if debug, otherwise false
     */
    public static boolean isDebug() {
        return instance.debug;
    }

    /** Return icon image
     * @return image
     */
    public static Image getIconImage() {
        if (instance.iconImage == null) {
            //instance.iconImage = Utilities.loadImage(instance.iconResource);
            instance.iconImage = WindowManager.getDefault().getMainWindow().getIconImage();
        }
        return instance.iconImage;
    }
}
