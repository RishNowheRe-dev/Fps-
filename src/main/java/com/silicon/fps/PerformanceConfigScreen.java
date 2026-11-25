package com.silicon.fps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PerformanceConfigScreen extends Screen {
    private final Screen parent;
    private Button particleButton;

    public PerformanceConfigScreen(Screen parent) {
        super(Component.literal("SILICON FPS Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.particleButton = this.addRenderableWidget(
                Button.builder(particleButtonText(), button -> {
                    PerformanceConfig.reduceAmbientParticles =
                            !PerformanceConfig.reduceAmbientParticles;
                    PerformanceConfig.save();
                    button.setMessage(particleButtonText());
                }).bounds(centerX - 105, centerY - 5, 210, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("Done"), button -> this.onClose())
                        .bounds(centerX - 105, centerY + 25, 210, 20)
                        .build()
        );
    }

    private static Component particleButtonText() {
        return Component.literal(
                "Ambient Particle Reduction: "
                        + (PerformanceConfig.reduceAmbientParticles ? "ON" : "OFF")
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
                Component.literal("Reduces selected decorative particles."),
                this.width / 2,
                this.height / 2 - 30,
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
