package com.silicon.fps.mixin;

import com.silicon.fps.PauseHubScreen;
import com.silicon.fps.PerformanceConfigScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    protected PauseScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void siliconfps$addPauseControls(CallbackInfo ci) {
        int right = this.width - 12;
        int buttonWidth = Math.min(86, Math.max(62, (this.width - 28) / 4));
        int gap = 5;
        int x = right - (buttonWidth * 3 + gap * 2);
        int y = 12;

        this.addRenderableWidget(Button.builder(Component.literal("ACCOUNTS"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.ACCOUNTS)))
                .bounds(x, y, buttonWidth, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("WARDROBE"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.WARDROBE)))
                .bounds(x + buttonWidth + gap, y, buttonWidth, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("PERFORMANCE"),
                button -> this.minecraft.setScreen(new PerformanceConfigScreen(this)))
                .bounds(x + (buttonWidth + gap) * 2, y, buttonWidth, 20).build());

        int moduleWidth = Math.min(200, Math.max(120, this.width / 4));
        this.addRenderableWidget(Button.builder(Component.literal("SILICON MODULES"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(this.width / 2 - moduleWidth / 2, this.height - 43, moduleWidth, 20).build());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void siliconfps$renderPlayerPreview(GuiGraphics graphics, int mouseX, int mouseY,
                                                  float delta, CallbackInfo ci) {
        if (this.minecraft == null || this.minecraft.player == null) return;
        int left = Math.max(18, this.width / 2 - 300);
        int top = Math.max(40, this.height / 2 - 112);
        int boxWidth = Math.min(150, this.width / 4);
        int boxHeight = Math.min(250, this.height - 100);
        graphics.fill(left, top, left + boxWidth, top + boxHeight, 0x66000000);
        graphics.fill(left, top, left + boxWidth, top + 1, 0xAA9D79D0);
        graphics.drawCenteredString(this.font, "PLAYER PROFILE", left + boxWidth / 2, top + 10, 0xFFD8C8F4);
        graphics.drawCenteredString(this.font, this.minecraft.player.getGameProfile().name(),
                left + boxWidth / 2, top + boxHeight - 18, 0xFFB9A8D5);

        // Uses the live client player: the actual account skin and Steve/Alex model
        // are rendered by Minecraft's own entity renderer. Pointer movement rotates it.
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                graphics, left + 8, top + 30, left + boxWidth - 8, top + boxHeight - 28,
                72, 0.0F, mouseX, mouseY, this.minecraft.player);
    }
}
