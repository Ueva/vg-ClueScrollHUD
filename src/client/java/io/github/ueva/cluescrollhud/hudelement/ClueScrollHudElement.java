package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import io.github.ueva.cluescrollhud.config.ElementDisplayMode;
import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClueScrollHudElement {

    public static final Identifier CLUESCROLL_HUD_LAYER =
            Identifier.fromNamespaceAndPath(VgClueScrollHUD.MOD_ID, "cluescroll_hud_layer");

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private static final ClueScrollManager scrollManager = new ClueScrollManager(config);
    private static final ClueScrollRenderer scrollRenderer = new ClueScrollRenderer(config);

    private static boolean isVisible = true;
    private static long nextUpdateTime = 0;

    public static void render(GuiGraphics context, DeltaTracker tickCounter) {

        // Obtain the client instance.
        Minecraft client = Minecraft.getInstance();

        // Check whether the F3 debug screen is visible.
        boolean isDebugScreenVisible = Minecraft.getInstance().getDebugOverlay().showDebugScreen();

        // Check whether the HUD is currently hidden.
        boolean isHudHidden = client.options.hideGui;

        // Update the local clue scroll list.
        if (System.currentTimeMillis() > nextUpdateTime) {
            scrollManager.updateScrolls(client);
            nextUpdateTime = System.currentTimeMillis() + config.updateInterval;
        }

        // Render the ClueScrollHudElement if it's enabled, the HUD is visible, and the F3 debug screen is not visible.
        if (isVisible && !isDebugScreenVisible && !isHudHidden) {
            Font textRenderer = client.font;

            // Get information about the selected scroll.
            int selectedIndex = scrollManager.getSelectedScrollIndex();
            int totalScrolls = scrollManager.getScrollCount();

            // Render the clue scrolls.
            if (totalScrolls > 0) {
                if (config.displayMode == ElementDisplayMode.COLLATED) {
                    int activeScrollCount = scrollManager.getNonExpiredScrollCount();

                    // Collated mode: show all tasks from all non-expired scrolls in one HUD panel.
                    if (activeScrollCount > 0) {
                        scrollRenderer.renderCollated(
                                context,
                                textRenderer,
                                scrollManager.getCollatedGroups(),
                                activeScrollCount
                        );
                    }
                    else {
                        scrollRenderer.render(context, textRenderer);
                    }
                }
                else {
                    // Single-scroll mode: show one selected scroll at a time.
                    ClueScroll selectedScroll = scrollManager.getSelectedScroll();
                    scrollRenderer.render(context, textRenderer, selectedScroll, selectedIndex, totalScrolls);
                }
            }
            else {
                scrollRenderer.render(context, textRenderer);
            }

        }
    }

    public static void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("Toggled visibility of ClueScrollHudElement to: {}", isVisible);
    }

    public static void prevScroll() {
        scrollManager.prevScroll();
    }

    public static void nextScroll() {
        scrollManager.nextScroll();
    }
}

