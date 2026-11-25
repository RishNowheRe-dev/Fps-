package com.silicon.fps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

/**
 * Lightweight diagnostic HUD. Displays the game's reported FPS and JVM heap use.
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
        Minecraft minecraft = Minecraft.getInstance();
        Runtime runtime = Runtime.getRuntime();

        int fps = minecraft.getFps();
        long usedBytes = runtime.totalMemory() - runtime.freeMemory();
        long maxBytes = runtime.maxMemory();

        String fpsText = "SILICON FPS  |  FPS: " + fps;
        String memoryText = "RAM: " + formatMiB(usedBytes) + " / " + formatMiB(maxBytes);

        graphics.drawString(minecraft.font, fpsText, 6, 6, 0xFFFFFFFF, true);
        graphics.drawString(minecraft.font, memoryText, 6, 18, 0xFFB8E0FF, true);
    }

    private static String formatMiB(long bytes) {
        return (bytes / (1024L * 1024L)) + " MiB";
    }
}
