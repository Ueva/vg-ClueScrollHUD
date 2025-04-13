package io.github.ueva.cluescrollhud.keybinds;

import io.github.ueva.cluescrollhud.hudelement.ClueScrollHudElement;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class ToggleVisibilityKeybind {

    public static void register() {

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ').
        KeyBinding toggleVisibleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vg-cluescrollhud.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                "category.vg-cluescrollhud"
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVisibleKeyBinding.wasPressed()) {
                ClueScrollHudElement.toggleVisibility();
            }
        });
    }
}
