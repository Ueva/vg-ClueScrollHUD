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

        // Set the created time to the current time.
        debugScrollData.putInt("ClueScrolls.created", (int) (System.currentTimeMillis()));

        // Set the time to complete to 60 minutes in the future.
        debugScrollData.putInt("ClueScrolls.expire", (int) (System.currentTimeMillis()) + 60 * 60 * 1000);

        // Add the first clue.
        debugScrollData.putString("ClueScrolls.clues.0.objective", "Ride a pig %amount% blocks");

        int amount = random.nextInt(5000) + 1;
        debugScrollData.putFloat("ClueScrolls.clues.0.amount", (float) amount);

        // Randomly choose a number between 0 and amount.
        int completed = random.nextInt(amount);
        debugScrollData.putFloat("ClueScrolls.clues.0.completed", (float) completed);

        return NbtComponent.of(debugScrollData);
    }
}
