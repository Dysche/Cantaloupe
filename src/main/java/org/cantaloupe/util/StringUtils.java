package org.cantaloupe.util;

public class StringUtils {
    public static String getSpacesForCentering(int maxLength, String string) {
        int length = string.length();

        if (length % 2 == 0) {
            length += 1;
        }

        int diff = maxLength - length;
        int amount = diff / 2;
        String toReturn = "";

        for (int i = 0; i < amount; i++) {
            toReturn += " ";
        }

        return toReturn;
    }
}