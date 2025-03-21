package io.github.ueva.cluescrollhud.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.ueva.cluescrollhud.utils.DebugUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class GiveDebugClueScrollCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("givedebugcluescroll")
                .requires(source -> source.hasPermissionLevel(2)) // Ensure player has OP or higher
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    // Ensure the command is executed by a player
                    if (source.getEntity() instanceof ServerPlayerEntity player) {
                        ItemStack debugScroll = DebugUtils.createDebugClueScroll();
                        boolean added = player.getInventory()
                                .insertStack(debugScroll);
                        if (added) {
                            source.sendFeedback(
                                    () -> Text.literal("Debug clue scroll added to your inventory."),
                                    false
                            );
                        } else {
                            source.sendError(Text.literal("Inventory full. Could not add the debug clue scroll."));
                        }
                    } else {
                        source.sendError(Text.literal("This command can only be executed by a player."));
                    }
                    return Command.SINGLE_SUCCESS;
                });
    }
}