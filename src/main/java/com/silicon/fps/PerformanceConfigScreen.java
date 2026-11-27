package com.silicon.fps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Responsive SILICON Control Center for compact launcher resolutions. */
public final class PerformanceConfigScreen extends Screen {
    private enum Tab {
        GENERAL("GENERAL"), QUALITY("QUALITY"), PERFORMANCE("PERFORMANCE"), ADVANCED("ADVANCED");
        private final String label;
        Tab(String label) { this.label = label; }
    }

    private static final int PURPLE = 0xFFB98AFF;
    private static final int PURPLE_BRIGHT = 0xFFD2B5FF;
    private static final int MUTED = 0xFFAAA1BB;
    private static final int WHITE = 0xFFF4F0FA;
    private final Screen parent;
    private Tab selectedTab = Tab.GENERAL;

    public PerformanceConfigScreen(Screen parent) {
        super(Component.literal("SILICON CONTROL CENTER"));
        this.parent = parent;
    }

    private int panelTop() { return 74; }
    private int panelBottom() { return this.height - 34; }
    private int sectionTop() { return panelTop() + 42 + 42 + 10; }
    private int sectionBottom() { return panelBottom() - 10; }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int tabWidth = Math.min(116, Math.max(62, (this.width - 34) / 4));
        int totalTabsWidth = tabWidth * 4 + 18;
        int tabStart = centerX - totalTabsWidth / 2;

        for (int i = 0; i < Tab.values().length; i++) {
            final Tab tab = Tab.values()[i];
            this.addRenderableWidget(Button.builder(
                    Component.literal((selectedTab == tab ? "◆ " : "") + tab.label),
                    button -> { selectedTab = tab; this.rebuildWidgets(); }
            ).bounds(tabStart + i * (tabWidth + 6), 43, tabWidth, 21).build());
        }

        int panelWidth = Math.min(560, this.width - 24);
        int buttonWidth = Math.min(360, panelWidth - 36);
        int buttonLeft = centerX - buttonWidth / 2;
        int actionY = sectionBottom() - 51;

        switch (selectedTab) {
            case GENERAL -> {
                this.addRenderableWidget(Button.builder(Component.literal(lowEndPresetText()), button -> {
                    PerformanceConfig.applyLowEndPreset(this.minecraft);
                    this.rebuildWidgets();
                }).bounds(buttonLeft, actionY, buttonWidth, 21).build());
                this.addRenderableWidget(Button.builder(particleButtonText(), button -> {
                    PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                    PerformanceConfig.save();
                    button.setMessage(particleButtonText());
                }).bounds(buttonLeft, actionY + 24, buttonWidth, 21).build());
            }
            case QUALITY -> { }
            case PERFORMANCE -> this.addRenderableWidget(Button.builder(particleButtonText(), button -> {
                PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                PerformanceConfig.save();
                button.setMessage(particleButtonText());
            }).bounds(buttonLeft, sectionBottom() - 27, buttonWidth, 21).build());
            case ADVANCED -> this.addRenderableWidget(Button.builder(hudButtonText(), button -> {
                PerformanceConfig.showPerformanceHud = !PerformanceConfig.showPerformanceHud;
                PerformanceConfig.save();
                button.setMessage(hudButtonText());
            }).bounds(buttonLeft, sectionBottom() - 27, buttonWidth, 21).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("CLOSE CONTROL CENTER"), button -> this.onClose())
                .bounds(centerX - Math.min(150, panelWidth / 2 - 12), this.height - 26,
                        Math.min(300, panelWidth - 24), 20).build());
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

