package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListenerIDs;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.KeyEvents;
import dev.chimera.amalthea.events.misc.TickEvent;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleInitializer {
    //DO NOT CHANGE THIS MAP, IT SHOULD ONLY BE ACCESSED AT STARTUP AND NOT CHANGED LATER ON (UNLESS YOU KNOW WHAT YOU'RE DOING)
    public static final HashMap<String, Module> MODULE_NAMES = new HashMap<>();
    public static final HashMap<Integer, Module> MODULE_KEYBINDS = new HashMap<>();

    public static ArrayList<Module> ENABLED_MODULES = new ArrayList<>();
    public static ArrayList<Module> DISABLED_MODULES = new ArrayList<>();

    private static boolean initialized = false;

    public void initializeModules() {
        MODULE_NAMES.values().forEach((v) -> {
            v.init();
            MODULE_KEYBINDS.put(v.getKeyBinding(), v);
        });
        ChimeraClient.LOGGER.error(MODULE_NAMES.toString());
        ChimeraClient.LOGGER.error(MODULE_KEYBINDS.toString());
        initialized = true;

        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }


    public static <T extends Module> void addModule(T module) {
        MODULE_NAMES.put(module.getModuleName(), module);
    }

    public static List<Module> getAllModules() {
        return MODULE_KEYBINDS.values().stream().toList();
    }

    public static List<Module> getEnabledModuleList() {
        return MODULE_NAMES.values().stream()
                .filter(Module::getModuleEnabled)
                .collect(Collectors.toList());
    }


    @EventListener(id = EventListenerIDs.moduleInitializerTickEventStart)
    public static void onTickStart(TickEvent.Start event) {
        if (!initialized || MinecraftClient.getInstance().player == null) {
            return;
        }
        MODULE_NAMES.values().forEach(module -> module.onTickStart(event));
    }

    @EventListener(id = EventListenerIDs.moduleInitializerTickEventEnd)
    public static void onTickEnd(TickEvent.End event) {
        if (!initialized || MinecraftClient.getInstance().player == null) {
            return;
        }
        MODULE_NAMES.values().forEach(module -> module.onTickEnd(event));
    }


    public static Module findModule(String name) {
        if (MODULE_NAMES.containsKey(name)) {
            return MODULE_NAMES.get(name);
        }
        return null;
    }

    @EventListener(id = EventListenerIDs.moduleInitializerKeyPress)
    public static void onKeyPress(KeyEvents.InGame.Press event) {
        MODULE_KEYBINDS.computeIfPresent(event.key, (k, v) -> {
            v.toggle();
//            v.sendToggledMsg();
            return v;
        });

    }

    @EventListener(id = EventListenerIDs.moduleInitializerKeyRelease)
    public static void onKeyRelease(KeyEvents.InGame.Press event) {

        MODULE_KEYBINDS.computeIfPresent(event.key, (k, v) -> {
            if (v.releaseToToggle) {
            }
            return v;
        });
    }
}

