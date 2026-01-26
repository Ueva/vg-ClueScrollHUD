package io.github.ueva.cluescrollhud.keybinds;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeybindRegistrar {

    public static final KeyMapping.Category VG_CLUESCROLL_KB_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(VgClueScrollHUD.MOD_ID, "main"));

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    public static void registerKeybinds() {
        ToggleVisibilityKeybind.register();
        LOGGER.info("- Successfully registered ToggleVisibility Keybind.");

        NextPrevScrollKeybind.register();
        LOGGER.info("- Successfully registered PrevScroll and NextScroll Keybinds.");
    }
}