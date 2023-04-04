package dev.chimera.managers.modules.player;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.packet.PacketEvent;
import dev.chimera.managers.modules.AbstractModule;
import dev.chimera.managers.modules.ModuleCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class FlightModule extends AbstractModule {

    public FlightModule() {
        super(ModuleCategory.PLAYER, "Flight", GLFW.GLFW_KEY_G);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        mc.player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.getAbilities().flying = false;
    }

    @Override
    public void onTickStart() {
        // We don't want this module triggering when disabled, since it breaks creative mode flight.
        if (mc.player == null || !isEnabled()) return;
        mc.player.getAbilities().flying = isEnabled();
        if (mc.player.age % 40 >= 0 && mc.player.age % 40 <= 2) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
    }

    @Override
    public void onTickEnd() {
        if (mc.player == null || !isEnabled()) return;
        mc.player.getAbilities().flying = isEnabled();
    }

    @EventListener(id="cancelflightsneak")
    public void onPacketSend(PacketEvent.Send event)
    {
        if (!this.isEnabled())
            return;

        // Presumably should reduce lagbacks when flying.
        if (event.packet instanceof ClientCommandC2SPacket packet)
        {
            if (packet.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY || packet.getMode() == ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY)
            {
                event.setCancelled(true);
            }
        }
    }
}
