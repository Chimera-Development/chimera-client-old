package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.combat.KillAuraModule;
import dev.chimera.modules.player.NoFallModule;
import net.minecraft.client.MinecraftClient;

import dev.chimera.modules.player.FlightModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleInitializer {
    private static boolean INITIALIZED = false;
    private static List<Module> MODULE_LIST = new ArrayList<>();

    private static void buildModuleList() {

        /* Register all modules here in the format `MODULE_LIST.add(new YourModule());` */

        MODULE_LIST.add(new ExampleModule());
        MODULE_LIST.add(new FlightModule());
        MODULE_LIST.add(new NoFallModule());
        MODULE_LIST.add(new KillAuraModule());

    }
    public void initializeModules() {
        buildModuleList();

        for (Module module : MODULE_LIST) {
            module.init();
        }

        INITIALIZED = true;

        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    public static List<Module> getModuleList() {
        return MODULE_LIST;
    }

    public static List<Module> getEnabledModuleList() {
        return new ArrayList<Module>(MODULE_LIST.stream().filter(Module::getModuleEnabled).toList());
    }

    public static Module findModule(String name) {
        for (Module module : MODULE_LIST) {
            if (module.getModuleName().equals(name)) {
                return module;
            }
        }
        return null;
    }

    @EventListener( tag = "start" )
    public static void onTickStart(TickEvent event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickStart(event);
        }
    }

    @EventListener( tag = "end" )
    public static void onTickEnd(TickEvent event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickEnd(event);
        }
    }

    @EventListener( tag = "key-press" )
    public static void onKeyPress(String key) {
        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(key)).toList();
        toToggleModulesList.forEach(Module::toggle);
    }

    @EventListener( tag = "key-release" )
    public static void onKeyRelease(String key) {
        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(key) && module.releaseToToggle).toList();
        toToggleModulesList.forEach(Module::toggle);
    }
}
