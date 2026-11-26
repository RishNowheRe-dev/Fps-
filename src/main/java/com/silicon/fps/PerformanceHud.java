package com.silicon.fps;

import com.silicon.fps.render.SiliconRenderEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

/**
 * Lightweight diagnostic HUD. Displays FPS, JVM heap use and render-pass timing.
 * Values are sampled only when the HUD is rendered; no background polling thread.
 */
public final class PerformanceHud {
    private PerformanceHud() {
    }

    public static void register() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(SiliconFpsClient.MOD_ID, "performance_hud"),
                PerformanceHud::render
        );
    }

    private static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        if (!PerformanceConfig.showPerformanceHud) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Runtime runtime = Runtime.getRuntime();

        int fps = minecraft.getFps();
        long usedBytes = runtime.totalMemory() - runtime.freeMemory();
        long maxBytes = runtime.maxMemory();

        String fpsText = "SILICON FPS  |  FPS: " + fps;
        String memoryText = "RAM: " + formatMiB(usedBytes) + " / " + formatMiB(maxBytes);
        String renderText = SiliconRenderEngine.isInitialized()
                ? String.format(java.util.Locale.ROOT, "WORLD PASS: %.2f ms avg",
                        SiliconRenderEngine.getAverageMainPassMillis())
                : "WORLD PASS: initializing";

        graphics.drawString(minecraft.font, fpsText, 6, 6, 0xFFFFFFFF, true);
        graphics.drawString(minecraft.font, memoryText, 6, 18, 0xFFB8E0FF, true);
        graphics.drawString(minecraft.font, renderText, 6, 30, 0xFF7FE7D5, true);
    }

    private static String formatMiB(long bytes) {
        return (bytes / (1024L * 1024L)) + " MiB";
    }
}
