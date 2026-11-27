package com.silicon.fps.mixin;

import com.silicon.fps.PauseHubScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces the vanilla title-screen controls with the SILICON main menu. */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void siliconfps$buildMainMenu(CallbackInfo ci) {
        this.clearWidgets();

        int center = this.width / 2;
        int panelWidth = Math.min(430, this.width - 28);
        int buttonWidth = Math.min(280, panelWidth - 48);
        int buttonHeight = 22;
        int gap = 6;
        int left = center - buttonWidth / 2;
        int stackHeight = buttonHeight * 3 + gap * 3 + 22;
        int startY = Math.max(92, Math.min(this.height - stackHeight - 28, this.height / 2 - 18));

        this.addRenderableWidget(Button.builder(Component.literal("SINGLEPLAYER"),
                button -> this.minecraft.setScreen(new SelectWorldScreen(this)))
                .bounds(left, startY, buttonWidth, buttonHeight).build());
        this.addRenderableWidget(Button.builder(Component.literal("MULTIPLAYER"),
                button -> this.minecraft.setScreen(new JoinMultiplayerScreen(this)))
                .bounds(left, startY + buttonHeight + gap, buttonWidth, buttonHeight).build());
        this.addRenderableWidget(Button.builder(Component.literal("SILICON HUB"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(left, startY + (buttonHeight + gap) * 2, buttonWidth, buttonHeight).build());

        int half = (buttonWidth - gap) / 2;
        this.addRenderableWidget(Button.builder(Component.literal("OPTIONS"),
                button -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options)))
                .bounds(left, startY + (buttonHeight + gap) * 3, half, buttonHeight).build());
        this.addRenderableWidget(Button.builder(Component.literal("QUIT GAME"),
                button -> this.minecraft.stop())
                .bounds(left + half + gap, startY + (buttonHeight + gap) * 3, half, buttonHeight).build());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void siliconfps$renderMainMenuBranding(GuiGraphics graphics, int mouseX, int mouseY,
                                                    float delta, CallbackInfo ci) {
        int center = this.width / 2;
        int panelWidth = Math.min(430, this.width - 28);
        int panelTop = (this.height - Math.min(310, this.height - 24)) / 2;
        graphics.drawCenteredString(this.font, "S I L I C O N", center, panelTop + 16, 0xFFE2D4FF);
        graphics.drawCenteredString(this.font, "PERFORMANCE CLIENT", center, panelTop + 30, 0xFFB9A8D5);
        graphics.fill(center - panelWidth / 2 + 24, panelTop + 46,
                center + panelWidth / 2 - 24, panelTop + 47, 0xAA9F79D2);
    }
}
