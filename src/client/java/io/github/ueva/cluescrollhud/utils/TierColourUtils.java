package io.github.ueva.cluescrollhud.utils;


import java.util.Map;


public class TierColourUtils {

    private static final Map<String, Integer> TIER_COLOURS = Map.of(
            "EASY",
            0x55FF55,
            "NORMAL",
            0xFF55FF,
            "HARD",
            0xFFAA00,
            "WEEKLY",
            0x5555FF,
            "FORTNIGHTLY",
            0x5555FF,
            "EXTENDED",
            0xFF5555
    );

    private static final int DEFAULT_COLOR = 0xFFFFFF;

    public static int getColour(String tier) {
        return TIER_COLOURS.getOrDefault(tier.toUpperCase(), DEFAULT_COLOR);
    }
}