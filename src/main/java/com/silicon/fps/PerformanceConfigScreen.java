package com.silicon.fps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * SILICON CONTROL CENTER: a tabbed, futuristic home for client-side settings.
 * Only settings backed by working code are interactive in this first UI pass.
 */
public final class PerformanceConfigScreen extends Screen {
    private enum Tab {
        GENERAL("GENERAL"), QUALITY("QUALITY"), PERFORMANCE("PERFORMANCE"), ADVANCED("ADVANCED");

        private final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    private final Screen parent;
    private Tab selectedTab = Tab.GENERAL;

    public PerformanceConfigScreen(Screen parent) {
        super(Component.literal("SILICON CONTROL CENTER"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int top = Math.max(38, this.height / 2 - 92);

        int tabWidth = Math.min(104, Math.max(70, (this.width - 28) / 4));
        int totalTabsWidth = tabWidth * 4 + 6 * 3;
        int tabStart = centerX - totalTabsWidth / 2;

        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            final Tab tab = tabs[i];
            this.addRenderableWidget(Button.builder(
                    Component.literal((selectedTab == tab ? "> " : "") + tab.label),
                    button -> {
                        selectedTab = tab;
                        this.rebuildWidgets();
                    }
            ).bounds(tabStart + i * (tabWidth + 6), top, tabWidth, 20).build());
        }

        int panelWidth = Math.min(360, this.width - 28);
        int left = centerX - panelWidth / 2;
        int buttonWidth = Math.min(280, panelWidth - 24);
        int buttonLeft = centerX - buttonWidth / 2;
        int contentTop = top + 48;

        switch (selectedTab) {
            case GENERAL -> {
                this.addRenderableWidget(Button.builder(
                        Component.literal("ACTIVE PROFILE: " + currentProfile()),
                        button -> applyNextProfile()
                ).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
                this.addRenderableWidget(Button.builder(
                        particleButtonText(),
                        button -> {
                            PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                            PerformanceConfig.save();
                            button.setMessage(particleButtonText());
                        }
                ).bounds(buttonLeft, contentTop + 74, buttonWidth, 22).build());
            }
            case QUALITY -> {
                // Quality controls are intentionally informational until real,
                // tested rendering implementations are added.
            }
            case PERFORMANCE -> this.addRenderableWidget(Button.builder(
                    particleButtonText(),
                    button -> {
                        PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                        PerformanceConfig.save();
                        button.setMessage(particleButtonText());
                    }
            ).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
            case ADVANCED -> this.addRenderableWidget(Button.builder(
                    hudButtonText(),
                    button -> {
                        PerformanceConfig.showPerformanceHud = !PerformanceConfig.showPerformanceHud;
                        PerformanceConfig.save();
                        button.setMessage(hudButtonText());
                    }
            ).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("CLOSE CONTROL CENTER"),
                button -> this.onClose())
                .bounds(centerX - 100, this.height - 32, 200, 20).build());
    }

    private String currentProfile() {
        if (PerformanceConfig.reduceAmbientParticles && !PerformanceConfig.showPerformanceHud) {
            return "ULTRA LOW";
        }
        if (PerformanceConfig.reduceAmbientParticles) {
            return "LOW";
        }
        if (PerformanceConfig.showPerformanceHud) {
            return "BALANCED";
        }
        return "CUSTOM";
    }

    private void applyNextProfile() {
        switch (currentProfile()) {
            case "BALANCED" -> {
                PerformanceConfig.reduceAmbientParticles = true;
                PerformanceConfig.showPerformanceHud = true;
            }
            case "LOW" -> {
                PerformanceConfig.reduceAmbientParticles = true;
                PerformanceConfig.showPerformanceHud = false;
            }
            case "ULTRA LOW" -> {
                PerformanceConfig.reduceAmbientParticles = false;
                PerformanceConfig.showPerformanceHud = false;
            }
            default -> {
                PerformanceConfig.reduceAmbientParticles = false;
                PerformanceConfig.showPerformanceHud = true;
            }
        }
        PerformanceConfig.save();
        this.rebuildWidgets();
    }

    private static Component particleButtonText() {
        return Component.literal("AMBIENT PARTICLES  [" +
                (PerformanceConfig.reduceAmbientParticles ? "REDUCED" : "FULL") + "]");
    }

    private static Component hudButtonText() {
        return Component.literal("PERFORMANCE HUD  [" +
                (PerformanceConfig.showPerformanceHud ? "ON" : "OFF") + "]");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int top = Math.max(38, this.height / 2 - 92);
        int panelWidth = Math.min(360, this.width - 28);
        int panelLeft = centerX - panelWidth / 2;
        int panelTop = top + 28;
        int panelBottom = this.height - 42;

        graphics.fill(0, 0, this.width, this.height, 0xD908101C);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelBottom, 0xE51A2638);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 2, 0xFF35D7FF);
        graphics.drawCenteredString(this.font, "S I L I C O N   /   C O N T R O L", centerX, 12, 0xFF54DFFF);
        graphics.drawCenteredString(this.font, "CLIENT PERFORMANCE SYSTEM  •  v1.0", centerX, 25, 0xFF9FB7D0);

        Minecraft minecraft = Minecraft.getInstance();
        Runtime runtime = Runtime.getRuntime();
        int fps = minecraft.getFps();
        long usedMiB = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
        long maxMiB = runtime.maxMemory() / (1024L * 1024L);

        graphics.drawString(this.font, "LIVE TELEMETRY", panelLeft + 12, panelTop + 10, 0xFF54DFFF);
        graphics.drawString(this.font, "FPS  " + fps, panelLeft + 14, panelTop + 25, 0xFFFFFFFF);
        graphics.drawString(this.font, "JAVA HEAP  " + usedMiB + " / " + maxMiB + " MiB",
                panelLeft + 105, panelTop + 25, 0xFFB8D8F0);

        int contentY = top + 48;
        switch (selectedTab) {
            case GENERAL -> {
                graphics.drawString(this.font, "SYSTEM PROFILE", panelLeft + 14, contentY + 10, 0xFF54DFFF);
                graphics.drawString(this.font, "Cycle the available safe presets below.", panelLeft + 14,
                        contentY + 28, 0xFFD1DCE8);
                graphics.drawString(this.font, "Presets only change features currently implemented.",
                        panelLeft + 14, contentY + 104, 0xFF9FB7D0);
            }
            case QUALITY -> {
                graphics.drawString(this.font, "VISUAL QUALITY", panelLeft + 14, contentY + 10, 0xFF54DFFF);
                graphics.drawString(this.font, "Rendering controls are being engineered.", panelLeft + 14,
                        contentY + 30, 0xFFD1DCE8);
                graphics.drawString(this.font, "No fake toggles — each option arrives with its",
                        panelLeft + 14, contentY + 48, 0xFF9FB7D0);
                graphics.drawString(this.font, "own tested implementation.", panelLeft + 14,
                        contentY + 62, 0xFF9FB7D0);
            }
            case PERFORMANCE -> {
                graphics.drawString(this.font, "FRAME-TIME & PARTICLE SYSTEMS",
                        panelLeft + 14, contentY + 10, 0xFF54DFFF);
                graphics.drawString(this.font, "Reduce selected ambient particles to lower",
                        panelLeft + 14, contentY + 28, 0xFFD1DCE8);
                graphics.drawString(this.font, "visual workload. Combat feedback is preserved.",
                        panelLeft + 14, contentY + 44, 0xFFD1DCE8);
            }
            case ADVANCED -> {
                graphics.drawString(this.font, "DIAGNOSTICS", panelLeft + 14, contentY + 10, 0xFF54DFFF);
                graphics.drawString(this.font, "Toggle the lightweight on-screen FPS/RAM HUD.",
                        panelLeft + 14, contentY + 28, 0xFFD1DCE8);
            }
        }

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
