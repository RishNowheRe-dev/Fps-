package com.silicon.fps.mixin;

import com.silicon.fps.PauseHubScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
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
    private void siliconfps$addHubButton(CallbackInfo ci) {
        int buttonWidth = Math.min(112, Math.max(82, this.width / 5));
        this.addRenderableWidget(Button.builder(Component.literal("SILICON HUB"),
                button -> this.minecraft.setScreen(new PauseHubScreen(this, PauseHubScreen.Tab.MODULES)))
                .bounds(this.width - buttonWidth - 12, 12, buttonWidth, 20).build());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void siliconfps$renderSkinOnlyPreview(GuiGraphics graphics, int mouseX, int mouseY,
                                                   float delta, CallbackInfo ci) {
        if (this.minecraft == null || this.minecraft.player == null) return;
        var player = this.minecraft.player;
        int left = Math.max(18, this.width / 2 - 300);
        int top = Math.max(40, this.height / 2 - 112);
        int boxWidth = Math.min(150, this.width / 4);
        int boxHeight = Math.min(250, this.height - 100);

        graphics.fill(left, top, left + boxWidth, top + boxHeight, 0x66000000);
        graphics.fill(left, top, left + boxWidth, top + 1, 0xAA9D79D0);
        graphics.drawCenteredString(this.font, "PLAYER PROFILE", left + boxWidth / 2, top + 10, 0xFFD8C8F4);
        graphics.drawCenteredString(this.font, player.getGameProfile().name(),
                left + boxWidth / 2, top + boxHeight - 18, 0xFFB9A8D5);

        // Hide equipped gear only for this one render pass, then restore every slot.
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHand = player.getItemBySlot(EquipmentSlot.OFFHAND);
        try {
            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics, left + 8, top + 30, left + boxWidth - 8, top + boxHeight - 28,
                    72, 0.0F, mouseX, mouseY, player);
        } finally {
            player.setItemSlot(EquipmentSlot.HEAD, head);
            player.setItemSlot(EquipmentSlot.CHEST, chest);
            player.setItemSlot(EquipmentSlot.LEGS, legs);
            player.setItemSlot(EquipmentSlot.FEET, feet);
            player.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
            player.setItemSlot(EquipmentSlot.OFFHAND, offHand);
        }
    }
}
