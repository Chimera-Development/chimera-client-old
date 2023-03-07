package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.EventListeners;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.KeyEvents;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.combat.KillAuraModule;
import dev.chimera.modules.common.FarmAuraModule;
import dev.chimera.modules.player.NoFallModule;
import net.minecraft.client.MinecraftClient;

import dev.chimera.modules.player.FlightModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//But yeah like unironically this class needs to be rewritten
/*  TODO
    1. Rewrite this to avoid using so many for loops. This will get terribly slow otherwise once there are more than like 30 modules.
       For example, let's say someone starts spamming the keybind for fly to burst fly. That will end up running a two for loops every press and release.
       (obviously not good)
    2. The way we do tickEvents also needs to be revamped. Right now every 1/20th of a second (or about that) we're calling a function for every module.
       Let's say that out of 30 modules like 5 use this function. That would mean that every second we execute 500(! not as in integral, as in exclamation mark)
       completely useless requests. (also not very good). All things considered, it might be a better choice to just leave onTickEvents to be registered by every module themselves.
    3. So yeah all in all we should probably change a lot of this behavior from using Lists (in whichever form they may be) to using more maps (for speeeeed)
       Probably use a multimap or something of the like for enabled modules. Same might go for keybinds (if we decide to allow binding multiple modules
       to a single key). 
 */
public class ModuleInitializer {
    private static boolean INITIALIZED = false;
    private static final ArrayList<Module> MODULE_LIST = new ArrayList<>();

    private static final HashMap<Integer, Module> keyBindMap = new HashMap<>();
    //TODO All of this needs to be reworked
    private static HashMap<Boolean, Module> enabledModuleMap = new HashMap<>();

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

    public static void addModule(Module module) {
        MODULE_LIST.add(module);
        module.init();
        keyBindMap.put(module.getKeyBinding(), module);
    }

    public static void addModules(List<Module> moduleList) {
        MODULE_LIST.addAll(moduleList);
        moduleList.forEach((module) -> {
            module.init();
            keyBindMap.put(module.getKeyBinding(), module);
        });
    }

    public static List<Module> getModuleList() {
        return MODULE_LIST;
    }

    //TODO Rework this!
    public static List<Module> getEnabledModuleList() {
        return new ArrayList<Module>(MODULE_LIST.stream().filter(Module::getModuleEnabled).toList());
    }
    //god how many for loops can you fit in a class O_O
    public static Module findModule(String name) {
        for (Module module : MODULE_LIST) {
            if (module.getModuleName().equals(name)) {
                return module;
            }
        }
        return null;
    }

    @EventListener(id = EventListeners.moduleInitializerTickEventStart)
    public static void onTickStart(TickEvent.Start event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickStart(event);
        }
    }

    @EventListener(id = EventListeners.moduleInitializerTickEventEnd)
    public static void onTickEnd(TickEvent.End event) {
        if (MinecraftClient.getInstance().player == null || !INITIALIZED) return;
        for (Module module : MODULE_LIST) {
            module.onTickEnd(event);
        }
    }

    @EventListener(id = EventListeners.moduleInitializerKeyPress)
    public static void onKeyPress(KeyEvents.InGame.Press event) {
//        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(event.key)).toList();
//        toToggleModulesList.forEach(Module::toggle);
        keyBindMap.get(event.key).toggle();
    }

    @EventListener(id = EventListeners.moduleInitializerKeyRelease)
    public static void onKeyRelease(KeyEvents.InGame.Press event) {
//        List<Module> toToggleModulesList = getModuleList().stream().filter((module) -> module.keyBindingMatches(event.key) && module.releaseToToggle).toList();
//        toToggleModulesList.forEach(Module::toggle);
        keyBindMap.get(event.key).toggle();
    }
}
