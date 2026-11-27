package com.silicon.fps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Lightweight popup surfaces for the pause-menu hub. */
public final class PauseHubScreen extends Screen {
    public enum Tab { ACCOUNTS, WARDROBE, MODULES }

    private final Screen parent;
    private final Tab tab;

    public PauseHubScreen(Screen parent, Tab tab) {
        super(Component.literal("SILICON " + tab.name()));
        this.parent = parent;
        this.tab = tab;
    }

    @Override
    protected void init() {
        int panelWidth = Math.min(430, this.width - 32);
        int left = (this.width - panelWidth) / 2;
        int top = Math.max(35, this.height / 2 - 105);

        this.addRenderableWidget(Button.builder(Component.literal("BACK TO GAME MENU"),
                button -> this.minecraft.setScreen(parent))
                .bounds(this.width / 2 - 95, this.height - 38, 190, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.fill(0, 0, this.width, this.height, 0x99000000);

        int panelWidth = Math.min(430, this.width - 32);
        int panelHeight = Math.min(230, this.height - 70);
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        graphics.fill(left, top, left + panelWidth, top + panelHeight, 0xF0000000);
        graphics.fill(left, top, left + panelWidth, top + 2, 0xFF9D79D0);
        graphics.drawCenteredString(this.font, "S I L I C O N", this.width / 2, top + 17, 0xFFD8BFFF);
        graphics.drawCenteredString(this.font, tab.name(), this.width / 2, top + 39, 0xFFF4F0FA);

        switch (tab) {
            case ACCOUNTS -> {
                graphics.drawCenteredString(this.font, "ACCOUNT MANAGER", this.width / 2, top + 83, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "Account switching UI is being built.", this.width / 2, top + 104, 0xFFB9AEC8);
                graphics.drawCenteredString(this.font, "No passwords are requested or stored by SILICON.", this.width / 2, top + 122, 0xFF91869F);
            }
            case WARDROBE -> {
                graphics.drawCenteredString(this.font, "COSMETICS & OUTFITS", this.width / 2, top + 88, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "WARDROBE SYSTEM — COMING SOON", this.width / 2, top + 111, 0xFFB9AEC8);
            }
            case MODULES -> {
                graphics.drawCenteredString(this.font, "SILICON MODULES", this.width / 2, top + 83, 0xFFFFFFFF);
                graphics.drawCenteredString(this.font, "Optimization modules will appear here as they ship.", this.width / 2, top + 104, 0xFFB9AEC8);
                graphics.drawCenteredString(this.font, "Only implemented and verified controls will be listed.", this.width / 2, top + 122, 0xFF91869F);
            }
        }
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
