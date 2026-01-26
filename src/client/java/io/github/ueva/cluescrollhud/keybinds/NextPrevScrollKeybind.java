package io.github.ueva.cluescrollhud.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.ueva.cluescrollhud.hudelement.ClueScrollHudElement;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;


public class NextPrevScrollKeybind {

    public static void register() {

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ]).
        KeyMapping prevScrollKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.vg-cluescrollhud.prev",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                KeybindRegistrar.VG_CLUESCROLL_KB_CATEGORY
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (prevScrollKeyBinding.consumeClick()) {
                ClueScrollHudElement.prevScroll();
            }
        });

        // Register a keybinding to toggle the visibility of the ClueScrollHudElement (default: ]).
        KeyMapping nextScrollKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.vg-cluescrollhud.next",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                KeybindRegistrar.VG_CLUESCROLL_KB_CATEGORY
        ));

        // Set up the keybinding to toggle the visibility of the ClueScrollHudElement.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (nextScrollKeyBinding.consumeClick()) {
                ClueScrollHudElement.nextScroll();
            }
        });
    }
}
