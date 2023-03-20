package dev.chimera.managers.modules;

import dev.chimera.ChimeraClient;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.reflect.Constructor;

public class ModuleManager {
    @Getter
    private static final List<AbstractModule> modules = new ArrayList<>();

    public void initializeModules() {

        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    public static void registerModule(Class<? extends AbstractModule> clazz) {
        // Def not stolen from coffee
        AbstractModule instance = null;
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterCount() != 0) {
                throw new IllegalArgumentException(clazz.getName() + " has invalid constructor: expected " + clazz.getName() + "(), got " + declaredConstructor);
            }
            try {
                instance = (AbstractModule) declaredConstructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to make instance of " + clazz.getName(), e);
            }
        }
        if (instance == null) {
            throw new IllegalArgumentException("Failed to make instance of " + clazz.getName());
        }

        modules.add(instance);
    }

    public static List<AbstractModule> getEnabledModuleList() {
        return modules.stream()
                .filter(AbstractModule::isEnabled)
                .collect(Collectors.toList());
    }

    public static AbstractModule findModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static AbstractModule findModule(Class<? extends AbstractModule> clazz) {
        return modules.stream()
                .filter(m -> m.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }
}

