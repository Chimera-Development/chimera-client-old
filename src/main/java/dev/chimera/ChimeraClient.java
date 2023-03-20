package dev.chimera;

import dev.chimera.amalthea.eventbus.EventBus;
import dev.chimera.managers.modules.ModuleManager;
import dev.chimera.nemean.GuiLayer;
import dev.chimera.nemean.elements.Gui;
import dev.chimera.sisyphus.AddonInitializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChimeraClient implements ClientModInitializer {
    public static final String MOD_ID = "chimera-client";
    public static final Logger LOGGER = LoggerFactory.getLogger("chimera-client");
    public static final EventBus EVENT_BUS = new EventBus();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Chimera sussers!");

        new GuiLayer();
        new Gui();

        AddonInitializer.initAddons();

        ModuleManager.initializeModules();
    }

}