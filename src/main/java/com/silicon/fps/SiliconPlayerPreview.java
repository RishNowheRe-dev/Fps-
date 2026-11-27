package com.silicon.fps;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

/**
 * Lightweight, detached 3D skin preview for SILICON menus.
 *
 * This renders the account skin directly through the GUI skin renderer. It does
 * not create, equip, or mutate the live player entity.
 */
public final class SiliconPlayerPreview {
    private static GameProfile cachedProfile;
    private static Supplier<PlayerSkin> skinLookup;
    private static PlayerModel wideModel;
    private static PlayerModel slimModel;

    private SiliconPlayerPreview() {}

    public static void render(GuiGraphics graphics, int left, int top, int right, int bottom,
                              int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        GameProfile profile = minecraft.getGameProfile();
        if (profile == null || right <= left || bottom <= top) return;

        if (!profile.equals(cachedProfile) || skinLookup == null) {
            cachedProfile = profile;
            skinLookup = minecraft.getSkinManager().createLookup(profile, false);
        }

        PlayerSkin skin;
        try {
            skin = skinLookup.get();
        } catch (RuntimeException exception) {
            skin = DefaultPlayerSkin.get(profile);
        }
        if (skin == null) skin = DefaultPlayerSkin.get(profile);

        PlayerModel model = getModel(minecraft, skin.model());
        model.setAllVisible(true);

        float centerX = (left + right) * 0.5F;
        float normalizedMouse = Math.max(-1.0F, Math.min(1.0F, (mouseX - centerX) / Math.max(1.0F, (right - left) * 0.5F)));
        float yaw = 180.0F + normalizedMouse * 32.0F;
        float pitch = Math.max(-8.0F, Math.min(8.0F, (mouseY - (top + bottom) * 0.5F) * 0.06F));

        graphics.submitSkinRenderState(model, skin.body().texturePath(),
                1.0F, pitch, yaw, 0.0F, left, top, right, bottom);
    }

    private static PlayerModel getModel(Minecraft minecraft, PlayerModelType type) {
        if (type == PlayerModelType.SLIM) {
            if (slimModel == null) {
                slimModel = new PlayerModel(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
            }
            return slimModel;
        }
        if (wideModel == null) {
            wideModel = new PlayerModel(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
        }
        return wideModel;
    }
}
