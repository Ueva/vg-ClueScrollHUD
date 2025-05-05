package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClueScrollHudElement {

    public static final Identifier CLUESCROLL_HUD_LAYER = Identifier.of(VgClueScrollHUD.MOD_ID, "cluescroll_hud_layer");

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class)
            .getConfig();
    private static final ClueScrollManager scrollManager = new ClueScrollManager(config);
    private static final ClueScrollRenderer scrollRenderer = new ClueScrollRenderer(config);

    private static boolean isVisible = true;
    private static long nextUpdateTime = 0;

    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        // Obtain the client instance.
        MinecraftClient client = MinecraftClient.getInstance();

        // Check whether the F3 debug screen is visible.
        boolean isDebugScreenVisible = MinecraftClient.getInstance()
                .getDebugHud()
                .shouldShowDebugHud();

        // Check whether the HUD is currently hidden.
        boolean isHudHidden = client.options.hudHidden;

        // Update the local clue scroll list.
        if (System.currentTimeMillis() > nextUpdateTime) {
            scrollManager.updateScrolls(client);
            nextUpdateTime = System.currentTimeMillis() + config.updateInterval;
        }

        // Render the ClueScrollHudElement if it's enabled, the HUD is visible, and the F3 debug screen is not visible.
        if (isVisible && !isDebugScreenVisible && !isHudHidden) {
            TextRenderer textRenderer = client.textRenderer;

            // Get information about the selected scroll.
            int selectedIndex = scrollManager.getSelectedScrollIndex();
            int totalScrolls = scrollManager.getScrollCount();

            // Render the clue scrolls.
            if (totalScrolls > 0) {
                ClueScroll selectedScroll = scrollManager.getSelectedScroll();
                scrollRenderer.render(context, textRenderer, selectedScroll, selectedIndex, totalScrolls);
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

