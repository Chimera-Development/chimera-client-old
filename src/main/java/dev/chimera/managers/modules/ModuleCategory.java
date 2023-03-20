package dev.chimera.managers.modules;

public record ModuleCategory(String name) {

    public static ModuleCategory PLAYER = new ModuleCategory("Player");
    public static ModuleCategory COMBAT = new ModuleCategory("Combat");
    public static ModuleCategory WORLD = new ModuleCategory("World");
    public static ModuleCategory RENDER = new ModuleCategory("Render");
    public static ModuleCategory MISC = new ModuleCategory("Misc");

}
