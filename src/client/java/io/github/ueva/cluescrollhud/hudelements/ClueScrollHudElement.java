package io.github.ueva.cluescrollhud.hudelements;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class ClueScrollHudElement {

    public static final Identifier CLUESCROLL_HUD_LAYER = Identifier.of(VgClueScrollHUD.MOD_ID, "cluescroll_hud_layer");

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final int PADDING = 10;
    private static float scale = 0.25f;

    private static boolean isVisible = true;
    private static int selectedClueScrollIndex = 0;

    private static ArrayList<ClueScroll> scrolls = new ArrayList<>();

    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        // Obtain the client instance.
        MinecraftClient client = MinecraftClient.getInstance();

        // Check whether the F3 debug screen is visible.
        boolean isDebugScreenVisible = MinecraftClient.getInstance()
                .getDebugHud()
                .shouldShowDebugHud();

        // Check whether the HUD is currently hidden.
        boolean isHudHidden = client.options.hudHidden;

        // Update the local clue scroll list.
        updateScrollList(client);

        // Render the ClueScrollHudElement if it's enabled, the HUD is visible, and the F3 debug screen is not visible.
        if (isVisible && !isDebugScreenVisible && !isHudHidden) {
            // Obtain  and text renderer and matrix stack instances.
            TextRenderer textRenderer = client.textRenderer;

            // Count the number of clue scrolls in the player's inventory.
            int clueScrollCount = scrolls.size();

            // If there are no clue scrolls in the player's inventory, render the no clue scrolls message.
            if (clueScrollCount == 0) {
                render_no_clue_scrolls(context, textRenderer);
                selectedClueScrollIndex = 0;
            }
            // Otherwise, render information about the selected clue scroll.
            else {
                // Check that the selected clue scroll index is within bounds.
                if (selectedClueScrollIndex >= clueScrollCount) {
                    selectedClueScrollIndex = clueScrollCount - 1;
                }
                else if (selectedClueScrollIndex < 0) {
                    selectedClueScrollIndex = 0;
                }

                render_clue_scroll(context, textRenderer, clueScrollCount);
            }
        }
    }

    public static void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("Toggled visibility of ClueScrollHudElement to: {}", isVisible);
    }

    public static void prev_scroll() {
        int clueScrollCount = getClueScrollCount(MinecraftClient.getInstance());

        // If there are no clue scrolls in the player's inventory, return.
        if (clueScrollCount == 0) {
            return;
        }

        // Decrement the selected clue scroll index, wrapping around to the last clue scroll if necessary.
        selectedClueScrollIndex = (selectedClueScrollIndex - 1 + clueScrollCount) % clueScrollCount;

        LOGGER.info("Selected previous clue scroll ({} of {}).", selectedClueScrollIndex + 1, clueScrollCount);
    }

    public static void next_scroll() {
        int clueScrollCount = getClueScrollCount(MinecraftClient.getInstance());

        // If there are no clue scrolls in the player's inventory, return.
        if (clueScrollCount == 0) {
            return;
        }

        // Increment the selected clue scroll index, wrapping around to the first clue scroll if necessary.
        selectedClueScrollIndex = (selectedClueScrollIndex + 1) % clueScrollCount;

        LOGGER.info("Selected next clue scroll ({} of {}).", selectedClueScrollIndex + 1, clueScrollCount);
    }

    private static int getClueScrollCount(MinecraftClient client) {
        int clueScrollCount = 0;

        // Ensure the client and the player are not null.
        if (client == null || client.player == null) {
            return clueScrollCount;
        }

        // Get the player's inventory.
        PlayerInventory inventory = client.player.getInventory();

        // Loop through all the player's inventory slots.
        for (int i = 0; i < inventory.size(); i++) {
            // Get the item stack in the current slot.
            ItemStack itemStack = inventory.getStack(i);

            // Check if the item stack is not empty.
            if (!itemStack.isEmpty()) {
                // Check if the item stack has a custom_data tag.
                if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
                    // Get the custom_data tag from the item stack.
                    NbtComponent customData = itemStack.get(DataComponentTypes.CUSTOM_DATA);

                    // Check if the custom_data tag has a ClueScrolls.uuid key.
                    if (customData != null && customData.contains("ClueScrolls.uuid")) {
                        clueScrollCount++;
                    }
                }
            }
        }

        return clueScrollCount;
    }

    private static void render_no_clue_scrolls(DrawContext context, TextRenderer textRenderer) {

        String noClueScrollsText = "No clue scrolls in inventory :(";
        Text text = Text.literal(noClueScrollsText);

        // Measure the text width and height.
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        // Render the background.
        context.fill(PADDING, PADDING, PADDING + 5 + textWidth + 5, PADDING + 5 + textHeight + 5, 0, 0x7F000000);

        // Draw the "no cluescrolls" message.
        context.drawTextWithShadow(textRenderer, text, PADDING + 5, PADDING + 5, 0xFFFFFF);
    }

    private static void render_clue_scroll(DrawContext context, TextRenderer textRenderer, int clueScrollCount) {
        // Render the background.
        context.fill(PADDING, PADDING, 150, 210, 0, 0x7F000000);

        // Draw the number of clue scrolls in the player's inventory.
        String clueScrollCountText = "Clue Scrolls: " + clueScrollCount;
        Text text = Text.literal(clueScrollCountText);
        context.drawTextWithShadow(textRenderer, text, PADDING + 5, PADDING + 5, 0xFFFFFF);

        // Draw the selected clue scroll index.
        String selectedClueScrollIndexText =
                "Selected Clue Scroll: " + (selectedClueScrollIndex + 1) + " of " + clueScrollCount;
        text = Text.literal(selectedClueScrollIndexText);
        context.drawTextWithShadow(textRenderer, text, PADDING + 5, PADDING + 20 + textRenderer.fontHeight, 0xFFFFFF);
    }

    private static void updateScrollList(MinecraftClient client) {

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
                        if (!isClueScrollInList(uuid)) {
                            addClueScrollToList(scrollData);
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

    private static boolean isClueScrollInList(String uuid) {
        // Check if any scrolls in the list have the same UUID as the one passed in.
        for (ClueScroll scroll : scrolls) {
            if (scroll.getUuid()
                    .equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private static void addClueScrollToList(NbtCompound scroll_data) {
        // Add the clue scroll to the list.

        // Extract the scroll's UUID, tier, created time, and expiration time.
        String uuid = scroll_data.getString("ClueScrolls.uuid");
        String tier = scroll_data.getString("ClueScrolls.tier");
        long created = scroll_data.getLong("ClueScrolls.created");
        long expire = scroll_data.getLong("ClueScrolls.expire");

        // Extract the clues from the NBT data.
        ArrayList<ClueTask> clues = new ArrayList<>();
        int n = 0;
        String baseKeyFormat = "ClueScrolls.clues.%d.%s";

        while (scroll_data.contains(String.format(baseKeyFormat, n, "objective"))) {
            String objective = scroll_data.getString(String.format(baseKeyFormat, n, "objective"));
            int amount = (int) scroll_data.getFloat(String.format(baseKeyFormat, n, "amount"));
            int completed = (int) scroll_data.getFloat(String.format(baseKeyFormat, n, "completed"));

            // Create a new ClueTask and add it to the list.
            clues.add(new ClueTask(objective, amount, completed));
            n++;
        }

        // Create a new ClueScroll object and add it to the list.
        ClueScroll newScroll = new ClueScroll(uuid, tier, created, expire, clues);
        scrolls.add(newScroll);
        LOGGER.info("Added new clue scroll to list: {}", uuid);
    }
}

