package dev.chimera.managers.modules.combat;

import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.lwjgl.glfw.GLFW;

public class KillAuraModule extends AbstractModule {
    private static final int TOTAL_TICK_COOLDOWN = 12;
    private static int tickCooldown = TOTAL_TICK_COOLDOWN;

    public KillAuraModule() {
        super(ModuleCategory.COMBAT, "Kill Aura", GLFW.GLFW_KEY_R);
    }

    @Override
    public void onTickStart() {
        tickCooldown--;

        if (!isEnabled()) return;
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
        if (
            targetEntity instanceof ItemEntity
            || targetEntity instanceof ExperienceOrbEntity
            || targetEntity instanceof ProjectileEntity
        ) return;
        if (targetEntity.squaredDistanceTo(player) > 20.25) return;

        manager.attackEntity(player, targetEntity);
        tickCooldown = TOTAL_TICK_COOLDOWN;
    }
}
