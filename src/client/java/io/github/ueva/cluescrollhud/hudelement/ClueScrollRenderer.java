package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.config.ModConfig;
import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;
import io.github.ueva.cluescrollhud.utils.DateTimeUtils;
import io.github.ueva.cluescrollhud.utils.TierColourUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;


public class ClueScrollRenderer {

    private static final int MARGIN = 5;
    private static final int PADDING = 10;
    private static final int SPACING = 5;
    private final ModConfig config;

    public ClueScrollRenderer(ModConfig config) {
        this.config = config;
    }

    public void render(GuiGraphics context, Font textRenderer) {
        render(context, textRenderer, null, 0, 0);
    }

    public void render(GuiGraphics context, Font textRenderer, ClueScroll selectedScroll, int selectedIndex,
                       int totalScrolls) {

        // Apply global scale and offset from config.
        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        matrices.translate(config.x, config.y);
        matrices.scale(config.globalScale, config.globalScale);

        // Render the currently selected scroll.
        if (totalScrolls > 0) {
            renderClueScrolls(context, textRenderer, selectedScroll, selectedIndex, totalScrolls);
        }
        // Render the "no scrolls" message.
        else {
            renderNoClueScrolls(context, textRenderer);
        }

        matrices.popMatrix();
    }

    public void renderCollated(GuiGraphics context, Font textRenderer, List<CollatedTaskEntry> collatedTasks,
                               int totalScrolls) {

        // Apply global scale and offset from config.
        Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        matrices.translate(config.x, config.y);
        matrices.scale(config.globalScale, config.globalScale);

        // Render all scroll tasks in a single collated panel.
        if (totalScrolls > 0) {
            renderCollatedContent(context, textRenderer, collatedTasks, totalScrolls);
        }
        // Render the "no scrolls" message.
        else {
            renderNoClueScrolls(context, textRenderer);
        }

        matrices.popMatrix();
    }

