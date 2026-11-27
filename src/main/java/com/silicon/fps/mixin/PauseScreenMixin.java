package com.silicon.fps.mixin;

import com.silicon.fps.PauseHubScreen;
import com.silicon.fps.SiliconPlayerPreview;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
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

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void siliconfps$buildCustomPauseMenu(CallbackInfo ci) {
        ci.cancel();

        int center = this.width / 2;
        int top = Math.max(48, this.height / 2 - 54);
        int mainWidth = Math.min(250, this.width - 176);
        if (mainWidth < 150) mainWidth = Math.max(120, this.width - 150);
        int mainLeft = center - mainWidth / 2 + 52;
        int rowGap = 5;
        int half = (mainWidth - rowGap) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("BACK TO GAME"),
                button -> this.minecraft.setScreen(null))
                .bounds(mainLeft, top, mainWidth, 22).build());
        this.addRenderableWidget(Button.builder(Component.literal("ADVANCEMENTS"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(mainLeft, top + 28, half, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("STATISTICS"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(mainLeft + half + rowGap, top + 28, half, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("OPTIONS"),
                button -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options)))
                .bounds(mainLeft, top + 53, half, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("SILICON MODULES"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(mainLeft + half + rowGap, top + 53, half, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("DISCONNECT"),
                button -> this.minecraft.disconnect(new TitleScreen(), false))
                .bounds(mainLeft, top + 78, mainWidth, 22).build());

        int tabWidth = Math.min(66, Math.max(48, (this.width - 24) / 4));
        int tabGap = 3;
        int tabsWidth = tabWidth * 4 + tabGap * 3;
        int tabsLeft = this.width - tabsWidth - 12;
        String[] labels = {"ACCOUNTS", "WARDROBE", "PERF", "MODULES"};
        PauseHubScreen.Tab[] tabs = {
                PauseHubScreen.Tab.ACCOUNTS, PauseHubScreen.Tab.WARDROBE,
                PauseHubScreen.Tab.MODULES, PauseHubScreen.Tab.MODULES
        };
        for (int i = 0; i < labels.length; i++) {
            final PauseHubScreen.Tab selected = tabs[i];
            this.addRenderableWidget(Button.builder(Component.literal(labels[i]),
                    button -> this.minecraft.setScreen(new PauseHubScreen(this, selected)))
                    .bounds(tabsLeft + i * (tabWidth + tabGap), 12, tabWidth, 19).build());
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void siliconfps$renderSkinOnlyPreview(GuiGraphics graphics, int mouseX, int mouseY,
                                                   float delta, CallbackInfo ci) {
        if (this.minecraft == null) return;
        int center = this.width / 2;
        int menuWidth = Math.min(440, this.width - 170);
        int menuLeft = center - menuWidth / 2 + 26;
        int menuTop = Math.max(38, this.height / 2 - 116);
        int mainWidth = Math.min(250, this.width - 176);
        if (mainWidth < 150) mainWidth = Math.max(120, this.width - 150);
        int mainLeft = center - mainWidth / 2 + 52;
        int top = Math.max(48, this.height / 2 - 54);
        int half = (mainWidth - 5) / 2;

        graphics.fill(menuLeft, menuTop, menuLeft + menuWidth, menuTop + 230, 0xE9090710);
        graphics.fill(menuLeft + 16, menuTop + 18, menuLeft + menuWidth - 16, menuTop + 19, 0xFFB8C0CC);
        graphics.drawCenteredString(this.font, "S I L I C O N", center + 26, menuTop + 30, 0xFFE6D8FF);
        graphics.drawCenteredString(this.font, "GAME MENU", center + 26, menuTop + 45, 0xFFC0C6D0);
        siliconfps$drawButton(graphics, "BACK TO GAME", mainLeft, top, mainWidth, 22, mouseX, mouseY);
        siliconfps$drawButton(graphics, "ADVANCEMENTS", mainLeft, top + 28, half, 20, mouseX, mouseY);
        siliconfps$drawButton(graphics, "STATISTICS", mainLeft + half + 5, top + 28, half, 20, mouseX, mouseY);
        siliconfps$drawButton(graphics, "OPTIONS", mainLeft, top + 53, half, 20, mouseX, mouseY);
        siliconfps$drawButton(graphics, "SILICON MODULES", mainLeft + half + 5, top + 53, half, 20, mouseX, mouseY);
        siliconfps$drawButton(graphics, "DISCONNECT", mainLeft, top + 78, mainWidth, 22, mouseX, mouseY);

        int tabWidth = Math.min(66, Math.max(48, (this.width - 24) / 4));
        int tabLeft = this.width - (tabWidth * 4 + 9) - 12;
        String[] tabLabels = {"ACCOUNTS", "WARDROBE", "PERF", "MODULES"};
        for (int i = 0; i < tabLabels.length; i++) {
            siliconfps$drawButton(graphics, tabLabels[i], tabLeft + i * (tabWidth + 3), 12,
                    tabWidth, 19, mouseX, mouseY);
        }

        int left = 14;
        int profileTop = Math.max(38, this.height / 2 - 92);
        int boxWidth = Math.min(150, Math.max(104, this.width / 4));
        int boxHeight = Math.min(194, this.height - profileTop - 12);
        graphics.fill(left, profileTop, left + boxWidth, profileTop + boxHeight, 0xC9000000);
        graphics.fill(left, profileTop, left + boxWidth, profileTop + 2, 0xFFB8C0CC);
        graphics.drawCenteredString(this.font, "PLAYER PROFILE", left + boxWidth / 2, profileTop + 9, 0xFFE0E4EA);
        SiliconPlayerPreview.render(graphics, left + 8, profileTop + 27,
                left + boxWidth - 8, profileTop + boxHeight - 24, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.minecraft.getGameProfile().getName(),
                left + boxWidth / 2, profileTop + boxHeight - 14, 0xFFB8C0CC);
    }

    private void siliconfps$drawButton(GuiGraphics graphics, String label, int x, int y, int width,
                                       int height, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        graphics.fill(x, y, x + width, y + height, hovered ? 0xFFE4E8EF : 0xFF66717F);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1,
                hovered ? 0xFF303943 : 0xFF171C23);
        graphics.drawCenteredString(this.font, label, x + width / 2, y + (height - 8) / 2,
                hovered ? 0xFFFFFFFF : 0xFFE1E5EA);
    }
}
