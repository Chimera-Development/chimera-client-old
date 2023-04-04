package dev.chimera;

import dev.chimera.amalthea.eventbus.EventBus;
import dev.chimera.managers.modules.ModuleManager;
import dev.chimera.sisyphus.AddonInitializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final EventBus EVENT_BUS = new EventBus();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Chimera sussers!");

        AddonInitializer.initAddons();

        ModuleManager.initializeModules();
    }

}