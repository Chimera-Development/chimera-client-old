package dev.chimera.client.registries;

import dev.chimera.client.modules.AbstractModule;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;

import static dev.chimera.client.ChimeraClient.MOD_ID;


public class ModuleRegistry {


    public static RegistryKey<Registry<AbstractModule>> registryKey = RegistryKey.ofRegistry(new Identifier(MOD_ID, "modules"));

    public static final Registry<AbstractModule> MODULE_REGISTRY = FabricRegistryBuilder
            .createSimple(registryKey)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public ModuleRegistry() {

    }

}
