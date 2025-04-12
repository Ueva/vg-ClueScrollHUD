package io.github.ueva.cluescrollhud.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.RandomSeed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class DebugUtils {

    public static ItemStack createDebugClueScroll() {

        // Create the debug scroll item.
        ItemStack debugScroll = new ItemStack(Items.SLIME_BALL);

        // Generate the NBT data for the debug scroll.
        NbtComponent debugScrollDataComponent = getDebugScrollDataComponent();

        // Apply the NBT data to the debug scroll item.
        debugScroll.set(DataComponentTypes.CUSTOM_DATA, debugScrollDataComponent);

        return debugScroll;
    }

    private static NbtComponent getDebugScrollDataComponent() {
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

        int clueCount = random.nextBetweenExclusive(2, 10); // 2–9
        Set<Integer> selectedIndices = new HashSet<>();
        while (selectedIndices.size() < clueCount) {
            int index = random.nextInt(objectives.size());
            selectedIndices.add(index);
        }

        List<String> selectedObjectives = selectedIndices.stream()
                .map(objectives::get)
                .toList();

        // Assign tier based on clue count
        String tier = switch (clueCount) {
            case 2, 3 -> "easy";
            case 4, 5 -> "normal";
            case 6, 7 -> "hard";
            default -> "weekly";
        };

        NbtCompound data = new NbtCompound();

        // Set scroll metadata.
        data.putString("ClueScrolls.tier", tier);
        data.putString("ClueScrolls.version", "5.0.8");
        data.putString(
                "ClueScrolls.uuid",
                UUID.randomUUID()
                        .toString()
        );
        data.putLong("ClueScrolls.created", System.currentTimeMillis());
        data.putLong("ClueScrolls.expire", System.currentTimeMillis() + 60 * 60 * 1000);

        // Set objective, amount, and completed for each clue.
        int i = 0;
        for (String objective : selectedObjectives) {
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
            i++;
        }

        return NbtComponent.of(data);
    }

}
