package com.silicon.fps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PerformanceConfigScreen extends Screen {
    private final Screen parent;

    public PerformanceConfigScreen(Screen parent) {
        super(Component.literal("SILICON FPS Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(
                Button.builder(particleButtonText(), button -> {
                    PerformanceConfig.reduceAmbientParticles =
                            !PerformanceConfig.reduceAmbientParticles;
                    PerformanceConfig.save();
                    button.setMessage(particleButtonText());
                }).bounds(centerX - 110, centerY - 12, 220, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(hudButtonText(), button -> {
                    PerformanceConfig.showPerformanceHud =
                            !PerformanceConfig.showPerformanceHud;
                    PerformanceConfig.save();
                    button.setMessage(hudButtonText());
                }).bounds(centerX - 110, centerY + 14, 220, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("Done"), button -> this.onClose())
                        .bounds(centerX - 110, centerY + 44, 220, 20)
                        .build()
        );
    }

    private static Component particleButtonText() {
        return Component.literal(
                "Ambient Particle Reduction: "
                        + (PerformanceConfig.reduceAmbientParticles ? "ON" : "OFF")
        );
    }

    private static Component hudButtonText() {
        return Component.literal(
                "Performance HUD: "
                        + (PerformanceConfig.showPerformanceHud ? "ON" : "OFF")
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                this.height / 2 - 55,
                0xFFFFFFFF
        );
        graphics.drawCenteredString(
                this.font,
                Component.literal("Toggle lightweight client-side options."),
                this.width / 2,
                this.height / 2 - 32,
                0xFFB8E0FF
        );
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
