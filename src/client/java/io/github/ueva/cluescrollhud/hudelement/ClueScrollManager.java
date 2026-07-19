package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import io.github.ueva.cluescrollhud.config.CollatedGroupMode;
import io.github.ueva.cluescrollhud.config.ElementDisplayMode;
import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.config.ScrollSortMode;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import io.github.ueva.cluescrollhud.net.RemoteDataFetcher;
import io.github.ueva.cluescrollhud.utils.TaskSortUtils;
import io.github.ueva.cluescrollhud.utils.TierColourUtils;
import io.github.ueva.cluescrollhud.utils.TierOrderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class ClueScrollManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);

    private final List<ClueScroll> scrolls = new ArrayList<>();
    private final ModConfig config;

    private int selectedIndex = 0;

    public ClueScrollManager(ModConfig config) {
        this.config = config;
    }

    public void updateScrolls(Minecraft client) {
        // Ensure the client and the player are not null.
        if (client == null || client.player == null) {
            return;
        }

        // Keep track of the scroll items and UUIDs we see while sweeping through the player's inventory.
        ArrayList<String> seenUUIDs = new ArrayList<>();

        // Iterate through all the scrolls in the player's inventory.
        Inventory inventory = client.player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            // Get the item stack in the current slot.
            ItemStack itemStack = inventory.getItem(i);

            // Check that the item stack is not empty and contains the custom data component.
            if (!itemStack.isEmpty() && itemStack.has(DataComponents.CUSTOM_DATA)) {
                CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);

                // If the custom data component is not null.
                if (customData != null) {

                    // Extract the NBT data from the custom data component.
                    CompoundTag scrollData = customData.copyTag();

                    // Check if the NBT data contains the "ClueScrolls.uuid" key.
                    if (scrollData.contains("ClueScrolls.uuid")) {
                        // Extract clue scroll data.
                        String uuid = scrollData.getString("ClueScrolls.uuid").orElseThrow();
                        seenUUIDs.add(uuid);

                        // If this is a new scroll, add it to the list.
                        if (!isScrollInList(uuid)) {
                            addScroll(scrollData, i);
                        }
                        // Otherwise, update the existing scroll's completion amount.
                        else {
                            updateScroll(uuid, scrollData, i);
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

        // Ensure the selected index is valid.
        updateSelectedIndex();
    }

    public List<ClueScroll> getScrolls() {
        return scrolls;
    }

    public List<ClueScroll> getSortedScrolls(ScrollSortMode mode, boolean reverse) {
        List<ClueScroll> sortedScrolls = new ArrayList<>(scrolls);

        Comparator<ClueScroll> comparator = switch (mode) {
            case TIER -> Comparator.comparingInt(scroll -> TierOrderUtils.getIndex(scroll.getTier()));
            case START_TIME -> Comparator.comparingLong(ClueScroll::getCreated);
            case EXPIRY_TIME -> Comparator.comparingLong(ClueScroll::getExpire);
            case INV_POSITION -> Comparator.comparingInt(ClueScroll::getInvPosition);
            case DEFAULT -> null;
        };

        if (comparator != null) {
            sortedScrolls.sort(comparator);
        }

        if (reverse) {
            Collections.reverse(sortedScrolls);
        }

        return sortedScrolls;
    }


    public int getScrollCount() {
        return scrolls.size();
    }

    public int getNonExpiredScrollCount() {
        int count = 0;
        for (ClueScroll scroll : scrolls) {
            if (!scroll.isExpired()) {
                count++;
            }
        }
        return count;
    }

    public ClueScroll getSelectedScroll() {
        if (scrolls.isEmpty()) {
            return null;
        }
        return getSortedScrolls(config.scrollSortMode, config.reverseScrollSort).get(selectedIndex);
    }

    public int getSelectedScrollIndex() {
        return selectedIndex;
    }

    public List<CollatedGroup> getCollatedGroups() {
        return switch (config.collatedGroupMode) {
            case MERGE_OBJECTIVES -> List.of(new CollatedGroup(null, null, buildMergedEntries()));
            case BY_SCROLL -> buildGroupsByScroll();
            case BY_OBJECTIVE_TYPE -> buildGroupsByObjectiveType();
        };
    }

    private List<CollatedTaskEntry> buildMergedEntries() {
        Map<String, CollatedTaskEntry> groupedTasks = new HashMap<>();

        for (ClueScroll scroll : scrolls) {
            if (scroll.isExpired()) {
                continue;
            }

            for (int i = 0; i < scroll.getClueCount(); i++) {
                ClueTask task = scroll.getClueTask(i);
                String objectiveKey = task.getObjective();

                if (groupedTasks.containsKey(objectiveKey)) {
                    CollatedTaskEntry existing = groupedTasks.get(objectiveKey);
                    ClueTask mergedTask = new ClueTask(
                            objectiveKey,
                            existing.task().getAmount() + task.getAmount(),
                            existing.task().getCompleted() + task.getCompleted()
                    );
                    boolean spansMultipleTiers = existing.spansMultipleTiers()
                            || !scroll.getTier().equals(existing.scroll().getTier());
                    groupedTasks.put(objectiveKey, new CollatedTaskEntry(
                            existing.scroll(),
                            mergedTask,
                            existing.taskIndex(),
                            spansMultipleTiers
                    ));
                }
                else {
                    groupedTasks.put(objectiveKey, new CollatedTaskEntry(scroll, task, i));
                }
            }
        }

        List<CollatedTaskEntry> collatedTasks = new ArrayList<>();
        for (CollatedTaskEntry entry : groupedTasks.values()) {
            if (config.hideCompleted && entry.task().isCompleted()) {
                continue;
            }
            collatedTasks.add(entry);
        }

        sortEntries(collatedTasks);
        return collatedTasks;
    }

    private List<CollatedGroup> buildGroupsByScroll() {
        List<CollatedGroup> groups = new ArrayList<>();

        for (ClueScroll scroll : getSortedScrolls(config.scrollSortMode, config.reverseScrollSort)) {
            if (scroll.isExpired()) {
                continue;
            }

            List<CollatedTaskEntry> entries = entriesForScroll(scroll);
            if (entries.isEmpty()) {
                continue;
            }

            String tier = scroll.getTier();
            String header = capitalize(tier) + " Clue Scroll";
            groups.add(new CollatedGroup(header, TierColourUtils.getColour(tier), entries));
        }

        return groups;
    }

    private List<CollatedGroup> buildGroupsByObjectiveType() {
        // TreeMap keeps group keys sorted case-insensitively.
        Map<String, List<CollatedTaskEntry>> buckets = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (ClueScroll scroll : scrolls) {
            if (scroll.isExpired()) {
                continue;
            }

            for (int i = 0; i < scroll.getClueCount(); i++) {
                ClueTask task = scroll.getClueTask(i);
                if (config.hideCompleted && task.isCompleted()) {
                    continue;
                }

                String typeKey = objectiveTypeKey(task);
                buckets.computeIfAbsent(typeKey, ignored -> new ArrayList<>())
                        .add(new CollatedTaskEntry(scroll, task, i));
            }
        }

        List<CollatedGroup> groups = new ArrayList<>();
        for (Map.Entry<String, List<CollatedTaskEntry>> bucket : buckets.entrySet()) {
            List<CollatedTaskEntry> entries = bucket.getValue();
            sortEntries(entries);
            groups.add(new CollatedGroup(capitalize(bucket.getKey()), 0xFFFFFFFF, entries));
        }

        return groups;
    }

    private List<CollatedTaskEntry> entriesForScroll(ClueScroll scroll) {
        List<CollatedTaskEntry> entries = new ArrayList<>();

        for (int i = 0; i < scroll.getClueCount(); i++) {
            ClueTask task = scroll.getClueTask(i);
            if (config.hideCompleted && task.isCompleted()) {
                continue;
            }
            entries.add(new CollatedTaskEntry(scroll, task, i));
        }

        sortEntries(entries);
        return entries;
    }

    private void sortEntries(List<CollatedTaskEntry> entries) {
        Comparator<ClueTask> taskComparator = TaskSortUtils.comparator(config.taskSortMode);
        Comparator<CollatedTaskEntry> comparator;

        if (taskComparator != null) {
            comparator = Comparator.comparing(CollatedTaskEntry::task, taskComparator)
                    .thenComparingInt(entry -> entry.scroll().getInvPosition())
                    .thenComparingInt(CollatedTaskEntry::taskIndex);
        }
        else {
            comparator = Comparator.comparingInt((CollatedTaskEntry entry) -> entry.scroll().getInvPosition())
                    .thenComparingInt(CollatedTaskEntry::taskIndex);
        }

        entries.sort(comparator);

        if (config.reverseTaskSort) {
            Collections.reverse(entries);
        }
    }

    private static String objectiveTypeKey(ClueTask task) {
        String[] parts = task.getFormattedObjective().split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    private static String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

    public void prevScroll() {
        // Scroll cycling is disabled in collated mode because all scrolls are shown at once.
        if (config.displayMode == ElementDisplayMode.COLLATED) {
            return;
        }

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
        // Scroll cycling is disabled in collated mode because all scrolls are shown at once.
        if (config.displayMode == ElementDisplayMode.COLLATED) {
            return;
        }

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
            if (scroll.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private void updateScroll(String uuid, CompoundTag scroll_data, int invPosition) {
        // Get the scroll with the given UUID from the list.
        ClueScroll scroll = null;
        for (ClueScroll s : scrolls) {
            if (s.getUuid().equals(uuid)) {
                scroll = s;
                break;
            }
        }


        if (scroll != null) {
            // Update the scroll's completion amount.
            for (int i = 0; i < scroll.getClueCount(); i++) {
                ClueTask clue = scroll.getClues().get(i);
                float completed = scroll_data.getFloat("ClueScrolls.clues." + i + ".completed").orElseThrow();
                clue.setCompleted((int) completed);
            }

            // Update the scroll's inventory position.
            scroll.setInvPosition(invPosition);

            // If the scroll is an extended scroll, update its expiry time.
            if (scroll.getTier().equals("extended")) {
                scroll.setExpire(RemoteDataFetcher.getExtendedExpiryTime());
            }
        }
    }

    private void addScroll(CompoundTag scrollData, int invPosition) {
        // Adds a new clue scroll to the list.
        ClueScroll newScroll = ClueScrollParser.parseScrollData(scrollData, invPosition);
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
