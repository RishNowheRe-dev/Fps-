package com.silicon.fps;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

/**
 * Draws SILICON's shared night landscape and restrained monochrome glass panels.
 * The scene is rendered with lightweight GUI primitives so it needs no external
 * texture pack, network request, or additional runtime dependency.
 */
public final class SiliconScreenTheme {
    private SiliconScreenTheme() {}

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof TitleScreen) && !(screen instanceof PauseScreen)) return;
            ScreenEvents.afterBackground(screen).register(
                    (current, graphics, mouseX, mouseY, tickDelta) -> renderThemeLayer(current, graphics)
            );
        });
    }

    private static void renderThemeLayer(Screen screen, GuiGraphics g) {
        final int w = screen.width;
        final int h = screen.height;
        drawNightSky(g, w, h);
        drawStars(g, w, h);
        drawMoon(g, w, h);
        drawMountains(g, w, h);
        drawAtmosphere(g, w, h);

        // Subtle charcoal glass surface; the landscape remains visible through it.
        int panelWidth = Math.min(screen instanceof TitleScreen ? 470 : 520, w - 28);
        int panelHeight = Math.min(screen instanceof TitleScreen ? 310 : 250, h - 24);
        if (panelWidth > 0 && panelHeight > 0) {
            int left = (w - panelWidth) / 2;
            int top = (h - panelHeight) / 2;
            roundedPanel(g, left, top, panelWidth, panelHeight, 12, 0xA8171A20);
            roundedPanel(g, left + 1, top + 1, panelWidth - 2, panelHeight - 2, 11, 0x5510141B);
            g.fill(left + 20, top + 12, left + panelWidth - 20, top + 13, 0xAABFC5D0);
            g.fill(left + 20, top + panelHeight - 13, left + panelWidth - 20,
                    top + panelHeight - 12, 0x557F8998);
        }
    }

    private static void drawNightSky(GuiGraphics g, int w, int h) {
        int[] stops = {0xFF080D19, 0xFF111B2B, 0xFF202B3A, 0xFF303744};
        for (int y = 0; y < h; y++) {
            float t = h <= 1 ? 0 : (float) y / (h - 1);
            float p = t * (stops.length - 1);
            int i = Math.min(stops.length - 2, (int) p);
            float f = p - i;
            g.fill(0, y, w, y + 1, mix(stops[i], stops[i + 1], f));
        }
    }

    private static void drawStars(GuiGraphics g, int w, int h) {
        // Fixed deterministic star field: no random allocations during rendering.
        for (int i = 0; i < 92; i++) {
            int x = Math.floorMod(i * 197 + 31, Math.max(1, w));
            int y = Math.floorMod(i * 83 + 17, Math.max(1, h * 2 / 3));
            int size = i % 13 == 0 ? 2 : 1;
            int alpha = i % 4 == 0 ? 0xD8 : 0x8A;
            int color = (alpha << 24) | 0x00E1E8F0;
            g.fill(x, y, Math.min(w, x + size), Math.min(h, y + size), color);
        }
    }

    private static void drawMoon(GuiGraphics g, int w, int h) {
        int cx = w * 3 / 4;
        int cy = Math.max(34, h / 4);
        int radius = Math.max(13, Math.min(27, h / 8));
        for (int row = -radius; row <= radius; row++) {
            int span = (int) Math.sqrt(radius * radius - row * row);
            g.fill(cx - span, cy + row, cx + span + 1, cy + row + 1, 0xFFE0E3E5);
            int glow = Math.max(1, radius / 5);
            if (span > radius - glow) {
                g.fill(cx - span - 1, cy + row, cx - span, cy + row + 1, 0x229BAFC6);
                g.fill(cx + span, cy + row, cx + span + 1, cy + row + 1, 0x229BAFC6);
            }
        }
        // Soft halo bands around the moon.
        g.fill(cx - radius - 5, cy - radius - 5, cx + radius + 6, cy - radius - 4, 0x189BAFC6);
        g.fill(cx - radius - 5, cy + radius + 4, cx + radius + 6, cy + radius + 5, 0x189BAFC6);
    }

    private static void drawMountains(GuiGraphics g, int w, int h) {
        int ridge = h * 2 / 3;
        drawRidge(g, w, h, ridge, 0xFF182333, 0);
        drawRidge(g, w, h, ridge + Math.max(12, h / 13), 0xFF111A27, 1);
        // Foreground foothills.
        for (int y = h * 3 / 4; y < h; y++) {
            int wave = (int) (Math.sin((double) y / Math.max(1, h) * 8.0) * (w / 18.0));
            int start = Math.max(0, w / 2 + wave - (y - h * 3 / 4) * 2);
            g.fill(start, y, w, y + 1, 0xFF0B121D);
        }
    }

    private static void drawRidge(GuiGraphics g, int w, int h, int base, int color, int phase) {
        for (int x = 0; x < w; x++) {
            double a = Math.sin((x + phase * 37.0) / Math.max(18.0, w / 8.0));
            double b = Math.sin((x + 73.0 + phase * 19.0) / Math.max(12.0, w / 15.0));
            double c = Math.sin((x + 151.0) / Math.max(10.0, w / 23.0));
            int peak = base - (int) ((0.10 + 0.055 * a + 0.035 * b + 0.02 * c) * h);
            peak = Math.max(0, Math.min(h, peak));
            g.fill(x, peak, x + 1, h, color);
        }
    }

    private static void drawAtmosphere(GuiGraphics g, int w, int h) {
        int mistY = h * 3 / 4;
        for (int i = 0; i < 5; i++) {
            int y = mistY + i * Math.max(2, h / 35);
            g.fill(0, y, w, y + 1, 0x0ECCD5E0);
        }
        g.fill(0, h - 2, w, h, 0xFF080D15);
    }

    private static int mix(int a, int b, float t) {
        int aa = (a >>> 24) & 255, ar = (a >>> 16) & 255, ag = (a >>> 8) & 255, ab = a & 255;
        int ba = (b >>> 24) & 255, br = (b >>> 16) & 255, bg = (b >>> 8) & 255, bb = b & 255;
        int ca = aa + Math.round((ba - aa) * t);
        int cr = ar + Math.round((br - ar) * t);
        int cg = ag + Math.round((bg - ag) * t);
        int cb = ab + Math.round((bb - ab) * t);
        return (ca << 24) | (cr << 16) | (cg << 8) | cb;
    }

    private static void roundedPanel(GuiGraphics g, int x, int y, int width, int height, int radius, int color) {
        if (width <= 0 || height <= 0) return;
        int r = Math.max(1, Math.min(radius, Math.min(width / 2, height / 2)));
        for (int row = 0; row < height; row++) {
            int inset = 0;
            if (row < r) {
                inset = r - 1 - (int) Math.floor(Math.sqrt(Math.max(0, r * r - (r - row) * (r - row))));
            } else if (row >= height - r) {
                int fromBottom = height - 1 - row;
                inset = r - 1 - (int) Math.floor(Math.sqrt(Math.max(0, r * r - (r - fromBottom) * (r - fromBottom))));
            }
            g.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }
}
