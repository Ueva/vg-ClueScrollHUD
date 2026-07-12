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
    public float largeTextScale = 1.15f;

    @ConfigEntry.Category("Display")
    public float smallTextScale = 0.70f;

    @ConfigEntry.Category("Display")
    public boolean rightAlign = false;

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
    public int updateInterval = 250;

    // Whether to colour based on clue progress.
    @ConfigEntry.Category("Behaviour")
    public boolean colourByProgress = true;

    // How scroll information is displayed on the HUD.
    @ConfigEntry.Category("Behaviour")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ElementDisplayMode displayMode = ElementDisplayMode.SINGLE_SCROLL;

    // Which scroll to show first when cycling between scrolls in single-scroll mode.
    @ConfigEntry.Category("Behaviour")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ScrollSortMode scrollSortMode = ScrollSortMode.INV_POSITION;

    // Reverse the scroll cycle order in single-scroll mode.
    @ConfigEntry.Category("Behaviour")
    public boolean reverseScrollSort = false;

    // How tasks are ordered within a scroll, or across the collated task list.
    @ConfigEntry.Category("Behaviour")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public TaskSortMode taskSortMode = TaskSortMode.DEFAULT;

    // Reverse task sort order.
    @ConfigEntry.Category("Behaviour")
    public boolean reverseTaskSort = false;

}
