////package dev.chimera.modules;
////
////import dev.chimera.ChimeraClient;
////import dev.chimera.amalthea.EventListenerIDs;
////import dev.chimera.amalthea.eventbus.EventListener;
////import dev.chimera.amalthea.events.misc.KeyEvents;
////
////import java.util.*;
////import java.util.stream.Collectors;
////
//////But yeah like unironically this class needs to be rewritten
/////*  TODO
////    1. Rewrite this to avoid using so many for loops. This will get terribly slow otherwise once there are more than like 30 modules.
////       For example, let's say someone starts spamming the keybind for fly to burst fly. That will end up running a two for loops every press and release.
////       (obviously not good)
////    2. The way we do tickEvents also needs to be revamped. Right now every 1/20th of a second (or about that) we're calling a function for every module.
////       Let's say that out of 30 modules like 5 use this function. That would mean that every second we execute 500(! not as in integral, as in exclamation mark)
////       completely useless requests. (also not very good). All things considered, it might be a better choice to just leave onTickEvents to be registered by every module themselves.
////    3. So yeah all in all we should probably change a lot of this behavior from using Lists (in whichever form they may be) to using more maps (for speeeeed)
////       Probably use a multimap or something of the like for enabled modules. Same might go for keybinds (if we decide to allow binding multiple modules
////       to a single key).
//// */
////
//public class ModuleInitializer {
//
//    private static final List<Module> MODULE_LIST = new ArrayList<>();
//    private static final Map<Integer, Module> KEYBINDING_MAP = new HashMap<>();
//
//    private static final HashMap<Boolean, ArrayList<Module>> MODULE_STATES = new HashMap<>();
//
//    private static boolean initialized = false;
//
//    public static void initializeModules() {
////        registerModules();
//
//        MODULE_LIST.forEach(Module::init);
//
//        initialized = true;
//
//        ChimeraClient.EVENT_BUS.registerListenersInClass(ModuleInitializer.class);
//    }
//
////    private static void registerModules() {
////        MODULE_LIST.add(new ExampleModule());
////        MODULE_LIST.add(new FlightModule());
////        MODULE_LIST.add(new NoFallModule());
////        MODULE_LIST.add(new KillAuraModule());
////        MODULE_LIST.add(new FarmAuraModule());
////        MODULE_LIST.forEach(module -> KEYBINDING_MAP.put(module.getKeyBinding(), module));
////    }
//
//
//    public static void addModule(Module module) {
//        MODULE_LIST.add(module);
//        module.init();
//        KEYBINDING_MAP.put(module.getKeyBinding(), module);
//    }
//
//    public static void addModules(List<Module> moduleList) {
//        MODULE_LIST.addAll(moduleList);
//        moduleList.forEach(module -> {
//            module.init();
//            KEYBINDING_MAP.put(module.getKeyBinding(), module);
//        });
//    }
//
//    public static List<Module> getModuleList() {
//        return MODULE_LIST;
//    }
//
//    public static List<Module> getEnabledModuleList() {
//        return MODULE_LIST.stream()
//                .filter(Module::getModuleEnabled)
//                .collect(Collectors.toList());
//    }
//
//    public static Module findModule(String name) {
//        return MODULE_LIST.stream()
//                .filter(module -> module.getModuleName().equals(name))
//                .findFirst()
//                .orElse(null);
//    }
//
//
//
//    //rewritten
//    @EventListener(id = EventListenerIDs.moduleInitializerKeyPress)
//    public static void onKeyPress(KeyEvents.InGame.Press event) {
//        Module module = KEYBINDING_MAP.get(event.key);
//        if (module != null) {
//            module.toggle();
//        }
//    }
//
//    //rewritten
//    @EventListener(id = EventListenerIDs.moduleInitializerKeyRelease)
//    public static void onKeyRelease(KeyEvents.InGame.Press event) {
//        Module module = KEYBINDING_MAP.get(event.key);
//        if (module != null && module.releaseToToggle) {
//            module.toggle();
//        }
//    }
//}
