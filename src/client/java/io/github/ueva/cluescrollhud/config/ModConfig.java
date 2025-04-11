package io.github.ueva.cluescrollhud.config;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = VgClueScrollHUD.MOD_ID)
public class ModConfig implements ConfigData {

    // ─── Scale and Positioning ────────────────────────────────────────────────────────

    // Scaling factors.
    @ConfigEntry.Category("Display")
    public float globalScale = 1.0f;

    @ConfigEntry.Category("Display")
    public float largeTextScale = 1.1f;

    @ConfigEntry.Category("Display")
    public float smallTextScale = 0.75f;

    // Position of the top-left corner of the HUD.
    @ConfigEntry.Category("Display")
    public float x = 0.0f;

    @ConfigEntry.Category("Display")
    public float y = 0.0f;

    // ─── Display Behaviour ────────────────────────────────────────────────────────

    // Hide the HUD when no clues in inventory.
    @ConfigEntry.Category("Behaviour")
    public boolean hideWhenNoClue = false;

    // Hide completed clues.
    @ConfigEntry.Category("Behaviour")
    public boolean hideCompleted = false;

    // Update interval (in milliseconds).
    @ConfigEntry.Category("Behaviour")
    public int updateInterval = 100;

    // Whether to colour based on clue progress, and fallback colour.
    @ConfigEntry.Category("Behaviour")
    public boolean colourByProgress = true;

}
