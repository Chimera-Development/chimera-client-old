package dev.chimera.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.eventbus.EventListener;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.amalthea.events.packet.PacketSendEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class BlockNBT extends Module {
    public BlockNBT()
    {
        super(ModuleCategory.MISC, "NBT Viewer");
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

    @EventListener(id = "NBTPacketSend")
    public void onPacket(PacketSendEvent e)
    {
        if (!this.getModuleEnabled())
            return;
        if (e.packet instanceof PlayerInteractBlockC2SPacket interact)
        {
            BlockPos block = interact.getBlockHitResult().getBlockPos();
            BlockEntity blockEntity = MinecraftClient.getInstance().world.getBlockEntity(block);
            if(blockEntity == null)
                return;
            String nbtString = blockEntity.createNbt().toString();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(nbtString));
        }
    }
}
