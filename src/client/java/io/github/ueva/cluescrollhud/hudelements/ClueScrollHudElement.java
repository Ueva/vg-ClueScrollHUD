package io.github.ueva.cluescrollhud.hudelements;

import io.github.ueva.cluescrollhud.VgClueScrollHUD;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClueScrollHudElement {

    public static final Identifier CLUESCROLL_HUD_LAYER = Identifier.of(VgClueScrollHUD.MOD_ID, "cluescroll_hud_layer");

    private static final Logger LOGGER = LoggerFactory.getLogger(VgClueScrollHUD.MOD_ID);
    private static final int PADDING = 10;
    private static boolean isVisible = true;

    public static void example_render(DrawContext context, RenderTickCounter tickCounter) {
        int red = 0xFFFF0000;
        int green = 0xFF00FF00;

        double currentTime = Util.getMeasuringTimeMs() / 1000.0;

        float lerpedAmount = MathHelper.abs(MathHelper.sin((float) currentTime));
        int lerpedColour = ColorHelper.lerp(lerpedAmount, red, green);

        context.fill(0, 0, 20, 20, 0, lerpedColour);
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        // Draw a translucent dark beige rectangle on the top-right of the screen.
        // Width: 180, Height: 300
        // Padding: 10
        if (isVisible) {
            context.fill(PADDING, PADDING, 150, 210, 0, 0x7F000000);
        }
    }

    public static void toggleVisibility() {
        isVisible = !isVisible;
        LOGGER.info("Toggled visibility of ClueScrollHudElement to: " + isVisible);
    }
}
