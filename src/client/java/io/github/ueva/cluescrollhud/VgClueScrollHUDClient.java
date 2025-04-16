package io.github.ueva.cluescrollhud;

import io.github.ueva.cluescrollhud.commands.CommandRegistrar;
import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.hudelement.HudElementRegistrar;
import io.github.ueva.cluescrollhud.keybinds.KeybindRegistrar;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VgClueScrollHUDClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    @Override
    public void onInitializeClient() {

        // Register the mod config.
        LOGGER.info("Registering mod config...");
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        LOGGER.info("...finished registering mod config!");

        // Register HUD elements.
        LOGGER.info("Registering HUD elements...");
        HudElementRegistrar.registerHudElements();
        LOGGER.info("...finished registering all HUD elements!");

        // Register keybinds.
        LOGGER.info("Registering keybinds...");
        KeybindRegistrar.registerKeybinds();
        LOGGER.info("...finished registering all keybinds!");


        // Register commands.
        LOGGER.info("Registering commands...");
        CommandRegistrar.registerCommands();
        LOGGER.info("...finished registering all commands.");
    }
}

