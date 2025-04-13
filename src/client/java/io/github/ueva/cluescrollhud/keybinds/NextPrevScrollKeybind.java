package io.github.ueva.cluescrollhud.keybinds;

import io.github.ueva.cluescrollhud.hudelements.ClueScrollHudElement;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class NextPrevScrollKeybind {

    public static void register() {

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ]).
        KeyBinding prevScrollKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vg-cluescrollhud.prev",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "category.vg-cluescrollhud"
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (prevScrollKeyBinding.wasPressed()) {
                ClueScrollHudElement.prevScroll();
            }
        });

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ]).
        KeyBinding nextScrollKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.vg-cluescrollhud.next",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "category.vg-cluescrollhud"
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (nextScrollKeyBinding.wasPressed()) {
                ClueScrollHudElement.nextScroll();
            }
        });
    }
}
