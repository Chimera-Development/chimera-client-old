package dev.chimera.modules.player;

import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class NoFallModule extends Module {
    private static MinecraftClient client;
    public NoFallModule() {
        super("No-Fall");
    }

    @Override
    public void init() {
        client = MinecraftClient.getInstance();
        setModuleState(true);
    }

    @Override
    public void onEnable() {
        if (client.player != null) client.player.sendMessage(Text.of("Enabled Module: " + getModuleName()), true);
    }

    @Override
    public void onDisable() {
        if (client.player != null) client.player.sendMessage(Text.of("Disabled Module: " + getModuleName()), true);
    }

    @Override
    public void onTickStart(TickEvent.Start event) {}

    @Override
    public void onTickEnd(TickEvent.End event) {}
}
