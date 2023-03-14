package dev.chimera.sisyphus;

import dev.chimera.ChimeraClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;

public class AddonInitializer {

    public static ArrayList<Addon> ADDON_LIST = new ArrayList<>();

    public static void initAddons(){
        addSampleAddon();
        for(EntrypointContainer<Addon> entrypointContainer: FabricLoader.getInstance().getEntrypointContainers("chimera-addon", Addon.class)){
            ModMetadata metadata = entrypointContainer.getProvider().getMetadata();
            Addon addon = entrypointContainer.getEntrypoint();
//            metadata.getIconPath();
            addon.MOD_ID = metadata.getId();
            addon.name = metadata.getName();
            addon.onInitialize();

            ADDON_LIST.add(addon);
        }
    }

    public static void addSampleAddon(){
        Addon addon = new Addon() {
            @Override
            public void onInitialize() {
                ChimeraClient.LOGGER.error("Hi sussy bakas");
            }
        };
        addon.name = "bozo";
        addon.MOD_ID= "chimera_vector";
        ADDON_LIST.add(addon);
    }

}