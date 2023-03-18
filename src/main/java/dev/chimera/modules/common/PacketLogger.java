package dev.chimera.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.amalthea.events.packet.PacketSendEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PacketLogger extends Module {
    public PacketLogger()
    {
        super(ModuleCategory.MISC, "Packet Logger");
        ChimeraClient.EVENT_BUS.registerListenersInClass(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTickStart(TickEvent.Start event) {

    }

    @Override
    public void onTickEnd(TickEvent.End event) {

    }

    @EventListener(id = "PacketLogger")
    public void onPacketSend(PacketSendEvent e)
    {
        if(!this.getModuleEnabled())
            return;
        if (MinecraftClient.getInstance().player != null)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(e.packet.getClass().getName()));
    }
}
