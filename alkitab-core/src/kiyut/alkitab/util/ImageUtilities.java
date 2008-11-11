/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Collection of Image Utilities
 */
public class ImageUtilities {

    private ImageUtilities() {
        throw new Error("ImageUtilities is a utility class for static methods"); // NOI18N
    }

    /** Create new composite Image (Type INT_ARGB) from the specified images
     * Image1 will be drawn first.
     * @param image1 Image
     * @param imaga2 Image
     * @return new composite Image
     */
    public static Image createCompositeImage(Image image1, Image image2) {
        if (image1 == null || image2 == null) {
            throw new IllegalArgumentException("image1 or image2 should not be null");
        }
        int w = Math.max(image1.getWidth(null), image2.getWidth(null));
        int h = Math.max(image1.getHeight(null), image2.getHeight(null));
        
        BufferedImage dest = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dest.createGraphics();
        g2d.drawImage(image1, 0, 0, null);
        g2d.drawImage(image2, 0, 0, null);
        g2d.dispose();
        g2d = null;

        return dest;
    }
}
