package com.josh.weighttracker.helpers;

/**
 * Helper class to see if string is parsable to int or double
 */
public class ParseNumbers {

    /**
     * Determine if input string is parsable to an int
     */
    public static boolean isParsableInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }


    /**
     * Determine if input string is parsable to a double
     */
    public static boolean isParsableDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
