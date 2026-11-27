package com.silicon.fps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** SILICON CONTROL CENTER: tested controls only; no fake performance switches. */
public final class PerformanceConfigScreen extends Screen {
    private enum Tab {
        GENERAL("GENERAL"), QUALITY("QUALITY"), PERFORMANCE("PERFORMANCE"), ADVANCED("ADVANCED");
        private final String label;
        Tab(String label) { this.label = label; }
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
        int totalTabsWidth = tabWidth * 4 + 18;
        int tabStart = centerX - totalTabsWidth / 2;

        for (int i = 0; i < Tab.values().length; i++) {
            final Tab tab = Tab.values()[i];
            this.addRenderableWidget(Button.builder(
                    Component.literal((selectedTab == tab ? "> " : "") + tab.label),
                    button -> { selectedTab = tab; this.rebuildWidgets(); }
            ).bounds(tabStart + i * (tabWidth + 6), top, tabWidth, 20).build());
        }

        int panelWidth = Math.min(360, this.width - 28);
        int buttonWidth = Math.min(280, panelWidth - 24);
        int buttonLeft = centerX - buttonWidth / 2;
        int contentTop = top + 48;

        switch (selectedTab) {
            case GENERAL -> {
                this.addRenderableWidget(Button.builder(Component.literal(lowEndPresetText()), button -> {
                    PerformanceConfig.applyLowEndPreset(this.minecraft);
                    this.rebuildWidgets();
                }).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
                this.addRenderableWidget(Button.builder(particleButtonText(), button -> {
                    PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                    PerformanceConfig.save();
                    button.setMessage(particleButtonText());
                }).bounds(buttonLeft, contentTop + 74, buttonWidth, 22).build());
            }
            case QUALITY -> { }
            case PERFORMANCE -> this.addRenderableWidget(Button.builder(particleButtonText(), button -> {
                PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                PerformanceConfig.save();
                button.setMessage(particleButtonText());
            }).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
            case ADVANCED -> this.addRenderableWidget(Button.builder(hudButtonText(), button -> {
                PerformanceConfig.showPerformanceHud = !PerformanceConfig.showPerformanceHud;
                PerformanceConfig.save();
                button.setMessage(hudButtonText());
            }).bounds(buttonLeft, contentTop + 44, buttonWidth, 22).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("CLOSE CONTROL CENTER"), button -> this.onClose())
                .bounds(centerX - 100, this.height - 32, 200, 20).build());
    }

    private static String lowEndPresetText() {
        return "APPLY POTATO MODE  [" + (PerformanceConfig.lowEndPresetApplied ? "APPLIED" : "READY") + "]";
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
        int panelWidth = Math.min(420, this.width - 24);
        int panelLeft = centerX - panelWidth / 2;
        int panelTop = top + 28;
        int panelBottom = this.height - 42;
        int innerLeft = panelLeft + 12;
        int innerWidth = panelWidth - 24;

        // Layered dark surfaces and restrained cyan identity accents.
        graphics.fill(0, 0, this.width, this.height, 0xE908101B);
        graphics.fill(panelLeft - 1, panelTop - 1, panelLeft + panelWidth + 1,
                panelBottom + 1, 0xFF263B50);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelBottom, 0xF0142030);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 3, 0xFF35D7FF);
        graphics.fill(panelLeft, panelTop + 3, panelLeft + 3, panelBottom, 0xFF247D9A);

        graphics.drawCenteredString(this.font, "S I L I C O N", centerX, 9, 0xFF54DFFF);
        graphics.drawCenteredString(this.font, "PERFORMANCE CONTROL CENTER", centerX, 22, 0xFFE2F2FF);
        graphics.drawString(this.font, "CLIENT OPTIMIZATION SUITE", innerLeft, panelTop + 9, 0xFF8DAFC8);
        graphics.fill(panelLeft + panelWidth - 83, panelTop + 8,
                panelLeft + panelWidth - 12, panelTop + 20, 0xFF173D43);
        graphics.drawCenteredString(this.font, "ACTIVE", panelLeft + panelWidth - 47,
                panelTop + 10, 0xFF7EF0D0);

        Minecraft minecraft = Minecraft.getInstance();
        Runtime runtime = Runtime.getRuntime();
        int fps = minecraft.getFps();
        long usedMiB = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
        long maxMiB = runtime.maxMemory() / (1024L * 1024L);

