package io.github.ueva.cluescrollhud.hudelements;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClueScrollHudElement {

    public static final Identifier CLUESCROLL_HUD_LAYER = Identifier.of(VgClueScrollHUD.MOD_ID, "cluescroll_hud_layer");

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final int PADDING = 10;
    private static boolean isVisible = true;

    private static int selectedClueScrollIndex = 0;

    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        // Obtain the client instance.
        MinecraftClient client = MinecraftClient.getInstance();

        // Check whether the F3 debug screen is visible.
        boolean isDebugScreenVisible = MinecraftClient.getInstance()
                .getDebugHud()
                .shouldShowDebugHud();

        // Check whether the HUD is currently hidden.
        boolean isHudHidden = client.options.hudHidden;

        // Render the ClueScrollHudElement if it's enabled, the HUD is visible, and the F3 debug screen is not visible.
        if (isVisible && !isDebugScreenVisible && !isHudHidden) {
            // Obtain  and text renderer instances.
            TextRenderer textRenderer = client.textRenderer;

            // Count the number of clue scrolls in the player's inventory.
            int clueScrollCount = getClueScrollCount(client);

            // If there are no clue scrolls in the player's inventory, render the no clue scrolls message.
            if (clueScrollCount == 0) {
                render_no_clue_scrolls(context, textRenderer);
                selectedClueScrollIndex = 0;
            }
            // Otherwise, render information about the selected clue scroll.
            else {
                render_clue_scroll(context, textRenderer, clueScrollCount);
            }
        }
    }

    public static void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("Toggled visibility of ClueScrollHudElement to: " + isVisible);
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

                    // Check if the custom_data tag has a ClueScrolls.tier key.
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
    }
}
