package io.github.ueva.cluescrollhud.utils;


import java.util.Map;


public class TierColourUtils {

    private static final Map<String, Integer> TIER_COLOURS = Map.of(
            "EASY",
            0xFF55FF55,
            "NORMAL",
            0xFFFF55FF,
            "HARD",
            0xFFFFAA00,
            "WEEKLY",
            0xFF5555FF,
            "FORTNIGHTLY",
            0xFF5555FF,
            "EXTENDED",
            0xFFFF5555
    );

    private static final int DEFAULT_COLOR = 0xFFFFFFFF;

    public static int getColour(String tier) {
        return TIER_COLOURS.getOrDefault(tier.toUpperCase(), DEFAULT_COLOR);
    }
}