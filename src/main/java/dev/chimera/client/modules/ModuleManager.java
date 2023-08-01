package dev.chimera.client.modules;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

import static dev.chimera.client.ChimeraClient.MOD_ID;

public class ModuleManager {
    //TODO Implement categories

    public static HashMap<Class<? extends AbstractModule>, AbstractModule> ACTIVE_MODULES = new HashMap<>();

    public static HashMap<Class<? extends AbstractModule>, AbstractModule> MODULES = new HashMap<>();

//    public static HashMap<Identifier, AbstractModule> MODULES_BY_CATEGORY = new HashMap<>();

    public static void addEnabledModule(AbstractModule module) {
        ACTIVE_MODULES.put(module.getClass(), module);
    }

    public static void registerModule(AbstractModule module) {
        MODULES.put(module.getClass(), module);

//        MODULES_BY_CATEGORY.put(module.CATEGORY.getCategoryId(), module);
    }

    public static Object getModule(Class<? extends AbstractModule> clazz) {
        return MODULES.get(clazz);
    }

    public static void removeEnabledModule(AbstractModule module) {
        ACTIVE_MODULES.remove(module.getClass());
    }

    public static List<AbstractModule> getActiveModules() {
        return ACTIVE_MODULES.values().parallelStream().toList();
    }

    public static List<AbstractModule> getAllModules() {
        return MODULES.values().parallelStream().toList();
    }

}
