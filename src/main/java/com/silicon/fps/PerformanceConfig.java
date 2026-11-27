package com.silicon.fps;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PerformanceConfig {
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("siliconfps.properties");

    public static volatile boolean reduceAmbientParticles = true;
    public static volatile boolean showPerformanceHud = true;
    public static volatile boolean lowEndPresetApplied = false;

    private PerformanceConfig() {
    }

    public static void load() {
        Properties properties = new Properties();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (Files.notExists(CONFIG_PATH)) {
                properties.setProperty("reduceAmbientParticles", "true");
                properties.setProperty("showPerformanceHud", "true");
                properties.setProperty("lowEndPresetApplied", "false");
                write(properties);
            }
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                properties.load(reader);
            }
            reduceAmbientParticles = Boolean.parseBoolean(properties.getProperty("reduceAmbientParticles", "true"));
            showPerformanceHud = Boolean.parseBoolean(properties.getProperty("showPerformanceHud", "true"));
            lowEndPresetApplied = Boolean.parseBoolean(properties.getProperty("lowEndPresetApplied", "false"));
            SiliconFpsClient.LOGGER.info("Ambient particle reduction: {}", reduceAmbientParticles ? "ON" : "OFF");
            SiliconFpsClient.LOGGER.info("Performance HUD: {}", showPerformanceHud ? "ON" : "OFF");
        } catch (IOException exception) {
            SiliconFpsClient.LOGGER.warn("Could not load SILICON FPS settings; using safe defaults.", exception);
        }
    }

    /**
     * Applies conservative low-end video settings. Entity distance and simulation
     * distance are deliberately preserved for multiplayer visibility and gameplay.
     */
    public static void applyLowEndPreset(Minecraft minecraft) {
        if (minecraft == null) return;

        var options = minecraft.options;
        options.renderDistance().set(2);
        options.particles().set(ParticleStatus.MINIMAL);
        options.cloudStatus().set(CloudStatus.OFF);
        options.ambientOcclusion().set(false);
        options.entityShadows().set(false);
        options.weatherRadius().set(3);
        options.biomeBlendRadius().set(0);
        options.chunkSectionFadeInTime().set(0.0);
        options.mipmapLevels().set(0);
        options.save();

        reduceAmbientParticles = true;
        showPerformanceHud = true;
        lowEndPresetApplied = true;
        save();
        SiliconFpsClient.LOGGER.info("Applied SILICON LOW-END preset.");
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("reduceAmbientParticles", Boolean.toString(reduceAmbientParticles));
        properties.setProperty("showPerformanceHud", Boolean.toString(showPerformanceHud));
        properties.setProperty("lowEndPresetApplied", Boolean.toString(lowEndPresetApplied));
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            write(properties);
        } catch (IOException exception) {
            SiliconFpsClient.LOGGER.warn("Could not save SILICON FPS settings.", exception);
        }
    }

    private static void write(Properties properties) throws IOException {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            properties.store(writer, "SILICON FPS settings");
        }
    }
}
