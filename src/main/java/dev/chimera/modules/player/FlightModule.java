package dev.chimera.modules.player;

import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;

import net.minecraft.client.MinecraftClient;

public class FlightModule extends Module {
    public FlightModule() {
        super("Flight", "G");
    }

    @Override
    public void init() {}

    @Override
    public void onEnable() {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = true;
    }

    @Override
    public void onTickStart(TickEvent event) {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = getModuleEnabled();
    }

    @Override
    public void onTickEnd(TickEvent event) {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = getModuleEnabled();
    }
}
