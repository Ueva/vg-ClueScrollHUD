package io.github.ueva.cluescrollhud;

import io.github.ueva.cluescrollhud.hudelements.ClueScrollHudElement;
import io.github.ueva.cluescrollhud.commands.CommandRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VgClueScrollHUDClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    @Override
    public void onInitializeClient() {
        // Register the ClueScrollHudElement to render on the HUD every frame.
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(
                IdentifiedLayer.CHAT,
                ClueScrollHudElement.CLUESCROLL_HUD_LAYER,
                ClueScrollHudElement::render
        ));
        LOGGER.info("Successfully added ClueScrollHudElement to the HUD.");

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement.
        KeyBinding toggleVisibleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vg-cluescrollhud.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                "category.vg-cluescrollhud"
        ));
        LOGGER.info("Successfully registered keybinding to toggle visibility of ClueScrollHudElement (Default: ').");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVisibleKeyBinding.wasPressed()) {
                ClueScrollHudElement.toggleVisibility();
            }
        });

        // Register commands.
        LOGGER.info("Registering commands...");
        CommandRegistrar.registerCommands();
        LOGGER.info("...finished registering all commands.");
    }
}

