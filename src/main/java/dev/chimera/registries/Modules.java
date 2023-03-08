package dev.chimera.registries;

import dev.chimera.ChimeraClient;
import dev.chimera.modules.Module;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Modules {

    public static final RegistryKey<Registry<Module>> MODULE_REGISTRY_KEY =
        RegistryKey.ofRegistry(new Identifier(ChimeraClient.MOD_ID, "modules"));
    public static final Registry<Module> MODULES =
            FabricRegistryBuilder.createSimple(MODULE_REGISTRY_KEY).buildAndRegister();

    public static void registerModules(){

    }


}
