package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import io.github.ueva.cluescrollhud.utils.DateTimeUtils;
import io.github.ueva.cluescrollhud.utils.TierColourUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.ArrayList;


public class ClueScrollRenderer {

    private static final int MARGIN = 5;
    private static final int PADDING = 10;
    private static final int SPACING = 5;
    private final ModConfig config;

    public ClueScrollRenderer(ModConfig config) {
        this.config = config;
    }

    public void render(DrawContext context, TextRenderer textRenderer) {
        render(context, textRenderer, null, 0, 0);
    }

    public void render(DrawContext context, TextRenderer textRenderer, ClueScroll selectedScroll, int selectedIndex,
                       int totalScrolls) {

        // Apply global scale and offset from config.
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(config.x, config.y, 0.0f);
        matrices.scale(config.globalScale, config.globalScale, 1.0f);

        // Render the currently selected scroll.
        if (totalScrolls > 0) {
            renderClueScrolls(context, textRenderer, selectedScroll, selectedIndex, totalScrolls);
        }
        // Render the "no scrolls" message.
        else {
            renderNoClueScrolls(context, textRenderer);
        }

        matrices.pop();
    }

    public void renderClueScrolls(DrawContext context, TextRenderer textRenderer, ClueScroll selectedScroll,
                                  int selectedIndex, int totalScrolls) {
        int maxTextWidth = measureMaxTextWidth(textRenderer, selectedScroll, selectedIndex, totalScrolls);

        int contentLeft = config.rightAlign ?
                (int) (context.getScaledWindowWidth() / config.globalScale) - (MARGIN + PADDING + maxTextWidth) :
                MARGIN + PADDING;

        renderClueScrollContent(
                context,
                textRenderer,
                selectedScroll,
                selectedIndex,
                totalScrolls,
                contentLeft,
                maxTextWidth
        );

    }

