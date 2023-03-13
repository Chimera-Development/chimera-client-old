package dev.chimera.modules.player;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.amalthea.events.packet.PacketSendEvent;
import dev.chimera.modules.Module;

import dev.chimera.modules.ModuleCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class FlightModule extends Module {
    private static MinecraftClient client;

    public FlightModule() {
        super(ModuleCategory.PLAYER, "Flight", GLFW.GLFW_KEY_G);
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @Override
    public void init() {
        client = MinecraftClient.getInstance();
    }

    @Override
    public void onEnable() {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.getAbilities().flying = false;
    }

    @Override
    public void onTickStart(TickEvent.Start event) {
        // We don't want this module triggering when disabled, since it breaks creative mode flight.
        if (client.player == null || !this.getModuleEnabled()) return;
        client.player.getAbilities().flying = getModuleEnabled();
        if (client.player.age % 40 >= 0 && client.player.age % 40 <= 2) client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY(), client.player.getZ(), client.player.isOnGround()));
    }

    @Override
    public void onTickEnd(TickEvent.End event) {
        if (client.player == null || !this.getModuleEnabled()) return;
        client.player.getAbilities().flying = getModuleEnabled();
    }

    @EventListener(id="cancelflightsneak")
    public void onPacketSend(PacketSendEvent event)
    {
        if (!this.getModuleEnabled())
            return;

        // Presumably should reduce lagbacks when flying.
        if (event.packet instanceof ClientCommandC2SPacket packet)
        {
            if (packet.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY || packet.getMode() == ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY)
            {
                event.cancelled = true;
            }
        }
    }
}
