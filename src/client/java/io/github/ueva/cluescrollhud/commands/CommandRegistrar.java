package io.github.ueva.cluescrollhud.commands;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(GiveDebugClueScrollCommand.register());
        });
        LOGGER.info("- Successfully registered `givedebugcluescroll` Command.");
    }
}