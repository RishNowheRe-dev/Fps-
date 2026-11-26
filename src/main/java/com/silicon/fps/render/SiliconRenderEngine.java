package com.silicon.fps.render;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

/**
 * First runtime layer of the SILICON Render Engine.
 *
 * This milestone instruments the vanilla world-render phases without
 * replacing them. A future backend can take over chunk extraction,
 * visibility, buffer construction and GPU submission behind this boundary.
 *
 * The measured interval is CPU wall-clock time between Fabric's START_MAIN
 * and END_MAIN callbacks; it is not a GPU timer and must not be presented as
 * pure GPU execution time.
 */
public final class SiliconRenderEngine {
    private static volatile boolean initialized;
    private static volatile long mainPassStartNanos;
    private static volatile double averageMainPassMillis;
    private static volatile double peakMainPassMillis;
    private static volatile long measuredPasses;

    private SiliconRenderEngine() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        WorldRenderEvents.START_MAIN.register(context ->
                mainPassStartNanos = System.nanoTime()
        );

        WorldRenderEvents.END_MAIN.register(context -> {
            long started = mainPassStartNanos;
            if (started == 0L) {
                return;
            }

            long elapsed = System.nanoTime() - started;
            mainPassStartNanos = 0L;

            if (elapsed <= 0L) {
                return;
            }

            double millis = elapsed / 1_000_000.0;
            // Exponential moving average smooths noisy individual frames.
            averageMainPassMillis = measuredPasses == 0L
                    ? millis
                    : averageMainPassMillis * 0.90 + millis * 0.10;
            peakMainPassMillis = Math.max(peakMainPassMillis, millis);
            measuredPasses++;
        });

        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static double getAverageMainPassMillis() {
        return averageMainPassMillis;
    }

    public static double getPeakMainPassMillis() {
        return peakMainPassMillis;
    }

    public static long getMeasuredPasses() {
        return measuredPasses;
    }

    public static String getBackendName() {
        return "VANILLA (INSTRUMENTED)";
    }
}
