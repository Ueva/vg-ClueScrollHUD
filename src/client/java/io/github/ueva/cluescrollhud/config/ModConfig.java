package io.github.ueva.cluescrollhud.config;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;


@Config(name = VgClueScrollHUD.MOD_ID)
public class ModConfig implements ConfigData {

    // ─── Scale and Positioning ────────────────────────────────────────────────────────

    // Scaling factors.
    public float globalScale = 1.0f;
    public float largeTextScale = 1.1f;
    public float smallTextScale = 0.75f;

    // Position of the top-left corner of the HUD.
    public float x = 0.0f;
    public float y = 0.0f;

    // ─── Display Behaviour ────────────────────────────────────────────────────────

    // Hide the HUD when no clues in inventory.
    public boolean hideWhenNoClue = false;

    // Hide completed clues.
    public boolean hideCompleted = false;

    // Update interval (in milliseconds).
    public int updateInterval = 100;

    // ─── Colours ────────────────────────────────────────────────────────

    // Whether to colour based on clue progress, and fallback colour.
    public boolean colourByProgress = true;
    public int fallbackClueColour = 0xFFAA00;

}
