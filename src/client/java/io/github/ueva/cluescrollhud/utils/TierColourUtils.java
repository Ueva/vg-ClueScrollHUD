package io.github.ueva.cluescrollhud.utils;


import java.util.Map;


public class TierColourUtils {

    private static final Map<String, Integer> TIER_COLOURS =
            Map.of("EASY", 0x55FF55, "NORMAL", 0xAA00AA, "HARD", 0x5555FF, "WEEKLY", 0xAA0000, "EXTENDED", 0xFFAA00);

    private static final int DEFAULT_COLOR = 0xFFFFFF;

    public static int getColour(String tier) {
        return TIER_COLOURS.getOrDefault(tier.toUpperCase(), DEFAULT_COLOR);
    }
}