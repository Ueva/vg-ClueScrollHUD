package io.github.ueva.cluescrollhud.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.ueva.cluescrollhud.hudelement.ClueScrollHudElement;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;


public class ToggleVisibilityKeybind {

    public static void register() {

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ').
        KeyMapping toggleVisibleKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.vg-cluescrollhud.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                KeybindRegistrar.VG_CLUESCROLL_KB_CATEGORY
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVisibleKeyBinding.consumeClick()) {
                ClueScrollHudElement.toggleVisibility();
            }
        });
    }
}
