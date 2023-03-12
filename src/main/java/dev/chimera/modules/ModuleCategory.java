package dev.chimera.modules;

public class ModuleCategory {

    public static ModuleCategory PLAYER = new ModuleCategory("Player");
    public static ModuleCategory COMBAT = new ModuleCategory("Combat");
    public static ModuleCategory WORLD = new ModuleCategory("World");
    public static ModuleCategory RENDER = new ModuleCategory("Render");
    public static ModuleCategory MISC = new ModuleCategory("Misc");

    private final String name;
    public ModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
