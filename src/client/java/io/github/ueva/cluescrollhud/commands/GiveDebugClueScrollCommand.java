package io.github.ueva.cluescrollhud.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.ueva.cluescrollhud.utils.DebugUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.ItemStack;


public class GiveDebugClueScrollCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("givedebugcluescroll")
                       .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                       .executes(context -> giveClueScroll(context, -1)) // No argument: random number of tasks
                       .then(Commands.argument("taskCount", IntegerArgumentType.integer(1)).executes(context -> {
                           int taskCount = IntegerArgumentType.getInteger(context, "taskCount");
                           return giveClueScroll(context, taskCount);
                       }));
    }

    private static int giveClueScroll(CommandContext<CommandSourceStack> context, int taskCount) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() instanceof ServerPlayer player) {
            ItemStack debugScroll = DebugUtils.createDebugClueScroll(taskCount);
            boolean added = player.getInventory().add(debugScroll);

            if (added) {
                source.sendSuccess(() -> Component.literal("Debug clue scroll added to your inventory."), false);
            }
            else {
                source.sendFailure(Component.literal("Inventory full. Could not add the debug clue scroll."));
            }
        }
        else {
            source.sendFailure(Component.literal("This command can only be executed by a player."));
        }

        return Command.SINGLE_SUCCESS;
    }
}
