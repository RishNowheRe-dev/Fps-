package com.silicon.fps;

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

    private PerformanceConfig() {
    }

    public static void load() {
        Properties properties = new Properties();

        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            if (Files.notExists(CONFIG_PATH)) {
                properties.setProperty("reduceAmbientParticles", "true");
                properties.setProperty("showPerformanceHud", "true");
                write(properties);
            }

            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                properties.load(reader);
            }

            reduceAmbientParticles = Boolean.parseBoolean(
                    properties.getProperty("reduceAmbientParticles", "true")
            );
            showPerformanceHud = Boolean.parseBoolean(
                    properties.getProperty("showPerformanceHud", "true")
            );

            SiliconFpsClient.LOGGER.info(
                    "Ambient particle reduction: {}",
                    reduceAmbientParticles ? "ON" : "OFF"
            );
            SiliconFpsClient.LOGGER.info(
                    "Performance HUD: {}",
                    showPerformanceHud ? "ON" : "OFF"
            );
        } catch (IOException exception) {
            SiliconFpsClient.LOGGER.warn(
                    "Could not load SILICON FPS settings; using safe defaults.",
                    exception
            );
        }
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("reduceAmbientParticles", Boolean.toString(reduceAmbientParticles));
        properties.setProperty("showPerformanceHud", Boolean.toString(showPerformanceHud));

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
