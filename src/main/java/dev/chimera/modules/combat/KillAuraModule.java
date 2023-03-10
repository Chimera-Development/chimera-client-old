package dev.chimera.modules.combat;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KillAuraModule extends Module {
    private static final int TOTAL_TICK_COOLDOWN = 12;
    private static int tickCooldown = TOTAL_TICK_COOLDOWN;
    private static MinecraftClient client;

    public KillAuraModule() {
        super("Kill Aura", GLFW.GLFW_KEY_R);
    }

    @Override
    public void init() {
        client = MinecraftClient.getInstance();
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
    public void onTickStart(TickEvent.Start event) {
        tickCooldown--;

        if (!getModuleEnabled()) return;
        if (tickCooldown > 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        ClientPlayerInteractionManager manager = client.interactionManager;

        if (world == null) return;
        if (manager == null) return;

        Entity targetEntity = world.getEntities().iterator().next();
        for (Entity e : client.world.getEntities()) {
            if (targetEntity.distanceTo(player) == 0 || e.distanceTo(player) < targetEntity.distanceTo(player)
                    && !(e instanceof ItemEntity) && !(e instanceof ExperienceOrbEntity)) {
                targetEntity = e;
            }
        }

        if (targetEntity == player) return;
        if (targetEntity instanceof ItemEntity || targetEntity instanceof ExperienceOrbEntity) return;
        if (targetEntity.squaredDistanceTo(player) > 20.25) return;

        manager.attackEntity(player, targetEntity);
        tickCooldown = TOTAL_TICK_COOLDOWN;
    }

    @Override
    public void onTickEnd(TickEvent.End event) {}
}
