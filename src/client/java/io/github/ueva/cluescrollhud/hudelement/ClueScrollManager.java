package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ClueScrollManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    private final List<ClueScroll> scrolls = new ArrayList<>();
    private int selectedIndex = 0;

    public void updateScrolls(MinecraftClient client) {
        // Ensure the client and the player are not null.
        if (client == null || client.player == null) {
            return;
        }

        // Keep track of the scroll items and UUIDs we see while sweeping through the player's inventory.
        ArrayList<String> seenUUIDs = new ArrayList<>();

        // Iterate through all the scrolls in the player's inventory.
        PlayerInventory inventory = client.player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            // Get the item stack in the current slot.
            ItemStack itemStack = inventory.getStack(i);

            // Check that the item stack is not empty and contains the custom data component.
            if (!itemStack.isEmpty() && itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
                NbtComponent customData = itemStack.get(DataComponentTypes.CUSTOM_DATA);

                // If the custom data component is not null.
                if (customData != null) {

                    // Extract the NBT data from the custom data component.
                    NbtCompound scrollData = customData.copyNbt();

                    // Check if the NBT data contains the "ClueScrolls.uuid" key.
                    if (scrollData.contains("ClueScrolls.uuid")) {
                        // Extract clue scroll data.
                        String uuid = scrollData.getString("ClueScrolls.uuid");
                        seenUUIDs.add(uuid);

                        // If this is a new scroll, add it to the list.
                        if (!isScrollInList(uuid)) {
                            addScroll(scrollData);
                        }
                        // Otherwise, update the existing scroll's completion amount.
                        else {
                            updateScroll(uuid, scrollData);
                        }
                    }
                }
            }
        }

        // Remove any scrolls that are no longer in the player's inventory.
        for (int j = scrolls.size() - 1; j >= 0; j--) {
            ClueScroll scroll = scrolls.get(j);
            if (!seenUUIDs.contains(scroll.getUuid())) {
                scrolls.remove(j);
                LOGGER.info("Removed clue scroll from list: {}", scroll.getUuid());
            }
        }
    }

    public List<ClueScroll> getScrolls() {
        return scrolls;
    }

    public int getScrollCount() {
        return scrolls.size();
    }

    public ClueScroll getSelectedScroll() {
        if (scrolls.isEmpty()) {
            return null;
        }
        return scrolls.get(selectedIndex);
    }

    public int getSelectedScrollIndex() {
        return selectedIndex;
    }

    public void prevScroll() {
        int clueScrollCount = scrolls.size();

        // If there are no clue scrolls in the player's inventory, return.
        if (clueScrollCount == 0) {
            return;
        }

        // Decrement the selected clue scroll index, wrapping around to the last clue scroll if necessary.
        selectedIndex = (selectedIndex - 1 + clueScrollCount) % clueScrollCount;

        LOGGER.info("Selected previous clue scroll ({} of {}).", selectedIndex + 1, clueScrollCount);
    }

    public void nextScroll() {
        int clueScrollCount = scrolls.size();

        // If there are no clue scrolls in the player's inventory, return.
        if (clueScrollCount == 0) {
            return;
        }

        // Increment the selected clue scroll index, wrapping around to the first clue scroll if necessary.
        selectedIndex = (selectedIndex + 1) % clueScrollCount;

        LOGGER.info("Selected next clue scroll ({} of {}).", selectedIndex + 1, clueScrollCount);
    }

    private boolean isScrollInList(String uuid) {
        // Check if any scrolls in the list have the same UUID as the one passed in.
        for (ClueScroll scroll : scrolls) {
            if (scroll.getUuid()
                    .equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private void updateScroll(String uuid, NbtCompound scroll_data) {
        // Get the scroll with the given UUID from the list.
        ClueScroll scroll = null;
        for (ClueScroll s : scrolls) {
            if (s.getUuid()
                    .equals(uuid)) {
                scroll = s;
                break;
            }
        }

        // Update the scroll's completion amount.
        if (scroll != null) {
            for (int i = 0; i < scroll.getClueCount(); i++) {
                ClueTask clue = scroll.getClues()
                        .get(i);
                int completed = (int) scroll_data.getFloat("ClueScrolls.clues." + i + ".completed");
                clue.setCompleted(completed);
            }
        }
    }

    private void addScroll(NbtCompound scrollData) {
        // Adds a new clue scroll to the list.
        ClueScroll newScroll = ClueScrollParser.parseScrollData(scrollData);
        scrolls.add(newScroll);
        LOGGER.info("Added new clue scroll to list: {}", newScroll.getUuid());
    }

    private void updateSelectedIndex() {
        int scrollCount = scrolls.size();

        // If there are no scrolls, reset the selected index to 0.
        if (scrolls.isEmpty()) {
            selectedIndex = 0;
        }
        // Otherwise, ensure the selected index is within bounds.
        else {
            // Check that the selected clue scroll index is within bounds.
            if (selectedIndex >= scrollCount) {
                selectedIndex = scrollCount - 1;
            }
            else if (selectedIndex < 0) {
                selectedIndex = 0;
            }
        }
    }
}
