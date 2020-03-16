package io.alerium.bonusblocks.utils;

public final class NumberUtil {

    private NumberUtil() {
    }

    /**
     * This method checks if a String contains an Integer
     * @param s The String
     * @return The Integer, null if not
     */
    public static Integer parseInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}
