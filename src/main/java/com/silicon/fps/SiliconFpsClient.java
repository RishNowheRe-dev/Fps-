package com.silicon.fps;

import com.silicon.fps.render.SiliconRenderEngine;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiliconFpsClient implements ClientModInitializer {

    public static final String MOD_ID = "siliconfps";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        PerformanceConfig.load();
        SiliconScreenTheme.register();
        SiliconRenderEngine.initialize();
        PerformanceHud.register();
        PerformanceControls.register();
        LOGGER.info("SILICON FPS initialized successfully!");
        LOGGER.info("SILICON Render Engine telemetry initialized (vanilla backend retained).");
    }
}
