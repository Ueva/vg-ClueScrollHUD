package io.github.ueva.cluescrollhud.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.RandomSeed;

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
        NbtComponent debugScrollDataComponent = getDebugScrollDataComponent(forcedClueCount);

        // Apply the NBT data to the debug scroll item.
        debugScroll.set(DataComponentTypes.CUSTOM_DATA, debugScrollDataComponent);

        return debugScroll;
    }

    private static NbtComponent getDebugScrollDataComponent(int forcedClueCount) {
        LocalRandom random = new LocalRandom(RandomSeed.getSeed());

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

        int clueCount = forcedClueCount > 0 ? forcedClueCount : random.nextBetweenExclusive(2, 11);
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

        NbtCompound data = new NbtCompound();

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

        return NbtComponent.of(data);
    }
}
