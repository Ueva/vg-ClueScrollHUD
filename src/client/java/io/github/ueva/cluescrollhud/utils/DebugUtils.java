package io.github.ueva.cluescrollhud.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DebugUtils {

    public static ItemStack createDebugClueScroll() {
        return createDebugClueScroll(-1); // Default: random number of tasks
    }

    public static ItemStack createDebugClueScroll(int forcedClueCount) {
        // Create the debug scroll item.
        ItemStack debugScroll = new ItemStack(Items.SLIME_BALL);

        // Generate the NBT data for the debug scroll.
        CustomData debugScrollDataComponent = getDebugScrollDataComponent(forcedClueCount);

        // Apply the NBT data to the debug scroll item.
        debugScroll.set(DataComponents.CUSTOM_DATA, debugScrollDataComponent);

        return debugScroll;
    }

    private static CustomData getDebugScrollDataComponent(int forcedClueCount) {
        SingleThreadedRandomSource random = new SingleThreadedRandomSource(RandomSupport.generateUniqueSeed());

        List<String> objectives = List.of(
                "Ride a pig %amount% blocks",
                "Mine %amount% coal ore",
                "Catch %amount% fish",
                "Chop %amount% oak logs",
                "Kill %amount% zombies",
                "Craft %amount% oak planks",
                "Eat %amount% cookies",
                "Place %amount% torches",
                "Walk %amount% blocks"
        );

        int clueCount = forcedClueCount > 0 ? forcedClueCount : random.nextInt(2, 11);
        List<String> selectedObjectives = new ArrayList<>();
        for (int i = 0; i < clueCount; i++) {
            int index = random.nextInt(objectives.size());
            selectedObjectives.add(objectives.get(index));
        }

        // Assign tier based on clue count
        String tier = switch (clueCount) {
            case 2, 3 -> "easy";
            case 4, 5 -> "normal";
            case 6, 7 -> "hard";
            case 8, 9 -> "weekly";
            default -> "extended";
        };

        CompoundTag data = new CompoundTag();

        // Set scroll metadata
        data.putString("ClueScrolls.tier", tier);
        data.putString("ClueScrolls.version", "5.0.8");
        data.putString("ClueScrolls.uuid", UUID.randomUUID().toString());
        data.putLong("ClueScrolls.created", System.currentTimeMillis());
        data.putLong("ClueScrolls.expire", System.currentTimeMillis() + 60 * 60 * 1000);

        // Set clues
        for (int i = 0; i < selectedObjectives.size(); i++) {
            String objective = selectedObjectives.get(i);
            int maxAmount = switch (i % 4) {
                case 0 -> 5000;
                case 1 -> 250;
                case 2 -> 100;
                default -> 50;
            };

            int amount = random.nextInt(maxAmount) + 1;
            int completed = random.nextInt(amount);

            data.putString("ClueScrolls.clues." + i + ".objective", objective);
            data.putFloat("ClueScrolls.clues." + i + ".amount", (float) amount);
            data.putFloat("ClueScrolls.clues." + i + ".completed", (float) completed);
        }

        return CustomData.of(data);
    }
}
