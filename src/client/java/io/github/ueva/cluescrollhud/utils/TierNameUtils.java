package io.github.ueva.cluescrollhud.utils;

public class TierNameUtils {

    public static String sanitiseTierName(String tier) {
        // If the string is "weekly2", return "Fortnightly".
        if (tier.equalsIgnoreCase("weekly2")) {
            return "fortnightly";
        }

        // Otherwise, return the string as-is.
        return tier;
    }
}
