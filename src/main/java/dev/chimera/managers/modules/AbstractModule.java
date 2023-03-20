package dev.chimera.managers.modules;

import dev.chimera.ChimeraClient;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractModule {
    @Getter
    private final ModuleCategory category;
    @Getter
    private final String name;

    @Getter
    private boolean enabled = false;
    @Getter
    @Setter
    private int key_bind;
    public boolean releaseToToggle;

    public AbstractModule(String name, ModuleCategory category) {
        this.name = name;
        this.category = category;
    }

    public AbstractModule(ModuleCategory category, String name, int bind) {
        this(category, name, bind, false);
    }

    public AbstractModule(ModuleCategory category, String name, int key_bind, boolean toggleOnRelease) {
        this.category = category;
        this.name = name;
        this.key_bind = key_bind;
        releaseToToggle = toggleOnRelease;
    }

    /* TODO: Makes this tomfoolery work
    public void sendToggledMsg() {
//         (this.hashCode(), Formatting.GRAY, "Toggled (highlight)%s(default) %s(default).", title, isActive() ? Formatting.GREEN + "on" : Formatting.RED + "off");
        String state = this.MODULE_ENABLED ? "enabled" : "disabled";
        MutableText message = Text.literal(this.MODULE_NAME + " has been " + state);
        message.setStyle(message.getStyle().withFormatting(Formatting.AQUA));
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }
    */

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            if (this.enabled) {
                onEnable();
                ChimeraClient.EVENT_BUS.registerListenersInClass(this);
            } else {
                onDisable();
                // no event unregistering smh
                //ChimeraClient.EVENT_BUS.(this);
            };
        }
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void onInit() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    // TODO: Add onTickStart and onTickEnd as Events
}
