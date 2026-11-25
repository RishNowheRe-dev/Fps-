package com.silicon.fps;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class PerformanceControls {
    private static final KeyMapping TOGGLE_AMBIENT_PARTICLES = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.siliconfps.toggle_ambient_particles",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_O,
                    KeyMapping.Category.register(
                            Identifier.fromNamespaceAndPath(SiliconFpsClient.MOD_ID, "controls")
                    )
            )
    );

    private PerformanceControls() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_AMBIENT_PARTICLES.consumeClick()) {
                PerformanceConfig.reduceAmbientParticles = !PerformanceConfig.reduceAmbientParticles;
                PerformanceConfig.save();

                if (client.player != null) {
                    String state = PerformanceConfig.reduceAmbientParticles ? "ON" : "OFF";
                    client.player.displayClientMessage(
                            Component.literal("SILICON FPS: Ambient particles " + state),
                            true
                    );
                }

                SiliconFpsClient.LOGGER.info(
                        "Ambient particle reduction toggled {}",
                        PerformanceConfig.reduceAmbientParticles ? "ON" : "OFF"
                );
            }
        });
    }
}
