/* This work has been placed into the public domain. */

package kiyut.alkitab.options;

import java.awt.Color;

/**
 * Collections of Options Utilities
 * 
 */
public class OptionsUtilities {

    private OptionsUtilities() {
        throw new Error("OptionsUtilities is a utility class for static methods");
    }

    /**Return Color from the supplied value string in hex format eg:#53F27C
     * @param value {@code String} that describe the Color
     * @return Color or null
     * @see #colorToString(Color)
     */
    public static Color stringToColor(String value) {
        return hexStringToColor(value);
    }

    /**Return Color from the supplied value string in hex format eg:#53F27C
     * @param value {@code String} that describe the Color
     * @param def the value to be returned in the event that this parsing fail for whatever reason
     * @return Color
     * @see #stringToColor(String)
     * @see #colorToString(Color)
     */
    public static Color stringToColor(String value, Color def) {
        Color color = hexStringToColor(value);
        if (color != null) {
            return color;
        }

        return def;
    }

    /** Return string representative of the color in hex format eg:#53F27C
     * @param color {@code Color} to be converted
     * @return string representative of the color
     * @see #stringToColor(String)
     */
    public static String colorToString(Color color) {
        return toHexString(color);
    }

    /** Return the hex string for the supplied{@code Color}
     * @param color AWT{@code Color}
     * @return the hex string for the supplied{@code Color}
     */
    private static String toHexString(Color color) {
        String str = null;

        try {
            str = "#";
            String hex = Integer.toHexString(color.getRed());
            if (hex.length() == 1) { hex = 0 + hex ; }
            str = str + hex;
            hex = Integer.toHexString(color.getGreen());
            if (hex.length() == 1) { hex = 0 + hex; }
            str = str + hex;
            hex = Integer.toHexString(color.getBlue());
            if (hex.length() == 1) { hex = 0 + hex; }
            str = str + hex;

        } catch (Exception e) {
            str = null;
        }
        return str;
    }

    /** Return the equivalent AWT{@code Color} of the supplied hexString in format eg #FF33D2
     * @param hexString Color in hexString format eg: #FF33D2
     * @return the AWT{@code Color}
     */
    private static Color hexStringToColor(String hexString) {
        Color color = null;
        int r = 0;
        int g = 0;
        int b = 0;

        try {
            String tmpStr = hexString.substring(1,3);
            r = Integer.parseInt(tmpStr,16);
            tmpStr = hexString.substring(3,5);
            g = Integer.parseInt(tmpStr,16);
            tmpStr = hexString.substring(5,7);
            b = Integer.parseInt(tmpStr,16);
            color = new Color(r,g,b);
        } catch (Exception e) {
            color = null;
        }

        return color;
    }
}
