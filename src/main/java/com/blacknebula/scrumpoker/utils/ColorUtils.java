package com.blacknebula.scrumpoker.utils;

import java.util.Random;

public class ColorUtils {

    /**
     * @should return a color code
     * @should return different color code when called twice
     * @return random hex color code
     */
    public static String getRandomColor() {
        final Random ra = new Random();
        int r, g, b;
        r = ra.nextInt(255);
        g = ra.nextInt(255);
        b = ra.nextInt(255);
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
