/* This work has been placed into the public domain. */

package kiyut.alkitab.util;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.Locale;

/**
 * Component Orientation Support
 * 
 */
public class ComponentOrientationSupport {

    private ComponentOrientationSupport() {
        throw new Error("ComponentOrientationSupport is a utility class for static methods"); // NOI18N
    }

    /** 
     * Return component orientation base on the value of
     * {@code System.getProperty("alkitab.orientation")} <br/>
     * If it is not specified it will return ComponentOrientation.LEFT_TO_RIGHT
     * Possible value are:
     * <code>
     * - auto (Default), automatic setting based on Locale.getDefault()
     * - ltr, force to use Left to Right orientation
     * - rtl, force to use Right to Left orientation
     * </code>
     * @return ComponentOrientation
     */
    public static ComponentOrientation getComponentOrientation() {
        ComponentOrientation orient = ComponentOrientation.getOrientation(Locale.getDefault());

        String str = System.getProperty("alkitab.orientation");

        if (str != null) {
            if (str.equals("ltr")) {
                orient = ComponentOrientation.LEFT_TO_RIGHT;
            } else if (str.equals("rtl")) {
                orient = ComponentOrientation.RIGHT_TO_LEFT;
            }
        }

        return orient;
    }

    /** Apply the getComponentOrientation to the specified container
     * @param container Container
     */
    public static void applyComponentOrientation(Container container) {
        container.applyComponentOrientation(getComponentOrientation());
    }
}
