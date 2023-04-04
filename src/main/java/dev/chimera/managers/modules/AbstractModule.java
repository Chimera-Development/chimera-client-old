package dev.chimera.managers.modules;

import dev.chimera.ChimeraClient;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public abstract class AbstractModule {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

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
        registerTickListeners();
    }

    public AbstractModule(ModuleCategory category, String name, int bind) {
        this(category, name, bind, false);
    }

    public AbstractModule(ModuleCategory category, String name, int key_bind, boolean toggleOnRelease) {
        this.category = category;
        this.name = name;
        this.key_bind = key_bind;
        releaseToToggle = toggleOnRelease;
        registerTickListeners();
    }

    private void registerTickListeners() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (isEnabled()) {
                onTickStart();
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (isEnabled()) {
                onTickEnd();
            }
        });
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            if (this.enabled) {
                onEnable();
                ChimeraClient.EVENT_BUS.registerListenersInClass(this);
                // Register the tick listeners in Fabric
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

    public void onTickStart() {

    }

    public void onTickEnd() {

    }
}
