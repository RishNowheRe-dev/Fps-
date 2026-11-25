package com.silicon.fps.mixin;

import com.silicon.fps.PerformanceConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class AmbientParticleMixin {
    @Unique
    private static long siliconfps$ambientParticleCounter;

    @Inject(
            method = "createParticle",
            at = @At("HEAD"),
            cancellable = true
    )
    private <T extends ParticleOptions> void siliconfps$reduceAmbientParticles(
            T options,
            double x,
            double y,
            double z,
            double velocityX,
            double velocityY,
            double velocityZ,
            CallbackInfoReturnable<Particle> cir
    ) {
        if (!PerformanceConfig.reduceAmbientParticles || !isAmbient(options.getType())) {
            return;
        }

        // Drop every second selected ambient particle. Combat and interaction
        // feedback particles are deliberately not included in this list.
        if ((siliconfps$ambientParticleCounter++ & 1L) == 0L) {
            cir.setReturnValue(null);
        }
    }

    @Unique
    private static boolean isAmbient(ParticleType<?> type) {
        return type == ParticleTypes.ASH
                || type == ParticleTypes.WHITE_ASH
                || type == ParticleTypes.MYCELIUM
                || type == ParticleTypes.CRIMSON_SPORE
                || type == ParticleTypes.WARPED_SPORE
                || type == ParticleTypes.SPORE_BLOSSOM_AIR
                || type == ParticleTypes.CAMPFIRE_COSY_SMOKE
                || type == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE
                || type == ParticleTypes.CHERRY_LEAVES
                || type == ParticleTypes.PALE_OAK_LEAVES
                || type == ParticleTypes.FIREFLY;
    }
}
