package io.github.ueva.cluescrollhud.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.ueva.cluescrollhud.utils.DebugUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


public class GiveDebugClueScrollCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("givedebugcluescroll")
                             .requires(source -> source.hasPermissionLevel(2))
                             .executes(context -> giveClueScroll(context, -1)) // No argument: random number of tasks
                             .then(CommandManager.argument("taskCount", IntegerArgumentType.integer(1))
                                                 .executes(context -> {
                                                     int taskCount =
                                                             IntegerArgumentType.getInteger(context, "taskCount");
                                                     return giveClueScroll(context, taskCount);
                                                 }));
    }

    private static int giveClueScroll(CommandContext<ServerCommandSource> context, int taskCount) {
        ServerCommandSource source = context.getSource();

        if (source.getEntity() instanceof ServerPlayerEntity player) {
            ItemStack debugScroll = DebugUtils.createDebugClueScroll(taskCount);
            boolean added = player.getInventory().insertStack(debugScroll);

            if (added) {
                source.sendFeedback(() -> Text.literal("Debug clue scroll added to your inventory."), false);
            }
            else {
                source.sendError(Text.literal("Inventory full. Could not add the debug clue scroll."));
            }
        }
        else {
            source.sendError(Text.literal("This command can only be executed by a player."));
        }

        return Command.SINGLE_SUCCESS;
    }
}
