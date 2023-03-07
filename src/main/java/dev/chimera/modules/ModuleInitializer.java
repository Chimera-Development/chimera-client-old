package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.EventListeners;
import dev.chimera.amalthea.EventListener;
import dev.chimera.amalthea.events.misc.KeyEvents;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.combat.KillAuraModule;
import dev.chimera.modules.common.FarmAuraModule;
import dev.chimera.modules.player.NoFallModule;
import net.minecraft.client.MinecraftClient;

import dev.chimera.modules.player.FlightModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleInitializer {
    private static boolean INITIALIZED = false;
    private static final List<Module> MODULE_LIST = new ArrayList<>();

    private static void buildModuleList() {

        /* Register all modules here in the format `MODULE_LIST.add(new YourModule());` */

        MODULE_LIST.add(new ExampleModule());
        MODULE_LIST.add(new FlightModule());
        MODULE_LIST.add(new NoFallModule());
        MODULE_LIST.add(new KillAuraModule());
        MODULE_LIST.add(new FarmAuraModule());

    }
    public void initializeModules() {
        buildModuleList();

        MODULE_LIST.forEach(Module::init);

        INITIALIZED = true;

        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    public static void addModules(List<Module> moduleList) {
        MODULE_LIST.addAll(moduleList);
        moduleList.forEach(Module::init);
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

    @EventListener( id = EventListeners.moduleInitializerTickEventStart )
    public static void onTickStart(TickEvent.Start event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickStart(event);
        }
    }

    @EventListener( id = EventListeners.moduleInitializerTickEventEnd )
    public static void onTickEnd(TickEvent.End event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickEnd(event);
        }
    }

    @EventListener( id = EventListeners.moduleInitializerKeyPress )
    public static void onKeyPress(KeyEvents.Press event) {
        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(event.key)).toList();
        toToggleModulesList.forEach(Module::toggle);
    }

    @EventListener( id = EventListeners.moduleInitializerKeyRelease )
    public static void onKeyRelease(KeyEvents.Release event) {
        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(event.key) && module.releaseToToggle).toList();
        toToggleModulesList.forEach(Module::toggle);
    }
}