    public void renderClueScrolls(GuiGraphics context, Font textRenderer, ClueScroll selectedScroll,
                                  int selectedIndex, int totalScrolls) {
        int maxTextWidth = measureMaxTextWidth(textRenderer, selectedScroll, selectedIndex, totalScrolls);

        int contentLeft = config.rightAlign ?
                (int) (context.guiWidth() / config.globalScale) - (MARGIN + PADDING + maxTextWidth) :
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

    private void renderCollatedContent(GuiGraphics context, Font textRenderer, List<CollatedTaskEntry> collatedTasks,
                                       int totalScrolls) {
        int maxTextWidth = measureCollatedMaxTextWidth(textRenderer, collatedTasks, totalScrolls);
        int contentLeft = config.rightAlign ?
                (int) (context.guiWidth() / config.globalScale) - (MARGIN + PADDING + maxTextWidth) :
                MARGIN + PADDING;

        // Measure bounds and draw background.
        int contentTop = MARGIN + PADDING;
        int contentHeight = measureCollatedTotalHeight(textRenderer, collatedTasks);
        int backgroundRight = contentLeft + maxTextWidth + PADDING;
        int backgroundBottom = contentTop + contentHeight + PADDING;

        context.fill(contentLeft - PADDING, MARGIN, backgroundRight, backgroundBottom, 0x7F000000);

        Matrix3x2fStack matrices = context.pose();
        float smallTextScale = config.smallTextScale;
        int cursorY = MARGIN + PADDING;

        // Draw "N Clue Scrolls".
        matrices.pushMatrix();
        matrices.scale(smallTextScale, smallTextScale);

        String headerText = totalScrolls + " Clue Scrolls";
        Component text = Component.literal(headerText);
        int scaledX = (int) (contentLeft / smallTextScale);
        int scaledY = (int) (cursorY / smallTextScale);
        context.drawString(textRenderer, text, scaledX, scaledY, 0xFFFFFFFF);
        matrices.popMatrix();

        cursorY += (int) (textRenderer.lineHeight * smallTextScale) + SPACING;

        // Draw clues from all scrolls.
        for (CollatedTaskEntry entry : collatedTasks) {
            // Omit the tier prefix when the same objective appears on scrolls of different tiers.
            String tierPrefix = entry.spansMultipleTiers() ? null : formatTierPrefix(entry.scroll().getTier());
            int tierColour = entry.spansMultipleTiers() ? 0xFFFFFFFF : TierColourUtils.getColour(entry.scroll().getTier());
            cursorY = renderTaskRow(context, textRenderer, entry.task(), contentLeft, cursorY, tierPrefix, tierColour);
        }
    }

    private int measureMaxTextWidth(Font textRenderer, ClueScroll scroll, int selectedIndex, int totalScrolls) {
        float large = config.largeTextScale;
        float small = config.smallTextScale;
        int maxWidth = 0;

        // Scroll index
        String indexText = "Scroll " + (selectedIndex + 1) + " of " + totalScrolls;
        maxWidth = Math.max(maxWidth, (int) (textRenderer.width(indexText) * small));

        // Tier name
        String tier = scroll.getTier();
        String tierText = tier.substring(0, 1).toUpperCase() + tier.substring(1) + " Clue Scroll";
        maxWidth = Math.max(maxWidth, (int) (textRenderer.width(tierText) * large));

        for (ClueTask clue : scroll.getClues()) {
            if (config.hideCompleted && clue.isCompleted()) {
                continue;
            }

            maxWidth = Math.max(maxWidth, measureTaskRowWidth(textRenderer, clue, null));
        }

        // Expiration
        String expireText = (scroll.getExpire() - System.currentTimeMillis()) > 0 ?
                "Expires in " + DateTimeUtils.formatDuration(scroll.getExpire() - System.currentTimeMillis()) :
                "Scroll expired!";
        maxWidth = Math.max(maxWidth, (int) (textRenderer.width(expireText) * small));

        return maxWidth;
    }

    private int measureCollatedMaxTextWidth(Font textRenderer, List<CollatedTaskEntry> collatedTasks,
                                            int totalScrolls) {
        float small = config.smallTextScale;
        int maxWidth = (int) (textRenderer.width(totalScrolls + " Clue Scrolls") * small);

        for (CollatedTaskEntry entry : collatedTasks) {
            String tierPrefix = entry.spansMultipleTiers() ? null : formatTierPrefix(entry.scroll().getTier());
            maxWidth = Math.max(maxWidth, measureTaskRowWidth(textRenderer, entry.task(), tierPrefix));
        }

        return maxWidth;
    }

    private int measureTaskRowWidth(Font textRenderer, ClueTask clue, String tierPrefix) {
        int maxWidth = 0;

        String objective = (tierPrefix != null ? tierPrefix : "") + clue.getFormattedObjective() + ".";
        maxWidth = Math.max(maxWidth, textRenderer.width(objective));

        String progress = clue.isCompleted() ?
                "Completed!" :
                "Progress: " + clue.getCompleted() + "/" + clue.getAmount() + " (" + clue.getPercentCompleted() + "%)";
        maxWidth = Math.max(maxWidth, textRenderer.width(progress));

        return maxWidth;
    }

    private int measureTotalHeight(Font textRenderer, ClueScroll scroll) {
        float large = config.largeTextScale;
        float small = config.smallTextScale;

        int height = 0;
        height += (int) (textRenderer.lineHeight * small);  // "Scroll X of Y"
        height += SPACING;
        height += (int) (textRenderer.lineHeight * large);  // "<Tier> Clue Scroll"
        height += SPACING;

        for (ClueTask clue : scroll.getClues()) {
            if (config.hideCompleted && clue.isCompleted()) {
                continue;
            }
            height += textRenderer.lineHeight;               // objective
            height += textRenderer.lineHeight;               // progress/completed
            height += SPACING;
        }

        height += (int) (textRenderer.lineHeight * small);  // expiration line
        // (no trailing SPACING after expiry in your current layout)
        return height;
    }

    private int measureCollatedTotalHeight(Font textRenderer, List<CollatedTaskEntry> collatedTasks) {
        float small = config.smallTextScale;

        int height = (int) (textRenderer.lineHeight * small);  // "N Clue Scrolls"
        height += SPACING;

        height += collatedTasks.size() * (2 * textRenderer.lineHeight + SPACING);

        return height;
    }

    private void renderClueScrollContent(GuiGraphics context, Font textRenderer, ClueScroll selectedScroll,
                                         int selectedIndex, int totalScrolls, int contentLeft, int maxTextWidth) {
        Matrix3x2fStack matrices = context.pose();

        // --- Measure bounds and draw background --------------------------------
        int contentTop = MARGIN + PADDING;
        int contentHeight = measureTotalHeight(textRenderer, selectedScroll);
        int backgroundLeft = contentLeft - PADDING;
        int backgroundTop = MARGIN;
        int backgroundRight = contentLeft + maxTextWidth + PADDING;
        int backgroundBottom = contentTop + contentHeight + PADDING;

        context.fill(contentLeft - PADDING, MARGIN, backgroundRight, backgroundBottom, 0x7F000000);

        float largeTextScale = config.largeTextScale;
        float smallTextScale = config.smallTextScale;

        int clueCount = selectedScroll.getClueCount();

        int cursorY = MARGIN + PADDING;

        // ─── Draw "Scroll X of Y" ──────────────────────────────────────────────
        matrices.pushMatrix();
        matrices.scale(smallTextScale, smallTextScale);

        String scrollIndexText = "Scroll " + (selectedIndex + 1) + " of " + totalScrolls;
        Component text = Component.literal(scrollIndexText);
        int scaledX = (int) (contentLeft / smallTextScale);
        int scaledY = (int) (cursorY / smallTextScale);
        context.drawString(textRenderer, text, scaledX, scaledY, 0xFFFFFFFF);
        maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.width(text) * smallTextScale));
        matrices.popMatrix();

