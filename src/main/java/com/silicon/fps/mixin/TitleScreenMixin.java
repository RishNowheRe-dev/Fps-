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

/** SILICON title screen: retains vanilla navigation actions, replaces its visual presentation. */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void siliconfps$buildMainMenu(CallbackInfo ci) {
        this.clearWidgets();

        int center = this.width / 2;
        int panelWidth = Math.min(470, this.width - 28);
        int buttonWidth = Math.min(280, panelWidth - 48);
        int buttonHeight = 22;
        int gap = 6;
        int left = center - buttonWidth / 2;
        int startY = Math.max(150, Math.min(this.height - 132, this.height / 2 - 18));

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
    private void siliconfps$renderCustomMainMenu(GuiGraphics graphics, int mouseX, int mouseY,
                                                   float delta, CallbackInfo ci) {
        int center = this.width / 2;
        int panelWidth = Math.min(470, this.width - 28);
        int panelLeft = center - panelWidth / 2;
        int panelTop = (this.height - Math.min(310, this.height - 24)) / 2;
        int buttonWidth = Math.min(280, panelWidth - 48);
        int left = center - buttonWidth / 2;
        int startY = Math.max(150, Math.min(this.height - 132, this.height / 2 - 18));
        int h = 22;
        int gap = 6;
        int half = (buttonWidth - gap) / 2;

        // Paint over the vanilla logo/splash in the header zone; the panorama remains visible outside the panel.
        graphics.fill(panelLeft + 2, panelTop + 2, panelLeft + panelWidth - 2, startY - 10, 0xF20A0811);
        graphics.fill(panelLeft + 24, panelTop + 22, panelLeft + panelWidth - 24, panelTop + 23, 0xFFB8C0CC);
        graphics.drawCenteredString(this.font, "S I L I C O N", center, panelTop + 39, 0xFFE6D8FF);
        graphics.drawCenteredString(this.font, "PERFORMANCE CLIENT", center, panelTop + 56, 0xFFC0C6D0);
        graphics.drawCenteredString(this.font, "A LIGHTWEIGHT CLIENT EXPERIENCE", center, panelTop + 73, 0xFF8D96A3);
        graphics.fill(panelLeft + 64, panelTop + 91, panelLeft + panelWidth - 64, panelTop + 92, 0x663F2D58);

        drawButton(graphics, "SINGLEPLAYER", left, startY, buttonWidth, h, mouseX, mouseY);
        drawButton(graphics, "MULTIPLAYER", left, startY + h + gap, buttonWidth, h, mouseX, mouseY);
        drawButton(graphics, "SILICON HUB", left, startY + (h + gap) * 2, buttonWidth, h, mouseX, mouseY);
        drawButton(graphics, "OPTIONS", left, startY + (h + gap) * 3, half, h, mouseX, mouseY);
        drawButton(graphics, "QUIT GAME", left + half + gap, startY + (h + gap) * 3,
                half, h, mouseX, mouseY);

        graphics.drawCenteredString(this.font, "SILICON FPS  •  CLIENT SIDE", center,
                Math.min(this.height - 20, panelTop + 286), 0xFFAEB7C3);
    }

    private void drawButton(GuiGraphics graphics, String label, int x, int y, int width, int height,
                            int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        int border = hovered ? 0xFFE4E8EF : 0xFF66717F;
        int fill = hovered ? 0xFF303943 : 0xFF171C23;
        graphics.fill(x, y, x + width, y + height, border);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, fill);
        graphics.fill(x + 7, y + 5, x + 9, y + height - 5, hovered ? 0xFFE7EBF1 : 0xFFB8C0CC);
        graphics.drawCenteredString(this.font, label, x + width / 2, y + (height - 8) / 2,
                hovered ? 0xFFFFFFFF : 0xFFE1E5EA);
    }
}