        int metricsTop = panelTop + 28;
        int gap = 6;
        int cardWidth = (innerWidth - gap * 2) / 3;
        int cardBottom = metricsTop + 35;
        for (int i = 0; i < 3; i++) {
            int x = innerLeft + i * (cardWidth + gap);
            graphics.fill(x, metricsTop, x + cardWidth, cardBottom, 0xFF1D2D40);
            graphics.fill(x, metricsTop, x + cardWidth, metricsTop + 1, 0xFF31536B);
        }
        graphics.drawString(this.font, "FPS", innerLeft + 7, metricsTop + 5, 0xFF8DAFC8);
        graphics.drawString(this.font, Integer.toString(fps), innerLeft + 7, metricsTop + 17,
                fps >= 30 ? 0xFF7EF0D0 : 0xFFFFD27A);
        int secondX = innerLeft + cardWidth + gap;
        graphics.drawString(this.font, "HEAP USED", secondX + 7, metricsTop + 5, 0xFF8DAFC8);
        graphics.drawString(this.font, usedMiB + " MiB", secondX + 7, metricsTop + 17, 0xFFE2F2FF);
        int thirdX = innerLeft + (cardWidth + gap) * 2;
        graphics.drawString(this.font, "HEAP LIMIT", thirdX + 7, metricsTop + 5, 0xFF8DAFC8);
        graphics.drawString(this.font, maxMiB + " MiB", thirdX + 7, metricsTop + 17, 0xFFE2F2FF);

        int contentY = top + 48;
        int sectionTop = contentY + 2;
        graphics.fill(innerLeft, sectionTop, innerLeft + innerWidth, panelBottom - panelTop - 17,
                0x66101A28);
        graphics.drawString(this.font, selectedTab.label, innerLeft + 9, sectionTop + 8, 0xFF54DFFF);
        graphics.fill(innerLeft + 9, sectionTop + 21, innerLeft + innerWidth - 9,
                sectionTop + 22, 0xFF2B455A);

        switch (selectedTab) {
            case GENERAL -> {
                graphics.drawString(this.font, "LOW-END DEVICE PROFILE", innerLeft + 9, contentY + 28, 0xFFE2F2FF);
                graphics.drawString(this.font, "A conservative preset for memory- and GPU-limited devices.",
                        innerLeft + 9, contentY + 46, 0xFFD1DCE8);
                graphics.drawString(this.font, "2 chunks  |  minimal particles  |  clouds, AO and shadows off",
                        innerLeft + 9, contentY + 112, 0xFF9FB7D0);
                graphics.drawString(this.font, "Player visibility and simulation distance are preserved.",
                        innerLeft + 9, contentY + 126, 0xFF9FB7D0);
            }
            case QUALITY -> {
                graphics.drawString(this.font, "VISUAL QUALITY", innerLeft + 9, contentY + 28, 0xFFE2F2FF);
                graphics.drawString(this.font, "Rendering controls are being engineered and validated.",
                        innerLeft + 9, contentY + 46, 0xFFD1DCE8);
                graphics.drawString(this.font, "Options will appear here only when their implementation",
                        innerLeft + 9, contentY + 64, 0xFF9FB7D0);
                graphics.drawString(this.font, "is complete and tested.", innerLeft + 9, contentY + 78, 0xFF9FB7D0);
            }
            case PERFORMANCE -> {
                graphics.drawString(this.font, "FRAME-TIME & PARTICLE SYSTEMS",
                        innerLeft + 9, contentY + 28, 0xFFE2F2FF);
                graphics.drawString(this.font, "Reduce selected ambient particles to lower visual workload.",
                        innerLeft + 9, contentY + 46, 0xFFD1DCE8);
                graphics.drawString(this.font, "Combat and interaction feedback remain untouched.",
                        innerLeft + 9, contentY + 64, 0xFF9FB7D0);
            }
            case ADVANCED -> {
                graphics.drawString(this.font, "DIAGNOSTICS", innerLeft + 9, contentY + 28, 0xFFE2F2FF);
                graphics.drawString(this.font, "Enable the lightweight on-screen FPS and RAM overlay.",
                        innerLeft + 9, contentY + 46, 0xFFD1DCE8);
                graphics.drawString(this.font, "More diagnostics will be added as their metrics are verified.",
                        innerLeft + 9, contentY + 64, 0xFF9FB7D0);
            }
        }
        graphics.drawString(this.font, "SILICON FPS  •  BUILD 1.0.0  •  CLIENT SIDE",
                innerLeft, panelBottom - 12, 0xFF6F8BA3);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }
}
