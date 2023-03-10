package dev.chimera.modules;

import dev.chimera.ChimeraClient;
import dev.chimera.Utils.ChimeraLogger;
import dev.chimera.amalthea.events.misc.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class Module {
    private final String MODULE_NAME;

    public Module(String MODULE_NAME) {
        this.MODULE_NAME = MODULE_NAME;
    }

    private boolean MODULE_ENABLED;
    private int KEY_BINDING;
    public boolean releaseToToggle;

    public Module(String name, int bind, boolean toggleOnRelease) {
        MODULE_NAME = name;
        MODULE_ENABLED = false;
        KEY_BINDING = bind;
        releaseToToggle = toggleOnRelease;
    }


    public Module(String name, boolean toggleOnRelease) {
        this(name, -1, toggleOnRelease);
    }

    public Module(String name, int bind) {
        this(name, bind, false);
    }

    public void sendToggledMsg() {
//         (this.hashCode(), Formatting.GRAY, "Toggled (highlight)%s(default) %s(default).", title, isActive() ? Formatting.GREEN + "on" : Formatting.RED + "off");
        String state = this.MODULE_ENABLED ? "enabled" : "disabled";
        MutableText message = Text.literal(this.MODULE_NAME + " has been " + state);
        message.setStyle(message.getStyle().withFormatting(Formatting.AQUA));
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
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
        if (state != MODULE_ENABLED) {
            if (state) {
                onEnable();
                ChimeraClient.LOGGER.info("Enabled " + getModuleName());
                ChimeraLogger.info("Enabled " + getModuleName());
            } else{
                onDisable();
                ChimeraClient.LOGGER.info("Disabled " + getModuleName());
                ChimeraLogger.info("Disabled " + getModuleName());
            }
        }
        MODULE_ENABLED = state;
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
    }

    public void toggle() {
        setModuleState(!getModuleEnabled());
    }

    public abstract void init();

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onTickStart(TickEvent.Start event);

    public abstract void onTickEnd(TickEvent.End event);
}