    private int measureMaxTextWidth(TextRenderer textRenderer, ClueScroll scroll, int selectedIndex, int totalScrolls) {
        float large = config.largeTextScale;
        float small = config.smallTextScale;
        int maxWidth = 0;

        // Scroll index
        String indexText = "Scroll " + (selectedIndex + 1) + " of " + totalScrolls;
        maxWidth = Math.max(maxWidth, (int) (textRenderer.getWidth(indexText) * small));

        // Tier name
        String tier = scroll.getTier();
        String tierText = tier.substring(0, 1)
                .toUpperCase() + tier.substring(1) + " Clue Scroll";
        maxWidth = Math.max(maxWidth, (int) (textRenderer.getWidth(tierText) * large));

        for (ClueTask clue : scroll.getClues()) {
            if (config.hideCompleted && clue.isCompleted()) {
                continue;
            }

            String objective = clue.getFormattedObjective() + ".";
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(objective));

            String progress = clue.isCompleted() ?
                    "Completed!" :
                    "Progress: " + clue.getCompleted() + "/" + clue.getAmount() + " (" + clue.getPercentCompleted() + "%)";
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(progress));
        }

        // Expiration
        String expireText = (scroll.getExpire() - System.currentTimeMillis()) > 0 ?
                "Expires in " + DateTimeUtils.formatDuration(scroll.getExpire() - System.currentTimeMillis()) :
                "Scroll expired!";
        maxWidth = Math.max(maxWidth, (int) (textRenderer.getWidth(expireText) * small));

        return maxWidth;
    }

    private void renderClueScrollContent(DrawContext context, TextRenderer textRenderer, ClueScroll selectedScroll,
                                         int selectedIndex, int totalScrolls, int contentLeft, int maxTextWidth) {
        MatrixStack matrices = context.getMatrices();

        float largeTextScale = config.largeTextScale;
        float smallTextScale = config.smallTextScale;

        int clueCount = selectedScroll.getClueCount();

        int cursorY = MARGIN + PADDING;

        // ─── Draw "Scroll X of Y" ──────────────────────────────────────────────
        matrices.push();
        matrices.scale(smallTextScale, smallTextScale, 1.0f);

        String scrollIndexText = "Scroll " + (selectedIndex + 1) + " of " + totalScrolls;
        Text text = Text.literal(scrollIndexText);
        int scaledX = (int) (contentLeft / smallTextScale);
        int scaledY = (int) (cursorY / smallTextScale);
        context.drawTextWithShadow(textRenderer, text, scaledX, scaledY, 0xFFFFFF);
        maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.getWidth(text) * smallTextScale));
        matrices.pop();

        cursorY += (int) (textRenderer.fontHeight * smallTextScale) + SPACING;

        // ─── Draw "<Tier> Clue Scroll" ────────────────────────────────────────
        matrices.push();
        matrices.scale(largeTextScale, largeTextScale, 1.0f);

        String tierName = selectedScroll.getTier()
                .substring(0, 1)
                .toUpperCase() + selectedScroll.getTier()
                .substring(1) + " Clue Scroll";
        text = Text.literal(tierName);
        scaledX = (int) (contentLeft / largeTextScale);
        scaledY = (int) (cursorY / largeTextScale);
        context.drawTextWithShadow(
                textRenderer,
                text,
                scaledX,
                scaledY,
                TierColourUtils.getColour(selectedScroll.getTier())
        );
        maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.getWidth(text) * largeTextScale));
        matrices.pop();

        cursorY += (int) (textRenderer.fontHeight * largeTextScale) + SPACING;


        // ─── Draw clues ────────────────────────────────────────────────────────
        ArrayList<ClueTask> sortedClues = selectedScroll.getSortedClues(config.taskSortMode, config.reverseTaskSort);
        for (int i = 0; i < clueCount; i++) {
            ClueTask clue = sortedClues.get(i);

            // Skip completed clues if the config option is enabled.
            if (config.hideCompleted && clue.isCompleted()) {
                continue;
            }

            // Task Objective.
            text = Text.literal(clue.getFormattedObjective() + ".");
            context.drawTextWithShadow(textRenderer, text, contentLeft, cursorY, 0xFFFFFF);
            maxTextWidth = Math.max(maxTextWidth, textRenderer.getWidth(text));
            cursorY += textRenderer.fontHeight;

            // Task Progress.
            if (clue.isCompleted()) {
                text = Text.literal("Completed!");
                context.drawTextWithShadow(textRenderer, text, contentLeft, cursorY, 0x55FF55);
            }
            else {
                String progress =
                        "Progress: " + clue.getCompleted() + "/" + clue.getAmount() + " (" + clue.getPercentCompleted() + "%)";
                text = Text.literal(progress);

                // Lerp progress colour between red (0xFF5555) and green (0x55FF55).
                if (config.colourByProgress) {
                    int progressColour =
                            ColorHelper.lerp((float) clue.getPercentCompleted() / 100.0f, 0xFF5555, 0x55FF55);
                    context.drawTextWithShadow(textRenderer, text, contentLeft, cursorY, progressColour);
                }
                // Use default colour (minecraft gold).
                else {
                    context.drawTextWithShadow(textRenderer, text, contentLeft, cursorY, 0xFFAA00);
                }


            }
            maxTextWidth = Math.max(maxTextWidth, textRenderer.getWidth(text));
            cursorY += textRenderer.fontHeight + SPACING;
        }

        // ─── Draw expiration ───────────────────────────────────────────────────
        matrices.push();
        matrices.scale(smallTextScale, smallTextScale, 1.0f);

        long timeLeft = selectedScroll.getExpire() - System.currentTimeMillis();
        // If there is time left, render time until expiration.
        if (timeLeft > 0) {
            String timeLeftText = "Expires in " + DateTimeUtils.formatDuration(timeLeft);
            text = Text.literal(timeLeftText);

            int color = timeLeft < 60 * 60 * 1000 ? 0xAA0000 : 0xAAAAAA; // Red if less than 1 hour, grey otherwise.
            scaledX = (int) (contentLeft / smallTextScale);
            scaledY = (int) (cursorY / smallTextScale);
            context.drawTextWithShadow(textRenderer, text, scaledX, scaledY, color);
            maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.getWidth(text) * smallTextScale));
        }
        // Otherwise, render expired message.
        else {
            String expiredText = "Scroll expired!";
            text = Text.literal(expiredText);
            scaledX = (int) (contentLeft / smallTextScale);
            scaledY = (int) (cursorY / smallTextScale);
            context.drawTextWithShadow(textRenderer, text, scaledX, scaledY, 0xAA0000);
            maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.getWidth(text) * smallTextScale));
        }

        matrices.pop();

        cursorY += (int) (textRenderer.fontHeight * smallTextScale);

        // ─── Draw Background ──────────────────────────────────────────────
        int backgroundRight = contentLeft + maxTextWidth + PADDING;
        int backgroundBottom = cursorY + PADDING;

        context.fill(contentLeft - PADDING, MARGIN, backgroundRight, backgroundBottom, 0x7F000000);

    }

    public void renderNoClueScrolls(DrawContext context, TextRenderer textRenderer) {
        if (config.hideWhenNoClue) {
            return;
        }

        String noClueScrollsText = "No clue scrolls in inventory :(";
        Text text = Text.literal(noClueScrollsText);

        // Measure the text width and height.
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        int contentLeft = config.rightAlign ?
                (int) (context.getScaledWindowWidth() / config.globalScale) - (MARGIN + PADDING + textWidth + PADDING) :
                MARGIN;

        // Render the background.
        context.fill(
                contentLeft,
                MARGIN,
                contentLeft + PADDING + textWidth + PADDING,
                MARGIN + PADDING + textHeight + PADDING,
                0,
                0x7F000000
        );

        // Draw the "no cluescrolls" message.
        context.drawTextWithShadow(textRenderer, text, contentLeft + PADDING, MARGIN + PADDING, 0xFFFFFF);
    }
}
