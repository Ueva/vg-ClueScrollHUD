package io.github.ueva.cluescrollhud.hudelements;

import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;


public class ClueScrollParser {

    public static ClueScroll parseScrollData(NbtCompound scrollData) {
        // Extract the scroll's UUID, tier, created time, and expiration time.
        String uuid = scrollData.getString("ClueScrolls.uuid");
        String tier = scrollData.getString("ClueScrolls.tier");
        long created = scrollData.getLong("ClueScrolls.created");
        long expire = scrollData.getLong("ClueScrolls.expire");

        // Extract the clues from the NBT data.
        ArrayList<ClueTask> clues = new ArrayList<>();
        int n = 0;
        String baseKeyFormat = "ClueScrolls.clues.%d.%s";

        while (scrollData.contains(String.format(baseKeyFormat, n, "objective"))) {
            String objective = scrollData.getString(String.format(baseKeyFormat, n, "objective"));
            int amount = (int) scrollData.getFloat(String.format(baseKeyFormat, n, "amount"));
            int completed = (int) scrollData.getFloat(String.format(baseKeyFormat, n, "completed"));

            // Create a new ClueTask and add it to the list.
            clues.add(new ClueTask(objective, amount, completed));
            n++;
        }

        return new ClueScroll(uuid, tier, created, expire, clues);
    }

    public static ClueScroll parseScrollItem(ItemStack scrollItem) {
        // Check if the item stack is empty or does not contain the custom data component.
        if (scrollItem.isEmpty() || !scrollItem.contains(DataComponentTypes.CUSTOM_DATA)) {
            return null;
        }

        // Extract the custom data component from the item stack.
        NbtComponent customData = scrollItem.get(DataComponentTypes.CUSTOM_DATA);

        // If the custom data component is null, return null.
        if (customData == null) {
            return null;
        }

        // Extract the NBT data from the custom data component.
        NbtCompound scrollData = customData.copyNbt();

        // Check if the NBT data contains the "ClueScrolls.uuid" key.
        if (!scrollData.contains("ClueScrolls.uuid")) {
            return null;
        }

        // Parse and return the ClueScroll object.
        return parseScrollData(scrollData);
    }
}
