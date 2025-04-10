package io.github.ueva.cluescrollhud.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.RandomSeed;


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
        // Create a random number generator for randomising parts of the debug clue.
        LocalRandom random = new LocalRandom(RandomSeed.getSeed());

        NbtCompound debugScrollData = new NbtCompound();

        // Add basic information about the debug scroll.
        debugScrollData.putString("ClueScrolls.tier", "easy");
        debugScrollData.putString("ClueScrolls.version", "5.0.8");
        debugScrollData.putString(
                "ClueScrolls.uuid",
                java.util.UUID.randomUUID()
                        .toString()
        );

        // Set the created time to the current time.
        debugScrollData.putLong("ClueScrolls.created", System.currentTimeMillis());

        // Set the time to complete to 60 minutes in the future.
        debugScrollData.putLong("ClueScrolls.expire", System.currentTimeMillis() + 60 * 60 * 1000);

        // Add the first clue.
        debugScrollData.putString("ClueScrolls.clues.0.objective", "Ride a pig %amount% blocks");

        int amount_1 = random.nextInt(5000) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.0.amount", (float) amount_1);

        int completed_1 = random.nextInt(amount_1);
        debugScrollData.putFloat("ClueScrolls.clues.0.completed", (float) completed_1);

        // Add the second clue.
        debugScrollData.putString("ClueScrolls.clues.1.objective", "Mine %amount% coal ore");

        int amount_2 = random.nextInt(250) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.1.amount", (float) amount_2);

        int completed_2 = random.nextInt(amount_2);
        debugScrollData.putFloat("ClueScrolls.clues.1.completed", (float) completed_2);

        // Add the third clue.
        debugScrollData.putString("ClueScrolls.clues.2.objective", "Catch %amount% fish");

        int amount_3 = random.nextInt(50) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.2.amount", (float) amount_3);

        int completed_3 = random.nextInt(amount_3);
        debugScrollData.putFloat("ClueScrolls.clues.2.completed", (float) completed_3);

        // Add the fourth clue.
        debugScrollData.putString("ClueScrolls.clues.3.objective", "Chop %amount% oak logs");

        int amount_4 = random.nextInt(100) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.3.amount", (float) amount_4);

        int completed_4 = random.nextInt(amount_4);
        debugScrollData.putFloat("ClueScrolls.clues.3.completed", (float) completed_4);

        // Add the fifth clue.
        debugScrollData.putString("ClueScrolls.clues.4.objective", "Kill %amount% zombies");

        int amount_5 = random.nextInt(75) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.4.amount", (float) amount_5);

        int completed_5 = random.nextInt(amount_5);
        debugScrollData.putFloat("ClueScrolls.clues.4.completed", (float) completed_5);

        return NbtComponent.of(debugScrollData);
    }
}
