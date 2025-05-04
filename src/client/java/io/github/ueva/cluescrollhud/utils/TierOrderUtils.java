package io.github.ueva.cluescrollhud.utils;

import java.util.Map;


public class TierOrderUtils {

    private static final Map<String, Integer> TIER_INDICES =
            Map.of("EASY", 0, "NORMAL", 1, "HARD", 2, "WEEKLY", 3, "EXTENDED", 4);

    private static final int DEFAULT_INDEX = 999;

    public static int getIndex(String tier) {
        return TIER_INDICES.getOrDefault(tier.toUpperCase(), DEFAULT_INDEX);
    }
}