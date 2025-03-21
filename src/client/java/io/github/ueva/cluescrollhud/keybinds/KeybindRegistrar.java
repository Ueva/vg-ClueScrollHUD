package io.github.ueva.cluescrollhud.keybinds;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeybindRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    public static void registerKeybinds() {
        ToggleVisibilityKeybind.register();
        LOGGER.info("- Successfully registered ToggleVisibility Keybind.");
    }
}