    private static void rounded(GuiGraphics g, int x, int y, int width, int height, int radius, int color) {
        if (width <= 0 || height <= 0) return;
        int r = Math.max(1, Math.min(radius, Math.min(width / 2, height / 2)));
        for (int row = 0; row < height; row++) {
            int inset = 0;
            if (row < r) inset = r - 1 - (int) Math.floor(Math.sqrt(Math.max(0, r * r - (r - row) * (r - row))));
            else if (row >= height - r) {
                int fromBottom = height - 1 - row;
                inset = r - 1 - (int) Math.floor(Math.sqrt(Math.max(0, r * r - (r - fromBottom) * (r - fromBottom))));
            }
            g.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }

    private static void roundedFrame(GuiGraphics g, int x, int y, int width, int height,
                                     int radius, int border, int fill) {
        rounded(g, x, y, width, height, radius, border);
        rounded(g, x + 1, y + 1, width - 2, height - 2, Math.max(1, radius - 1), fill);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.fill(0, 0, this.width, this.height, 0x99000000);

        int centerX = this.width / 2;
        int panelWidth = Math.min(560, this.width - 24);
        int panelLeft = centerX - panelWidth / 2;
        int panelTop = panelTop();
        int panelBottom = panelBottom();
        int innerLeft = panelLeft + 14;
        int innerWidth = panelWidth - 28;

        roundedFrame(graphics, panelLeft - 1, panelTop - 1, panelWidth + 2,
                panelBottom - panelTop + 2, 12, 0xFF65468B, 0xFF000000);
        rounded(graphics, panelLeft + 14, panelTop + 10, panelWidth - 28, 1, 1, 0xFF3C2A54);

        graphics.drawCenteredString(this.font, "S I L I C O N", centerX, 7, PURPLE_BRIGHT);
        graphics.drawCenteredString(this.font, "P E R F O R M A N C E   C O N T R O L   C E N T E R",
                centerX, 24, 0xFFD8CBEA);
        graphics.drawCenteredString(this.font, "CLIENT OPTIMIZATION SUITE", centerX, panelTop + 13, MUTED);

        Minecraft minecraft = Minecraft.getInstance();
        Runtime runtime = Runtime.getRuntime();
        int fps = minecraft.getFps();
        long usedMiB = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
        long maxMiB = runtime.maxMemory() / (1024L * 1024L);

        int metricsTop = panelTop + 32;
        int gap = 7;
        int cardWidth = (innerWidth - gap * 2) / 3;
        int cardHeight = 42;
        String[] labels = {"FPS", "HEAP USED", "HEAP LIMIT"};
        String[] values = {Integer.toString(fps), usedMiB + " MiB", maxMiB + " MiB"};
        for (int i = 0; i < 3; i++) {
            int x = innerLeft + i * (cardWidth + gap);
            roundedFrame(graphics, x, metricsTop, cardWidth, cardHeight, 7, 0xFF39284F, 0xFF09070D);
            graphics.drawCenteredString(this.font, labels[i], x + cardWidth / 2, metricsTop + 5, MUTED);
            graphics.drawCenteredString(this.font, values[i], x + cardWidth / 2, metricsTop + 21,
                    i == 0 && fps < 30 ? 0xFFFFD18D : WHITE);
        }

        int sectionTop = sectionTop();
        int sectionBottom = sectionBottom();
        roundedFrame(graphics, innerLeft, sectionTop, innerWidth,
                sectionBottom - sectionTop, 9, 0xFF38264D, 0xFF030205);
        graphics.drawString(this.font, selectedTab.label, innerLeft + 13, sectionTop + 9, PURPLE_BRIGHT);
        graphics.fill(innerLeft + 13, sectionTop + 24, innerLeft + innerWidth - 13,
                sectionTop + 25, 0xFF352449);

        int textX = innerLeft + 14;
        int contentY = sectionTop + 34;
        switch (selectedTab) {
            case GENERAL -> {
                graphics.drawString(this.font, "LOW-END DEVICE PROFILE", textX, contentY, WHITE);
                graphics.drawString(this.font, "2 chunks; minimal particles; clouds, AO and shadows off.",
                        textX, contentY + 16, MUTED);
                graphics.drawString(this.font, "Player visibility and simulation distance are preserved.",
                        textX, contentY + 32, MUTED);
            }
            case QUALITY -> {
                graphics.drawString(this.font, "VISUAL QUALITY", textX, contentY, WHITE);
                graphics.drawString(this.font, "Additional rendering controls are not implemented yet.",
                        textX, contentY + 16, MUTED);
            }
            case PERFORMANCE -> {
                graphics.drawString(this.font, "AMBIENT PARTICLE REDUCTION", textX, contentY, WHITE);
                graphics.drawString(this.font, "Only selected decorative particles are reduced.",
                        textX, contentY + 16, MUTED);
            }
            case ADVANCED -> {
                graphics.drawString(this.font, "DIAGNOSTICS", textX, contentY, WHITE);
                graphics.drawString(this.font, "Toggle the lightweight FPS and memory overlay.",
                        textX, contentY + 16, MUTED);
            }
        }

        graphics.drawCenteredString(this.font, "SILICON FPS  •  CLIENT SIDE",
                centerX, panelBottom - 10, 0xFF8E78B3);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }
}
