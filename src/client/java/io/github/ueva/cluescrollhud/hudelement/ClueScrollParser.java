package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import io.github.ueva.cluescrollhud.net.RemoteDataFetcher;
import io.github.ueva.cluescrollhud.utils.TierNameUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;


public class ClueScrollParser {

    public static ClueScroll parseScrollData(CompoundTag scrollData, int invPosition) {
        // Extract the scroll's UUID, tier, created time, and expiration time.
        String uuid = scrollData.getString("ClueScrolls.uuid").orElseThrow();
        String rawTier = scrollData.getString("ClueScrolls.tier").orElseThrow();
        String tier = TierNameUtils.sanitiseTierName(rawTier).toLowerCase();
        long created = scrollData.getLong("ClueScrolls.created").orElseThrow();

        // If the scroll's tier is "Extended", use the expiry time from the remote data fetcher, otherwise use the
        // one from the scroll data.
        long expire = -1L;
        if (tier.equals("extended")) {
            expire = RemoteDataFetcher.getExtendedExpiryTime();
        }
        else {
            expire = scrollData.getLong("ClueScrolls.expire").orElseThrow();
        }

        // Extract the clues from the NBT data.
        ArrayList<ClueTask> clues = new ArrayList<>();
        int n = 0;
        String baseKeyFormat = "ClueScrolls.clues.%d.%s";

        while (scrollData.contains(String.format(baseKeyFormat, n, "objective"))) {
            String objective = scrollData.getString(String.format(baseKeyFormat, n, "objective")).orElseThrow();
            float amount = scrollData.getFloat(String.format(baseKeyFormat, n, "amount")).orElseThrow();
            float completed = scrollData.getFloat(String.format(baseKeyFormat, n, "completed")).orElseThrow();

            // Create a new ClueTask and add it to the list.
            clues.add(new ClueTask(objective, (int) amount, (int) completed));
            n++;
        }

        return new ClueScroll(uuid, tier, created, expire, invPosition, clues);
    }

    public static ClueScroll parseScrollItem(ItemStack scrollItem, int invPosition) {
        // Check if the item stack is empty or does not contain the custom data component.
        if (scrollItem.isEmpty() || !scrollItem.has(DataComponents.CUSTOM_DATA)) {
            return null;
        }

        // Extract the custom data component from the item stack.
        CustomData customData = scrollItem.get(DataComponents.CUSTOM_DATA);

        // If the custom data component is null, return null.
        if (customData == null) {
            return null;
        }

        // Extract the NBT data from the custom data component.
        CompoundTag scrollData = customData.copyTag();

        // Check if the NBT data contains the "ClueScrolls.uuid" key.
        if (!scrollData.contains("ClueScrolls.uuid")) {
            return null;
        }

        // Parse and return the ClueScroll object.
        return parseScrollData(scrollData, invPosition);
    }
}
