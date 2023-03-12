package dev.chimera.modules;

import dev.chimera.amalthea.events.misc.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class Module {
    private final ModuleCategory MODULE_CATEGORY;
    private final String MODULE_NAME;

    public Module(ModuleCategory MODULE_CATEGORY, String MODULE_NAME) {
        this.MODULE_CATEGORY = MODULE_CATEGORY;
        this.MODULE_NAME = MODULE_NAME;
    }

    private boolean MODULE_ENABLED;
    private int KEY_BINDING;
    public boolean releaseToToggle;

    public Module(ModuleCategory category, String name, int bind, boolean toggleOnRelease) {
        MODULE_CATEGORY = category;
        MODULE_NAME = name;
        MODULE_ENABLED = false;
        KEY_BINDING = bind;
        releaseToToggle = toggleOnRelease;
    }


    public Module(ModuleCategory category, String name, boolean toggleOnRelease) {
        this(category, name, -1, toggleOnRelease);
    }

    public Module(ModuleCategory category, String name, int bind) {
        this(category, name, bind, false);
    }

    public void sendToggledMsg() {
//         (this.hashCode(), Formatting.GRAY, "Toggled (highlight)%s(default) %s(default).", title, isActive() ? Formatting.GREEN + "on" : Formatting.RED + "off");
        String state = this.MODULE_ENABLED ? "enabled" : "disabled";
        MutableText message = Text.literal(this.MODULE_NAME + " has been " + state);
        message.setStyle(message.getStyle().withFormatting(Formatting.AQUA));
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }

    public ModuleCategory getModuleCategory() {
        return MODULE_CATEGORY;
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public boolean getModuleEnabled() {
        return MODULE_ENABLED;
    }

    public int getKeyBinding() {
        return KEY_BINDING;
    }

    public void setKeyBinding(int bind) {
        KEY_BINDING = bind;
    }

    public boolean keyBindingMatches(int compareBind) {
        return KEY_BINDING == compareBind;
    }

    public void setModuleState(boolean state) {
        try {
            if (state != MODULE_ENABLED) {
                if (state) onEnable();
                else onDisable();
            }
            MODULE_ENABLED = state;
        } catch (Exception e) {
          System.out.println("Exception while enabling module...");
          e.printStackTrace();
        }

//        sendToggledMsg();
    }

    public void toggle() {
        setModuleState(!getModuleEnabled());
        sendToggledMsg();
    }

    public abstract void init();

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onTickStart(TickEvent.Start event);

    public abstract void onTickEnd(TickEvent.End event);
}
