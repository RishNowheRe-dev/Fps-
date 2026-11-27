package com.silicon.fps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Unified SILICON hub shared by the title screen and in-game pause menu. */
public final class PauseHubScreen extends Screen {
    public enum Tab { MODULES, ACCOUNTS, WARDROBE }

    private final Screen parent;
    private final Tab tab;

    public PauseHubScreen(Screen parent, Tab tab) {
        super(Component.literal("SILICON HUB"));
        this.parent = parent;
        this.tab = tab;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int panelHeight = Math.min(360, this.height - 24);
        int panelTop = (this.height - panelHeight) / 2;
        int tabWidth = Math.min(112, Math.max(72, (this.width - 48) / 3));
        int gap = 5;
        int start = center - (tabWidth * 3 + gap * 2) / 2;
        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            Tab selected = tabs[i];
            String label = (selected == tab ? "◆ " : "") + selected.name();
            this.addRenderableWidget(Button.builder(Component.literal(label),
                    button -> this.minecraft.setScreen(new PauseHubScreen(parent, selected)))
                    .bounds(start + i * (tabWidth + gap), panelTop + 50, tabWidth, 20).build());
        }

        int actionWidth = Math.min(230, this.width - 40);
        int actionLeft = center - actionWidth / 2;
        if (tab == Tab.MODULES) {
            this.addRenderableWidget(Button.builder(ambientText(), button -> {
                PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                PerformanceConfig.save();
                button.setMessage(ambientText());
            }).bounds(actionLeft, panelTop + 145, actionWidth, 22).build());

            this.addRenderableWidget(Button.builder(hudText(), button -> {
                PerformanceConfig.showPerformanceHud = !PerformanceConfig.showPerformanceHud;
                PerformanceConfig.save();
                button.setMessage(hudText());
            }).bounds(actionLeft, panelTop + 174, actionWidth, 22).build());

            this.addRenderableWidget(Button.builder(Component.literal("APPLY POTATO MODE"),
                    button -> {
                        PerformanceConfig.applyLowEndPreset(this.minecraft);
                        this.rebuildWidgets();
                    }).bounds(actionLeft, panelTop + 203, actionWidth, 22).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("OPEN PERFORMANCE CONTROL CENTER"),
                button -> this.minecraft.setScreen(new PerformanceConfigScreen(this)))
                .bounds(center - Math.min(150, actionWidth / 2), panelTop + panelHeight - 72,
                        Math.min(300, actionWidth), 22).build());

        this.addRenderableWidget(Button.builder(Component.literal(parent instanceof net.minecraft.client.gui.screens.TitleScreen
                        ? "BACK TO TITLE" : "BACK TO GAME"),
                button -> this.onClose())
                .bounds(center - 95, panelTop + panelHeight - 43, 190, 20).build());
    }

    private static Component ambientText() {
        return Component.literal("AMBIENT PARTICLES  [" +
                (PerformanceConfig.reduceAmbientParticles ? "REDUCED" : "FULL") + "]");
    }

    private static Component hudText() {
        return Component.literal("PERFORMANCE HUD  [" +
                (PerformanceConfig.showPerformanceHud ? "ON" : "OFF") + "]");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.fill(0, 0, this.width, this.height, 0xB8000000);

        int panelWidth = Math.min(500, this.width - 24);
        int panelHeight = Math.min(360, this.height - 24);
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        graphics.fill(left, top, left + panelWidth, top + panelHeight, 0xF5000000);
        graphics.fill(left, top, left + panelWidth, top + 2, 0xFF9D79D0);
        graphics.fill(left + 16, top + 38, left + panelWidth - 16, top + 39, 0xFF39284F);

        graphics.drawCenteredString(this.font, "S I L I C O N", this.width / 2, top + 12, 0xFFD8BFFF);
        graphics.drawCenteredString(this.font, "UNIFIED CLIENT HUB", this.width / 2, top + 27, 0xFFB9AEC8);

        switch (tab) {
            case MODULES -> {
                graphics.drawCenteredString(this.font, "PERFORMANCE MODULES", this.width / 2, top + 83, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "Toggle lightweight client-side features or apply the low-end preset.",
                        this.width / 2, top + 105, 0xFFB9AEC8);
                graphics.drawCenteredString(this.font, "Potato Mode lowers visual workload while preserving player visibility.",
                        this.width / 2, top + 119, 0xFF91869F);
                graphics.drawCenteredString(this.font, "Preset status: " + (PerformanceConfig.lowEndPresetApplied ? "APPLIED" : "NOT APPLIED"),
                        this.width / 2, top + 239, 0xFFD8BFFF);
            }
            case ACCOUNTS -> {
                graphics.drawCenteredString(this.font, "ACCOUNT MANAGER", this.width / 2, top + 103, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "Account switching is not implemented yet.", this.width / 2, top + 125, 0xFFB9AEC8);
                graphics.drawCenteredString(this.font, "SILICON never asks for or stores your password.", this.width / 2, top + 143, 0xFF91869F);
            }
            case WARDROBE -> {
                graphics.drawCenteredString(this.font, "COSMETICS & OUTFITS", this.width / 2, top + 103, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "Wardrobe and custom cosmetics are not implemented yet.", this.width / 2, top + 125, 0xFFB9AEC8);
            }
        }
        super.render(graphics, mouseX, mouseY, delta);

        int center = this.width / 2;
        int panelHeight = Math.min(360, this.height - 24);
        int panelTop = (this.height - panelHeight) / 2;
        int tabWidth = Math.min(112, Math.max(72, (this.width - 48) / 3));
        int gap = 5;
        int tabStart = center - (tabWidth * 3 + gap * 2) / 2;
        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            String label = (tabs[i] == tab ? "◆ " : "") + tabs[i].name();
            drawSiliconButton(graphics, label, tabStart + i * (tabWidth + gap),
                    panelTop + 50, tabWidth, 20, mouseX, mouseY);
        }
        int actionWidth = Math.min(230, this.width - 40);
        int actionLeft = center - actionWidth / 2;
        if (tab == Tab.MODULES) {
            drawSiliconButton(graphics, ambientText().getString(), actionLeft, panelTop + 145,
                    actionWidth, 22, mouseX, mouseY);
            drawSiliconButton(graphics, hudText().getString(), actionLeft, panelTop + 174,
                    actionWidth, 22, mouseX, mouseY);
            drawSiliconButton(graphics, "APPLY POTATO MODE", actionLeft, panelTop + 203,
                    actionWidth, 22, mouseX, mouseY);
        }
        drawSiliconButton(graphics, "OPEN PERFORMANCE CONTROL CENTER",
                center - Math.min(150, actionWidth / 2), panelTop + panelHeight - 72,
                Math.min(300, actionWidth), 22, mouseX, mouseY);
        drawSiliconButton(graphics, parent instanceof net.minecraft.client.gui.screens.TitleScreen
                        ? "BACK TO TITLE" : "BACK TO GAME",
                center - 95, panelTop + panelHeight - 43, 190, 20, mouseX, mouseY);
    }

    private void drawSiliconButton(GuiGraphics graphics, String label, int x, int y, int width,
                                   int height, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        graphics.fill(x, y, x + width, y + height, hovered ? 0xFFDCC4FF : 0xFF6C568A);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1,
                hovered ? 0xFF30233E : 0xFF17121F);
        graphics.fill(x + 6, y + 5, x + 8, y + height - 5, hovered ? 0xFFE2CAFF : 0xFF9D79D0);
        graphics.drawCenteredString(this.font, label, x + width / 2, y + (height - 8) / 2,
                hovered ? 0xFFFFFFFF : 0xFFE1D8ED);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
