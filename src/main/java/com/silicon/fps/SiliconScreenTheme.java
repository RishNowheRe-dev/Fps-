package com.silicon.fps;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.PauseScreen;

/** Applies the SILICON backdrop only to the two screens with designed panels. */
public final class SiliconScreenTheme {
    private static final int LAVENDER = 0x887F5AA8;

    private SiliconScreenTheme() {
    }

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof TitleScreen) && !(screen instanceof PauseScreen)) return;
            ScreenEvents.afterBackground(screen).register(
                    (current, graphics, mouseX, mouseY, tickDelta) -> renderThemeLayer(current, graphics)
            );
        });
    }

    private static void renderThemeLayer(Screen screen, GuiGraphics graphics) {
        int width = screen.width;
        int height = screen.height;
        graphics.fill(0, 0, width, height, 0x35000000);

        int panelWidth = Math.min(520, width - 32);
        int panelHeight = Math.min(screen instanceof TitleScreen ? 300 : 250, height - 36);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        roundedPanel(graphics, left, top, panelWidth, panelHeight, 12, LAVENDER);
        roundedPanel(graphics, left + 1, top + 1, panelWidth - 2,
                panelHeight - 2, 11, 0xA9000000);
        graphics.fill(left + 18, top + 12, left + panelWidth - 18, top + 13, 0xAA9F79D2);
    }

    private static void roundedPanel(GuiGraphics graphics, int x, int y, int width,
                                     int height, int radius, int color) {
        if (width <= 0 || height <= 0) return;
        int r = Math.max(1, Math.min(radius, Math.min(width / 2, height / 2)));
        for (int row = 0; row < height; row++) {
            int inset = 0;
            if (row < r) {
                inset = r - 1 - (int) Math.floor(Math.sqrt(
                        Math.max(0, r * r - (r - row) * (r - row))));
            } else if (row >= height - r) {
                int fromBottom = height - 1 - row;
                inset = r - 1 - (int) Math.floor(Math.sqrt(
                        Math.max(0, r * r - (r - fromBottom) * (r - fromBottom))));
            }
            graphics.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }
}
