package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HudElementRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    public static void registerHudElements() {
        // Register the ClueScrollHudElement to render on the HUD every frame.
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                ClueScrollHudElement.CLUESCROLL_HUD_LAYER,
                ClueScrollHudElement::render
        );

        LOGGER.info("- Successfully added ClueScrollHudElement to the HUD.");
    }
}


