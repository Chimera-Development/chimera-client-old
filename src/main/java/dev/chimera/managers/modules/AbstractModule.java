package dev.chimera.managers.modules;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractModule {
    @Getter
    private final ModuleCategory MODULE_CATEGORY;
    @Getter
    private final String MODULE_NAME;

    @Getter
    private boolean enabled = false;
    @Getter
    @Setter
    private int key_bind;
    public boolean releaseToToggle;

    public AbstractModule( String MODULE_NAME, ModuleCategory MODULE_CATEGORY) {
        this.MODULE_NAME = MODULE_NAME;
        this.MODULE_CATEGORY = MODULE_CATEGORY;
    }

    public AbstractModule(ModuleCategory category, String name, int bind) {
        this(category, name, bind, false);
    }

    public AbstractModule(ModuleCategory category, String name, int key_bind, boolean toggleOnRelease) {
        MODULE_CATEGORY = category;
        MODULE_NAME = name;
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
            if (this.enabled) onEnable();
            else onDisable();
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
