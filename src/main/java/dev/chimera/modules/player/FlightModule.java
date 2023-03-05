package dev.chimera.modules.player;

import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class FlightModule extends Module {
    public FlightModule() {
        super("Flight", GLFW.GLFW_KEY_G);
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
    public void onTickStart(TickEvent.Start event) {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = getModuleEnabled();
    }

    @Override
    public void onTickEnd(TickEvent.End event) {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = getModuleEnabled();
    }
}