        cursorY += (int) (textRenderer.lineHeight * smallTextScale) + SPACING;

        // ─── Draw "<Tier> Clue Scroll" ────────────────────────────────────────
        matrices.pushMatrix();
        matrices.scale(largeTextScale, largeTextScale);

        String tierName = selectedScroll.getTier().substring(0, 1).toUpperCase() + selectedScroll.getTier()
                                                                                                 .substring(1) + " " + "Clue Scroll";
        text = Component.literal(tierName);
        scaledX = (int) (contentLeft / largeTextScale);
        scaledY = (int) (cursorY / largeTextScale);
        context.drawString(textRenderer, text, scaledX, scaledY, TierColourUtils.getColour(selectedScroll.getTier()));
        maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.width(text) * largeTextScale));
        matrices.popMatrix();

        cursorY += (int) (textRenderer.lineHeight * largeTextScale) + SPACING;


        // ─── Draw clues ────────────────────────────────────────────────────────
        ArrayList<ClueTask> sortedClues = selectedScroll.getSortedClues(config.taskSortMode, config.reverseTaskSort);
        for (int i = 0; i < clueCount; i++) {
            ClueTask clue = sortedClues.get(i);

            // Skip completed clues if the config option is enabled.
            if (config.hideCompleted && clue.isCompleted()) {
                continue;
            }

            cursorY = renderTaskRow(context, textRenderer, clue, contentLeft, cursorY, null, 0xFFFFFFFF);
            maxTextWidth = Math.max(maxTextWidth, measureTaskRowWidth(textRenderer, clue, null));
        }

        // ─── Draw expiration ───────────────────────────────────────────────────
        matrices.pushMatrix();
        matrices.scale(smallTextScale, smallTextScale);

        long timeLeft = selectedScroll.getExpire() - System.currentTimeMillis();
        // If there is time left, render time until expiration.
        if (timeLeft > 0) {
            String timeLeftText = "Expires in " + DateTimeUtils.formatDuration(timeLeft);
            text = Component.literal(timeLeftText);

            int color = timeLeft < 60 * 60 * 1000 ? 0xFFAA0000 : 0xFFAAAAAA; // Red if less than 1 hour, grey otherwise.
            scaledX = (int) (contentLeft / smallTextScale);
            scaledY = (int) (cursorY / smallTextScale);
            context.drawString(textRenderer, text, scaledX, scaledY, color);
            maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.width(text) * smallTextScale));
        }
        // Otherwise, render expired message.
        else {
            String expiredText = "Scroll expired!";
            text = Component.literal(expiredText);
            scaledX = (int) (contentLeft / smallTextScale);
            scaledY = (int) (cursorY / smallTextScale);
            context.drawString(textRenderer, text, scaledX, scaledY, 0xFFAA0000);
            maxTextWidth = Math.max(maxTextWidth, (int) (textRenderer.width(text) * smallTextScale));
        }

        matrices.popMatrix();

    }

    /**
     * Renders a single clue task row (objective + progress/completed lines).
     * When {@code tierPrefix} is provided, it is drawn in {@code tierColour} before the objective text.
     */
    private int renderTaskRow(GuiGraphics context, Font textRenderer, ClueTask clue, int contentLeft, int cursorY,
                              String tierPrefix, int tierColour) {
        Component text;

        // Task Objective.
        if (tierPrefix != null && !tierPrefix.isEmpty()) {
            text = Component.literal(tierPrefix);
            context.drawString(textRenderer, text, contentLeft, cursorY, tierColour);
            int prefixWidth = textRenderer.width(text);
            text = Component.literal(clue.getFormattedObjective() + ".");
            context.drawString(textRenderer, text, contentLeft + prefixWidth, cursorY, 0xFFFFFFFF);
        }
        else {
            text = Component.literal(clue.getFormattedObjective() + ".");
            context.drawString(textRenderer, text, contentLeft, cursorY, 0xFFFFFFFF);
        }

        cursorY += textRenderer.lineHeight;

        // Task Progress.
        if (clue.isCompleted()) {
            text = Component.literal("Completed!");
            context.drawString(textRenderer, text, contentLeft, cursorY, 0xFF55FF55);
        }
        else {
            String progress =
                    "Progress: " + clue.getCompleted() + "/" + clue.getAmount() + " (" + clue.getPercentCompleted() + "%)";
            text = Component.literal(progress);

            // Lerp progress colour between red (0xFF5555) and green (0x55FF55).
            if (config.colourByProgress) {
                int progressColour =
                        ARGB.srgbLerp((float) clue.getPercentCompleted() / 100.0f, 0xFFFF5555, 0xFF55FF55);
                context.drawString(textRenderer, text, contentLeft, cursorY, progressColour);
            }
            // Use default colour (minecraft gold).
            else {
                context.drawString(textRenderer, text, contentLeft, cursorY, 0xFFFFAA00);
            }
        }

        return cursorY + textRenderer.lineHeight + SPACING;
    }

    /** Formats a scroll tier as a collated-mode label prefix, e.g. "[Easy] ". */
    private String formatTierPrefix(String tier) {
        String capitalized = tier.substring(0, 1).toUpperCase() + tier.substring(1);
        return "[" + capitalized + "] ";
    }

    public void renderNoClueScrolls(GuiGraphics context, Font textRenderer) {
        if (config.hideWhenNoClue) {
            return;
        }

        String noClueScrollsText = "No clue scrolls in inventory :(";
        Component text = Component.literal(noClueScrollsText);

        // Measure the text width and height.
        int textWidth = textRenderer.width(text);
        int textHeight = textRenderer.lineHeight;

        int contentLeft = config.rightAlign ?
                (int) (context.guiWidth() / config.globalScale) - (MARGIN + PADDING + textWidth + PADDING) :
                MARGIN;

        // Render the background.
        context.fill(
                contentLeft,
                MARGIN,
                contentLeft + PADDING + textWidth + PADDING,
                MARGIN + PADDING + textHeight + PADDING,
                0x7F000000
        );

        // Draw the "no cluescrolls" message.
        context.drawString(textRenderer, text, contentLeft + PADDING, MARGIN + PADDING, 0xFFFFFFFF);
    }
}
