package org.example.lab4back.utils;

public class AreaChecker {

    public static boolean isInArea(double x, double y, double r) {
        // Check for the rectangle in the lower-left quadrant
        if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r;
        }
        // Check for the semicircle in the upper-right quadrant
        else if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= r * r;
        }
        // Check for the triangle in the lower-right quadrant
        else if (x >= 0 && y <= 0) {
            return 2 * y >= (x - r);
        }
        return false;
    }
}