package com.silicon.fps.mixin;

import com.silicon.fps.PauseHubScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds the same SILICON hub entry point to the main menu. */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void siliconfps$addHubButton(CallbackInfo ci) {
        int buttonWidth = 112;
        this.addRenderableWidget(Button.builder(Component.literal("SILICON FPS"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(this.width - buttonWidth - 12, 12, buttonWidth, 20).build());
    }
}
