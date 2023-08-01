package dev.chimera.client.modules;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public abstract class AbstractModule {

    public Identifier MODULE_ID;
    protected boolean ENABLED = false;
    public String MODULE_NAME = "";

    /*moduleId should be in the following format -
      namespace:moduleName
    */

    public AbstractModule(Identifier moduleId, String moduleName) {

        if (moduleId.getPath().equals("") || moduleName.equals(""))
            throw new RuntimeException("You need to specify a name and namespace for every module");

        MODULE_NAME = moduleName;

        MODULE_ID = moduleId;

        ModuleManager.registerModule(this);
    }

    public boolean isEnabled() {
        return ENABLED;
    }

    public void enable() {
        if (ENABLED) return;
        ENABLED = true;
        ModuleManager.addEnabledModule(this);
        onEnable();
        onStateChange(true);
    }

    public void disable() {
        if (!ENABLED) return;
        ENABLED = false;
        ModuleManager.removeEnabledModule(this);
        onDisable();
        onStateChange(false);
    }

    public KeyBinding registerKeybind(int key, String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                key,
                "category.dev.chimera.client"
        ));
    }

    public boolean toggle() {
        if (ENABLED) disable();
        else enable();

        return ENABLED;
    }

    public void onStateChange(boolean newState) {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
