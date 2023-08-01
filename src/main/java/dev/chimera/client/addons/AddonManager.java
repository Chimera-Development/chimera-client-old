package dev.chimera.client.addons;

import dev.chimera.client.ChimeraClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;

public class AddonManager {

    public static ArrayList<AbstractAddon> ADDON_LIST = new ArrayList<>();

    public static void initAddons() {

        addSampleAddon();

        for (EntrypointContainer<AbstractAddon> entrypointContainer : FabricLoader.getInstance().getEntrypointContainers("chimera-addon", AbstractAddon.class)) {
            ModMetadata metadata = entrypointContainer.getProvider().getMetadata();
            AbstractAddon addon = entrypointContainer.getEntrypoint();

            addon.MOD_ID = metadata.getId();
            addon.name = metadata.getName();

            ADDON_LIST.add(addon);

            addon.onInitialize();

        }
    }

    public static void addSampleAddon() {
        AbstractAddon addon = new AbstractAddon() {
            @Override
            public void onInitialize() {
                ChimeraClient.LOG.error("Initialized example addon");
            }
        };

        addon.name = "Sample Addon";
        addon.MOD_ID = "chimera-sample-addon";
        ADDON_LIST.add(addon);
    }


}
